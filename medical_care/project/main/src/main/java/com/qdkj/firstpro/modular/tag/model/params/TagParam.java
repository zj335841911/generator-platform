package com.qdkj.firstpro.modular.tag.model.params;

import lombok.Data;
import cn.stylefeng.roses.kernel.model.validator.BaseValidatingParam;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 医院标签表
 * </p>
 *
 * @author Arron
 * @since 2019-09-07
 */
@Data
public class TagParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    private Integer id;

    /**
     * 标签名字
     */
    private String tagName;

    @Override
    public String checkParam() {
        return null;
    }

}
