package top.flya.system.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.core.validate.EditGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 活动介绍业务对象 pzc_intro
 *
 * @author ruoyi
 * @date 2023-08-04
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PzcIntroBo extends BaseEntity {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空", groups = { EditGroup.class })
    private Long introId;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 内容
     */
//    @NotBlank(message = "内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 0 场地舞台介绍 1 更多介绍
     */
    @NotNull(message = "0 场地舞台介绍 1 更多介绍不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long type;

    /**
     * 图片
     */
    @NotBlank(message = "图片不能为空", groups = { AddGroup.class, EditGroup.class })
    private String imageFullUrl;


}
