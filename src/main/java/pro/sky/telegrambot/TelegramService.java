package pro.sky.telegrambot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramService {
    private NotificationTaskService notificationTaskService;
    private final String START = "/start";
    private final String token;
    private final TelegramBot telegramBot;

    //    @Value load bot token from application properties, and now variable String token init with value with token
//    we don't add telegramBot to the parameter of constructor because Spring doesn't know about it, and it is now our bean, not our created class , bean - connection class and spring
    public TelegramService(NotificationTaskService notificationTaskService, @Value("${bot.token}") String token) {
        this.token = token;
        this.telegramBot = new TelegramBot(token);
        this.notificationTaskService = notificationTaskService;
        //Object of TelegramBot isn't our bean and property, and we have to create a new object. Spring doesn't know how to
        //work with library telegramBot.
        telegramBot.setUpdatesListener(new TelegramBotUpdatesListener());
    }

//    @Scheduled(fixedDelay = 1000) //every second send notifications
    @Scheduled(cron = "0 0/1 * * * *") //second, minute, hours, day, month, weekday
    public void scheduleChecker() {
        List<NotificationTask> notificationTasks = notificationTaskService.findByDateAndTime();
        for (NotificationTask task : notificationTasks) {
            SendMessage sendMessage = new SendMessage(task.getIdChat(), task.getNotifyText());
            telegramBot.execute(sendMessage);
        }
    }

    public class TelegramBotUpdatesListener implements UpdatesListener {
        @Override
        public int process(List<Update> list) {
            //going through all messages from user
            for (Update update : list) {
                String text = update.message().text();
                Long chatId = update.message().chat().id();
                //for getting chatId of user
                if (text.equals(START)) {
                    //if we find equal text /start then we create new object of SendMessage,and we get from message chat id
                    // and write answered message for user
                    SendMessage sendMessage = new SendMessage(update.message().chat().id(), "Hi");
                    //send message to the bot with help of execute method
                    telegramBot.execute(sendMessage);  //sending hi to client
                } else {
                    //creating object of pattern for regex which symbols should be in text that user sent to bot
                    Pattern pattern = Pattern.compile("^([\\d.:\\s]{16})(\\s)(.+)$");   // . include any symbol
                    //can name the group of regex directly in it "^(?<dateAndTime>[\d.:\s]{16})
                    //we are checking regex with the text that we receive
                    Matcher matcher = pattern.matcher(text);
                    if (matcher.find()) {
                        String dateAndTime = matcher.group(1);
                        String notifyText = matcher.group(3);
                        LocalDateTime localDateTime = LocalDateTime.parse(dateAndTime,
                                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                        notificationTaskService.addNewTask(localDateTime, notifyText, chatId);
                        SendMessage sendMessage = new SendMessage(chatId, "Your message is sent");
                        telegramBot.execute(sendMessage);
                    }
                }
            }
            //Constant field inside UpdatesListener that confirms that our messages has been sent / updates were executed/done
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }
    }
}
