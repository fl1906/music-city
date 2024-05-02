package top.flya.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import top.flya.common.annotation.ExcelDictFormat;
import top.flya.common.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;


/**
 * 活动主办方视图对象 pzc_organizer
 *
 * @author ruoyi
 * @date 2023-06-02
 */
@Data
@ExcelIgnoreUnannotated
@AllArgsConstructor
@NoArgsConstructor
public class PzcOrganizerVo {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    private Long organizerId;

    /**
     * 电话号码
     */
    @ExcelProperty(value = "电话号码")
    private String phone;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String name;

    /**
     * 组织者标志图片
     */
    @ExcelProperty(value = "组织者标志图片")
    private String logo;

    /**
     * 主办方介绍
     */
    @ExcelProperty(value = "主办方介绍")
    private String content;

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
