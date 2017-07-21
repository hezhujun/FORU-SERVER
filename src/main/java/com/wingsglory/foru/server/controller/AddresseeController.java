package com.wingsglory.foru.server.controller;

import com.wingsglory.foru.server.model.Addressee;
import com.wingsglory.foru.server.model.PageBean;
import com.wingsglory.foru.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hezhujun on 2017/6/25.
 */
@RestController
@RequestMapping(value = "/addressee")
public class AddresseeController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Map save(@RequestBody Addressee addressee) {
        Map<String, Object> map = new HashMap<>();
        Result result = new Result();
        try {
            addressee = userService.saveAddressee(addressee);
            map.put("addressee", addressee);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErr(e.getMessage());
            e.printStackTrace();
        }
        map.put("result", result);
        return map;
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Map update(@RequestBody Addressee addressee) {
        Map<String, Object> map = new HashMap();
        Result result = new Result();
        try {
            addressee = userService.updateAddressee(addressee);
            map.put("addressee", addressee);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErr(e.getMessage());
        }
        map.put("result", result);
        return map;
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public Map remove(@RequestParam Integer userId, @RequestParam Integer addresseeId) {
        Map<String, Object> map = new HashMap();
        Result result = new Result();
        try {
            Addressee addressee = new Addressee();
            addressee.setUserId(userId);
            addressee.setId(addresseeId);
            userService.removeAddressee(addressee);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErr(e.getMessage());
        }
        map.put("result", result);
        return map;
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Map list(@RequestParam Integer userId,
                    @RequestParam(name = "page", defaultValue = "1") int page,
                    @RequestParam int rows) {
        Map<String, Object> map = new HashMap();
        Result result = new Result();
        try {
            PageBean<Addressee> addresseePageBean = userService.listAddressee(userId, page, rows);
            map.put("addressees", addresseePageBean);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErr(e.getMessage());
        }
        map.put("result", result);
        return map;
    }
}
