package top.flya.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import top.flya.common.annotation.ExcelDictFormat;
import top.flya.common.convert.ExcelDictConvert;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 用户操作历史记录视图对象 pzc_user_history
 *
 * @author ruoyi
 * @date 2023-07-06
 */
@Data
@ExcelIgnoreUnannotated
public class PzcUserHistoryVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long historyId;

    /**
     * 关联用户Id
     */
    @ExcelProperty(value = "关联用户Id")
    private Long userId;

    /**
     * 关联活动Id
     */
    @ExcelProperty(value = "关联活动Id")
    private Long activityId;

    /**
     * 操作类型
     */
    @ExcelProperty(value = "操作类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "user_history_type")
    private Long type;

    /**
     * 信息
     */
    @ExcelProperty(value = "信息")
    private String message;

    private BigDecimal money;

    private Date createTime;


}
