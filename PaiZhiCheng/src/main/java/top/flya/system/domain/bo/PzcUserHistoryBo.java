package top.flya.system.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.core.validate.EditGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 用户操作历史记录业务对象 pzc_user_history
 *
 * @author ruoyi
 * @date 2023-07-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PzcUserHistoryBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long historyId;

    /**
     * 关联用户Id
     */
    @NotNull(message = "关联用户Id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 关联活动Id
     */
    @NotNull(message = "关联活动Id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long activityId;

    /**
     * 操作类型
     */
    @NotNull(message = "操作类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long[] type;

    /**
     * 信息
     */
    @NotBlank(message = "信息不能为空", groups = { AddGroup.class, EditGroup.class })
    private String message;


    private String nowTime;

}
