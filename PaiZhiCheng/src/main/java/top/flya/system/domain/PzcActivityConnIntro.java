package top.flya.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;


/**
 * 活动介绍与活动关联对象 pzc_activity_conn_intro
 *
 * @author ruoyi
 * @date 2023-06-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pzc_activity_conn_intro")
public class PzcActivityConnIntro extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "activity_conn_intro_id", type = IdType.AUTO)
    private Integer activityConnIntroId;
    /**
     * 活动ID
     */
    private Integer activityId;
    /**
     * 活动介绍ID
     */
    private Integer introId;
    /**
     * 删除状态，默认为1表示正常状态
     */
    private Integer state;

}
