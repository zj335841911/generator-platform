/**
 * Copyright (C), 2019-2019, 贵州宏思锐达科技有限公司
 * FileName: ProtalUtil
 * Author:   Arron-Wu
 * Date:     2019/9/5 14:52
 * Description: 微信端接口
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.qdkj.firstpro.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qdkj.firstpro.modular.banner.entity.Banner;
import com.qdkj.firstpro.modular.banner.service.BannerService;
import com.qdkj.firstpro.modular.doctor.service.DoctorService;
import com.qdkj.firstpro.modular.functionBanner.entity.FunctionBanner;
import com.qdkj.firstpro.modular.functionBanner.service.FunctionBannerService;
import com.qdkj.firstpro.modular.hospital.entity.Hospital;
import com.qdkj.firstpro.modular.hospital.service.HospitalService;
import com.qdkj.firstpro.modular.hostype.entity.HosType;
import com.qdkj.firstpro.modular.hostype.service.HosTypeService;
import com.qdkj.firstpro.modular.information.entity.Information;
import com.qdkj.firstpro.modular.information.service.InformationService;
import com.qdkj.firstpro.modular.level.entity.Level;
import com.qdkj.firstpro.modular.level.service.LevelService;
import com.qdkj.firstpro.modular.tag.entity.Tag;
import com.qdkj.firstpro.modular.tag.service.TagService;
import com.qdkj.firstpro.modular.weuser.entity.Weuser;
import com.qdkj.firstpro.sys.modular.system.mapper.Dao;
import com.qdkj.firstpro.util.IdentityUtil;
import com.qdkj.firstpro.util.ResultMsg;
import com.qdkj.firstpro.util.Tool;
import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈微信端接口〉
 *
 * @author Arron
 * @create 2019/9/5
 * @since 1.0.0
 */
@Controller
@Api(tags = "微信端统一接口---Arron")
@RequestMapping("protalApi")
public class ProtalApi {

    @Autowired
    private Dao dao;

    @Autowired
    private BannerService bannerService;

    @Autowired
    private FunctionBannerService functionBannerService;

    @Autowired
    private InformationService informationService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private LevelService levelService;

    @Autowired
    private HosTypeService hosTypeService;

    @Autowired
    private TagService tagService;

