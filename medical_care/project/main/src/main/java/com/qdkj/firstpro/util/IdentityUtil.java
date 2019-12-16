/**
 * Copyright (C), 2019-2019, 贵州宏思锐达科技有限公司
 * FileName: IdentityUtil
 * Author:   Arron-Wu
 * Date:     2019/8/29 11:49
 * Description: 身份鉴权接口
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.qdkj.firstpro.util;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qdkj.firstpro.modular.weuser.entity.Weuser;
import com.qdkj.firstpro.modular.weuser.service.WeuserService;
import com.qdkj.firstpro.sys.core.util.CacheUtil;
import com.qdkj.firstpro.sys.modular.system.mapper.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈身份鉴权接口〉
 *
 * @author Arron
 * @create 2019/8/29
 * @since 1.0.0
 */
@Configuration
public class IdentityUtil {

    private static WeuserService plaUserService;

    private static Dao dao;

    @Autowired
    public void setPlaUserService(WeuserService plaUserService) {
        IdentityUtil.plaUserService = plaUserService;
    }

    @Autowired
    public void setDao(Dao dao) {
        IdentityUtil.dao = dao;
    }

    //用户全局鉴权
    public static Weuser identify(String oppenId){
        Weuser constant = CacheUtil.get("CONSTANT", oppenId);
        if (constant == null){
            //鉴权
            com.qdkj.firstpro.modular.weuser.entity.Weuser weuser = plaUserService.getOne(new QueryWrapper<com.qdkj.firstpro.modular.weuser.entity.Weuser>().eq("openid", oppenId));
            if (weuser == null){
                return null;
            }else {
                CacheUtil.put("CONSTANT",oppenId,weuser);
                return  weuser;
            }
        }else {
            return constant;
        }
    }

    //商户鉴权
    public static int getShopId(String oppenId){
        Integer constant = CacheUtil.get("CONSTANT", oppenId+"shop");
        if (constant == null){
            //鉴权
            List<Map<String, Object>> maps = dao.selectBySQL("SELECT\n" +
                    "\tb.id \n" +
                    "FROM\n" +
                    "\tt_pla_user a,\n" +
                    "\tt_shop b \n" +
                    "WHERE\n" +
                    "\tb.owner_id = a.id \n" +
                    "\tAND a.id =" + 1);
            if (Tool.listIsNull(maps)){
                return 0;
            }else {
                int id = Integer.parseInt(maps.get(0).get("id").toString());
                CacheUtil.put("CONSTANT",oppenId+"shop",id);
                return  id;
            }
        }else {
            return constant;
        }
    }

}