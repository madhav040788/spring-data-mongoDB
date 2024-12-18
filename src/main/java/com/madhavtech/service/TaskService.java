package com.madhavtech.service;

import com.madhavtech.model.Task;
import com.madhavtech.repository.TaskRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Data
@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    //CRUD OPERATION

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    public Task saveTask(Task task){
        //Random id like afdff43f-rwe34dfd-32ddfda3 ==> set[0---saveInDb]
        task.setTaskId(UUID.randomUUID().toString().split("-")[0]);
        logger.info("TaskService :: saveTask in db");
        Task saveInDb = taskRepository.save(task);
        return saveInDb;
    }

    public List<Task> getAllTask(){
        return taskRepository.findAll();
    }

    public Task singleTask(String taskId){
        return taskRepository.findById(taskId).get();
    }

    public Task updateTask(Task taskRequest){
        // first get existing object from db by taskId
        // then set new value from request
        logger.info("TaskService :: updateTask() get existing obj by taskid {}",taskRequest.getTaskId());
        Task existingTask = taskRepository.findById(taskRequest.getTaskId()).get(); // DB data
        existingTask.setAssignee(taskRequest.getAssignee());
        existingTask.setDescription(taskRequest.getDescription());
        existingTask.setPriority(taskRequest.getPriority());
        existingTask.setStoryPoint(taskRequest.getStoryPoint());
        return  taskRepository.save(existingTask);
    }

    public String deleteTask(String taskId){
         taskRepository.deleteById(taskId);
         return taskId +" -> : taskId is deleted successfully... !!!";
    }

    public List<Task> getTaskByAssigneeAndPriorityTwo(String assignee, String priority){
//        List<Task> responseByAssigneeAndpri = taskRepository.findByAssigneeAndPriority(assignee, priority);
        List<Task> reposnseTwoElement = taskRepository. findByAssigneeAndPriorityGiven(assignee, priority);
//        return responseByAssigneeAndpri;
        return reposnseTwoElement;
    }

    public List<Task> getTasksByStoreypointAndPriority(int storyPoint,String priority){
        List<Task> responseFromDb = taskRepository.findByStoryPointAndPriority(storyPoint,priority);
        return responseFromDb;
    }

    // OPERATORS ==> IN,LIKE,MIN,MAX,BETWEEN,IGNORE CASE

    public List<Task> getTaskByMultipleStoryPoints(int storyPoint){
        return taskRepository.findByStoryPointIn(storyPoint);
    }
    //Between - Min and Max
    public List<Task> getTaskBetweenPriority(String minPriority,String maxPriority){
         return taskRepository.findByPriorityBetween(minPriority,maxPriority);
    }

    // lessthan
    public List<Task> getTasksLessThanStoryPoint(int storyPoint){
        return taskRepository.findByStoryPointLessThan(storyPoint);
    }

    //greater than
    public List<Task> getTaskGraterThanStoryPoint(int storyPoint){
        return taskRepository.findByStoryPointGreaterThan(storyPoint);
    }

    //LIKE
    public List<Task> getTasksLikeAssigneeName(String assignee){
        return taskRepository.findByAssigneeLike(assignee);
    }

    public List<Task> getTasksIgnoreCaseAssignee(String assignee){
        return taskRepository.findByAssigneeIgnoreCaseContaining(assignee);
    }

    // SORTING
    public List<Task> getTasksSortingOnName(@PathVariable String fieldName){
        return taskRepository.findAll(Sort.by(Sort.Direction.ASC,fieldName));
    }


}
