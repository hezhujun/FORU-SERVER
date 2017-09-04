package com.wingsglory.foru.server.common;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import com.wingsglory.foru.server.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hezhujun on 2017/8/31.
 */
public class JPushUtil {
    private static final Logger logger = LoggerFactory.getLogger(JPushUtil.class);

    public static void sendMessageToUser(int userId, String title, String message, Map<String, String> extra) {
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(userId));
        sendMessage(list, title, message, extra);
    }

    public static void sendMessageToUsers(List<Integer> userIdList, String title, String message, Map<String, String> extra) {
        List<String> list = new ArrayList<>();
        for (Integer userId :
                userIdList) {
            list.add(String.valueOf(userId));
        }
        sendMessage(list, title, message, extra);
    }

    public static void sendMessage(List<String> userIdList, String title, String message, Map<String, String> extra) {
        sendMessage(Audience.alias(userIdList), title, message, extra);
    }

    public static void sendMessageToAll(String title, String message, Map<String, String> extra) {
        sendMessage(Audience.all(), title, message, extra);
    }

    public static void sendMessage(Audience audience, String title, String message, Map<String, String> extra) {
        Message.Builder builder = new Message.Builder()
                .setMsgContent(message)
                .setContentType("json")
                .setTitle(title);
        if (extra != null) {
            for (Map.Entry<String, String> entry :
                    extra.entrySet()) {
                builder.addExtra(entry.getKey(), entry.getValue());
            }
        }

        JPushClient client = new JPushClient(Const.MASTER_SECRET, Const.APP_KEY);
        PushPayload pushPayload = new PushPayload.Builder()
                .setPlatform(Platform.all())
                .setAudience(audience)
                .setMessage(builder.build())
                .build();
        try {
            PushResult result = client.sendPush(pushPayload);
        } catch (APIConnectionException e) {
            e.printStackTrace();
            if (logger.isErrorEnabled()) {
                logger.error("极光推送APIConnectionException", e);
            }
        } catch (APIRequestException e) {
            e.printStackTrace();
            if (logger.isErrorEnabled()) {
                logger.error("极光推送APIRequestException", e);
                logger.error("HTTP Status: " + e.getStatus());
                logger.error("Error Code: " + e.getErrorCode());
                logger.error("Error Message: " + e.getErrorMessage());
            }
        }
    }
}
