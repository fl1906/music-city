package top.flya.system.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.core.validate.EditGroup;

import javax.validation.constraints.*;


/**
 * 活动主办方业务对象 pzc_organizer
 *
 * @author ruoyi
 * @date 2023-06-02
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PzcOrganizerBo extends BaseEntity {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空", groups = { EditGroup.class })
    private Long organizerId;

    /**
     * 电话号码
     */
    @NotBlank(message = "电话号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String phone;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 组织者标志图片
     */
    private String logo;

    /**
     * 主办方介绍
     */
    @NotBlank(message = "主办方介绍不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;


}
