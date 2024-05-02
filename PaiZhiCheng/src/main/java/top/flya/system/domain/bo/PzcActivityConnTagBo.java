package top.flya.system.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.core.validate.EditGroup;

import javax.validation.constraints.*;


/**
 * 活动标签与活动关联业务对象 pzc_activity_conn_tag
 *
 * @author ruoyi
 * @date 2023-06-03
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PzcActivityConnTagBo extends BaseEntity {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空", groups = { EditGroup.class })
    private Integer activityConnTagId;

    /**
     * 活动ID
     */
    @NotNull(message = "活动ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer activityId;

    /**
     * 活动标签ID
     */
    @NotNull(message = "活动标签ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer tagId;


}