    //已测
    @ApiOperation(value = "微信端---首页", notes = "<img src='" + "hylx.png' />平台的主页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openId", value = "openId", required = true),
            @ApiImplicitParam(name = "lat", value = "维度", required = true),
            @ApiImplicitParam(name = "lng", value = "经度", required = true),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            " \"shopServiceLst\": \"店铺所有的的服务集合\" \n" +
            "    {\n" +
            "        \"id\": \"主键\",\n" +
            "        \"avatar\":  头像\n" +
            "         \"userName\": 用户的名称 \n" +
            "         \"reservationTime\": 预约时间\n" +
            "         \"phone\": 手机号\n" +
            "        },\n" +
            " \"shopId\": \"店铺的id\" \n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "getIndex", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getIndex(String openId,String lat,String lng) {
        if (Tool.isNull(openId)) return  ResultMsg.fail("400","openId不能为空");
        Weuser identify = IdentityUtil.identify(openId);
        if (identify==null) return ResultMsg.fail("403","非法！");
        HashMap<String, Object> resultMap = new HashMap<>();
        //医院列表
        List<Map<String, Object>> mapList = dao.selectBySQL("SELECT\n" +
                "\ta.id,\n" +
                "\ta.img,\n" +
                "\ta.hospital_name,\n" +
                "\tb.`name` hospital_level,\n" +
                "\tIFNULL(( SELECT AVG( h_score ) FROM t_remark d WHERE d.hospital_id = a.id ), 3 ) score,\n" +
                "IF\n" +
                "\t( c.user_id = "+identify.getId()+" AND c.hospital_id = a.id, 1, 0 ) is_follow,\n" +
                "\ta.mobile,\n" +
                "\ta.good_dept, \n" +
                "\tROUND(\n" +
                "\t\t6378.138 * 2 * ASIN(\n" +
                "\t\t\tSQRT(\n" +
                "\t\t\t\tPOW( SIN( ( "+lat+" * PI() / 180 - latitude * PI() / 180 ) / 2 ), 2 ) + COS( "+lat+" * PI() / 180 ) * COS( latitude * PI() / 180 ) * POW( SIN( ( "+lng+" * PI() / 180 - longitude * PI() / 180 ) / 2 ), 2 ) \n" +
                "\t\t\t) \n" +
                "\t\t) * 1000 \n" +
                "\t) AS distance \n" +
                "FROM\n" +
                "\tt_hospital a\n" +
                "\tLEFT JOIN t_level b ON a.level_id = b.id\n" +
                "\tLEFT JOIN t_follow c ON a.id = c.hospital_id limit 0,3");
        //医生列表
        List<Map<String, Object>> maps = dao.selectBySQL("SELECT\n" +
                "\ta.id,\n" +
                "\ta.avatar,\n" +
                "\ta.`name`,\n" +
                "\tb.hospital_name,\n" +
                "\tIFNULL(( SELECT AVG( h_score ) FROM t_remark d WHERE d.hospital_id = a.id ), 3 ) score,\n" +
                "IF\n" +
                "\t( c.user_id = "+identify.getId()+" AND c.hospital_id = a.id, 1, 0 ) is_follow,\n" +
                "\ta.good_at,\n" +
                "\ta.job_title \n" +
                "FROM\n" +
                "\tt_doctor a\n" +
                "\tLEFT JOIN t_hospital b ON a.hospital_id = b.id\n" +
                "\tLEFT JOIN t_follow c ON a.id = c.followed_id limit 0,3");
        List<Information> informationList = informationService.list(new QueryWrapper<Information>().eq("role", "0"));
        HashMap<String, Object> map = new HashMap<>();
        map.put("hospitalList",mapList);
        map.put("docterList",maps);
        map.put("informationList",informationList);
        return ResultMsg.success("调用成功", map);
    }


    //
    @ApiOperation(value = "微信端---搜索医院", notes = "<img src='" + "hylx.png' />点击搜索医院的输入框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openId", value = "openId", required = true),
            @ApiImplicitParam(name = "area", value = "地区的拼串  如：四川省成都市锦江区", required = false),
            @ApiImplicitParam(name = "sort", value = "智能排序 score/好评优先 distance/距离有限  score,distance/综合排序", required = false),
            @ApiImplicitParam(name = "level_id", value = "等级的id", required = false),
            @ApiImplicitParam(name = "type_id", value = "医院类型的id", required = false),
            @ApiImplicitParam(name = "tag_id", value = "医院标签的id", required = false),
            @ApiImplicitParam(name = "pageNo", value = "分页页码 不传默认为0,第一次进页面可不传,数据需要分页需要使用", required = false),
            @ApiImplicitParam(name = "pageSize", value = "分页页大小 不传默认为10,数据需要分页需要使用", required = false),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            " \"shopServiceLst\": \"店铺所有的的服务集合\" \n" +
            "    {\n" +
            "        \"id\": \"主键\",\n" +
            "        \"avatar\":  头像\n" +
            "         \"userName\": 用户的名称 \n" +
            "         \"reservationTime\": 预约时间\n" +
            "         \"phone\": 手机号\n" +
            "        },\n" +
            " \"shopId\": \"店铺的id\" \n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "searchHospital", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg searchHospital(String openId,String area,String sort,String level_id,String type_id,String tag_id,String lat,String lng,@RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                    @RequestParam(name = "pageNo", defaultValue = "0", required = false) Integer pageNo) {
        if (Tool.isNull(openId)) return  ResultMsg.fail("400","openId不能为空");
        Weuser identify = IdentityUtil.identify(openId);
        if (identify==null) return ResultMsg.fail("403","非法！");
        StringBuffer stringBuffer = new StringBuffer("where ");
        StringBuffer sb = new StringBuffer();
        if (!Tool.isNull(area)) stringBuffer.append(" CONCAT(province,city,region) = '"+area+"' ");
        if (!Tool.isNull(level_id) && !Tool.isNull(type_id) && !Tool.isNull(tag_id)) stringBuffer.append(" level_id = "+level_id + " AND type = "+ type_id + " and tag_id = " +tag_id);
        if (Tool.isNull(level_id) && !Tool.isNull(type_id) && !Tool.isNull(tag_id)) stringBuffer.append(" type = "+ type_id + " and tag_id = " +tag_id);
        if (!Tool.isNull(level_id) && Tool.isNull(type_id) && !Tool.isNull(tag_id)) stringBuffer.append(" level_id = "+level_id + " AND tag_id = " +tag_id);
        if (!Tool.isNull(level_id) && !Tool.isNull(type_id) && Tool.isNull(tag_id)) stringBuffer.append(" level_id = "+level_id + " AND type = "+ type_id);
        if (!Tool.isNull(level_id) && Tool.isNull(type_id) && Tool.isNull(tag_id)) stringBuffer.append(" level_id = "+level_id );
        if (Tool.isNull(level_id) && !Tool.isNull(type_id) && Tool.isNull(tag_id)) stringBuffer.append("  type = "+ type_id );
        if (Tool.isNull(level_id) && Tool.isNull(type_id) && !Tool.isNull(tag_id)) stringBuffer.append(" tag_id in (" +tag_id+")");
        if ( !Tool.isNull(sort) && Tool.isNull(area) || Tool.isNull(level_id) || Tool.isNull(type_id) || Tool.isNull(tag_id)) sb.append(" ORDER BY "+sort );
        else stringBuffer.append(" ORDER BY "+sort );
        //医院列表
        List<Map<String, Object>> mapList = dao.selectBySQL("SELECT\n" +
                "\ta.id,\n" +
                "\ta.img,\n" +
                "\ta.hospital_name,\n" +
                "\tb.`name` hospital_level,\n" +
                "\tIFNULL(( SELECT AVG( h_score ) FROM t_remark d WHERE d.hospital_id = a.id ), 3 ) score,\n" +
                "IF\n" +
                "\t( c.user_id = "+identify.getId()+" AND c.hospital_id = a.id, 1, 0 ) is_follow,\n" +
                "\ta.mobile,\n" +
                "\ta.good_dept, \n" +
                "\tROUND(\n" +
                "\t\t6378.138 * 2 * ASIN(\n" +
                "\t\t\tSQRT(\n" +
                "\t\t\t\tPOW( SIN( ( "+lat+" * PI() / 180 - latitude * PI() / 180 ) / 2 ), 2 ) + COS( "+lat+" * PI() / 180 ) * COS( latitude * PI() / 180 ) * POW( SIN( ( "+lng+" * PI() / 180 - longitude * PI() / 180 ) / 2 ), 2 ) \n" +
                "\t\t\t) \n" +
                "\t\t) * 1000 \n" +
                "\t) AS distance \n" +
                "FROM\n" +
                "\tt_hospital a\n" +
                "\tLEFT JOIN t_level b ON a.level_id = b.id\n" +
                "\tLEFT JOIN t_follow c ON a.id = c.hospital_id "+(Tool.isNull(sb+"")?stringBuffer:sb)+ " limit " + pageNo * pageSize + "," + pageSize + "");
        //省级集合
        List<Map<String, Object>> shengList = dao.selectBySQL("select * from sys_area where pid=0");
        HashMap<String, Object> resultMap = new HashMap<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("resultList",mapList);
        map.put("proList",shengList);
        return ResultMsg.success("调用成功", map);
    }

    /**
     * 获取地区三级联动接口
     */
    @RequestMapping("getAreaByPid")
    @ResponseBody
    public Object getAreaByPid(String pid){
        return dao.selectBySQL("select * from sys_area where pid="+pid);
    }

    @ApiOperation(value = "微信端---搜索医院筛选页数据源", notes = "<img src='" + "hylx.png' />搜索医院筛选页数据源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openId", value = "openId", required = true),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            " \"shopServiceLst\": \"店铺所有的的服务集合\" \n" +
            "    {\n" +
            "        \"id\": \"主键\",\n" +
            "        \"avatar\":  头像\n" +
            "         \"userName\": 用户的名称 \n" +
            "         \"reservationTime\": 预约时间\n" +
            "         \"phone\": 手机号\n" +
            "        },\n" +
            " \"shopId\": \"店铺的id\" \n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "getLevelAndTypeAndTag", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getLevelAndTypeAndTag(String openId) {
        if (Tool.isNull(openId)) return  ResultMsg.fail("400","openId不能为空");
        Weuser identify = IdentityUtil.identify(openId);
        if (identify==null) return ResultMsg.fail("403","非法！");
        List<Level> levels = levelService.list();
        List<HosType> types = hosTypeService.list();
        List<Tag> tags = tagService.list();
        HashMap<String, Object> resultMap = new HashMap<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("levelList",levels);
        map.put("types",types);
        map.put("tags",tags);
        return ResultMsg.success("调用成功", map);
    }


    @ApiOperation(value = "微信端---医院详情", notes = "<img src='" + "hylx.png' />医院详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openId", value = "openId", required = true),
            @ApiImplicitParam(name = "hid", value = "医院的id", required = true),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            " \"shopServiceLst\": \"店铺所有的的服务集合\" \n" +
            "    {\n" +
            "        \"id\": \"主键\",\n" +
            "        \"avatar\":  头像\n" +
            "         \"userName\": 用户的名称 \n" +
            "         \"reservationTime\": 预约时间\n" +
            "         \"phone\": 手机号\n" +
            "        },\n" +
            " \"shopId\": \"店铺的id\" \n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "hospitalDetail", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg hospitalDetail(String openId,String id) {
        if (Tool.isNull(openId)) return  ResultMsg.fail("400","openId不能为空");
        Weuser identify = IdentityUtil.identify(openId);
        if (identify==null) return ResultMsg.fail("403","非法！");
        List<Map<String, Object>> maps = dao.selectBySQL("SELECT\n" +
                "\ta.id,\n" +
                "\ta.img,\n" +
                "\ta.hospital_name,\n" +
                "\ta.more_address,\n" +
                "\ta.introduce,\n" +
                "\tb.`name` hospital_level,\n" +
                "\tIFNULL(( SELECT AVG( h_score ) FROM t_remark d WHERE d.hospital_id = a.id ), 3 ) score,\n" +
                "\t(SELECT COUNT(1) FROM t_follow WHERE hospital_id = a.id) followerNum\n" +
                " FROM\n" +
                "\tt_hospital a\n" +
                "\tLEFT JOIN t_level b ON a.level_id = b.id\n" +
                "\tWHERE a.id =" + id);
        List<Information> informationList = informationService.list(new QueryWrapper<Information>().eq("role", "1").eq("author_id",id));
        //从his拿取科室信息

        HashMap<String, Object> map = new HashMap<>();
        if (!Tool.listIsNull(maps)) map.put("hospitalInfo",maps.get(0));
        map.put("infoList",informationList);
        return ResultMsg.success("调用成功", map);
    }



    //已测
    @ApiOperation(value = "微信端---搜索医生", notes = "<img src='" + "hylx.png' />点击搜索医生的输入框，进入搜索主页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openId", value = "openId", required = true),
            @ApiImplicitParam(name = "pageNo", value = "分页页码 不传默认为0,第一次进页面可不传,数据需要分页需要使用", required = false),
            @ApiImplicitParam(name = "pageSize", value = "分页页大小 不传默认为10,数据需要分页需要使用", required = false),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            " \"shopServiceLst\": \"店铺所有的的服务集合\" \n" +
            "    {\n" +
            "        \"id\": \"主键\",\n" +
            "        \"avatar\":  头像\n" +
            "         \"userName\": 用户的名称 \n" +
            "         \"reservationTime\": 预约时间\n" +
            "         \"phone\": 手机号\n" +
            "        },\n" +
            " \"shopId\": \"店铺的id\" \n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "searchDoctor", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg searchDoctor(String openId,@RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                  @RequestParam(name = "pageNo", defaultValue = "0", required = false) Integer pageNo) {
        if (Tool.isNull(openId)) return  ResultMsg.fail("400","openId不能为空");
        Weuser identify = IdentityUtil.identify(openId);
        if (identify==null) return ResultMsg.fail("403","非法！");
        //医生列表
        List<Map<String, Object>> maps = dao.selectBySQL("SELECT\n" +
                "\ta.id,\n" +
                "\ta.avatar,\n" +
                "\ta.`name`,\n" +
                "\tb.hospital_name,\n" +
                "\tIFNULL(( SELECT AVG( h_score ) FROM t_remark d WHERE d.hospital_id = a.id ), 3 ) score,\n" +
                "IF\n" +
                "\t( c.user_id = "+identify.getId()+" AND c.hospital_id = a.id, 1, 0 ) is_follow,\n" +
                "\ta.good_at,\n" +
                "\ta.job_title \n" +
                "FROM\n" +
                "\tt_doctor a\n" +
                "\tLEFT JOIN t_hospital b ON a.hospital_id = b.id\n" +
                "\tLEFT JOIN t_follow c ON a.id = c.followed_id "+ " limit " + pageNo * pageSize + "," + pageSize + "");
        HashMap<String, Object> resultMap = new HashMap<>();
        HashMap<String, Object> map = new HashMap<>();
        return ResultMsg.success("调用成功", map);
    }

    //更多资讯
    @ApiOperation(value = "微信端---更多资讯", notes = "<img src='" + "hylx.png' />更多资讯")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openId", value = "openId", required = true),
            @ApiImplicitParam(name = "role", value = "角色 0/平台 1/医院 2、医生", required = false),
            @ApiImplicitParam(name = "id", value = "医生或医院id", required = false),
            @ApiImplicitParam(name = "pageNo", value = "分页页码 不传默认为0,第一次进页面可不传,数据需要分页需要使用", required = false),
            @ApiImplicitParam(name = "pageSize", value = "分页页大小 不传默认为10,数据需要分页需要使用", required = false),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            " \"shopServiceLst\": \"店铺所有的的服务集合\" \n" +
            "    {\n" +
            "        \"id\": \"主键\",\n" +
            "        \"avatar\":  头像\n" +
            "         \"userName\": 用户的名称 \n" +
            "         \"reservationTime\": 预约时间\n" +
            "         \"phone\": 手机号\n" +
            "        },\n" +
            " \"shopId\": \"店铺的id\" \n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "moreInformation", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg moreInformation(String openId,String role,String id,@RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                     @RequestParam(name = "pageNo", defaultValue = "0", required = false) Integer pageNo) {
        if (Tool.isNull(openId)) return  ResultMsg.fail("400","openId不能为空");
        Weuser identify = IdentityUtil.identify(openId);
        if (identify==null) return ResultMsg.fail("403","非法！");
        List<Map<String, Object>> maps = null;
        if ("0".equals(role)) {
             maps = dao.selectBySQL("SELECT id,\n" +
                    "\tinfo_title,\n" +
                    "\timg,\n" +
                    "\tbrowse_num,\n" +
                    "\tgood_num \n" +
                    "FROM\n" +
                    "\tt_information \n" +
                    "WHERE\n" +
                    "\trole = 0" + " limit " + pageNo * pageSize + "," + pageSize + "");
        }else {
            maps = dao.selectBySQL("SELECT id,\n" +
                    "\tinfo_title,\n" +
                    "\timg,\n" +
                    "\tbrowse_num,\n" +
                    "\tgood_num \n" +
                    "FROM\n" +
                    "\tt_information \n" +
                    "WHERE\n" +
                    "\trole = "+role+" \n" +
                    "\tAND author_id = "+ id + " limit " + pageNo * pageSize + "," + pageSize + "");
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("informationList",maps);
        return ResultMsg.success("调用成功", resultMap);
    }

}