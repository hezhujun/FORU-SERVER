package com.wingsglory.foru.server.service.impl;

import com.wingsglory.foru.server.Const;
import com.wingsglory.foru.server.common.AMapUtils;
import com.wingsglory.foru.server.common.JPushUtil;
import com.wingsglory.foru.server.dao.*;
import com.wingsglory.foru.server.model.*;
import com.wingsglory.foru.server.service.TaskPool;
import com.wingsglory.foru.server.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hezhujun on 2017/7/20.
 */
@Service("TaskService")
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AddresseeMapper addresseeMapper;
    @Autowired
    private TaskContentMapper taskContentMapper;
    @Autowired
    private RelationMapper relationMapper;

    @Transactional
    @Override
    public Task publish(Task task) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("保存任务信息：" + task.toString());
        }
        if (task.getPublisherId() == null) {
            throw new Exception("发布人id不能为空");
        }
        if (task.getState() == null) {
            task.setState(Task.TASK_STATE_NEW);
        }
        if (task.getGmtCreate() == null) {
            task.setGmtCreate(new Date());
        }
        TaskContent taskContent = task.getContent();
        if (taskContent == null) {
            throw new Exception("任务内容不能为空");
        }
        if (taskContent.getTitle() == null || "".equals(taskContent.getTitle())) {
            throw new Exception("任务标题不能为空");
        }
        if (taskContent.getContent() == null || "".equals(taskContent.getContent())) {
            throw new Exception("任务内容不能为空");
        }
        if (taskContent.getTargetPosition() == null || "".equals(taskContent.getTargetPosition())) {
            throw new Exception("任务地点不能为空");
        }
        if (taskContent.getReward() == null) {
            throw new Exception("报酬不能为空");
        }
        if (taskContent.getTimeout() == null) {
            throw new Exception("任务期限不能为空");
        }
        Addressee addressee = taskContent.getAddressee();
        if (addressee == null) {
            throw new Exception("收货信息不能为空");
        }
        if (addressee.getName() == null || "".equals(addressee.getName())) {
            throw new Exception("收货人信息不能为空");
        }
        if (addressee.getPhone() == null || "".equals(addressee.getPhone())) {
            throw new Exception("收货人电话号码不能为空");
        }
        if (addressee.getAddress() == null || "".equals(addressee.getAddress())) {
            throw new Exception("收货地址不能为空");
        }

        // 先保存收货信息
        addressee.setId(null);
        addressee.setUserId(null);
        int i = addresseeMapper.insertSelective(addressee);
        if (!(i == 1) || addressee.getId() == null) {
            throw new Exception("保存收货信息失败");
        }
        // 再任务内容
        taskContent.setAddresseeId(addressee.getId());
        i = taskContentMapper.insertSelective(taskContent);
        if (!(i == 1) || taskContent.getId() == null) {
            throw new Exception("保存任务内容失败");
        }
        // 最后保存任务
        task.setContentId(taskContent.getId());
        i = taskMapper.insertSelective(task);
        if (!(i == 1) || task.getId() == null) {
            throw new Exception("保存任务失败");
        }
        User publisher = userMapper.selectByPrimaryKey(task.getPublisherId());
        if (publisher.getPublishCount() == null) {
            publisher.setPublishCount(0);
        }
        publisher.setPublishCount(publisher.getPublishCount() + 1);
        userMapper.updateByPrimaryKeySelective(publisher);
        if (!(i == 1)) {
            throw new Exception("增加用户的发布任务数失败");
        }
        Task t = taskMapper.selectByPrimaryKey(task.getId());
        t.setPublisher(publisher);
        t.setContent(taskContentMapper.selectByPrimaryKey(t.getContentId()));
        t.getContent().setAddressee(addresseeMapper.selectByPrimaryKey(t.getContent().getAddresseeId()));
        // 推送新用户给用户
        PushTaskNewTask pushTaskNewTask = new PushTaskNewTask(t);
        TaskPool.submitTask(pushTaskNewTask);
        if (logger.isDebugEnabled()) {
            logger.debug("保存任务信息成功：" + t.toString());
        }
        return t;
    }

    class PushTaskNewTask implements Runnable {

        private Task task;

        public PushTaskNewTask(Task task) {
            this.task = task;
        }

        @Override
        public void run() {
            List<String> userIdList = new ArrayList<>();
            TaskContent taskContent = task.getContent();
            LatLng target = new LatLng(taskContent.getLatitude().doubleValue(), taskContent.getLongitude().doubleValue());
            List<User> userList = userMapper.selectByExample(null);
            for (User user :
                    userList) {
                // 用户位置没有记录
                if (user.getLatitude() == null || user.getLongitude() == null) {
                    continue;
                }
                // 不需要推送到发布人
//            if (user.getId().equals(task.getPublisherId())) {
//                continue;
//            }
                LatLng userPosition = new LatLng(user.getLatitude().doubleValue(), user.getLongitude().doubleValue());
                long distance = (long) AMapUtils.calculateLineDistance(userPosition, target);
                // 距离低于5km就推送
                if (distance <= 5000) {
                    userIdList.add(String.valueOf(user.getId()));
                }
            }
            String message = String.format("{\"taskId\":%d}", task.getId());
            // 使用极光推送接口推送
            JPushUtil.sendMessage(userIdList, Const.TASK_NEW, message, null);
        }
    }

    @Transactional
    @Override
    public Task updateTask(Task task) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("修改任务信息：" + task.toString());
        }
        if (task.getId() == null) {
            throw new Exception("任务Id不能为空");
        }
        Task theTask = taskMapper.selectByPrimaryKey(task.getId());
        // 只能在新建状态更改信息
        if (!Task.TASK_STATE_NEW.equals(theTask.getState())) {
            throw new Exception("该任务所在状态不能修改信息");
        }
        // 只能更改任务内容
        TaskContent taskContent = task.getContent();
        if (taskContent == null) {
            throw new Exception("任务内容不能为空");
        }
        if (taskContent.getId() == null) {
            throw new Exception("任务内容Id不能为空");
        }
        // 以下判断，可以为null，null的不会修改
        // 但是不能为空字符串
        if ("".equals(taskContent.getTitle())) {
            throw new Exception("任务标题不能为空");
        }
        if ("".equals(taskContent.getContent())) {
            throw new Exception("任务内容不能为空");
        }
        if ("".equals(taskContent.getTargetPosition())) {
            throw new Exception("任务地点不能为空");
        }
        int i = taskContentMapper.updateByPrimaryKeySelective(taskContent);
        if (!(i == 1)) {
            throw new Exception("修改任务内容失败");
        }
        // 修改收货信息
        Addressee addressee = taskContent.getAddressee();
        if (addressee != null) {
            if (addressee.getId() == null) {
                throw new Exception("收货信息ID不能为空");
            }
            // 以下判断，可以为null，null的不会修改
            // 但是不能为空字符串
            if ("".equals(addressee.getName())) {
                throw new Exception("收货人信息不能为空");
            }
            if ("".equals(addressee.getPhone())) {
                throw new Exception("收货人电话号码不能为空");
            }
            if ("".equals(addressee.getAddress())) {
                throw new Exception("收货地址不能为空");
            }
            i = addresseeMapper.updateByPrimaryKeySelective(addressee);
            if (!(i == 1)) {
                throw new Exception("修改收货信息失败");
            }
        }
        Task t = taskMapper.selectByPrimaryKey(task.getId());
        t.setPublisher(userMapper.selectByPrimaryKey(t.getPublisherId()));
        t.setContent(taskContentMapper.selectByPrimaryKey(t.getContentId()));
        t.getContent().setAddressee(addresseeMapper.selectByPrimaryKey(t.getContent().getAddresseeId()));
        if (logger.isDebugEnabled()) {
            logger.debug("修改任务信息成功：" + t.toString());
        }
        return t;
    }

    @Override
    public void accept(Task task) throws Exception {
        if (task.getId() == null) {
            throw new Exception("任务Id不能为空");
        }
        Task theTask = taskMapper.selectByPrimaryKey(task.getId());
        if (theTask == null) {
            throw new Exception("该任务已删除");
        }
        if (!Task.TASK_STATE_NEW.equals(theTask.getState())) {
            throw new Exception("该任务所在状态不能接受");
        }
        if (task.getRecipientId() == null) {
            throw new Exception("接受人ID不能为空");
        }
        if (theTask.getPublisherId().equals(task.getRecipientId())) {
            throw new Exception("自己不能接受自己的任务");
        }
        task.setState(Task.TASK_STATE_WAIT_FOR_COMPLETE);
        int i = taskMapper.updateByPrimaryKeySelective(task);
        if (!(i == 1)) {
            throw new Exception("操作失败");
        }
        TaskPool.submitTask(new PushTaskAcceptTask(theTask.getId()));
        if (logger.isDebugEnabled()) {
            logger.debug("用户" + task.getRecipientId() + "接受任务" + task.getId());
        }
    }

    class PushTaskAcceptTask implements Runnable {

        private int taskId;

        public PushTaskAcceptTask(int taskId) {
            this.taskId = taskId;
        }

        @Override
        public void run() {
            String message = String.format("{\"taskId\":%d}", taskId);
            JPushUtil.sendMessageToAll(Const.TASK_ACCEPT, message, null);
        }
    }

    @Override
    public void abandon(Task task) throws Exception {
        if (task.getId() == null) {
            throw new Exception("任务Id不能为空");
        }
        Task theTask = taskMapper.selectByPrimaryKey(task.getId());
        if (!Task.TASK_STATE_WAIT_FOR_COMPLETE.equals(theTask.getState())) {
            throw new Exception("该任务所在状态不能放弃");
        }
        if (!theTask.getRecipientId().equals(task.getRecipientId())) {
            throw new Exception("你没有接受此任务，不能操作");
        }
        theTask.setState(Task.TASK_STATE_NEW);
        theTask.setRecipientId(null);
        int i = taskMapper.updateByPrimaryKey(theTask);
        if (!(i == 1)) {
            throw new Exception("操作失败");
        }
        TaskPool.submitTask(new PushTaskAbandonTask(theTask.getId()));
        if (logger.isDebugEnabled()) {
            logger.debug("用户" + task.getRecipientId() + "放弃任务" + task.getId());
        }
    }

    class PushTaskAbandonTask implements Runnable {

        private int taskId;

        public PushTaskAbandonTask(int taskId) {
            this.taskId = taskId;
        }

        @Override
        public void run() {
            String message = String.format("{\"taskId\":%d}", taskId);
            JPushUtil.sendMessageToAll(Const.TASK_ABANDON, message, null);
        }
    }

    @Override
    public void complete(Task task) throws Exception {
        if (task.getId() == null) {
            throw new Exception("任务Id不能为空");
        }
        Task theTask = taskMapper.selectByPrimaryKey(task.getId());
        if (!Task.TASK_STATE_WAIT_FOR_COMPLETE.equals(theTask.getState())) {
            throw new Exception("该任务所在状态不能完成");
        }
        if (!theTask.getRecipientId().equals(task.getRecipientId())) {
            throw new Exception("你没有接受此任务，不能操作");
        }
        task.setState(Task.TASK_STATE_WAIT_FOR_CONFIRM);
        int i = taskMapper.updateByPrimaryKeySelective(task);
        if (!(i == 1)) {
            throw new Exception("操作失败");
        }
        TaskPool.submitTask(new PushTaskCompleteTask(task.getPublisherId(), task.getId()));
        if (logger.isDebugEnabled()) {
            logger.debug("用户" + task.getRecipientId() + "完成任务" + task.getId());
        }
    }

    class PushTaskCompleteTask implements Runnable {
        private int userId;
        private int taskId;

        public PushTaskCompleteTask(int userId, int taskId) {
            this.userId = userId;
            this.taskId = taskId;
        }

        @Override
        public void run() {
            String message = String.format("{\"taskId\":%d}", taskId);
            JPushUtil.sendMessageToUser(userId, Const.TASK_COMPLETE, message, null);
        }
    }

    @Transactional
    @Override
    public void confirm(Task task) throws Exception {
        if (task.getId() == null) {
            throw new Exception("任务Id不能为空");
        }
        Task theTask = taskMapper.selectByPrimaryKey(task.getId());
        if (!Task.TASK_STATE_WAIT_FOR_CONFIRM.equals(theTask.getState())) {
            throw new Exception("该任务所在状态不能确认完成");
        }
        if (!theTask.getPublisherId().equals(task.getPublisherId())) {
            throw new Exception("你没有发布此任务，不能操作");
        }
        task.setState(Task.TASK_STATE_COMPLETE);
        int i = taskMapper.updateByPrimaryKeySelective(task);
        if (!(i == 1)) {
            throw new Exception("操作失败");
        }
        TaskPool.submitTask(new PushTaskConfirmTask(task.getPublisherId(), task.getId()));
        // 更新双方的关系
        RelationKey key = new RelationKey(theTask.getPublisherId(), theTask.getRecipientId());
        Relation relation = relationMapper.selectByPrimaryKey(key);
        if (relation == null) {
            relation = new Relation(theTask.getPublisherId(), theTask.getRecipientId());
            relation.setInteractionCount(1);
            relation.setRelation(Relation.RELATION_NORMAL);
            relationMapper.insertSelective(relation);
        } else {
            relation.setInteractionCount(relation.getInteractionCount() + 1);
            relationMapper.updateByPrimaryKeySelective(relation);
        }
        key = null;
        relation = null;
        key = new RelationKey(theTask.getRecipientId(), theTask.getPublisherId());
        relation = relationMapper.selectByPrimaryKey(key);
        if (relation == null) {
            relation = new Relation(theTask.getRecipientId(), theTask.getPublisherId());
            relation.setInteractionCount(1);
            relation.setRelation(Relation.RELATION_NORMAL);
            relationMapper.insertSelective(relation);
        } else {
            relation.setInteractionCount(relation.getInteractionCount() + 1);
            relationMapper.updateByPrimaryKeySelective(relation);
        }
        User recipient = userMapper.selectByPrimaryKey(theTask.getRecipientId());
        if (recipient.getDoneCount() == null) {
            recipient.setDoneCount(0);
        }
        recipient.setDoneCount(recipient.getDoneCount() + 1);
        i = userMapper.updateByPrimaryKeySelective(recipient);
        if (i != 1) {
            throw new Exception("增加用户完成任务数失败");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("用户" + theTask.getPublisherId() + "确认任务" + theTask.getId() + "完成");
        }
    }

    class PushTaskConfirmTask implements Runnable {

        private int userId;
        private int taskId;

        public PushTaskConfirmTask(int userId, int taskId) {
            this.userId = userId;
            this.taskId = taskId;
        }

        @Override
        public void run() {
            String message = String.format("{\"taskId\":%d}", taskId);
            JPushUtil.sendMessageToUser(userId, Const.TASK_FINISH, message, null);
        }
    }

    @Transactional
    @Override
    public void delete(Task task) throws Exception {
        if (task.getId() == null) {
            throw new Exception("任务Id不能为空");
        }
        Task theTask = taskMapper.selectByPrimaryKey(task.getId());
        // 已删除
        if (theTask == null) {
            return;
        }
        if (!Task.TASK_STATE_NEW.equals(theTask.getState())) {
            throw new Exception("该任务所在状态不能删除");
        }
        if (!theTask.getPublisherId().equals(task.getPublisherId())) {
            throw new Exception("你没有发布此任务，不能操作");
        }
        theTask.setState(Task.TASK_STATE_DELETE);
        int i = taskMapper.updateByPrimaryKeySelective(theTask);
        if (!(i == 1)) {
            throw new Exception("操作失败");
        }
        TaskPool.submitTask(new PushTaskDeleteTask(theTask.getId()));
        if (logger.isDebugEnabled()) {
            logger.debug("用户" + task.getPublisherId() + "删除任务" + task.getId());
        }
        User publisher = userMapper.selectByPrimaryKey(theTask.getPublisherId());
        publisher.setPublishCount(publisher.getPublishCount() - 1);
        i = userMapper.updateByPrimaryKeySelective(publisher);
        if (i != 1) {
            throw new Exception("减少用户发布任务数失败");
        }
    }

    class PushTaskDeleteTask implements Runnable {

        private int taskId;

        public PushTaskDeleteTask(int taskId) {
            this.taskId = taskId;
        }

        @Override
        public void run() {
            String message = String.format("{\"taskId\":%d}", taskId);
            JPushUtil.sendMessageToAll(Const.TASK_DELETE, message, null);
        }
    }

    @Transactional
    @Override
    public PageBean<Task> listTask(Integer userId, BigDecimal latitude, BigDecimal longitude,
                                   long radius, int page, int rows)
            throws Exception {
        PageBean<Task> taskPageBean = null;
        TaskExample example = new TaskExample();
        TaskExample.Criteria criteria = example.createCriteria();
        criteria.andStateEqualTo(Task.TASK_STATE_NEW);
        int totalRows = taskMapper.countByExample(example);
        if (totalRows == 0) {
            taskPageBean = new PageBean<Task>(0, rows, page);
        } else {
            example.setOrderByClause("id DESC");
            example.setOffset((page - 1) * rows);
            example.setRows(rows);
            List<Task> taskList = taskMapper.selectByExample(example);
            LatLng userPosition = new LatLng(latitude.doubleValue(), longitude.doubleValue());
            Iterator<Task> iterator = taskList.iterator();
            while (iterator.hasNext()) {
                Task task = iterator.next();
                TaskContent content = taskContentMapper.selectByPrimaryKey(task.getContentId());
                LatLng taskPosition = new LatLng(content.getLatitude().doubleValue(),
                        content.getLongitude().doubleValue());
                // 去掉方圆radius之外的任务
                long distance = (long) AMapUtils.calculateLineDistance(userPosition, taskPosition);
                if (distance > radius) {
                    iterator.remove();
                    continue;
                }
                task.setContent(taskContentMapper.selectByPrimaryKey(task.getContentId()));
                task.setPublisher(userMapper.selectByPrimaryKey(task.getPublisherId()));
                task.getContent().setAddressee(
                        addresseeMapper.selectByPrimaryKey(task.getContent().getAddresseeId()));
            }
            taskPageBean = new PageBean<Task>(totalRows, rows, page, taskList);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("返回任务列表：" + taskPageBean);
        }
        return taskPageBean;
    }

    @Transactional
    @Override
    public PageBean<Task> listPublishTask(Integer userId, int page, int rows) throws Exception {
        PageBean<Task> taskPageBean = null;
        TaskExample example = new TaskExample();
        TaskExample.Criteria criteria = example.createCriteria();
        criteria.andPublisherIdEqualTo(userId);
        criteria.andStateNotEqualTo(Task.TASK_STATE_DELETE);
        int totalRows = taskMapper.countByExample(example);
        if (totalRows == 0) {
            taskPageBean = new PageBean<Task>(0, rows, page);
        } else {
            example.setOrderByClause("id DESC");
            example.setOffset((page - 1) * rows);
            example.setRows(rows);
            List<Task> taskList = taskMapper.selectByExample(example);
            for (Task task :
                    taskList) {
                task.setPublisher(userMapper.selectByPrimaryKey(task.getPublisherId()));
                Integer recipientId = task.getRecipientId();
                if (recipientId != null) {
                    task.setRecipient(userMapper.selectByPrimaryKey(recipientId));
                }
                task.setContent(taskContentMapper.selectByPrimaryKey(task.getContentId()));
                task.getContent().setAddressee(
                        addresseeMapper.selectByPrimaryKey(task.getContent().getAddresseeId()));
            }
            taskPageBean = new PageBean<Task>(totalRows, rows, page, taskList);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("返回任务列表：" + taskPageBean);
        }
        return taskPageBean;
    }

    @Transactional
    @Override
    public PageBean<Task> listAcceptTask(Integer userId, int page, int rows) throws Exception {
        PageBean<Task> taskPageBean = null;
        TaskExample example = new TaskExample();
        TaskExample.Criteria criteria = example.createCriteria();
        criteria.andRecipientIdEqualTo(userId);
        int totalRows = taskMapper.countByExample(example);
        if (totalRows == 0) {
            taskPageBean = new PageBean<Task>(0, rows, page);
        } else {
            example.setOrderByClause("id DESC");
            example.setOffset((page - 1) * rows);
            example.setRows(rows);
            List<Task> taskList = taskMapper.selectByExample(example);
            for (Task task :
                    taskList) {
                task.setPublisher(userMapper.selectByPrimaryKey(task.getPublisherId()));
                task.setRecipient(userMapper.selectByPrimaryKey(task.getRecipientId()));
                task.setContent(taskContentMapper.selectByPrimaryKey(task.getContentId()));
                task.getContent().setAddressee(
                        addresseeMapper.selectByPrimaryKey(task.getContent().getAddresseeId()));
            }
            taskPageBean = new PageBean<Task>(totalRows, rows, page, taskList);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("返回任务列表：" + taskPageBean);
        }
        return taskPageBean;
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 设置每天23:00:00执行一次
    @Override
    @Scheduled(cron = "0 23 * * * *")
    public void checkTaskTimeout() throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("开始处理过期的任务");
        }
        new CheckTaskTimeout().start();
    }

    class CheckTaskTimeout extends Thread {

        @Override
        public void run() {
            TaskExample example = new TaskExample();
            TaskExample.Criteria criteria = example.createCriteria();
            List<String> states = new ArrayList<>();
            states.add(Task.TASK_STATE_NEW);
            states.add(Task.TASK_STATE_WAIT_FOR_COMPLETE);
            criteria.andStateIn(states);
            Date timeout = null;
            long now = System.currentTimeMillis();
            List<Task> taskList = taskMapper.selectByExample(example);
            for (Task task :
                    taskList) {
                TaskContent content = taskContentMapper.selectByPrimaryKey(task.getContentId());
                timeout = content.getTimeout();
                if ((timeout.getTime() - now) <= 0) {
                    // 已过期
                    if (Task.TASK_STATE_NEW.equals(task.getState())) {
                        task.setState(Task.TASK_STATE_OVERDUE);
                        taskMapper.updateByPrimaryKeySelective(task);
                    } else if (Task.TASK_STATE_WAIT_FOR_COMPLETE.equals(task.getState())) {
                        task.setState(Task.TASK_STATE_FAIL);
                        taskMapper.updateByPrimaryKeySelective(task);
                        User recipient = userMapper.selectByPrimaryKey(task.getRecipientId());
                        if (recipient.getFailCount() == null) {
                            recipient.setFailCount(0);
                        }
                        recipient.setFailCount(recipient.getFailCount() + 1);
                        userMapper.updateByPrimaryKeySelective(recipient);
                    }
                }
            }
        }
    }

    @Override
    public Task findTaskById(Integer taskId) throws Exception {
        Task task = taskMapper.selectByPrimaryKey(taskId);
        task.setPublisher(userMapper.selectByPrimaryKey(task.getPublisherId()));
        Integer recipientId = task.getRecipientId();
        if (recipientId != null) {
            task.setRecipient(userMapper.selectByPrimaryKey(recipientId));
        }
        task.setContent(taskContentMapper.selectByPrimaryKey(task.getContentId()));
        Integer addresseeId = task.getContent().getAddresseeId();
        task.getContent().setAddressee(addresseeMapper.selectByPrimaryKey(addresseeId));
        return task;
    }
}