package com.wingsglory.foru.server.service;

import com.wingsglory.foru.server.model.PageBean;
import com.wingsglory.foru.server.model.Relation;

/**
 * Created by hezhujun on 2017/7/18.
 */
public interface RelationService {

    void addFriend(Integer user1Id, Integer user2Id);

    void addBlack(Integer user1Id, Integer user2Id);

    PageBean<Relation> listFriend(Integer userId);

    PageBean<Relation> listBack(Integer userId);

    Relation queryRelation(Integer user1Id, Integer user2Id);
}
