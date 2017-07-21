package com.wingsglory.foru.server.controller;


import com.wingsglory.foru.server.model.PageBean;
import com.wingsglory.foru.server.model.Relation;
import com.wingsglory.foru.server.service.RelationService;
import com.wingsglory.foru.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hezhujun on 2017/6/25.
 */
@RestController
@RequestMapping(value = "/relation")
public class RelationController {
    @Autowired
    private RelationService relationService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/list/concern", method = RequestMethod.POST)
    public Map listConcern(@RequestParam Integer userId,
                           @RequestParam(name = "page", defaultValue = "1") int page,
                           int rows) {
        Map<String, Object> map = new HashMap();
        Result result = new Result();
        try {
            PageBean<Relation> relationPageBean = relationService.listFriend(userId, page, rows);
            map.put("relations", relationPageBean);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErr(e.getMessage());
        }
        map.put("result", result);
        return map;
    }

    @RequestMapping(value = "/list/black", method = RequestMethod.POST)
    public Map listBlack(@RequestParam Integer userId,
                         @RequestParam(name = "page", defaultValue = "1") int page,
                         int rows) {
        Map<String, Object> map = new HashMap();
        Result result = new Result();
        try {
            PageBean<Relation> relationPageBean = relationService.listBack(userId, page, rows);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErr(e.getMessage());
        }
        map.put("result", result);
        return map;
    }

    @RequestMapping(value = "/concern", method = RequestMethod.POST)
    public Map concern(Integer user1Id, Integer user2Id) {
        Map<String, Object> map = new HashMap();
        Result result = new Result();
        try {
            relationService.addFriend(user1Id, user2Id);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErr(e.getMessage());
        }
        map.put("result", result);
        return map;
    }

    @RequestMapping(value = "/black", method = RequestMethod.POST)
    public Map black(Integer user1Id, Integer user2Id) {
        Map<String, Object> map = new HashMap();
        Result result = new Result();
        try {
            relationService.addBlack(user1Id, user2Id);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErr(e.getMessage());
        }
        map.put("result", result);
        return map;
    }

    @RequestMapping(value = "/normal", method = RequestMethod.POST)
    private Map setNormal(Integer user1Id, Integer user2Id) {
        Map<String, Object> map = new HashMap();
        Result result = new Result();
        try {
            relationService.setNormal(user1Id, user2Id);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErr(e.getMessage());
        }
        map.put("result", result);
        return map;
    }

}
