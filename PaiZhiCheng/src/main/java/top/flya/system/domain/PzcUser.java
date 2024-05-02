package top.flya.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;
import top.flya.system.domain.vo.PzcActivityGroupApplyVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户对象 pzc_user
 *
 * @author ruoyi
 * @date 2023-07-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pzc_user")
public class PzcUser extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 用户主键
     */
    @TableId(value = "user_id",type = IdType.AUTO)
    private Long userId;
    /**
     * 用户在小程序端的 openId 唯一
     */
    private String openid;
    /**
     * 派币余额
     */
    private BigDecimal money;
    /**
     * 用户等级
     */
    private Long userLevel;
    /**
     * 用户累计积分
     */
    private Long integration;
    /**
     * 用户现有积分
     */
    private Long integrationNow;
    /**
     * 真实姓名
     */
    private String realname;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 用户性别 0 男  1 女  2 未知
     */
    private Integer sex;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 地址
     */
    private String address;
    /**
     * 个人介绍
     */
    private String intro;
    /**
     * 年龄
     */
    private Long age;
    /**
     * 星座
     */
    private String constellation;
    /**
     * 人格类型
     */
    private String mbti;
    /**
     * 兴趣爱好
     */
    private String hobby;
    /**
     * 学校
     */
    private String school;
    /**
     * 职业
     */
    private String occupation;
    /**
     * 喜欢的音乐风格
     */
    private String musicStyle;
    /**
     * 状态 是否被封禁
     */
    private Long state;

    @TableField(exist = false)
    private List<PzcUserPhoto> userPhoto;


    @TableField(exist = false)
    private PzcActivityGroup pzcActivityGroup;

    @TableField(exist = false)
    private PzcActivityGroupApplyVo pzcActivityGroupApplyVo;


    /**
     * 用户免责取消次数  0点定时任务刷新
     */
    private Integer exemptCancel;


    @TableField(exist = false)
    private Boolean liveStatus;


    @TableField(exist = false)
    private Integer notReadCount;

}
