package com.wingsglory.foru.server.service.impl;

import com.wingsglory.foru.server.dao.*;
import com.wingsglory.foru.server.model.*;
import com.wingsglory.foru.server.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
        i = taskContentMapper.insertSelective(taskContent);
        if (!(i == 1) || taskContent.getId() == null) {
            throw new Exception("保存任务内容失败");
        }
        // 最后保存任务
        i = taskMapper.insertSelective(task);
        if (!(i == 1) || task.getId() == null) {
            throw new Exception("保存任务失败");
        }
        Task t = taskMapper.selectByPrimaryKey(task.getId());
        t.setPublisher(userMapper.selectByPrimaryKey(t.getPublisherId()));
        t.setContent(taskContentMapper.selectByPrimaryKey(t.getContentId()));
        t.getContent().setAddressee(addresseeMapper.selectByPrimaryKey(t.getContent().getAddresseeId()));
        if (logger.isDebugEnabled()) {
            logger.debug("保存任务信息成功：" + t.toString());
        }
        return t;
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
        if (!Task.TASK_STATE_NEW.equals(theTask.getState())) {
            throw new Exception("该任务所在状态不能接受");
        }
        if (task.getRecipientId() == null) {
            throw new Exception("接受人ID不能为空");
        }
        task.setState(Task.TASK_STATE_WAIT_FOR_COMPLETE);
        int i = taskMapper.updateByPrimaryKeySelective(task);
        if (!(i == 1)) {
            throw new Exception("操作失败");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("用户" + task.getRecipientId() + "接受任务" + task.getId());
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
        if (logger.isDebugEnabled()) {
            logger.debug("用户" + task.getRecipientId() + "放弃任务" + task.getId());
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
        if (logger.isDebugEnabled()) {
            logger.debug("用户" + task.getRecipientId() + "完成任务" + task.getId());
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
            throw new Exception("该任务所在状态不能删除");
        }
        if (!theTask.getPublisherId().equals(task.getPublisherId())) {
            throw new Exception("你没有发布此任务，不能操作");
        }
        task.setState(Task.TASK_STATE_COMPLETE);
        int i = taskMapper.updateByPrimaryKeySelective(task);
        if (!(i == 1)) {
            throw new Exception("操作失败");
        }
        // 更新双方的关系
        RelationKey key = new RelationKey(task.getPublisherId(), task.getRecipientId());
        Relation relation = relationMapper.selectByPrimaryKey(key);
        if (relation == null) {
            relation = new Relation(task.getPublisherId(), task.getRecipientId());
            relation.setInteractionCount(1);
            relation.setRelation(Relation.RELATION_NORMAL);
            relationMapper.insertSelective(relation);
        } else {
            relation.setInteractionCount(relation.getInteractionCount() + 1);
            relationMapper.updateByPrimaryKeySelective(relation);
        }
        key = null;
        relation = null;
        key = new RelationKey(task.getRecipientId(), task.getPublisherId());
        relation = relationMapper.selectByPrimaryKey(key);
        if (relation == null) {
            relation = new Relation(task.getRecipientId(), task.getPublisherId());
            relation.setInteractionCount(1);
            relation.setRelation(Relation.RELATION_NORMAL);
            relationMapper.insertSelective(relation);
        } else {
            relation.setInteractionCount(relation.getInteractionCount() + 1);
            relationMapper.updateByPrimaryKeySelective(relation);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("用户" + task.getPublisherId() + "删除任务" + task.getId());
        }
    }

    @Transactional
    @Override
    public void delete(Task task) throws Exception {
        if (task.getId() == null) {
            throw new Exception("任务Id不能为空");
        }
        Task theTask = taskMapper.selectByPrimaryKey(task.getId());
        if (!Task.TASK_STATE_NEW.equals(theTask.getState())) {
            throw new Exception("该任务所在状态不能确认完成");
        }
        if (!theTask.getPublisherId().equals(task.getPublisherId())) {
            throw new Exception("你没有发布此任务，不能操作");
        }
        theTask.setState(Task.TASK_STATE_DELETE);
        TaskContent content = taskContentMapper.selectByPrimaryKey(theTask.getContentId());
        int i = taskMapper.deleteByPrimaryKey(theTask.getId());
        if (!(i == 1)) {
            throw new Exception("操作失败");
        }
        i = taskContentMapper.deleteByPrimaryKey(content.getId());
        if (!(i == 1)) {
            throw new Exception("操作失败");
        }
        i = addresseeMapper.deleteByPrimaryKey(content.getAddresseeId());
        if (!(i == 1)) {
            throw new Exception("操作失败");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("用户" + task.getPublisherId() + "完成任务" + task.getId());
        }
    }

    @Transactional
    @Override
    public PageBean<Task> listTask(Integer userId, BigDecimal latitude, BigDecimal longitude, int page, int rows) {
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
            for (Task task :
                    taskList) {
                task.setPublisher(userMapper.selectByPrimaryKey(task.getPublisherId()));
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
    public PageBean<Task> listPublishTask(Integer userId, int page, int rows) {
        PageBean<Task> taskPageBean = null;
        TaskExample example = new TaskExample();
        TaskExample.Criteria criteria = example.createCriteria();
        criteria.andPublisherIdEqualTo(userId);
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
    public PageBean<Task> listAcceptTask(Integer userId, int page, int rows) {
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
}
