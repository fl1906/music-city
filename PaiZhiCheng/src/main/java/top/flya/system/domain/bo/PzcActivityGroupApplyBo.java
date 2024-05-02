package top.flya.system.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.core.validate.EditGroup;

import javax.validation.constraints.*;

import java.math.BigDecimal;

/**
 * 活动组队申请列业务对象 pzc_activity_group_apply
 *
 * @author ruoyi
 * @date 2023-07-10
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PzcActivityGroupApplyBo extends BaseEntity {

    /**
     * 申请ID
     */
    @NotNull(message = "申请ID不能为空", groups = { EditGroup.class })
    private Long applyId;

    /**
     * 申请人ID
     */
//    @NotNull(message = "申请人ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 申请的活动ID
     */
    @NotNull(message = "申请的活动ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long activityId;

    /**
     * 申请加入的组ID
     */
    @NotNull(message = "申请加入的组ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long groupId;

    /**
     * 申请状态
     *  0 位于申请列表中 1 申请通过待确认时 2 确认通过进行中 3 组队结束  //申请状态不应该由前端传入 由后端自动计算
     */
//    private Integer applyStatus;
    /**
     * 0 AA制
1 我买单
2 你买单
     */
    @NotNull(message = "0 AA制 我买单 2 你买单不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long groupType;

    /**
     * 活动保证金
     */
    @NotNull(message = "活动保证金不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal money;

    /**
     * 留言内容
     */
    @NotBlank(message = "留言内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String message;


    // 无限制确认到达
    private Integer wxz;

}
