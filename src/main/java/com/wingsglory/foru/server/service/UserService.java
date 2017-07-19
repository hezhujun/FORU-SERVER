package com.wingsglory.foru.server.service;

import com.wingsglory.foru.server.model.Addressee;
import com.wingsglory.foru.server.model.PageBean;
import com.wingsglory.foru.server.model.User;

/**
 * Created by hezhujun on 2017/7/18.
 */
public interface UserService {
    User login(String username, String passwrod) throws Exception;

    User register(User user, String verificationCode) throws Exception;

    String verificationCodeToPhone(String phone) throws Exception;

    String verificationCodeToEmail(String email) throws Exception;

    User updateUser(User user) throws Exception;

    Addressee saveAddressee(Addressee addressee) throws Exception;

    Addressee updateAddressee(Addressee addressee) throws Exception;

    boolean removeAddressee(Integer addresseeId);

    PageBean<Addressee> listAddressee(Integer userId, int page, int rows);
}
