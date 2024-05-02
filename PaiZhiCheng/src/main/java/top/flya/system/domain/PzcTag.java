package top.flya.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;


/**
 * 活动标签对象 pzc_tag
 *
 * @author ruoyi
 * @date 2023-06-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pzc_tag")
public class PzcTag extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "tag_id", type = IdType.AUTO)
    private Long tagId;
    /**
     * 名称
     */
    private String name;
    /**
     * 图片
     */
    private String imageUrl;
    /**
     * 删除状态，默认为1表示正常状态
     */
    private Integer state;

}
