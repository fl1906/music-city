package top.flya.system.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 *  活动介绍  设置 多对多 关联 方便 以后扩展
 */
@Data
@EqualsAndHashCode(callSuper = true) //活动介绍
@TableName("pzc_intro")
public class Intro extends FLBaseEntity{

    @TableId(value = "intro_id", type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private Integer introId;  // ID

    private String content;  // 内容

    private String imageFullUrl;  // 完整图片URL

}
