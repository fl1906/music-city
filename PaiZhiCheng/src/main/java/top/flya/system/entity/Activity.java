package top.flya.system.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true) //活动
@TableName("pzc_activity")
public class Activity extends FLBaseEntity {

    @TableId(value = "activity_id", type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private String activityId; //活动id

    private String address;  // 地址

    private Integer regionId;  // 城市ID

    private String title; //活动标题

    private String startTime;  // 开始时间

    private String endDate;  // 结束日期

    private String innerImage;  // 活动详情主图

    private String showTime;  // 展示时间

    private String coverImage;  // 封面图片

    private Integer classify; //属于哪个分类

    private Integer region; // 0 国内 1 国外

    @TableField(exist = false)
    private List<Intro> introList;  // 简介列表

    @TableField(exist = false) //标签列表
    private List<Tag> tagList;

    @TableField(exist = false)
    private List<Artist> artistList;  // 艺人列表

    @TableField(exist = false)
    private Organizer organizerList;  // 主办方列表

}
