package top.flya.system.xxlJob;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.flya.system.domain.PzcUser;
import top.flya.system.mapper.PzcUserMapper;

import javax.annotation.Resource;
import java.util.List;

@Component
@Slf4j
public class WxHandler {

    @Resource
    private PzcUserMapper pzcUserMapper;


//    /**
//     * 1、简单任务示例（Bean模式）
//     */
//    @XxlJob("demoJobHandler")
//    public void demoJobHandler() throws Exception {
//        XxlJobHelper.log("XXL-JOB, Hello World.");
//
//        for (int i = 0; i < 5; i++) {
//            XxlJobHelper.log("beat at:" + i);
//        }
//        // default success
//    }

    /**
     * 每天的0点自动同步  用户可以取消的次数取决于用户当前等级
     */
    @XxlJob("wxJobHandler")
    public void syncWxUserCancel() {
        XxlJobHelper.log("定时同步微信用户 的 取消活动次数 FL1906");
        log.info("定时同步微信用户 的 取消活动次数");
        List<PzcUser> pzcUsers = pzcUserMapper.selectList();
        for (PzcUser pzcUser : pzcUsers) {
            pzcUser.setUserLevel(Long.valueOf(getLevel(Math.toIntExact(pzcUser.getIntegration()))));
            pzcUser.setExemptCancel(Math.toIntExact(pzcUser.getUserLevel()));
            pzcUserMapper.updateById(pzcUser);
        }
        log.info("定时同步微信用户 的 取消活动次数 完成");
        XxlJobHelper.log("定时同步微信用户 的 取消活动次数 完成 FL1906");
    }


    public Integer getLevel(Integer jf) {
        if (jf <= 30) {
            return 1;
        }
        if (jf <= 70) {
            return 2;
        }
        if (jf <= 120) {
            return 3;
        }
        if (jf <= 180) {
            return 4;
        } else {
            return 5;
        }
    }

    /**
     *  每天的 1点自动同步积分 与取消次数 与用户等级 的映射关系 （用户升级） TODO 这个好像用不上 直接在订单处 进行
     */
}
