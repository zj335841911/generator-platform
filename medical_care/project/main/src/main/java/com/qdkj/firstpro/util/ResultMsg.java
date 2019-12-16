package com.qdkj.firstpro.util;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Arron Cotter on 2019/5/14.
 */

@ApiModel(value = "返回说明")
public class ResultMsg {
    @ApiModelProperty(value = "状态码(200:调用成功,400:参数有误,500:服务器异常)")
    private String code;
    @ApiModelProperty(value = "详细信息")
    private String message;
    @ApiModelProperty(value = "请参看每个接口Responses里状态码为200的补充说明")
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static ResultMsg success(String message, Object data) {
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setCode("200");
        resultMsg.setMessage(message);
        if (data != null)
            resultMsg.setData(data);

        return resultMsg;
    }

    public static ResultMsg fail(String code, String message) {
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setCode(code);
        resultMsg.setMessage(message);
        return resultMsg;
    }

    public static ResultMsg unSuccess(String message) {
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setMessage(message);
        return resultMsg;
    }

}
