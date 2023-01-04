package com.example.springdemobot.service;

import com.example.springdemobot.config.BotConfig;
import com.example.springdemobot.model.User;
import com.example.springdemobot.model.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserService userService;
    final BotConfig config;

    static  final String HELP_TEXT = "This bot is created to demonstrate Spring capabilities.\n\n" +
            "You can execute commands from the main menu of the left or by typing a command:\n\n" +
            "Type /start to see a welcome message\n\n"+
            "Type /mydata to see data stored about yourself\n\n"+
            "Type /help to see this message again";

    public TelegramBot(BotConfig config){
        this.config = config;
        List<BotCommand> listofCommands = new ArrayList();
        listofCommands.add(new BotCommand("/start","get a welcome message"));
        listofCommands.add(new BotCommand("/mydata","get your data stored"));
        listofCommands.add(new BotCommand("/deletedata","delete my data"));
        listofCommands.add(new BotCommand("/help","info how to use this bot"));
        listofCommands.add(new BotCommand("/settings","set your preferences"));
        try {
            this.execute(new SetMyCommands(listofCommands,new BotCommandScopeDefault(), null));
        }catch (TelegramApiException e){
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText){
                case "/start":
                    registerUser(update.getMessage());
                    startCommandReceived(chatId,update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    sendMessage(chatId,HELP_TEXT);
                    break;
                default: sendMessage(chatId,"Sorry, command was not recognized");
            }
        }
    }
    private void registerUser(Message msg){
        if(userService.findByChatId(msg.getChatId()).isEmpty()){

            var chatId = msg.getChatId();
            var chat = msg.getChat();

            User user = new User();

            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

            userService.save(user);
            log.info("user saved: " + user);
        }
    }

    private void startCommandReceived(long chatId,String name){
        String answer = "Hi, " + name + ", nice to meet you!";

        sendMessage(chatId,answer);
        log.info("Replied to user " + name);
    }
    private void sendMessage(long chatId, String textToSent){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSent);
        try {
            execute(message);
        }catch (TelegramApiException e){
            log.error("Error occurred: "+ e.getMessage());
        }
    }
}
