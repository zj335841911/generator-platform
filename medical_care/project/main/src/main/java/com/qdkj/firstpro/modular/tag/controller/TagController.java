package com.qdkj.firstpro.modular.tag.controller;

import com.qdkj.firstpro.base.pojo.page.LayuiPageInfo;
import com.qdkj.firstpro.modular.tag.entity.Tag;
import com.qdkj.firstpro.modular.tag.model.params.TagParam;
import com.qdkj.firstpro.modular.tag.service.TagService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 医院标签表控制器
 *
 * @author Arron
 * @Date 2019-09-07 04:05:06
 */
@Controller
@RequestMapping("/tag")
public class TagController extends BaseController {

    private String PREFIX = "/tag";

    @Autowired
    private TagService tagService;

    /**
     * 跳转到主页面
     *
     * @author Arron
     * @Date 2019-09-07
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/tag.html";
    }

    /**
     * 新增页面
     *
     * @author Arron
     * @Date 2019-09-07
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/tag_add.html";
    }

    /**
     * 编辑页面
     *
     * @author Arron
     * @Date 2019-09-07
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/tag_edit.html";
    }

    /**
     * 新增接口
     *
     * @author Arron
     * @Date 2019-09-07
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(TagParam tagParam) {
        this.tagService.add(tagParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Arron
     * @Date 2019-09-07
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(TagParam tagParam) {
        this.tagService.update(tagParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Arron
     * @Date 2019-09-07
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(TagParam tagParam) {
        this.tagService.delete(tagParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Arron
     * @Date 2019-09-07
     */
    @RequestMapping("/detail")
    @ResponseBody
    public ResponseData detail(TagParam tagParam) {
        Tag detail = this.tagService.getById(tagParam.getId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author Arron
     * @Date 2019-09-07
     */
    @ResponseBody
    @RequestMapping("/list")
    public LayuiPageInfo list(TagParam tagParam) {
        return this.tagService.findPageBySpec(tagParam);
    }

}


