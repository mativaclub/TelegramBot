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
        NotificationTask notificationTask = new NotificationTask(chatID, allSymbols, localDateTime);
        notificationTaskRepository.save(notificationTask);
    }

    public void addNewTask(NotificationTask notificationTask) {
        notificationTaskRepository.save(notificationTask);
    }
//    public void addManyTasks(List<NotificationTask> taskList) {
//        notificationTaskRepository.saveAll(taskList);
//    }

    public List<NotificationTask> findByDateAndTime() {
        return notificationTaskRepository.findByDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
    }
    //Controller -> Service -> Repository -> model  --- creating object of model in Service for
    // Repository














//    public List<Long> getId(Long id, String text) {
//        return notificationTaskRepository.getAllId(id, text);
//    }
//
//    public void printCount() {
//        notificationTaskRepository.count();
//        System.out.println(notificationTaskRepository.getCount());
//    }



}
