package com.wingsglory.foru.server.common;

/**
 * Created by hezhujun on 2017/7/10.
 * 把byte数组转成16进制字符串
 */
public class HexCoder {

    private static final int FOUR = 0xf;

    /**
     * 编码
     * @param input byte
     * @return 对应的16进制字符串
     */
    public static String encode(byte[] input) {
        char[] out = new char[input.length * 2];
        int a;
        for (int i = 0, j = 0; i < input.length; i++) {
            a = input[i] >>> 4 & FOUR;
            out[j++] = toChar(a);
            a = input[i] & FOUR;
            out[j++] = toChar(a);
        }
        return new String(out);
    }

    /**
     * 把10进制转换为16进制
     * @param i 10进制
     * @return
     */
    private static char toChar(int i) {
        if (i >= 10) {
            return (char) ('A' - 10 + i);
        } else {
            return (char) ('0' + i);
        }
    }

    /**
     * 简单的测试
     * @param args
     */
    public static void main(String[] args) {
        byte[] in = new byte[5];
        in[0] = (byte) 0xf1;
        in[1] = (byte) 0xe8;
        in[2] = (byte) 0x6a;
        in[3] = (byte) 0x69;
        in[4] = (byte) 0xb7;
        System.out.println(encode(in));
    }
}
