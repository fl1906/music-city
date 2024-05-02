package top.flya.system.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.util.DateUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.flya.common.constant.Constants;
import top.flya.common.core.controller.BaseController;
import top.flya.common.core.domain.R;
import top.flya.common.core.domain.event.LogininforEvent;
import top.flya.common.core.domain.model.XcxLoginUser;
import top.flya.common.enums.DeviceType;
import top.flya.common.helper.LoginHelper;
import top.flya.common.utils.MessageUtils;
import top.flya.common.utils.ServletUtils;
import top.flya.common.utils.spring.SpringUtils;
import top.flya.system.domain.*;
import top.flya.system.domain.bo.PayOrderBo;
import top.flya.system.domain.bo.PzcUserBo;
import top.flya.system.domain.bo.SuccessCallBackObjBo;
import top.flya.system.domain.vo.PzcUserHistoryVo;
import top.flya.system.handel.WxPayInitHandel;
import top.flya.system.mapper.*;
import top.flya.system.utils.CreateSign;
import top.flya.system.utils.WxUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wx/user")
@Slf4j
public class WxUserController extends BaseController {

    @Value("${wx.appId}")
    private String appId;

    @Value("${wx.appSecret}")
    private String secret;

    @Value("${sa-token.token-prefix}")
    private String tokenPrefix;

    @Value("${wx.merchantId}")
    private String mchId;

    @Value("${wx.api3}")
    private String api3;

    @Value("${wx.notifyUrl}")
    private String notifyUrl;

    @Value("${api.school}")
    private String schoolUrl;

    @Value("${api.apiKey}")
    private String apiKey;

    private final PzcUserMapper userMapper;

    private final WxUtils wxUtils;

    private final PzcUserHistoryMapper userHistoryMapper;

    @Autowired
    private WxPayInitHandel wxPayInitHandel;

    @Autowired
    private CreateSign createSign;

    @Autowired
    private PzcOrderMapper orderMapper;

    @Autowired
    private PzcOfficialMapper officialMapper;

    @Autowired
    private PzcUserTalkMapper talkMapper;

    @Autowired
    private SysUserMapper sysUserMapper;


    @Value("${neteaseCloudMusicApi}")
    private String neteaseCloudMusicApi;


    @GetMapping("/filterKeyWords")
    public R filterKeyWords(@RequestParam("msg") String msg) {
        wxUtils.checkMgc(msg);
        return R.ok();
    }

    @GetMapping("/music")
    public R music() {
        String url =  neteaseCloudMusicApi + sysUserMapper.selectUserById(1L).getNickName();
        log.info("url is {}", url);
        String result = HttpUtil.get(url);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return R.ok(jsonObject.getJSONArray("data").getJSONObject(0).getString("url"));
    }

    @GetMapping("/notRead") // 获取首页 未读消息 （红点点）
    public R notRead() {
        PzcUser user = wxUtils.checkUser();
        List<PzcOfficial> pzcOfficials = officialMapper.selectList(new QueryWrapper<PzcOfficial>().eq("to_user_id", user.getUserId()).eq("is_read", 0));
        Integer size1 = pzcOfficials.size();
        Integer size2 = talkMapper.selectList(new QueryWrapper<PzcUserTalk>().eq("to_user_id", user.getUserId()).eq("message_status", 0)).size();
        return R.ok(Math.min(size1 + size2, 99));
    }


    @PostMapping("/login") // 登录
    public R login(@RequestBody @Validated PzcUserBo loginUser) {
        String tokenValue = userLogin(loginUser);
        return (tokenValue != null) ? R.ok(tokenPrefix + " " + tokenValue) : R.fail("登录失败");
    }


    @GetMapping("/getSchoolList")
    public R getSchoolList(@RequestParam("schoolName") String schoolName) {
        String baseUrl = schoolUrl + schoolName + "&" + apiKey;
        log.info("baseUrl is {}", baseUrl);
        String result = HttpUtil.get(baseUrl);
        return R.ok(JSONObject.parseObject(result).get("data"));
    }

    @GetMapping("/userInfo") // 获取用户信息
    public R userInfo() {
        return R.ok(wxUtils.checkUser());
    }

