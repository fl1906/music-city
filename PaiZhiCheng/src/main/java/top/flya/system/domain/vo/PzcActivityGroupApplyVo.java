package top.flya.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import top.flya.common.annotation.ExcelDictFormat;
import top.flya.common.convert.ExcelDictConvert;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 活动组队申请列视图对象 pzc_activity_group_apply
 *
 * @author ruoyi
 * @date 2023-07-10
 */
@Data
@ExcelIgnoreUnannotated
public class PzcActivityGroupApplyVo {

    private static final long serialVersionUID = 1L;

    /**
     * 申请ID
     */
    @ExcelProperty(value = "申请ID")
    private Long applyId;

    /**
     * 申请人ID
     */
    @ExcelProperty(value = "申请人ID")
    private Long userId;

    /**
     * 申请的活动ID
     */
    @ExcelProperty(value = "申请的活动ID")
    private Long activityId;


    /**
     * 活动标题
     */
    private String activityTitle;

    /**
     * 组队的标题
     */
    private String groupTitle;

    /**
     * 申请加入的组ID
     */
    @ExcelProperty(value = "申请加入的组ID")
    private Long groupId;

    /**
     * 0 AA制
1 我买单
2 你买单
     */
    @ExcelProperty(value = "0 AA制 1 我买单 2 你买单", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "group_pay_type")
    private Long groupType;

    /**
     * 活动保证金
     */
    @ExcelProperty(value = "活动保证金")
    private BigDecimal money;

    /**
     * 留言内容
     */
    @ExcelProperty(value = "留言内容")
    private String message;


    /**
     * 0 位于申请列表中 1 申请通过待确认时 2 确认通过进行中 3 组队结束
     */
    @ExcelProperty(value = "-1 已取消 0 位于申请列表中 1 申请通过待确认时 2 确认通过进行中 3 组队结束", converter = ExcelDictConvert.class)
    private Integer applyStatus;
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

    //申请人的头像以及用户名
    private String nickName;

    private String avatar;

    /**
     *  无限制确认到达 0 未确认 1 已确认
     */
    private Integer wxz;


    //发起方当前位置
    private String startAddress;
    //申请方当前位置
    private String applyAddress;

}
