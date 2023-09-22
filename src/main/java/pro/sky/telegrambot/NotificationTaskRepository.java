package pro.sky.telegrambot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

//Repository should be connected with model, now model name is NotificationTask, which should be
//first param, and the second will be type of id
@Repository
public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {
//connection with model that is written in service here is in params --- NotificationTask
    public List<NotificationTask> findByDateTime(LocalDateTime localDateTime);
    //we need list of tasks for current time, and connect to DB with connection to model
    // tasks we search by date,time
    //findByDateTime this is strict required type of writing in JpaRepository

}
