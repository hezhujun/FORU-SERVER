package com.wingsglory.foru.server.common;

/**
 * Created by hezhujun on 2017/6/24.
 */
public class VerificationCodeGenerator {
    public static String generate(int num) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < num; i++) {
            result.append((int)(Math.random() * 10));
        }
        return result.toString();
    }
}
