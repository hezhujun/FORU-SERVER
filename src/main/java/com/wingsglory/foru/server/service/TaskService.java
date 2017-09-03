package com.wingsglory.foru.server.service;

import com.wingsglory.foru.server.model.PageBean;
import com.wingsglory.foru.server.model.Task;

import java.math.BigDecimal;

/**
 * Created by hezhujun on 2017/7/18.
 */
public interface TaskService {
    Task publish(Task task) throws Exception;

    Task updateTask(Task task) throws Exception;

    void accept(Task task) throws Exception;

    void abandon(Task task) throws Exception;

    void complete(Task task) throws Exception;

    void confirm(Task task) throws Exception;

    void delete(Task task) throws Exception;

    PageBean<Task> listTask(Integer userId, BigDecimal latitude, BigDecimal longitude, long radius, int page, int rows)
            throws Exception;

    PageBean<Task> listPublishTask(Integer userId, int page, int rows) throws Exception;

    PageBean<Task> listAcceptTask(Integer userId, int page, int rows) throws Exception;

    /**
     * 把过期的任务设置成对应的状态
     */
    void checkTaskTimeout() throws Exception;

    Task findTaskById(Integer taskId) throws Exception;
}
