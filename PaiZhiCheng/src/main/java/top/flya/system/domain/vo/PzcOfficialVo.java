package top.flya.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 官方消息视图对象 pzc_official
 *
 * @author ruoyi
 * @date 2023-07-27
 */
@Data
@ExcelIgnoreUnannotated
public class PzcOfficialVo {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    private Long officialId;

    /**
     * 来自谁的消息
     */
    @ExcelProperty(value = "来自谁的消息")
    private Long fromUserId;

    /**
     * 给谁发的消息
     */
    @ExcelProperty(value = "给谁发的消息")
    private Long toUserId;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 主体消息
     */
    @ExcelProperty(value = "主体消息")
    private String content;

    /**
     * 是否已读
     */
    @ExcelProperty(value = "是否已读")
    private Long isRead;

    /**
     * 来自那场组队的消息
     */
    @ExcelProperty(value = "来自那场组队的消息")
    private Long groupId;

    /**
     * 来自那场活动的消息
     */
    @ExcelProperty(value = "来自那场活动的消息")
    private Long activityId;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private Date updateTime;


}
