package top.flya.system.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.core.validate.EditGroup;

import javax.validation.constraints.*;


/**
 * 用户聊天业务对象 pzc_user_talk
 *
 * @author ruoyi
 * @date 2023-07-16
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PzcUserTalkBo extends BaseEntity {

    /**
     * 聊天ID
     */
    @NotNull(message = "聊天ID不能为空", groups = { EditGroup.class })
    private Long talkId;

    /**
     * 发起方
     */
    @NotNull(message = "发起方不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long fromUserId;

    /**
     * 接受方
     */
    @NotNull(message = "接受方不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long toUserId;

    /**
     * 消息
     */
    @NotBlank(message = "消息不能为空", groups = { AddGroup.class, EditGroup.class })
    private String message;

    /**
     * 消息状态
     */
//    @NotNull(message = "消息状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long messageStatus;

    /**
     * 消息类型
     */
    @NotNull(message = "消息类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long messageType;


    private Long userId; //消息的归属者 单向删除


    private Long groupId; //群组id


}
