package top.flya.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.flya.system.domain.PzcArtist;
import top.flya.system.domain.PzcIntro;
import top.flya.system.domain.PzcOrganizer;
import top.flya.system.domain.PzcTag;

import java.util.Date;
import java.util.List;


/**
 * 活动视图对象 pzc_activity
 *
 * @author ruoyi
 * @date 2023-06-02
 */
@Data
@ExcelIgnoreUnannotated
@AllArgsConstructor
@NoArgsConstructor
public class PzcActivityVo {

    private static final long serialVersionUID = 1L;

    /**
     * 活动id
     */
    @ExcelProperty(value = "活动id")
    private Integer activityId;

    /**
     * 地址
     */
    @ExcelProperty(value = "地址")
    private String address;

    /**
     * 城市ID
     */
    @ExcelProperty(value = "城市ID")
    private Integer regionId;

    /**
     * 活动标题
     */
    @ExcelProperty(value = "活动标题")
    private String title;

    /**
     * 开始时间
     */
    @ExcelProperty(value = "开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @ExcelProperty(value = "结束时间")
    private String endDate;

    /**
     * 活动详情主图
     */
    @ExcelProperty(value = "活动详情主图")
    private String innerImage;

    /**
     * 展示时间
     */
    @ExcelProperty(value = "展示时间")
    private String showTime;

    /**
     * 封面图片
     */
    @ExcelProperty(value = "封面图片")
    private String coverImage;

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

    /**
     * 删除状态，默认为1表示正常状态
     */
    @ExcelProperty(value = "删除状态，默认为1表示正常状态")
    private Integer state;



    private Long organizerId; // 主办方id

    private Integer classify; //属于哪个分类

    private Integer region; // 0 国内 1 国外

    private List<PzcIntro> introList;  // 简介列表

    private List<PzcTag> tagList;  // 标签列表

    private List<PzcArtist> artistList;  // 艺人列表

    private PzcOrganizer organizerList;  // 主办方列表

    private List<PzcIntro> stageList;  // 场地列表


    private String shareImage; //分享图片
//    private String distance; //距我多少km



}
