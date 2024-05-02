package top.flya.system.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.core.validate.EditGroup;

import javax.validation.constraints.*;

import java.math.BigDecimal;

/**
 * 用户业务对象 pzc_user
 *
 * @author ruoyi
 * @date 2023-07-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PzcUserBo extends BaseEntity {

    /**
     * 用户主键
     */
    @NotNull(message = "用户主键不能为空", groups = { EditGroup.class })
    private Long userId;

    /**
     * OpenId
     */
    private String openid;


    /**
     * 换取openid的code
     */
    private String loginCode;

    /**
     * 换取手机号的code
     */
    private String phoneCode;

    /**
     * 派币余额
     */
    private BigDecimal money;

    /**
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String realname;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String nickname;

    /**
     * 性别
     */
    @NotNull(message = "性别不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer sex;


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
     * 介绍
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
     * 音乐风格
     */
    private String musicStyle;

    /**
     * 封禁状态
     */
    private Integer state;


    private Integer exemptCancel;


}
