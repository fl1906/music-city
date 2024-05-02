package top.flya.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;


/**
 * 用户聊天对象 pzc_user_talk
 *
 * @author ruoyi
 * @date 2023-07-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pzc_user_talk")
public class PzcUserTalk extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 聊天ID
     */
    @TableId(value = "talk_id",type = IdType.AUTO)
    private Long talkId;
    /**
     * 发起方
     */
    private Long fromUserId;
    /**
     * 接受方
     */
    private Long toUserId;


    private Long userId; //消息的归属者 单向删除
    /**
     * 消息
     */
    private String message;
    /**
     * 消息状态
     */
    private Long messageStatus;
    /**
     * 消息类型
     */
    private Long messageType;

    @TableField(exist = false)
    private Integer notReadCount;

    private Long groupId; //群组id

}
