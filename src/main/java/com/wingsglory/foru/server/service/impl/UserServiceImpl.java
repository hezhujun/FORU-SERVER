package com.wingsglory.foru.server.service.impl;

import com.wingsglory.foru.server.common.MD5Coder;
import com.wingsglory.foru.server.common.SmsNumSendUtil;
import com.wingsglory.foru.server.common.VerificationCodeGenerator;
import com.wingsglory.foru.server.dao.AddresseeMapper;
import com.wingsglory.foru.server.dao.UserMapper;
import com.wingsglory.foru.server.dao.VerificationCodeEmailMapper;
import com.wingsglory.foru.server.dao.VerificationCodePhoneMapper;
import com.wingsglory.foru.server.model.*;
import com.wingsglory.foru.server.service.UserService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by hezhujun on 2017/7/18.
 */
@Service("UserService")
public class UserServiceImpl implements UserService {
    private static final String PHONE_REGEX = "0?(13|14|15|18)[0-9]{9}";
    private static final String EMAIL_REGEX = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private VerificationCodePhoneMapper verificationCodePhoneMapper;
    @Autowired
    private VerificationCodeEmailMapper verificationCodeEmailMapper;
    @Autowired
    private AddresseeMapper addresseeMapper;

    @Override
    public User login(String username, String passwrod) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("登录 %s %s", username, passwrod));
        }
        String encoderPassword = MD5Coder.encode(passwrod);
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUsernameEqualTo(username)
                .andPasswordEqualTo(encoderPassword);
        List<User> userList = userMapper.selectByExample(userExample);
        if (userList != null || userList.size() > 0) {
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("登录返回" + userList.get(0)));
            }
            return userList.get(0);
        } else {
            throw new Exception("账号或密码错误");
        }
    }

    @Override
    public User register(User user, String verificationCode) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("注册 %s %s", user.toString(), verificationCode));
        }
        if (user.getUsername() == null || "".equals(user.getUsername().trim())) {
            throw new Exception("账号名不能为空");
        }
        if (user.getPassword() == null || "".equals(user.getPassword().trim())) {
            throw new Exception("密码不能为空");
        }
        String password = user.getPassword();
        String encoderPassword = MD5Coder.encode(password);
        user.setPassword(encoderPassword);
        if (user.getPhone() == null || "".equals(user.getPhone())) {
            throw new Exception("电话号码不能为空");
        }
        String phone = user.getPhone();
        if (!phone.matches(PHONE_REGEX)) {
            throw new Exception("电话号码格式不正确");
        }
        VerificationCodePhone verificationCodePhone = verificationCodePhoneMapper.selectByPrimaryKey(phone);
        if (verificationCodePhone == null) {
            throw new Exception("验证码错误");
        }
        if (verificationCodePhone.getCode().equals(verificationCodePhone.getCode())) {
            Date updateTime = verificationCodePhone.getGmtModified();
            Date now = new Date();
            // 发送验证码到现在过了多少分钟
            long minuteDiff = (now.getTime() - updateTime.getTime()) / 1000 / 60;
            if (minuteDiff > 5) {
                throw new Exception("验证码已过期");
            } else {
                int i = userMapper.insertSelective(user);
                if (!(i == 1)) {
                    throw new Exception("注册失败");
                }
                if (user.getId() == null) {
                    throw new Exception("注册失败");
                }
                User u = userMapper.selectByPrimaryKey(user.getId());
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("注册成功返回" + u.toString()));
                }
                return u;
            }
        } else {
            throw new Exception("验证码不正确");
        }
    }

    @Override
    public String verificationCodeToPhone(String phone) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("发送验证码到" + phone));
        }
        if (!phone.matches(PHONE_REGEX)) {
            throw new Exception("电话号码格式不正确");
        }
        String code = VerificationCodeGenerator.generate(6);
        String resBody = SmsNumSendUtil.send(phone, code);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("发送验证码api返回结果" + resBody));
        }
        JSONObject jsonObject = new JSONObject(resBody);
        if (jsonObject.has("alibaba_aliqin_fc_sms_num_send_response")) {
            JSONObject alibaba = jsonObject.getJSONObject("alibaba_aliqin_fc_sms_num_send_response");
            JSONObject result = alibaba.getJSONObject("result");
            boolean success = result.getBoolean("success");
            if (success) {
                VerificationCodePhone verificationCodePhone = new VerificationCodePhone();
                verificationCodePhone.setPhone(phone);
                verificationCodePhone.setCode(code);
                verificationCodePhone.setGmtModified(new Timestamp(System.currentTimeMillis()));
                int i = verificationCodePhoneMapper.insert(verificationCodePhone);
                if (!(i == 1)) {
                    throw new Exception("保存验证码失败");
                }
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("发送验证码成功，为" + code));
                }
                return code;
            } else {
                throw new Exception("发送验证码失败");
            }
        } else {
            throw new Exception("发送验证码失败");
        }
    }

    @Override
    public String verificationCodeToEmail(String email) throws Exception {
        // TODO
        throw new RuntimeException("未实现");
//        return null;
    }

    @Override
    public User updateUser(User user) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("更新用户信息: " + user.toString());
        }
        if (user.getId() == null) {
            throw new Exception("修改的用户信息不全");
        }
        userMapper.updateByPrimaryKeySelective(user);
        User u = userMapper.selectByPrimaryKey(user.getId());
        if (logger.isDebugEnabled()) {
            logger.debug("更新用户后返回: " + u.toString());
        }
        return u;
    }

    @Override
    public Addressee saveAddressee(Addressee addressee) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("保存收货信息: " + addressee.toString());
        }
        if (addressee.getName() == null || "".equals(addressee.getName())) {
            throw new Exception("收货人不能为空");
        }
        if (addressee.getPhone() == null || "".equals(addressee.getPhone())) {
            throw new Exception("收货人电话号码不能为空");
        }
        if (!addressee.getPhone().matches(PHONE_REGEX)) {
            throw new Exception("收货人电话号码格式不正确");
        }
        if (addressee.getAddress() == null || "".equals(addressee.getAddress())) {
            throw new Exception("收货地址不能为空");
        }
        int i = addresseeMapper.insertSelective(addressee);
        if (!(i == 1)) {
            throw new Exception("保存失败");
        }
        if (addressee.getId() == null) {
            throw new Exception("保存失败");
        }
        Addressee a = addresseeMapper.selectByPrimaryKey(addressee.getId());
        if (logger.isDebugEnabled()) {
            logger.debug("保存收货信息成功：" + a.toString());
        }
        return a;
    }

    @Override
    public Addressee updateAddressee(Addressee addressee) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("更新收货信息" + addressee.toString());
        }
        if (addressee.getId() == null) {
            throw new Exception("修改的收货信息不全");
        }
        addresseeMapper.updateByPrimaryKeySelective(addressee);
        Addressee a = addresseeMapper.selectByPrimaryKey(addressee.getId());
        if (logger.isDebugEnabled()) {
            logger.debug("修改收货信息成功：" + a.toString());
        }
        return a;
    }

    @Override
    public boolean removeAddressee(Integer addresseeId) {
        int i = addresseeMapper.deleteByPrimaryKey(addresseeId);
        boolean r = (i == 1) ? true : false;
        if (logger.isDebugEnabled()) {
            logger.debug("删除收货信息" + addresseeId + " " + r);
        }
        return r;
    }

    @Override
    public PageBean<Addressee> listAddressee(Integer userId, int page, int rows) {
        PageBean<Addressee> addresseePageBean = null;
        AddresseeExample example = new AddresseeExample();
        AddresseeExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        int totalRows = addresseeMapper.countByExample(example);
        if (totalRows == 0) {
            // 没有记录，直接返回
            addresseePageBean = new PageBean<Addressee>(totalRows, rows, 0, null);
        } else {
            example.setOrderByClause("id");
            example.setOffset((page - 1) * rows);
            example.setRows(rows);
            List<Addressee> addresseeList = addresseeMapper.selectByExample(example);
            addresseePageBean = new PageBean<>(totalRows, rows, page, addresseeList);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("返回收货信息列表" + addresseePageBean);
        }
        return addresseePageBean;
    }
}
