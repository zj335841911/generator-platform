package com.qdkj.firstpro.modular.tag.mapper;

import com.qdkj.firstpro.modular.tag.entity.Tag;
import com.qdkj.firstpro.modular.tag.model.params.TagParam;
import com.qdkj.firstpro.modular.tag.model.result.TagResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 医院标签表 Mapper 接口
 * </p>
 *
 * @author Arron
 * @since 2019-09-07
 */
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 获取列表
     *
     * @author Arron
     * @Date 2019-09-07
     */
    List<TagResult> customList(@Param("paramCondition") TagParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Arron
     * @Date 2019-09-07
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") TagParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Arron
     * @Date 2019-09-07
     */
    Page<TagResult> customPageList(@Param("page") Page page, @Param("paramCondition") TagParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Arron
     * @Date 2019-09-07
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") TagParam paramCondition);

}
