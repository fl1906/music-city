package top.flya.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import top.flya.common.core.domain.BaseEntity;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.core.validate.EditGroup;
/**
 * 轮播图对象 pzc_view_pager
 *
 * @author ruoyi
 * @date 2023-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pzc_view_pager")
public class PzcViewPager extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 轮播图id
     */
    @TableId(value = "view_pager_id", type = IdType.AUTO)
    private Integer viewPagerId;
    /**
     * 轮播图名称
     */
    private String name;
    /**
     * 轮播图图片Url
     */
    private String imageUrl;
    /**
     * 轮播图链接Url
     */
    private String linkUrl;
    /**
     * 删除状态，默认为1表示正常状态
     */
    private Integer state;
    /**
     * 关联活动id  0表示不关联
     */
    private Long activityId;

}
