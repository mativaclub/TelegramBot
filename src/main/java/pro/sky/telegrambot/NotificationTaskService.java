package pro.sky.telegrambot;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class NotificationTaskService {
    private NotificationTaskRepository notificationTaskRepository;
    public NotificationTaskService(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }
    public void addNewTask(LocalDateTime localDateTime, String allSymbols, Long chatID) {
        notificationTaskRepository.save(new NotificationTask(chatID, allSymbols, localDateTime));
    }

    public List<NotificationTask> findByDateAndTime() {
        return notificationTaskRepository.findByDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
    }
    //Controller -> Service -> Repository -> model  --- creating object of model in Service for
    // Repository


}
