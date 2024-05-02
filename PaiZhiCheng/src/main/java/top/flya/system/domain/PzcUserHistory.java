package top.flya.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;

import java.math.BigDecimal;


/**
 * 用户操作历史记录对象 pzc_user_history
 *
 * @author ruoyi
 * @date 2023-07-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pzc_user_history")
public class PzcUserHistory extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "history_id",type = IdType.AUTO)
    private Long historyId;
    /**
     * 关联用户Id
     */
    private Long userId;
    /**
     * 关联活动Id
     */
    private Long activityId;
    /**
     * 操作类型
     */
    private Long type;
    /**
     * 信息
     */
    private String message;

    private BigDecimal money;

}
