package top.flya.system.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.core.validate.EditGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 官方消息业务对象 pzc_official
 *
 * @author ruoyi
 * @date 2023-07-27
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PzcOfficialBo extends BaseEntity {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空", groups = { EditGroup.class })
    private Long officialId;

    /**
     * 来自谁的消息
     */
    @NotNull(message = "来自谁的消息不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long fromUserId;

    /**
     * 给谁发的消息
     */
    @NotNull(message = "给谁发的消息不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long toUserId;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 主体消息
     */
    @NotBlank(message = "主体消息不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 是否已读
     */
    @NotNull(message = "是否已读不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long isRead;

    /**
     * 来自那场组队的消息
     */
    @NotNull(message = "来自那场组队的消息不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long groupId;

    /**
     * 来自那场活动的消息
     */
    @NotNull(message = "来自那场活动的消息不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long activityId;


}
