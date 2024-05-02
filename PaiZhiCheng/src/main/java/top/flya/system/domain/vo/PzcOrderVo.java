package top.flya.system.domain.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import top.flya.common.annotation.ExcelDictFormat;
import top.flya.common.convert.ExcelDictConvert;
import lombok.Data;


/**
 * 订单视图对象 pzc_order
 *
 * @author ruoyi
 * @date 2023-07-09
 */
@Data
@ExcelIgnoreUnannotated
public class PzcOrderVo {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @ExcelProperty(value = "订单ID")
    private Long orderId;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    private Long userId;

    /**
     * 活动ID
     */
    @ExcelProperty(value = "活动ID")
    private Long activityId;

    /**
     * 订单金额
     */
    @ExcelProperty(value = "订单金额")
    private BigDecimal money;

    /**
     * 订单状态
     */
    @ExcelProperty(value = "订单状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "order_status")
    private Long orderStatus;

    /**
     * 订单类型
     */
    @ExcelProperty(value = "订单类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "order_type")
    private Long type;

    /**
     * 外部订单号
     */
    @ExcelProperty(value = "外部订单号")
    private String outOrderNum;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String intro;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 订单创建时间
     */
    @ExcelProperty(value = "订单创建时间")
    private Date createTime;

    /**
     * 订单更新时间
     */
    @ExcelProperty(value = "订单更新时间")
    private Date updateTime;


}