    @PostMapping("/updateUserInfo") // 更新用户信息
    public R updateUserInfo(@RequestBody PzcUserBo pzcUserBo) {
        log.info("更新用户信息： pzcUserBo is {}", pzcUserBo);
        PzcUser user = wxUtils.checkUser();
        //获取现在时间和一年前的时间 并格式化
        String nowTime = DateUtils.format(new Date());
        String lastYearNow = LocalDateTime.of(LocalDate.now().minusYears(1), LocalDateTime.now().toLocalTime()).toString();
        log.info("nowTime is {} , lastYearNow is {}", nowTime, lastYearNow);

        if (pzcUserBo.getNickname() != null && !user.getNickname().equals(pzcUserBo.getNickname())) {
            //判断用户是否之前一年内是否更新过昵称
            List<PzcUserHistoryVo> pzcUserHistoryVos = userHistoryMapper.
                selectVoList(new QueryWrapper<PzcUserHistory>().eq("user_id", user.getUserId()).eq("type", 0).like("message", "%昵称%")
                    .between("create_time", lastYearNow, nowTime));
            if (!pzcUserHistoryVos.isEmpty()) {
                return R.fail("一年内只能修改一次昵称");
            } else {
                wxUtils.checkMgc(pzcUserBo.getNickname());

                //更新用户信息
                user.setNickname(pzcUserBo.getNickname());
                userMapper.updateById(user);
                //存入用户历史记录
                wxUtils.insertUserHistory(user.getUserId(), 0L, 0L, "昵称修改为" + pzcUserBo.getNickname(), null);
                return R.ok(userMapper.selectById(user.getUserId()));
            }
        } else {
            pzcUserBo.setMoney(user.getMoney());//余额不允许修改
            pzcUserBo.setUserId(user.getUserId());//用户id不允许修改
            pzcUserBo.setRealname(user.getRealname());//真实姓名不允许修改
            pzcUserBo.setPhone(user.getPhone());//手机号不允许修改
            pzcUserBo.setOpenid(user.getOpenid());//openid不允许修改
            pzcUserBo.setExemptCancel(user.getExemptCancel());//免责不允许修改

            Map<String, Object> map = BeanUtil.beanToMap(pzcUserBo);

            //存入用户历史记录
            wxUtils.insertUserHistory(user.getUserId(), 0L, 0L, "更新用户其他信息", null);
            return R.ok(updateUser(map, user));
        }

    }


