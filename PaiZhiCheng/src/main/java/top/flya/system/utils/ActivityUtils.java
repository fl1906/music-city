package top.flya.system.utils;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.flya.common.helper.LoginHelper;
import top.flya.common.utils.DateUtils;
import top.flya.system.domain.vo.PzcActivityGroupVo;
import top.flya.system.domain.vo.PzcActivityVo;
import top.flya.system.mapper.PzcActivityGroupMapper;
import top.flya.system.mapper.PzcActivityMapper;
import top.flya.system.service.IPzcActivityGroupService;
import top.flya.system.service.IPzcActivityService;

import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class ActivityUtils {

    private final IPzcActivityService iPzcActivityService;

    private final PzcActivityMapper pzcActivityMapper;

    private final IPzcActivityGroupService iPzcActivityGroupService;

    private final PzcActivityGroupMapper pzcActivityGroupMapper;


    /**
     * 检查活动相关问题  活动是否存在 活动是否结束
     * @param activityId
     * @return
     */
    public Boolean checkActivity(Integer activityId) {
        log.info("checkActivity: activityId = {}", activityId);
        if(activityId==0)
        {
            return true;//城市活动 无需检查
        }
        PzcActivityVo pzcActivityVo = iPzcActivityService.queryById(activityId);
        if (pzcActivityVo == null) {
            log.error("活动不存在");
            throw  new RuntimeException("活动不存在");
        }
        String endDate = pzcActivityVo.getEndDate();
        Date now = new Date();
        Date end = DateUtils.parseDate(endDate);
        if(now.after(end))
        {
            log.error("活动已结束");
            throw  new RuntimeException("活动已结束");
        }
        return true;
    }

    /**
     * 检查活动组是否存在 和当前组是否在当前活动下
     * @param groupId
     * @return
     */
    public Boolean checkGroup(Integer activityId,Long groupId) {
        log.info("checkGroup: groupId = {}", groupId);
        PzcActivityGroupVo pzcActivityGroupVo = iPzcActivityGroupService.queryById(groupId);
        if(pzcActivityGroupVo == null) {
            log.error("组队不存在");
           throw new RuntimeException("组队不存在");
        }
        //不可以参与自己发起的组队
        if(pzcActivityGroupVo.getUserId().equals(LoginHelper.getUserId()))
        {
            log.error("不可以参与自己发起的组队");
            throw new RuntimeException("不可以参与自己发起的组队");
        }

        Long activityId1 = pzcActivityGroupVo.getActivityId();
        if(activityId.equals(Math.toIntExact(activityId1)))
        {
            return true;
        }else {
            log.error("组队不在当前活动下");
            throw new RuntimeException("组队不在当前活动下");
        }

    }


    public Boolean allCheck(Integer activityId, Long groupId) {
        return checkActivity(activityId) && checkGroup(activityId,groupId);
    }


}
