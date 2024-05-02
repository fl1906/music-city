package top.flya.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;


/**
 * 地区对象 pzc_region
 *
 * @author ruoyi
 * @date 2023-07-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pzc_region")
public class PzcRegion extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 地区id
     */
    @TableId(value = "region_id",type = IdType.AUTO)
    private Long regionId;
    /**
     * 省
     */
    private String base;
    /**
     * 地区名称
     */
    private String name;
    /**
     * 城市主活动图
     */
    private String imgUrl;
    /**
     * 删除状态，默认为1表示正常状态
     */
    private Integer state;

}