    @PostMapping("/recharge") // 充值
    @Transactional
    public R createOrder(@RequestBody @Validated PayOrderBo payOrder) throws Exception {
        PzcUser user = wxUtils.checkUser();

        String openId = user.getOpenid();

        CloseableHttpClient httpClient = wxPayInitHandel.setup();

        //请求URL
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");

        HashMap<String, Object> amount = new HashMap<>();
        amount.put("total", payOrder.getCount());
        amount.put("currency", "CNY");

        HashMap<String, Object> payer = new HashMap<>();
        payer.put("openid", openId);

        String orderNum = IdUtil.getSnowflakeNextIdStr();

        HashMap<String, Object> toData = new HashMap<>();
        toData.put("amount", amount);
        toData.put("mchid", mchId);
        toData.put("description", "派币充值订单");
        toData.put("notify_url", notifyUrl);
        toData.put("payer", payer);
        toData.put("attach", orderNum);

        toData.put("out_trade_no", orderNum);
        toData.put("goods_tag", "WXG");
        toData.put("appid", appId);

        String s = JSONObject.toJSONString(toData);

        log.info("订单： " + s);


        StringEntity entity = new StringEntity(s, "utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        //完成签名并执行请求
        CloseableHttpResponse response = httpClient.execute(httpPost);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {

            String result = "prepay_id=" + JSONObject.parseObject(EntityUtils.toString(response.getEntity())).get("prepay_id").toString();
            response.close();
            //关闭连接
            wxPayInitHandel.after(httpClient);
            String nonceStr = "5K8264ILTKCH16CQ2502SI8ZNMTM67VS";
            long timestamp = System.currentTimeMillis() / 1000;

            String paySign = createSign.getSign(appId, timestamp, nonceStr, result);

            HashMap<String, Object> map = new HashMap<>();

            map.put("timestamp", String.valueOf(timestamp));
            map.put("nonceStr", nonceStr);
            map.put("package", result);
            map.put("signType", "RSA");
            map.put("paySign", paySign);
            map.put("orderNum", orderNum);

            //创建未支付的充值订单
            PzcOrder pzcRechargeOrder = new PzcOrder();
            pzcRechargeOrder.setActivityId(null);
            pzcRechargeOrder.setMoney(BigDecimal.valueOf(payOrder.getCount()).divide(BigDecimal.valueOf(100)));
            pzcRechargeOrder.setOrderStatus(0L);
            pzcRechargeOrder.setType(0L);
            pzcRechargeOrder.setOutOrderNum(orderNum);
            pzcRechargeOrder.setIntro("派币充值订单");
            pzcRechargeOrder.setTitle("派币充值");
            pzcRechargeOrder.setUserId(user.getUserId());

            orderMapper.insert(pzcRechargeOrder);


            return R.ok(map);

        } else if (statusCode == 204) {
            log.info("success");
            response.close();
            //关闭连接
            wxPayInitHandel.after(httpClient);
            return R.ok("成功但未返回预支付订单号");
        } else {
            log.info("failed,resp code = " + statusCode + ",return body = " + EntityUtils.toString(response.getEntity()));
            String result = EntityUtils.toString(response.getEntity());
            response.close();
            //关闭连接
            wxPayInitHandel.after(httpClient);
            return R.fail("创建预支付订单失败: " + result);
        }
    }

    /**/
    @RequestMapping("/callback")
    @Transactional
    public R callback(HttpServletRequest request, @RequestBody SuccessCallBackObjBo obj) {
        log.info("进入支付回调啦~");
        String associated_data = obj.getResource().getAssociated_data();
        String nonce = obj.getResource().getNonce();
        String ciphertext = obj.getResource().getCiphertext();
        AesUtil aesUtil = new AesUtil(api3.getBytes());
        try {
            String s = aesUtil.decryptToString(associated_data.getBytes(), nonce.getBytes(), ciphertext);

            JSONObject jsonObject = JSONObject.parseObject(s);
            jsonObject.forEach((k, v) -> log.info("k:" + k + "  v:" + v + "\n"));
            String orderNum = jsonObject.getString("out_trade_no");
            //更新订单状态和用户余额
            PzcOrder pzcOrder = orderMapper.selectOne(new QueryWrapper<PzcOrder>().eq("out_order_num", orderNum));
            if (pzcOrder == null) {
                return R.fail("订单不存在");
            }
            if (pzcOrder.getOrderStatus() == 1) {
                return R.fail("订单已支付");
            }
            pzcOrder.setOrderStatus(1L);
            orderMapper.updateById(pzcOrder);
            Long userId = pzcOrder.getUserId();
            PzcUser user = userMapper.selectById(userId);
            user.setMoney(user.getMoney().add(pzcOrder.getMoney()));
            userMapper.updateById(user);
            wxUtils.insertUserHistory(user.getUserId(), 0L, 2L, "派币充值【" + pzcOrder.getMoney() + "】", pzcOrder.getMoney());


        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        return R.ok();
    }

    // 假设接收到的请求参数为Map<String, Object> userInfo
    public PzcUser updateUser(Map<String, Object> userInfo, PzcUser user) {

        // 反射动态更新用户信息
        try {
            Class<?> clazz = user.getClass();
            for (Map.Entry<String, Object> entry : userInfo.entrySet()) {
                String fieldName = entry.getKey();
                Object fieldValue = entry.getValue();
                log.info("fieldName is {} , fieldValue is {}", fieldName, fieldValue);
                if (fieldValue != null) {
                    wxUtils.checkMgc(String
                        .valueOf(fieldValue)); //敏感词检测
                }


                if (fieldValue instanceof Map) //跳过map类型
                {
                    continue;
                }

                if (fieldValue != null) {
                    Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.set(user, fieldValue);
                }

            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("更新用户信息失败 反射异常");
        }

        // 保存更新后的用户信息
        userMapper.updateById(user);
        return userMapper.selectById(user.getUserId());
    }

    public String userLogin(PzcUserBo pzcUserBo) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId +
            "&secret=" + secret + "&js_code=" + pzcUserBo.getLoginCode() + "&grant_type=authorization_code";

        String response = HttpUtil.get(url);
        log.info("微信小程序登录 url : {}，response is {}", url, response);
        JSONObject wxUser = JSONObject.parseObject(response);
        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.checkValNull(wxUser) || wxUser.get("errcode") != null) {
            throw new RuntimeException("微信登录失败 可能是code过期了");
        }
        String openId = wxUser.get("openid").toString();
        //如果存在 就直接返回 不存在就新建用户
        PzcUser user = userMapper.selectOne(new QueryWrapper<PzcUser>().eq("openid", openId));
        if (user == null) {
            //存入用户信息
            PzcUser newUser = new PzcUser();
            newUser.setNickname(pzcUserBo.getNickname());
            newUser.setOpenid(openId);
            newUser.setAvatar(pzcUserBo.getAvatar());

            //新注册时 根据 POST https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=ACCESS_TOKEN 获取手机号
            String getPhoneUrl = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + wxUtils.getAccessToken();
            Map<String, String> codeMap = new HashMap<>();
            codeMap.put("code", pzcUserBo.getPhoneCode());
            String phoneResponse = HttpUtil.post(getPhoneUrl, JSONUtil.toJsonStr(codeMap));
            log.info("微信小程序获取用户手机号信息 url : {}，response is {}", getPhoneUrl, phoneResponse);
            cn.hutool.json.JSONObject phoneJson = JSONUtil.parseObj(phoneResponse);
            if (phoneJson.getInt("errcode") != 0) {
                log.info("微信小程序获取用户手机号信息失败");
                throw new RuntimeException("微信小程序获取用户手机号信息失败");
            }
            newUser.setPhone(phoneJson.getJSONObject("phone_info").getStr("purePhoneNumber"));
            newUser.setSex(pzcUserBo.getSex());
            newUser.setMoney(new BigDecimal(100)); //新用户注册送1元
            newUser.setUserLevel(1L);

            int insert = userMapper.insert(newUser);
            log.info("insertUser: " + insert);
            user = userMapper.selectOne(new QueryWrapper<PzcUser>().eq("openid", openId));
        }

        if (user.getState() == 0) {
            throw new RuntimeException("用户已被禁用");
        }


        // 此处可根据登录用户的数据不同 自行创建 loginUser
        XcxLoginUser loginUser = new XcxLoginUser();
        loginUser.setUserId(Long.valueOf(user.getUserId()));
        loginUser.setUsername(user.getNickname());
        loginUser.setUserType("微信小程序用户");
        loginUser.setOpenid(openId);
        // 生成token
        LoginHelper.loginByDevice(loginUser, DeviceType.XCX);
        recordLogininfor(user.getNickname(), MessageUtils.message("user.login.success"));
        return StpUtil.getTokenValue();
    }

    private void recordLogininfor(String username, String message) {
        LogininforEvent logininforEvent = new LogininforEvent();
        logininforEvent.setUsername(username);
        logininforEvent.setStatus(Constants.LOGIN_SUCCESS);
        logininforEvent.setMessage(message);
        logininforEvent.setRequest(ServletUtils.getRequest());
        SpringUtils.context().publishEvent(logininforEvent);
    }


    @PostMapping("/sendArriveMsg") //推送微信小程序通知
    public R sendArriveMsg(String toUserOpenId, String data) {
        String getTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?" + "grant_type=client_credential&appid=" + appId + "&secret=" + secret;
        String response = HttpUtil.get(getTokenUrl);
        log.info("微信小程序获取token url : {}，response is {}", getTokenUrl, response);
        JSONObject wxUser = JSONObject.parseObject(response);
        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.checkValNull(wxUser) || wxUser.get("errcode") != null) {
            throw new RuntimeException("微信登录失败 可能是code过期了");
        }
        String accessToken = wxUser.get("access_token").toString();
        String msgUrl = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken;
        Map<String, Object> map = new HashMap<>();
        map.put("template_id", "MMHCiz9Z5faTwbDI9ywE0ScIvGMeDduTxXm00wdLxmw");
        map.put("touser", toUserOpenId);
        map.put("data", data);
        map.put("miniprogram_state", "trial");//developer为开发版；trial为体验版；formal为正式版；默认为正式版
        map.put("lang", "zh_CN");
        String msgResponse = HttpUtil.post(msgUrl, JSONUtil.toJsonStr(map));
        log.info("微信小程序推送消息 url : {}，response is {}", msgUrl, msgResponse);
        JSONObject msgJson = JSONObject.parseObject(msgResponse);
        if (msgJson.getInteger("errcode") != 0) {
            throw new RuntimeException("微信小程序推送消息失败");
        }

        return R.ok(msgJson.get("errcode").toString());
    }


}
