package com.qdkj.firstpro.modular.tag.service.impl;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qdkj.firstpro.base.pojo.page.LayuiPageFactory;
import com.qdkj.firstpro.base.pojo.page.LayuiPageInfo;
import com.qdkj.firstpro.modular.tag.entity.Tag;
import com.qdkj.firstpro.modular.tag.mapper.TagMapper;
import com.qdkj.firstpro.modular.tag.model.params.TagParam;
import com.qdkj.firstpro.modular.tag.model.result.TagResult;
import  com.qdkj.firstpro.modular.tag.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 医院标签表 服务实现类
 * </p>
 *
 * @author Arron
 * @since 2019-09-07
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public void add(TagParam param){
        Tag entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(TagParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(TagParam param){
        Tag oldEntity = getOldEntity(param);
        Tag newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public TagResult findBySpec(TagParam param){
        return null;
    }

    @Override
    public List<TagResult> findListBySpec(TagParam param){
        return null;
    }

    @Override
    public LayuiPageInfo findPageBySpec(TagParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(TagParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private Tag getOldEntity(TagParam param) {
        return this.getById(getKey(param));
    }

    private Tag getEntity(TagParam param) {
        Tag entity = new Tag();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
