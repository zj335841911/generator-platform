package com.qdkj.firstpro.api.wechat.controller;

import com.qdkj.firstpro.common.apliyun.SmsUtil;
import com.qdkj.firstpro.common.wechat.util.AccessToken;
import com.qdkj.firstpro.common.wechat.util.WeChatUtil;
import com.qdkj.firstpro.api.wechat.service.WeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("wechat")
public class WeUserAuthLoginController {

    @Autowired
    WeUserService us;
    @Autowired
    SmsUtil su;
    private String tempCode;

    /**
     * 授权接口 通过code获取openid
     *
     * @param code
     * @result openid+state  新注册还是老用户
     */
    @RequestMapping("auth")
    public String auth(String code) {
        AccessToken token = WeChatUtil.getAcessToken(code);
        us.saveToken(token.getOpenId());
        String result = us.findUser(token.getOpenId());
        return "{\"token\":" + token.getOpenId() + "," + "\"status:\"" + result;
    }

    @RequestMapping("sms")
    public String sendSms(String tel) {
        int code = (int) (Math.random() * 899999 + 100000);
        tempCode = code + "";
        su.sendSms(tel, code);

        return "OK";
    }

    @RequestMapping("checkSms")
    public String checkSms(String tel, String openid, String code) {
        if (tempCode.equals(code)) {
            us.bindTel(tel, openid);
            return "T";
        } else {
            return "F";
        }
    }

}
