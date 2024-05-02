package top.flya.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import top.flya.common.annotation.ExcelDictFormat;
import top.flya.common.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;


/**
 * 用户聊天视图对象 pzc_user_talk
 *
 * @author ruoyi
 * @date 2023-07-16
 */
@Data
@ExcelIgnoreUnannotated
public class PzcUserTalkVo {

    private static final long serialVersionUID = 1L;

    /**
     * 聊天ID
     */
    @ExcelProperty(value = "聊天ID")
    private Long talkId;

    /**
     * 发起方
     */
    @ExcelProperty(value = "发起方")
    private Long fromUserId;

    /**
     * 接受方
     */
    @ExcelProperty(value = "接受方")
    private Long toUserId;

    /**
     * 消息
     */
    @ExcelProperty(value = "消息", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "user_talk_msg_type")
    private String message;

    /**
     * 消息状态
     */
    @ExcelProperty(value = "消息状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "user_talk_msg_status")
    private Long messageStatus;

    /**
     * 消息类型
     */
    @ExcelProperty(value = "消息类型")
    private Long messageType;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Date createTime;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Date updateTime;


    private Integer notReadCount; //未读消息数量

    private String username;

    private String avatar;

    private Long groupId;


}
