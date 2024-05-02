package top.flya.system.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true) //活动主办方
@TableName("pzc_organizer")
public class Organizer extends FLBaseEntity{ //活动主办方

    @TableId(value = "organizer_id", type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private Integer organizerId;  // ID

    private String phone;  // 电话号码

    private String name;  // 名称

    private String logo;  // 组织者标志图片

    private String content; //主办方介绍

    @TableField(exist = false)
    private List<OrganizerTicket> organizerTickets;  // 组织者票务列表

}
