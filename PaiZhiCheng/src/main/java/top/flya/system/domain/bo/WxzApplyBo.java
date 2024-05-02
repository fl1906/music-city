package top.flya.system.domain.bo;


import lombok.Data;

@Data
public class WxzApplyBo {

    private Integer fromUserId;

    private Integer toUserId;

    private Integer activityId;

    private Integer groupId;

    private Integer applyId;

    private String message;

}
