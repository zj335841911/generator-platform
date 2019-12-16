package com.qdkj.firstpro.api;

import com.alibaba.fastjson.JSONObject;
import com.qdkj.firstpro.modular.information.service.InformationService;
import com.qdkj.firstpro.sys.modular.system.mapper.Dao;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * 〈一句话功能简述〉<br>
 * 〈微信端接口〉
 *
 * @author lihr
 * @create 2019/9/7
 * @since 1.0.0
 */
@RestController
@Api(tags = "用户挂号接口---lihr")
@RequestMapping("registerApi")
public class RegisterApi {

    @Autowired
    private Dao dao;

    @Autowired
    private InformationService informationService;
    @Autowired
    private RestTemplate restTemplate;


    /**
     * 查询医院列表
     */
    @RequestMapping(value = "getHospital", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getHospital(String directoryType) {
        String url = "http://47.93.216.252:8080/his/comprehensive/catalogue";
        JSONObject postData = new JSONObject();
        postData.put("directoryType", directoryType);
        JSONObject json = restTemplate.postForEntity(url, postData, JSONObject.class).getBody();
        return json;
    }

    /**
     * 查询科室列表
     */
    @GetMapping(value = "/getDepartment")
    @ResponseBody
    public JSONObject getDepartment(String directoryType,String directoryName) {
        String url = "http://47.93.216.252:8080/his/comprehensive/catalogue";
        JSONObject postData = new JSONObject();
        postData.put("directoryType", directoryType);
        postData.put("directoryName", directoryName);
        JSONObject json = restTemplate.postForEntity(url, postData, JSONObject.class).getBody();
        return json;
    }

    /**
     * 查询科室下医生
     */
    @GetMapping(value = "/getDoctor")
    @ResponseBody
    public JSONObject getDoctor(String departmentId) {
        String url = "http://47.93.216.252:8080/his/hospital/doctor/list";
        JSONObject postData = new JSONObject();
        postData.put("departmentId", departmentId);
        JSONObject json = restTemplate.postForEntity(url, postData, JSONObject.class).getBody();
        return json;
    }

    /**
     * 查询排班信息
     */
    @GetMapping(value = "/getScheduling")
    @ResponseBody
    public JSONObject getScheduling(String departmentId) {
        String url = "http://47.93.216.252:8080/his/hospital/doctor/schedule/list";
        JSONObject postData = new JSONObject();
        postData.put("departmentId", departmentId);
        JSONObject json = restTemplate.postForEntity(url, postData, JSONObject.class).getBody();
        return json;
    }



}
