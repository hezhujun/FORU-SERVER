package com.wingsglory.foru.server.common;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * Created by hezhujun on 2017/6/24.
 */
public class SmsNumSendUtil {
    public static String send(String phone, String code) throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "24465442", "13d891293b4b3380488009a51692125e");
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend( "" );
        req.setSmsType( "normal" );
        req.setSmsFreeSignName( "FORU验证码" );
        req.setSmsParamString( "{number:'" + code + "'}" );
        req.setRecNum(phone);
        req.setSmsTemplateCode( "SMS_71216300" );
        AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
        // {"error_response":{"code":29,"msg":"Invalid app Key","sub_code":"isv.appkey-not-exists","request_id":"157dldoj7tr3e"}}
        // {"alibaba_aliqin_fc_sms_num_send_response":{"result":{"err_code":"0","model":"108328717526^1111277877587","success":true},"request_id":"qm2xsck5fkzi"}}
        return rsp.getBody();
    }

    public static void main(String[] args) {
        try {
            send("18932442453", "456789");
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
