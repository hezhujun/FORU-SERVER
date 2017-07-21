package com.wingsglory.foru.server.controller;

import com.wingsglory.foru.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hezhujun on 2017/6/24.
 */
@RestController
@RequestMapping(value = {"/verification_code"})
public class VerificationCodeController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "phone", method = RequestMethod.POST)
    public Map phone(@RequestParam String phone) {
        Result result = new Result();
        Map<String, Object> map = new HashMap<>();
        try {
            String code = userService.verificationCodeToPhone(phone);
            if ("".equals(code)) {
                result.setSuccess(false);
                result.setErr("验证码为空");
            } else {
                map.put("verification_code", code);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setErr(e.getMessage());
            result.setSuccess(false);
        }
        map.put("result", result);
        return map;
    }

    @RequestMapping(value = "email", method = RequestMethod.POST)
    public Map email(@RequestParam String email) {
        Map<String, Object> map = new HashMap<>();
        Result result = new Result();
        try {
            userService.verificationCodeToEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErr(e.getMessage());
        }
        map.put("result", result);
        return map;
    }

}
