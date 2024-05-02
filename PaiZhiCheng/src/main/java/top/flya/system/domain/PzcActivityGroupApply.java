package top.flya.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 活动组队申请列对象 pzc_activity_group_apply
 *
 * @author ruoyi
 * @date 2023-07-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pzc_activity_group_apply")
public class PzcActivityGroupApply extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 申请ID
     */
    @TableId(value = "apply_id",type = IdType.AUTO)
    private Long applyId;
    /**
     * 申请人ID
     */
    private Long userId;
    /**
     * 申请的活动ID
     */
    private Long activityId;
    /**
     * 申请加入的组ID
     */
    private Long groupId;
    /**
     * 0 AA制
1 我买单
2 你买单
     */
    private Long groupType;
    /**
     * 活动保证金
     */
    private BigDecimal money;
    /**
     * 留言内容
     */
    private String message;

    /**
     * 0 位于申请列表中 1 申请通过待确认时 2 确认通过进行中 3 组队结束
     */
    private Integer applyStatus;

    /**
     * 无限制确认到达  0 未确认 1 已确认
     */
    private Integer wxz;


    @TableField(exist = false)
    private BigDecimal otherMoney; //对方的保证金

    @TableField(exist = false)
    private String otherName; //对方的名字

    @TableField(exist = false)
    private String otherAvatar; //对方的头像

    @TableField(exist = false)
    private String otherUserId; //对方的userId

    @TableField(exist = false)
    private Integer otherLevel; //对方的等级


    @TableField(exist = false)
    private String myAvatar; //我的头像

    @TableField(exist = false)
    private String title;


    //发起方当前位置
    private String startAddress;
//申请方当前位置
    private String applyAddress;

}
