package top.flya.system.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true) //活动标签
@TableName("pzc_activity_conn_tag")
public class ActivityConnTag extends FLBaseEntity{

    @TableId(value = "activity_conn_tag_id", type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private Integer activityConnTagId;  // ID

    private Integer activityId;  // 活动ID

    private Integer tagId;  // 活动标签ID
}
