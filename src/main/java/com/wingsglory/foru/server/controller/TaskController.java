package com.wingsglory.foru.server.controller;

import com.wingsglory.foru.server.model.PageBean;
import com.wingsglory.foru.server.model.Task;
import com.wingsglory.foru.server.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hezhujun on 2017/6/24.
 */
@Controller
@RestController
@RequestMapping(value = "/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/published", method = RequestMethod.POST)
    public Map listPublishedTask(@RequestParam Integer userId,
                                 @RequestParam BigDecimal latitude,
                                 @RequestParam BigDecimal longitude,
                                 @RequestParam(name = "page", defaultValue = "1") int page,
                                 int rows) {
        Map<String, Object> map = new HashMap<>();
        Result result = new Result();
        try {
            PageBean<Task> taskPageBean = taskService.listTask(userId, latitude, longitude, page, rows);
            map.put("tasks", taskPageBean);
        } catch (Exception e) {
            e.printStackTrace();
            result.setErr(e.getMessage());
            result.setSuccess(false);
        }
        map.put("result", result);
        return map;
    }

    @RequestMapping(value = "/history/published", method = RequestMethod.POST)
    public Map listUserPublishedTask(@RequestParam Integer userId,
                                     @RequestParam(name = "page", defaultValue = "1") int page,
                                     int rows) {
        Map<String, Object> map = new HashMap<>();
        Result result = new Result();
        try {
            PageBean<Task> taskPageBean = taskService.listPublishTask(userId, page, rows);
            map.put("tasks", taskPageBean);
        } catch (Exception e) {
            e.printStackTrace();
            result.setErr(e.getMessage());
            result.setSuccess(false);
        }
        map.put("result", result);
        return map;
    }

    @RequestMapping(value = "/history/accepted", method = RequestMethod.POST)
    public Map listUserAcceptedTask(@RequestParam Integer userId,
                                    @RequestParam(name = "page", defaultValue = "1") int page,
                                    int rows) {
        Map<String, Object> map = new HashMap<>();
        Result result = new Result();
        try {
            PageBean<Task> taskPageBean = taskService.listAcceptTask(userId, page, rows);
            map.put("tasks", taskPageBean);
        } catch (Exception e) {
            e.printStackTrace();
            result.setErr(e.getMessage());
            result.setSuccess(false);
        }
        map.put("result", result);
        return map;
    }

    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public Map publish(@RequestBody Task task) {
        Map<String, Object> map = new HashMap<>();
        Result result = new Result();
        try {
            task = taskService.publish(task);
            map.put("task", task);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErr(e.getMessage());
        }
        map.put("result", result);
        return map;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map update(@RequestBody Task task) {
        Map<String, Object> map = new HashMap<>();
        Result result = new Result();
        try {
            task = taskService.updateTask(task);
            map.put("task", task);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErr(e.getMessage());
        }
        map.put("result", result);
        return map;
    }

    @RequestMapping(value = "/accept", method = RequestMethod.POST)
    public Map accept(@RequestParam Integer userId, @RequestParam Integer taskId) {
        Map<String, Object> map = new HashMap<>();
        Result result = new Result();
        try {
            Task task = new Task();
            task.setId(taskId);
            task.setRecipientId(userId);
            taskService.accept(task);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErr(e.getMessage());
        }
        map.put("result", result);
        return map;
    }

    @RequestMapping(value = "/abandon", method = RequestMethod.POST)
    public Map abandon(@RequestParam Integer userId, @RequestParam Integer taskId) {
        Map<String, Object> map = new HashMap<>();
        Result result = new Result();
        try {
            Task task = new Task();
            task.setId(taskId);
            task.setRecipientId(userId);
            taskService.abandon(task);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErr(e.getMessage());
        }
        map.put("result", result);
        return map;
    }

    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    public Map complete(@RequestParam Integer userId, @RequestParam Integer taskId) {
        Map<String, Object> map = new HashMap<>();
        Result result = new Result();
        try {
            Task task = new Task();
            task.setId(taskId);
            task.setRecipientId(userId);
            taskService.complete(task);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErr(e.getMessage());
        }
        map.put("result", result);
        return map;
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public Map confirm(@RequestParam Integer userId, @RequestParam Integer taskId) {
        Map<String, Object> map = new HashMap<>();
        Result result = new Result();
        try {
            Task task = new Task();
            task.setId(taskId);
            task.setPublisherId(userId);
            taskService.confirm(task);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErr(e.getMessage());
        }
        map.put("result", result);
        return map;
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public Map remove(@RequestParam Integer userId, @RequestParam Integer taskId) {
        Map<String, Object> map = new HashMap<>();
        Result result = new Result();
        try {
            Task task = new Task();
            task.setId(taskId);
            task.setRecipientId(userId);
            taskService.delete(task);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErr(e.getMessage());
        }
        map.put("result", result);
        return map;
    }

}
