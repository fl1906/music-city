package top.flya.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import top.flya.common.annotation.ExcelDictFormat;
import top.flya.common.convert.ExcelDictConvert;
import top.flya.system.domain.PzcUserPhoto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 活动组队视图对象 pzc_activity_group
 *
 * @author ruoyi
 * @date 2023-07-10
 */
@Data
@ExcelIgnoreUnannotated
public class PzcActivityGroupVo {

    private static final long serialVersionUID = 1L;

    /**
     * 组队ID

     */
    @ExcelProperty(value = "组队ID")
    private Long groupId;

    /**
     * 活动ID
     */
    @ExcelProperty(value = "活动ID")
    private Long activityId;


    private Integer region;
    /**
     * 活动组队发起人ID
     */
    @ExcelProperty(value = "活动组队发起人ID")
    private Long userId;

    /**
     * 活动组队发起人
     */
    private PzcUserVo user;

    /**
     * 活动主题
     */
    @ExcelProperty(value = "活动主题")
    private String title;


    private String activityTitle;

    /**
     * 活动组队所缴纳的保证金
     */
    @ExcelProperty(value = "活动组队所缴纳的保证金")
    private BigDecimal money;

    /**
     * 买单方式
     */
    @ExcelProperty(value = "买单方式", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "group_pay_type")
    private Long groupType;

    /**
     * 活动地址
     */
    @ExcelProperty(value = "活动地址")
    private String address;

    /**
     * 一起约定好的时间
     */
    @ExcelProperty(value = "一起约定好的时间")
    private Date activityTime;

    /**
     * 权限
     */
    @ExcelProperty(value = "权限", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "group_auth")
    private Long auth;

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


    private List<PzcUserPhoto> photo;


    private Boolean ifShow =  true;


    private BigDecimal distance; //距离多少米
}
