package top.flya.system.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.flya.common.core.domain.R;
import top.flya.common.helper.LoginHelper;
import top.flya.system.domain.PzcOfficial;
import top.flya.system.domain.PzcUser;
import top.flya.system.domain.PzcUserHistory;
import top.flya.system.domain.vo.PzcActivityGroupApplyVo;
import top.flya.system.mapper.PzcOfficialMapper;
import top.flya.system.mapper.PzcUserHistoryMapper;
import top.flya.system.mapper.PzcUserMapper;
import top.flya.system.service.IPzcActivityGroupApplyService;
import top.flya.system.utils.sensitivewordsfiliter.WorldsFilterUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class WxUtils {

    @Value("${wx.appId}")
    private String appId;

    @Value("${wx.appSecret}")
    private String secret;

    @Resource
    private PzcUserMapper userMapper;

    @Resource
    private PzcUserHistoryMapper userHistoryMapper;

    @Resource
    private IPzcActivityGroupApplyService iPzcActivityGroupApplyService;

    @Resource
    private PzcOfficialMapper pzcOfficialMapper;

    public void checkMgc(String msg)
    {
        if(msg==null|| msg.isEmpty())
        {
            return;
        }
       if(WorldsFilterUtils.checkBySystemWords(msg))
       {
           throw new RuntimeException("输入内容包含敏感词汇,请重新输入");
       }
    }



    public R sendArriveMsg(String toUserOpenId, Map data) {
        String getTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?" + "grant_type=client_credential&appid=" + appId + "&secret=" + secret;
        String response = HttpUtil.get(getTokenUrl);
        log.info("微信小程序获取token url : {}，response is {}", getTokenUrl, response);
        JSONObject wxUser = JSONObject.parseObject(response);
        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.checkValNull(wxUser) || wxUser.get("errcode") != null) {
            throw new RuntimeException("微信登录失败 可能是code过期了");
        }
        String accessToken = wxUser.get("access_token").toString();
        String msgUrl= "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token="+accessToken;
        Map<String, Object> map = new HashMap<>();
        map.put("template_id","MMHCiz9Z5faTwbDI9ywE0ScIvGMeDduTxXm00wdLxmw");
        map.put("touser",toUserOpenId);
        map.put("data",data);
        map.put("miniprogram_state","formal");//developer为开发版；trial为体验版；formal为正式版；默认为正式版
        map.put("lang","zh_CN");
        log.info("request is {}",JSONUtil.toJsonStr(map));
        String msgResponse = HttpUtil.post(msgUrl, JSONUtil.toJsonStr(map));
        log.info("response is {}",msgResponse);
        JSONObject msgJson = JSONObject.parseObject(msgResponse);
        if (msgJson.getInteger("errcode") != 0) {
            throw new RuntimeException("微信小程序推送消息失败");
        }

        return R.ok(msgJson.get("errcode").toString());
    }


    public PzcActivityGroupApplyVo  checkApplyConfirm(Long applyId)
    {

        //首先判断 这个applyId 的状态 以及是否存在
        PzcActivityGroupApplyVo pzcActivityGroupApplyVo = iPzcActivityGroupApplyService.queryById(applyId);
        if(pzcActivityGroupApplyVo==null)
        {
            throw new RuntimeException("申请不存在");
//            return R.fail("申请不存在");
        }
        Integer applyStatus = pzcActivityGroupApplyVo.getApplyStatus();
        if(applyStatus==-1||applyStatus==0||applyStatus==3)
        {
            throw  new RuntimeException("该订单位于【"+applyStatus(applyStatus)+"】状态，不可确认");
        }


        return pzcActivityGroupApplyVo;
    }

    public PzcActivityGroupApplyVo checkApplyPj(long applyId) {
        //首先判断 这个applyId 的状态 以及是否存在
        PzcActivityGroupApplyVo pzcActivityGroupApplyVo = iPzcActivityGroupApplyService.queryById(applyId);
        if(pzcActivityGroupApplyVo==null)
        {
            throw new RuntimeException("申请不存在");
        }
        Integer applyStatus = pzcActivityGroupApplyVo.getApplyStatus();
        //-1 已取消 0 位于申请列表中 1 申请通过待确认时
        //2 确认通过进行中 3 组队结束  9发起方已确认
        // 10申请方已确认 11 发起方已打卡 12 申请方已打卡
        //13 申请方已评价 14 发起方已评价 15 双方已评价
        if(applyStatus==3||applyStatus==13||applyStatus==14)
        {
            return pzcActivityGroupApplyVo;
        }else {
            throw  new RuntimeException("该订单位于【"+applyStatus(applyStatus)+"】状态，不可评价");
        }

    }




    public String applyStatus(Integer applyStatus)
    {
        if(applyStatus==-1)
        {
            return "已取消";
        }
        if(applyStatus==0)
        {
            return "位于申请列表中";
        }
        if(applyStatus==1)
        {
            return "申请通过待确认";
        }
        if(applyStatus==2)
        {
            return "已确认，进行中";
        }
        if(applyStatus==3)
        {
            return "已完成";
        }
        if(applyStatus==9)
        {
            return "发起方已确认";
        }
        if(applyStatus==10)
        {
            return "申请方已确认";
        }
        if(applyStatus==11)
        {
            return "发起方已打卡";
        }
        if (applyStatus==12)
        {
            return "申请方已打卡";
        }
        if (applyStatus==13)
        {
            return "发起方已评价";
        }
        if (applyStatus==14)
        {
            return "申请方已评价";
        }
        if (applyStatus==15)
        {
            return "双方已评价";
        }

        return null;
    }

    public String getAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId +
            "&secret=" + secret;
        String result = HttpUtil.get(url);
        return JSONUtil.parseObj(result).getStr("access_token");
    }
    public PzcUser checkUser()
    {
        Long userId = LoginHelper.getUserId();
        PzcUser user = userMapper.selectById(userId);
        if (user == null || StringUtils.isEmpty(user.getOpenid())|| user.getState() == 0) {
            throw new RuntimeException("用户不存在 或者已被禁用");
        }
        return user;
    }

    public void insertPzcOfficialMsg(Long fromUserId,Long toUserId,String title,String content,Long groupId,Long activityId)
    {
        PzcOfficial pzcOfficial = new PzcOfficial();
        pzcOfficial.setIsRead(0L);

        pzcOfficial.setFromUserId(fromUserId);
        pzcOfficial.setTitle(title);
        pzcOfficial.setContent(content);
        pzcOfficial.setToUserId(toUserId);
        pzcOfficial.setGroupId(groupId);
        pzcOfficial.setActivityId(activityId);
        int insert = pzcOfficialMapper.insert(pzcOfficial);
        log.info("插入官方消息条数：{}\n内容为：{}", insert,JSONUtil.toJsonPrettyStr(pzcOfficial));
    }


    public void insertUserHistory(Long userId, Long activityId, Long type, String message, BigDecimal money)
    {
        PzcUserHistory userHistory = new PzcUserHistory();
        userHistory.setUserId(userId);
        userHistory.setActivityId(activityId);
        userHistory.setType(type);
        userHistory.setMessage(message);
        userHistory.setMoney(money);
        int insert = userHistoryMapper.insert(userHistory);
        log.info("插入用户历史记录 信息为： {} 条数为： {}",message,insert);
    }


    public void checkApplyScore(Integer score) {
        if(score!=0&&score!=3&&score!=-3)
        {
            throw new RuntimeException("评分只能为差评（-3 积分） 中评（+0 积分） 好评（+3 积分）");
        }
    }

    public void updateUserMsg(PzcUser otherUser) {
        if(otherUser.getIntegrationNow()>=30)
        {
            otherUser.setUserLevel(2L);
        }
        if(otherUser.getIntegrationNow()>=70)
        {
            otherUser.setUserLevel(3L);
        }
        if(otherUser.getIntegrationNow()>=150)
        {
            otherUser.setUserLevel(4L);
        }
        if(otherUser.getIntegrationNow()>=300)
        {
            otherUser.setUserLevel(5L);
        }
    }
}
