package com.qdkj.firstpro.modular.tag.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 医院标签表
 * </p>
 *
 * @author Arron
 * @since 2019-09-07
 */
@TableName("t_tag")
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标签名字
     */
    @TableField("tag_name")
    private String tagName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public String toString() {
        return "Tag{" +
        "id=" + id +
        ", tagName=" + tagName +
        "}";
    }
}
