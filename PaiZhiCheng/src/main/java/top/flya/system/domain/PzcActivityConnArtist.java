package top.flya.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;


/**
 * 活动关联艺人对象 pzc_activity_conn_artist
 *
 * @author ruoyi
 * @date 2023-06-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pzc_activity_conn_artist")
public class PzcActivityConnArtist extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "activity_conn_artist_id", type = IdType.AUTO)
    private Integer activityConnArtistId;
    /**
     * 活动ID
     */
    private Integer activityId;
    /**
     * 艺人ID
     */
    private Integer artistId;
    /**
     * 删除状态，默认为1表示正常状态
     */
    private Integer state;

}
