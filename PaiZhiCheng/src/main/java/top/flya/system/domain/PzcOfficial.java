package top.flya.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;


/**
 * 官方消息对象 pzc_official
 *
 * @author ruoyi
 * @date 2023-07-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pzc_official")
public class PzcOfficial extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "official_id",type = IdType.AUTO)
    private Long officialId;
    /**
     * 来自谁的消息
     */
    private Long fromUserId;
    /**
     * 给谁发的消息
     */
    private Long toUserId;
    /**
     * 标题
     */
    private String title;
    /**
     * 主体消息
     */
    private String content;
    /**
     * 是否已读
     */
    private Long isRead;
    /**
     * 来自那场组队的消息
     */
    private Long groupId;
    /**
     * 来自那场活动的消息
     */
    private Long activityId;

}
