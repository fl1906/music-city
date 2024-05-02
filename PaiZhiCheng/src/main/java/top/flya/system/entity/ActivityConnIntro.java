package top.flya.system.entity;



import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("pzc_activity_conn_intro")
@EqualsAndHashCode(callSuper = true) //活动介绍 与 活动 关联表
public class ActivityConnIntro extends FLBaseEntity{

    @TableId(value = "activity_conn_intro_id", type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private Integer activityConnIntroId;  // ID

    private Integer activityId;  // 活动ID

    private Integer introId;  // 活动介绍ID
}
