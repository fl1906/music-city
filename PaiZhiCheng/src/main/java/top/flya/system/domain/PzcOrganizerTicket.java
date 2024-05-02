package top.flya.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;


/**
 * 主办方票务对象 pzc_organizer_ticket
 *
 * @author ruoyi
 * @date 2023-07-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pzc_organizer_ticket")
public class PzcOrganizerTicket extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "organizer_ticket_id", type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private Long organizerTicketId;
    /**
     * 名称
     */
    private String name;
    /**
     * 二维码图片
     */
    private String qrImage;
    /**
     * logo图
     */
    private String logoImage;
    /**
     * 关联主办方ID
     */
    private Long organizerId;
    /**
     * 删除状态，默认为1表示正常状态
     */
    private Integer state;

}
