package top.flya.system.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true) //主办方票务
@TableName("pzc_organizer_ticket")
public class OrganizerTicket extends FLBaseEntity{ // 主办方票务

    @TableId(value = "organizer_ticket_id", type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private int organizerTicketId;  // ID

    private String name;  // 名称

    private String startDate;  // 开始日期

    private String coverImage;  // 封面图片

    private Integer organizerId;  // 关联主办方ID
}
