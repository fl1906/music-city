package top.flya.system.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.core.validate.EditGroup;

import javax.validation.constraints.*;


/**
 * 轮播图业务对象 pzc_view_pager
 *
 * @author ruoyi
 * @date 2023-05-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PzcViewPagerBo extends BaseEntity {

    /**
     * 轮播图id
     */
    @NotNull(message = "轮播图id不能为空", groups = { EditGroup.class })
    private Integer viewPagerId;

    /**
     * 轮播图名称
     */
    @NotBlank(message = "轮播图名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 轮播图图片Url
     */
    @NotBlank(message = "轮播图图片Url不能为空", groups = { AddGroup.class, EditGroup.class })
    private String imageUrl;

    /**
     * 轮播图链接Url
     */
    @NotBlank(message = "轮播图链接Url不能为空", groups = { AddGroup.class, EditGroup.class })
    private String linkUrl;

    /**
     * 删除状态，默认为1表示正常状态
     */
    @NotNull(message = "删除状态，默认为1表示正常状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer state;

    /**
     * 关联活动id  0表示不关联
     */
    @NotNull(message = "关联活动id  0表示不关联不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long activityId;


}
