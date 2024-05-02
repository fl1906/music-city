package top.flya.system.entity;



import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true) //活动关联艺人
@TableName("pzc_activity_conn_artist")
public class ActivityConnArtist extends FLBaseEntity{

    @TableId(value = "activity_conn_artist_id", type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private Integer activityConnArtistId;  // ID

    private Integer activityId;  // 活动ID

    private Integer artistId;  // 艺人ID

}

