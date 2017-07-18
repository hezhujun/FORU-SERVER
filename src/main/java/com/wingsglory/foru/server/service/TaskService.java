package com.wingsglory.foru.server.service;

import com.wingsglory.foru.server.model.PageBean;
import com.wingsglory.foru.server.model.Task;

import java.math.BigDecimal;

/**
 * Created by hezhujun on 2017/7/18.
 */
public interface TaskService {
    Task publish(Task task);

    Task updateTask(Task task);

    void accept(Task task);

    void abandon(Task task);

    void complete(Task task);

    void confirm(Task task);

    void delete(Task task);

    PageBean<Task> listTask(Integer userId, BigDecimal latitude, BigDecimal longitude, int page, int rows);

    PageBean<Task> listPublishTask(Integer userId, int page, int rows);

    PageBean<Task> listAcceptTask(Integer userId, int page, int rows);
}
