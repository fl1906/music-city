package top.flya.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;


/**
 * 艺人对象 pzc_artist
 *
 * @author flya
 * @date 2023-06-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pzc_artist")
public class PzcArtist extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "artist_id", type = IdType.AUTO)
    private Long artistId;
    /**
     * 艺人名
     */
    private String name;
    /**
     * 艺人照片
     */
    private String imageUrl;
    /**
     * 艺人介绍
     */
    private String description;
    /**
     * 删除状态，默认为1表示正常状态
     */
    private Integer state;

}
