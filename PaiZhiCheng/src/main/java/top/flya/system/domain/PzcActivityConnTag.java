package top.flya.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;


/**
 * 活动标签与活动关联对象 pzc_activity_conn_tag
 *
 * @author ruoyi
 * @date 2023-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pzc_activity_conn_tag")
public class PzcActivityConnTag extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "activity_conn_tag_id")
    private Integer activityConnTagId;
    /**
     * 活动ID
     */
    private Integer activityId;
    /**
     * 活动标签ID
     */
    private Integer tagId;
    /**
     * 删除状态，默认为1表示正常状态
     */
    private Integer state;

}
