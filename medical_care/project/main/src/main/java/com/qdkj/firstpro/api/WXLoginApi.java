package com.qdkj.firstpro.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qdkj.firstpro.common.apliyun.SmsUtil;
import com.qdkj.firstpro.common.wechat.util.AccessToken;
import com.qdkj.firstpro.common.wechat.util.UserInformation;
import com.qdkj.firstpro.common.wechat.util.WeChatUtil;
import com.qdkj.firstpro.modular.weuser.entity.Weuser;
import com.qdkj.firstpro.modular.weuser.model.params.WeuserParam;
import com.qdkj.firstpro.modular.weuser.service.WeuserService;
import com.qdkj.firstpro.sys.core.util.CacheUtil;
import com.qdkj.firstpro.util.IdentityUtil;
import com.qdkj.firstpro.util.ResultMsg;
import com.qdkj.firstpro.util.Tool;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Api(tags = "微信端统一接口---zj")
@RequestMapping("wxLoginApi")
public class WXLoginApi {
    @Autowired
    private WeuserService weuserService;
    @Autowired
    private SmsUtil smsUtil;

    //已测
    @ApiOperation(value = "微信端---授权认证", notes = "<img src='" + "hylx.png' />授权认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "code", required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            " \"weuser\": \"用户信息\" \n" +
            "    {\n" +
            "        \"id\": \"主键\",\n" +
            "        \"openid\":  用户识别号\n" +
            "         \"session_key\": 用户登录状态 \n" +
            "         \"phone\": 手机号\n" +
            "        },\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "auth",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg auth(String code){
        if (Tool.isNull(code)) return ResultMsg.fail("400","code不能为空");
        AccessToken token = WeChatUtil.getAcessToken(code);
        UserInformation userInformation=WeChatUtil.getUserImformation(token.getToken(),token.getOpenId());
        String nickName=userInformation.getNickname();
        Integer sex=Integer.parseInt(userInformation.getSex());
        Weuser weuser=weuserService.getOne(new QueryWrapper<Weuser>().eq("openid",token.getOpenId()));
        if (weuser==null) {
            WeuserParam weuserParam = new WeuserParam();
            weuserParam.setOpenid(token.getOpenId());
            weuserParam.setSessionKey(token.getToken());
            weuserParam.setName(nickName);
            weuserParam.setSex(sex);
            weuserService.add(weuserParam);
            weuser = weuserService.getOne(new QueryWrapper<Weuser>().eq("openid", token.getOpenId()));
            CacheUtil.put("CONSTANT",token.getOpenId(),token.getOpenId());
            return ResultMsg.success("调用接口成功", weuser);
        }else {
            CacheUtil.put("CONSTANT",token.getOpenId(),token.getOpenId());
            return  ResultMsg.success("调用成功",weuser);
        }
    }

    @ApiOperation(value = "用户端---生成手机验证码", notes = "<img src='" + "hylx.png' />绑定手机号之生成手机验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true),
            @ApiImplicitParam(name = "openId", value = "用户识别号", required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "  ],\n" +
            "  \"code\": \"手机验证码\" \n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "createCode", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg createCode(String phone,String openId) {
        if (Tool.isNull(openId)) return ResultMsg.fail("400", "openId不能为空");
        Weuser weuser = IdentityUtil.identify(openId);
        if (weuser == null) return ResultMsg.fail("403", "非法");
        int code = (int) (Math.random() * 899999 + 100000);
        //向该手机发送短信
        smsUtil.sendSms(phone,code);
        CacheUtil.put("手机验证码","code",code);
        return ResultMsg.success("调用接口成功",code);
    }

    @ApiOperation(value = "用户端---绑定手机号", notes = "<img src='" + "hylx.png' />用户端授权登录后绑定手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true),
            @ApiImplicitParam(name = "openId", value = "用户识别号", required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "addPhone", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg addPhone(String phone,String code,String openId) {
        if (Tool.isNull(openId)) return ResultMsg.fail("400", "openId不能为空");
        Weuser weuser = IdentityUtil.identify(openId);
        if (weuser == null) return ResultMsg.fail("403", "非法");
        if (Tool.isNull(phone)) return ResultMsg.fail("400", "手机号不能为空");
        if (code.equals(CacheUtil.get("手机验证码","code"))){
            weuser.setTel(phone);
            weuserService.update(weuser,new QueryWrapper<Weuser>().eq("id",weuser.getId()));
            return ResultMsg.success("调用接口成功",null);
        }else {
            return ResultMsg.fail("403","验证码不正确");
        }
    }





}
