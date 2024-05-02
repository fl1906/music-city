package top.flya.system.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.core.validate.EditGroup;

import javax.validation.constraints.*;


/**
 * 艺人业务对象 pzc_artist
 *
 * @author flya
 * @date 2023-06-01
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PzcArtistBo extends BaseEntity {

    /**
     * ID
     */
    private Long artistId;

    /**
     * 艺人名
     */
    @NotBlank(message = "艺人名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 艺人照片
     */
    private String imageUrl;

    /**
     * 艺人介绍
     */
    private String description;


}
