package top.flya.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import top.flya.common.annotation.ExcelDictFormat;
import top.flya.common.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;


/**
 * 活动介绍与活动关联视图对象 pzc_activity_conn_intro
 *
 * @author ruoyi
 * @date 2023-06-02
 */
@Data
@ExcelIgnoreUnannotated
public class PzcActivityConnIntroVo {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    private Integer activityConnIntroId;

    /**
     * 活动ID
     */
    @ExcelProperty(value = "活动ID")
    private Integer activityId;

    /**
     * 活动介绍ID
     */
    @ExcelProperty(value = "活动介绍ID")
    private Integer introId;

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
