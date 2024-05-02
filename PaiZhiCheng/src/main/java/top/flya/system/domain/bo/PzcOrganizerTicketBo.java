package top.flya.system.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.core.validate.EditGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 主办方票务业务对象 pzc_organizer_ticket
 *
 * @author ruoyi
 * @date 2023-07-22
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PzcOrganizerTicketBo extends BaseEntity {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空", groups = { EditGroup.class })
    private Long organizerTicketId;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 二维码图片
     */
    @NotBlank(message = "二维码图片不能为空", groups = { AddGroup.class, EditGroup.class })
    private String qrImage;

    /**
     * logo图
     */
    @NotBlank(message = "logo图不能为空", groups = { AddGroup.class, EditGroup.class })
    private String logoImage;

    /**
     * 关联主办方ID
     */
    @NotNull(message = "关联主办方ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long organizerId;


}
