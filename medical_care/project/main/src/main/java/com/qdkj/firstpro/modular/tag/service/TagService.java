package com.qdkj.firstpro.modular.tag.service;

import com.qdkj.firstpro.base.pojo.page.LayuiPageInfo;
import com.qdkj.firstpro.modular.tag.entity.Tag;
import com.qdkj.firstpro.modular.tag.model.params.TagParam;
import com.qdkj.firstpro.modular.tag.model.result.TagResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 医院标签表 服务类
 * </p>
 *
 * @author Arron
 * @since 2019-09-07
 */
public interface TagService extends IService<Tag> {

    /**
     * 新增
     *
     * @author Arron
     * @Date 2019-09-07
     */
    void add(TagParam param);

    /**
     * 删除
     *
     * @author Arron
     * @Date 2019-09-07
     */
    void delete(TagParam param);

    /**
     * 更新
     *
     * @author Arron
     * @Date 2019-09-07
     */
    void update(TagParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Arron
     * @Date 2019-09-07
     */
    TagResult findBySpec(TagParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Arron
     * @Date 2019-09-07
     */
    List<TagResult> findListBySpec(TagParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Arron
     * @Date 2019-09-07
     */
     LayuiPageInfo findPageBySpec(TagParam param);

}
