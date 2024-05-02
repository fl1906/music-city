package top.flya.system.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.core.validate.EditGroup;

import javax.validation.constraints.*;


/**
 * 活动标签业务对象 pzc_tag
 *
 * @author ruoyi
 * @date 2023-06-02
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PzcTagBo extends BaseEntity {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空", groups = { EditGroup.class })
    private Long tagId;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 图片
     */
    @NotBlank(message = "图片不能为空", groups = { AddGroup.class, EditGroup.class })
    private String imageUrl;


}
