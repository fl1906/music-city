package top.flya.system.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.core.validate.EditGroup;
import top.flya.system.domain.PzcArtist;
import top.flya.system.domain.PzcIntro;
import top.flya.system.domain.PzcOrganizer;
import top.flya.system.domain.PzcTag;

import javax.validation.constraints.*;
import java.util.List;


/**
 * 活动业务对象 pzc_activity
 *
 * @author ruoyi
 * @date 2023-06-02
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PzcActivityBo extends BaseEntity {

    /**
     * 活动id
     */
    @NotNull(message = "活动id不能为空", groups = { EditGroup.class })
    private Integer activityId;

    /**
     * 地址
     */
    @NotBlank(message = "地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String address;

    /**
     * 城市ID
     */
    @NotNull(message = "城市ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer regionId;

    /**
     * 活动标题
     */
    @NotBlank(message = "活动标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 开始时间
     */
    @NotBlank(message = "开始时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private String startTime;

    /**
     * 结束时间
     */
    @NotBlank(message = "结束时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private String endDate;


    /**
     * 展示时间
     */
//    @NotBlank(message = "展示时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private String showTime;


    /**
     * 封面图片
     */
    @NotBlank(message = "封面图片不能为空", groups = { AddGroup.class, EditGroup.class })
    private String coverImage;
    /**
     * 活动详情主图
     */
    @NotNull(message = "活动详情主图不能为空", groups = { AddGroup.class, EditGroup.class })
    private String innerImage;

    /**
     * 删除状态，默认为1表示正常状态
     */
    private Integer state;


    private Integer classify; //属于哪个分类

    private Integer region; // 0 国际 1 国内


    private List<PzcIntro> stageList;  // 场地舞台列表

    private List<PzcIntro> introList;  // 简介列表

    private List<PzcTag> tagList;  // 标签列表

    private List<PzcArtist> artistList;  // 艺人列表

    @NotNull(message = "主办方不能为空", groups = { AddGroup.class, EditGroup.class })
    private PzcOrganizer organizerList;  // 主办方列表


    private String shareImage; //分享图片




}
