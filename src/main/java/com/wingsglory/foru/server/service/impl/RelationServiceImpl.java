package com.wingsglory.foru.server.service.impl;

import com.wingsglory.foru.server.dao.RelationMapper;
import com.wingsglory.foru.server.dao.UserMapper;
import com.wingsglory.foru.server.model.*;
import com.wingsglory.foru.server.service.RelationService;
import com.wingsglory.foru.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hezhujun on 2017/7/20.
 */
@Service("RelationService")
public class RelationServiceImpl implements RelationService {
    private static final Logger logger = LoggerFactory.getLogger(RelationServiceImpl.class);

    @Autowired
    private RelationMapper relationMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public void addFriend(Integer user1Id, Integer user2Id) throws Exception {
        RelationKey key = new Relation();
        key.setUser1Id(user1Id);
        key.setUser2Id(user2Id);
        Relation relation = relationMapper.selectByPrimaryKey(key);
        if (relation == null) {
            relation = new Relation();
            relation.setUser1Id(user1Id);
            relation.setUser2Id(user2Id);
            relation.setRelation(Relation.RELATION_FRIEND);
        } else {
            relation.setRelation(Relation.RELATION_FRIEND);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(user1Id + "关注" + user2Id);
        }
    }

    @Override
    public void addBlack(Integer user1Id, Integer user2Id) throws Exception {
        RelationKey key = new Relation();
        key.setUser1Id(user1Id);
        key.setUser2Id(user2Id);
        Relation relation = relationMapper.selectByPrimaryKey(key);
        if (relation == null) {
            relation = new Relation();
            relation.setUser1Id(user1Id);
            relation.setUser2Id(user2Id);
            relation.setRelation(Relation.RELATION_BLACK);
        } else {
            relation.setRelation(Relation.RELATION_BLACK);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(user1Id + "拉黑" + user2Id);
        }
    }

    @Override
    public void setNormal(Integer user1Id, Integer user2Id) throws Exception {
        RelationKey key = new Relation();
        key.setUser1Id(user1Id);
        key.setUser2Id(user2Id);
        Relation relation = relationMapper.selectByPrimaryKey(key);
        if (relation == null) {
            relation = new Relation();
            relation.setUser1Id(user1Id);
            relation.setUser2Id(user2Id);
            relation.setRelation(Relation.RELATION_NORMAL);
        } else {
            relation.setRelation(Relation.RELATION_NORMAL);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(user1Id + "不再关注或拉黑" + user2Id);
        }
    }

    @Override
    public PageBean<Relation> listFriend(Integer userId, int page, int rows) throws Exception {
        PageBean<Relation> relationPageBean = null;
        RelationExample example = new RelationExample();
        RelationExample.Criteria criteria = example.createCriteria();
        criteria.andUser1IdEqualTo(userId);
        criteria.andRelationEqualTo(Relation.RELATION_FRIEND);
        int totalRows = relationMapper.countByExample(example);
        if (totalRows == 0) {
            relationPageBean = new PageBean<Relation>(0, rows, page);
        } else {
            example.setOffset((page - 1) * rows);
            example.setRows(rows);
            List<Relation> relationList = relationMapper.selectByExample(example);
            for (Relation relation :
                    relationList) {
                relation.setUser1(userMapper.selectByPrimaryKey(relation.getUser1Id()));
                relation.setUser2(userMapper.selectByPrimaryKey(relation.getUser2Id()));
            }
            relationPageBean = new PageBean<Relation>(totalRows, rows, page, relationList);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("获取" + userId + "的关注列表" + relationPageBean);
        }
        return relationPageBean;
    }

    @Override
    public PageBean<Relation> listBack(Integer userId, int page, int rows) throws Exception {
        PageBean<Relation> relationPageBean = null;
        RelationExample example = new RelationExample();
        RelationExample.Criteria criteria = example.createCriteria();
        criteria.andUser1IdEqualTo(userId);
        criteria.andRelationEqualTo(Relation.RELATION_BLACK);
        int totalRows = relationMapper.countByExample(example);
        if (totalRows == 0) {
            relationPageBean = new PageBean<Relation>(0, rows, page);
        } else {
            example.setOffset((page - 1) * rows);
            example.setRows(rows);
            List<Relation> relationList = relationMapper.selectByExample(example);
            for (Relation relation :
                    relationList) {
                relation.setUser1(userMapper.selectByPrimaryKey(relation.getUser1Id()));
                relation.setUser2(userMapper.selectByPrimaryKey(relation.getUser2Id()));
            }
            relationPageBean = new PageBean<Relation>(totalRows, rows, page, relationList);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("获取" + userId + "的拉黑列表" + relationPageBean);
        }
        return relationPageBean;
    }

    @Override
    public Relation queryRelation(Integer user1Id, Integer user2Id) throws Exception {
        RelationKey key = new RelationKey();
        key.setUser1Id(user1Id);
        key.setUser2Id(user2Id);
        Relation relation = relationMapper.selectByPrimaryKey(key);
        if (relation == null) {
            relation.setUser1Id(user1Id);
            relation.setUser2Id(user2Id);
            relation.setInteractionCount(0);
            relation.setRelation(Relation.RELATION_NORMAL);
            relationMapper.insertSelective(relation);
        }
        relation.setUser1(userMapper.selectByPrimaryKey(user1Id));
        relation.setUser2(userMapper.selectByPrimaryKey(user2Id));
        if (logger.isDebugEnabled()) {
            logger.debug(user1Id + "和" + user2Id + "的关系是：" + relation);
        }
        return relation;
    }
}
