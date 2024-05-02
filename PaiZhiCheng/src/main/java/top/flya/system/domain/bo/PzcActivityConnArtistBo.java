package top.flya.system.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.core.validate.EditGroup;

import javax.validation.constraints.*;


/**
 * 活动关联艺人业务对象 pzc_activity_conn_artist
 *
 * @author ruoyi
 * @date 2023-06-02
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PzcActivityConnArtistBo extends BaseEntity {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空", groups = { EditGroup.class })
    private Integer activityConnArtistId;

    /**
     * 活动ID
     */
    @NotNull(message = "活动ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer activityId;

    /**
     * 艺人ID
     */
    @NotNull(message = "艺人ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer artistId;


}
