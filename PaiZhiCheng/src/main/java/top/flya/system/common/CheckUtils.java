package top.flya.system.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.flya.common.core.domain.R;
import top.flya.common.utils.StringUtils;
import top.flya.system.domain.PzcActivity;
import top.flya.system.mapper.PzcActivityMapper;

import javax.annotation.Resource;

@Slf4j
@Component
public class CheckUtils {
    @Resource
    private PzcActivityMapper pzcActivityMapper;

    /**
     * 0 是创建活动 1是修改活动
     * @param activity
     * @param type
     * @return
     */
    public R<Void> checkCreateActivity(PzcActivity activity, Integer type) {

        log.info("checkActivity check init");
        if(type==1)
        {

            if(activity.getActivityId()==null)
            {
                return R.fail("活动id不能为空");
            }
            PzcActivity checkActivity = pzcActivityMapper.selectById(activity.getActivityId());
            if(checkActivity==null)
            {
                return R.fail("活动不存在");
            }

        }
        if(StringUtils.isEmpty(activity.getTitle()))
        {
            return R.fail("活动标题不能为空");
        }
        if(StringUtils.isEmpty(activity.getStartTime()))
        {
            return R.fail("活动开始时间不能为空");
        }
        if(StringUtils.isEmpty(activity.getEndDate()))
        {
            return R.fail("活动结束时间不能为空");
        }
        if(StringUtils.isEmpty(activity.getCoverImage()))
        {
            return R.fail("活动封面不能为空");
        }


        return R.ok();
    }

}
