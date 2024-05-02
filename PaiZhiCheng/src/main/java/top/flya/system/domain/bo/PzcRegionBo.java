package top.flya.system.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.core.validate.EditGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 地区业务对象 pzc_region
 *
 * @author ruoyi
 * @date 2023-07-22
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PzcRegionBo extends BaseEntity {

    /**
     * 地区id
     */
    @NotNull(message = "地区id不能为空", groups = { EditGroup.class })
    private Long regionId;

    /**
     * 省
     */
    @NotBlank(message = "省不能为空", groups = { AddGroup.class, EditGroup.class })
    private String base;

    /**
     * 地区名称
     */
    @NotBlank(message = "地区名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 城市主活动图
     */
    @NotBlank(message = "城市主活动图不能为空", groups = { AddGroup.class, EditGroup.class })
    private String imgUrl;


}
