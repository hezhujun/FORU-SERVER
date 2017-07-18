package com.wingsglory.foru.server.service;

import com.wingsglory.foru.server.model.Addressee;
import com.wingsglory.foru.server.model.PageBean;
import com.wingsglory.foru.server.model.User;

/**
 * Created by hezhujun on 2017/7/18.
 */
public interface UserService {
    User login(String username, String passwrod);

    User register(User user);

    String verificationCodeToPhone(String phone);

    String verificationCodeToEmail(String email);

    User updateUser(User user);

    Addressee saveAddressee(Addressee addressee);

    Addressee updateAddressee(Addressee addressee);

    boolean removeAddressee(Integer addresseeId);

    PageBean<Addressee> listAddressee(Integer userId, int page, int rows);
}
