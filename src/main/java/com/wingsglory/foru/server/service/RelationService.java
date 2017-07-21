package com.wingsglory.foru.server.service;

import com.wingsglory.foru.server.model.PageBean;
import com.wingsglory.foru.server.model.Relation;

/**
 * Created by hezhujun on 2017/7/18.
 */
public interface RelationService {

    void addFriend(Integer user1Id, Integer user2Id) throws Exception;

    void addBlack(Integer user1Id, Integer user2Id) throws Exception;

    PageBean<Relation> listFriend(Integer userId, int page, int rows) throws Exception;

    PageBean<Relation> listBack(Integer userId, int page, int rows) throws Exception;

    Relation queryRelation(Integer user1Id, Integer user2Id) throws Exception;
}
