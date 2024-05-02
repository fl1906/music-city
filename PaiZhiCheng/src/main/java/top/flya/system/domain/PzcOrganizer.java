package top.flya.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;

import java.util.List;


/**
 * 活动主办方对象 pzc_organizer
 *
 * @author ruoyi
 * @date 2023-06-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pzc_organizer")
public class PzcOrganizer extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "organizer_id", type = IdType.AUTO)
    private Long organizerId;
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 名称
     */
    private String name;
    /**
     * 组织者标志图片
     */
    private String logo;
    /**
     * 主办方介绍
     */
    private String content;
    /**
     * 删除状态，默认为1表示正常状态
     */
    private Integer state;

    @TableField(exist = false)
    private List<PzcOrganizerTicket> organizerTickets;  // 组织者票务列表

}
