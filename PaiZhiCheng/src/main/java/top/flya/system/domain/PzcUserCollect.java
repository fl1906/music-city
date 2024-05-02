package top.flya.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;


/**
 * 用户收藏活动对象 pzc_user_collect
 *
 * @author ruoyi
 * @date 2023-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pzc_user_collect")
public class PzcUserCollect extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "collect_id",type = IdType.AUTO)
    private Long collectId;
    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 收藏活动的Id
     */
    private Long activityId;

}
