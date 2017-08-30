package com.wingsglory.foru.server.controller;

import com.wingsglory.foru.server.Const;
import com.wingsglory.foru.server.model.User;
import com.wingsglory.foru.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hezhujun on 2017/6/24.
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/sign_up", method = RequestMethod.POST)
    public Map signUp(@RequestParam String phone,
                      @RequestParam String password,
                      @RequestParam String username,
                      @RequestParam String verificationCode) {
        Map<String, Object> map = new HashMap();
        Result result = new Result();
        User user = new User();
        user.setPhone(phone);
        user.setUsername(username);
        user.setPassword(password);
        user.setProtraitUrl(Const.DEFAULT_IMAGE_URL);
        try {
            user = userService.register(user, verificationCode);
            map.put("user", user);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErr(e.getMessage());
        }
        map.put("result", result);
        return map;
    }

    @RequestMapping(value = "/sign_in", method = RequestMethod.POST)
    public Map signIn(@RequestParam String phone, @RequestParam String password) {
        Map<String, Object> map = new HashMap<>();
        Result result = new Result();
        try {
            User user = userService.login(phone, password);
            map.put("user", user);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErr(e.getMessage());
            return map;
        }
        map.put("result", result);
        return map;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json")
    public Map updateUser(@RequestBody User user) {
        Map<String, Object> map = new HashMap();
        Result result = new Result();
        try {
            user = userService.updateUser(user);
            map.put("user", user);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErr(e.getMessage());
        }
        map.put("result", result);
        return map;
    }

    @RequestMapping(value = "/position", method = RequestMethod.POST)
    public Map updateUserPosition(Integer userId, BigDecimal latitude, BigDecimal longitude) {
        Map<String, Object> map = new HashMap();
        Result result = new Result();
        try {
            User user = new User();
            user.setId(userId);
            user.setLatitude(latitude);
            user.setLongitude(longitude);
            userService.updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErr(e.getMessage());
        }
        map.put("result", result);
        return map;
    }
}
