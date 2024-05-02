package top.flya.system.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.core.validate.EditGroup;

import javax.validation.constraints.*;


/**
 * 用户收藏活动业务对象 pzc_user_collect
 *
 * @author ruoyi
 * @date 2023-07-08
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PzcUserCollectBo extends BaseEntity {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空", groups = { EditGroup.class })
    private Long collectId;

    /**
     * 用户Id
     */
    @NotNull(message = "用户Id不能为空", groups = { EditGroup.class })
    private Long userId;

    /**
     * 收藏活动的Id
     */
    @NotNull(message = "收藏活动的Id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long activityId;


    private Integer type ; // 收藏类型 0电音节 1派对


}
