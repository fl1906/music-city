package top.flya.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.util.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.flya.common.annotation.Log;
import top.flya.common.annotation.RepeatSubmit;
import top.flya.common.core.controller.BaseController;
import top.flya.common.core.domain.PageQuery;
import top.flya.common.core.domain.R;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.core.validate.EditGroup;
import top.flya.common.enums.BusinessType;
import top.flya.common.helper.LoginHelper;
import top.flya.common.utils.JsonUtils;
import top.flya.common.utils.poi.ExcelUtil;
import top.flya.system.domain.*;
import top.flya.system.domain.bo.PzcActivityGroupBo;
import top.flya.system.domain.bo.RefurbishBo;
import top.flya.system.domain.vo.PzcActivityGroupApplyVo;
import top.flya.system.domain.vo.PzcActivityGroupVo;
import top.flya.system.domain.vo.RefurbishVO;
import top.flya.system.mapper.*;
import top.flya.system.service.IPzcActivityGroupApplyService;
import top.flya.system.service.IPzcActivityGroupService;
import top.flya.system.utils.WxUtils;
import top.flya.system.utils.gaode.GaoDeMapUtil;
import top.flya.system.xxlJob.ScheduledExecutorUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static top.flya.system.config.ClientCache.concurrentHashMap;

/**
 * 活动组队
 *
 * @author ruoyi
 * @date 2023-07-10
 */
@Validated
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/system/activityGroup")
public class PzcActivityGroupController extends BaseController {

    private final IPzcActivityGroupService iPzcActivityGroupService;

    private final IPzcActivityGroupApplyService iPzcActivityGroupApplyService;

    private final PzcUserMapper pzcUserMapper;

    private final PzcActivityMapper pzcActivityMapper;

    private final PzcUserPhotoMapper pzcUserPhotoMapper;

    private final WxUtils wxUtils;

    private final PzcActivityGroupMapper pzcActivityGroupMapper;

    private final PzcActivityGroupApplyMapper pzcActivityGroupApplyMapper;

    private final PzcOfficialMapper pzcOfficialMapper;

    private final PzcUserTalkMapper pzcUserTalkMapper;

    private final GaoDeMapUtil gaoDeMapUtil;
    private final ExecutorService newSingleThreadExecutor = new ThreadPoolExecutor(10, 20, 200L,
        TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(100),
        Executors.defaultThreadFactory(),
        new ThreadPoolExecutor.CallerRunsPolicy() //指定拒绝策略 防止任务不执行
    );

    @PostMapping("/cancelIssueGroup") //取消组队的发布
    public R cancelIssueGroup(@RequestParam("groupId") Long groupId) {
        log.info("取消组队的发布: {}", groupId);
        Long userId = LoginHelper.getUserId();
        //首先看看组队是否是我发布的 并且是否有人申请 如果有人申请则不能取消
        PzcActivityGroup pzcActivityGroup = pzcActivityGroupMapper.selectById(groupId);
        if (pzcActivityGroup == null || !pzcActivityGroup.getUserId().equals(userId)) {
            return R.fail("组队不存在~");
        }
        // 查询申请列表 如果有人申请则不能取消
        QueryWrapper<PzcActivityGroupApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_id", groupId);
        List<PzcActivityGroupApply> pzcActivityGroupApplies = pzcActivityGroupApplyMapper.selectList(queryWrapper);
        List<Long> delApplyIds = new ArrayList<>();
        pzcActivityGroupApplies.forEach(
            pzcActivityGroupApply -> {
                if (pzcActivityGroupApply.getApplyStatus() != -1 && pzcActivityGroupApply.getApplyStatus() != 0 && pzcActivityGroupApply.getApplyStatus() != 15) {
                    throw new RuntimeException("有组队正在进行中，不能取消");
                }
                if (pzcActivityGroupApply.getApplyStatus() == 0) {
                    delApplyIds.add(pzcActivityGroupApply.getActivityId());
                    //异步给对方发消息 通知对方已拒绝 修改为异步请求
                    newSingleThreadExecutor.execute(() -> {

                        wxUtils.insertPzcOfficialMsg(userId, pzcActivityGroupApply.getUserId(),
                            "来自您的申请组队【" + pzcActivityGroup.getTitle() + "】信息：",
                            "您的申请组队【" + pzcActivityGroup.getTitle() + "】已被发布者取消",
                            pzcActivityGroup.getGroupId(), pzcActivityGroup.getActivityId());
                    });

                }

            }
        );
        //删除申请
        if (delApplyIds.size() > 0) {
            pzcActivityGroupApplyMapper.deleteBatchIds(delApplyIds);
        }
        //删除组队
        pzcActivityGroupMapper.deleteById(groupId);
        return R.ok();
    }


    @PostMapping("/refurbish") //刷新
    public R refurbish(@RequestBody RefurbishBo refurbishBo) throws Exception {
        log.info("刷新: {}", JsonUtils.toJsonString(refurbishBo));
        //首先查询这个组队是否是我发起的
        PzcActivityGroupApply pzcActivityGroupApply = pzcActivityGroupApplyMapper.selectById(refurbishBo.getApplyId());
        if (pzcActivityGroupApply == null) {
            return R.fail("申请不存在");
        }

        PzcActivityGroup pzcActivityGroup = pzcActivityGroupMapper.selectById(pzcActivityGroupApply.getGroupId());
        Pattern pattern = Pattern.compile("【(.*?)】(.*?)");
        Matcher matcher = pattern.matcher(pzcActivityGroup.getAddress());

        String end = "";
        if (matcher.find()) {
            end = matcher.group(1);
        } else {
            return R.fail("地址解析失败,请联系管理员");
        }

        String[] startJwd = refurbishBo.getAddress().split(",");
        Long data = gaoDeMapUtil.getDistance(refurbishBo.getAddress(), end).getData();
        refurbishBo.setAddress(gaoDeMapUtil.getAddress(startJwd[0], startJwd[1]).getData().toString());
        log.info("调用高德API获取到的用户目前地址为: {}", refurbishBo.getAddress());
        log.info("调用高德API获取到的用户距离目标地址距离为: {}", data);
        if (refurbishBo.getRole() == 0) //申请方
        {
            //判断申请方现在是否可以打卡
            pzcActivityGroupApply.setApplyAddress(refurbishBo.getAddress());
            pzcActivityGroupApplyMapper.updateById(pzcActivityGroupApply);
            RefurbishVO refurbishVO = new RefurbishVO();
            refurbishVO.setApplyAddress(refurbishBo.getAddress());
            refurbishVO.setApplyId(refurbishBo.getApplyId());
            refurbishVO.setStartAddress(pzcActivityGroupApply.getStartAddress());
            refurbishVO.setDistance(data);
            log.info("刷新返回的信息为： {}", JSONUtil.toJsonPrettyStr(refurbishVO));

            return R.ok(refurbishVO);

        } else {
            pzcActivityGroupApply.setStartAddress(refurbishBo.getAddress());
            pzcActivityGroupApplyMapper.updateById(pzcActivityGroupApply);
            RefurbishVO refurbishVO = new RefurbishVO();
            refurbishVO.setApplyAddress(pzcActivityGroupApply.getApplyAddress());
            refurbishVO.setApplyId(refurbishBo.getApplyId());
            refurbishVO.setStartAddress(refurbishBo.getAddress());
            refurbishVO.setDistance(data);
            log.info("刷新返回的信息为： {}", JSONUtil.toJsonPrettyStr(refurbishVO));

            return R.ok(refurbishVO);
        }
    }


    @PostMapping("/cancel") //取消 双方都可以取消
    @Transactional
    @RepeatSubmit()
    public R cancel(@RequestParam("applyId") Integer applyId) {

        //首先查询这个组队是否是我发起的
        PzcActivityGroupApplyVo pzcActivityGroupApplyVo = iPzcActivityGroupApplyService.queryById(applyId.longValue());
        if (pzcActivityGroupApplyVo == null) {
            return R.fail("申请不存在");
        }
        Long userId = LoginHelper.getUserId();
        Long groupId = pzcActivityGroupApplyVo.getGroupId();
        PzcActivityGroupVo pzcActivityGroupVo = iPzcActivityGroupService.queryById(groupId);
        if (pzcActivityGroupVo == null) {
            return R.fail("组队不存在");
        }


        PzcUser otherUser = null;
        if (pzcActivityGroupVo.getUserId().equals(userId)) //我是发起人 取消
        {
            otherUser = pzcUserMapper.selectById(pzcActivityGroupApplyVo.getUserId());

        } else { //我是申请人
            otherUser = pzcUserMapper.selectById(pzcActivityGroupMapper.selectById(groupId).getUserId());
        }
        // 给对方发官方消息 通知对方已取消
        wxUtils.insertPzcOfficialMsg(userId, otherUser.getUserId(),
            "来自" + otherUser.getNickname() + "与您的组队信息：",
            "在【" + pzcActivityGroupVo.getTitle() + "】组队中途，对方已经取消本次组队。您可以再次同意或者申请其他用户",
            groupId, pzcActivityGroupVo.getActivityId());

        Integer applyStatus = pzcActivityGroupApplyVo.getApplyStatus();
        if (applyStatus != 0 && applyStatus != 1 && applyStatus != 9 && applyStatus != 10) {
            return R.fail("该订单位于【" + wxUtils.applyStatus(applyStatus) + "】状态，不可取消");
        }

        //查询用户是否有免责取消次数
        PzcUser pzcUser = pzcUserMapper.selectById(LoginHelper.getUserId());
        if (pzcUser.getExemptCancel() > 0) {
            pzcUser.setExemptCancel(pzcUser.getExemptCancel() - 1);
        } else {
            pzcUser.setMoney(pzcUser.getMoney().subtract(new BigDecimal("10"))); //扣除10派币
            wxUtils.insertUserHistory(pzcUser.getUserId(), pzcActivityGroupVo.getActivityId(), 4L, "取消组队扣除10派币", new BigDecimal("10").negate());
        }
        pzcUserMapper.updateById(pzcUser);
        //修改状态为 已取消

        newSingleThreadExecutor.execute(() -> {
            // 更新 组队状态为已结束
            PzcActivityGroup pzcActivityGroup = pzcActivityGroupMapper.selectById(pzcActivityGroupVo.getGroupId());
            pzcActivityGroup.setStatus(1); //已结束
            pzcActivityGroupMapper.updateById(pzcActivityGroup);
        });
        return R.ok(iPzcActivityGroupApplyService.updateStatus(applyId.longValue(), -1));
    }


    @PostMapping("/cancelByGroupIn") //活动过程中取消组队 扣除保证金 + 20派币 退还对方派币 + 对方的保证金 通知对方
    @Transactional
    @RepeatSubmit()
    public R cancelByGroupIn(@RequestParam("applyId") Integer applyId) {
        Long userId = LoginHelper.getUserId();
        PzcUser my = pzcUserMapper.selectById(userId);
        PzcActivityGroupApplyVo pzcActivityGroupApplyVo = iPzcActivityGroupApplyService.queryById(applyId.longValue());
        if (pzcActivityGroupApplyVo == null) {
            return R.fail("申请不存在");
        }

        PzcActivityGroup pzcActivityGroup = pzcActivityGroupMapper.selectById(pzcActivityGroupApplyVo.getGroupId());
        if (pzcActivityGroupApplyVo.getUserId().equals(userId)) //我是申请方
        {
            //把钱都返还给发起方
            BigDecimal money = pzcActivityGroupApplyVo.getMoney();
            money = money.subtract(new BigDecimal("20"));
            PzcUser startUser = pzcUserMapper.selectById(pzcActivityGroup.getUserId());
            startUser.setMoney(startUser.getMoney().add(money).add(pzcActivityGroup.getMoney())); //全额返回给发起方的保证金 + 对方扣除 0.2保证金 后的派币
            pzcUserMapper.updateById(startUser);


            BigDecimal finalMoney = money;
            newSingleThreadExecutor.execute(() -> {

                //官方推送消息
                wxUtils.insertPzcOfficialMsg(userId, startUser.getUserId(),
                    "来自" + my.getNickname() + "与您的组队信息：",
                    "很遗憾地通知您：您在【" + pzcActivityGroup.getTitle() + "】组队活动中，申请方已经取消本次组队活动。对方的违约金 【" + finalMoney + "派币】已纳入您的账户。您可以再次同意或者申请其他用户。",
                    pzcActivityGroup.getGroupId(), pzcActivityGroup.getActivityId());

                //历史记录
                wxUtils.insertUserHistory(userId, pzcActivityGroup.getActivityId(), 4L, "在【" + pzcActivityGroup.getTitle() + "】活动中途取消组队，扣除违约金 【" + pzcActivityGroup.getMoney() + "】派币", pzcActivityGroup.getMoney().negate());
                wxUtils.insertUserHistory(startUser.getUserId(), pzcActivityGroup.getActivityId(), 2L, "在【" + pzcActivityGroup.getTitle() + "】活动中途取消组队，违约金所得 【" + pzcActivityGroup.getMoney().subtract(new BigDecimal("20")) + "】派币", pzcActivityGroup.getMoney().subtract(new BigDecimal("20")));

            });


        } else {
            //我是发起方
            PzcUser applyUser = pzcUserMapper.selectById(pzcActivityGroupApplyVo.getUserId());
            applyUser.setMoney(applyUser.getMoney().
                add(pzcActivityGroup.getMoney().subtract(new BigDecimal("20")))
                .add(pzcActivityGroupApplyVo.getMoney())); //全额返回给申请方的保证金
            pzcUserMapper.updateById(applyUser);


            newSingleThreadExecutor.execute(() -> {
                //官方推送消息
                wxUtils.insertPzcOfficialMsg(userId, applyUser.getUserId(),
                    "来自" + my.getNickname() + "与您的组队信息：",
                    "很遗憾地通知您：您在在【" + pzcActivityGroup.getTitle() + "】组队活动中，发起方已经取消本次组队活动。对方的违约金 【" + pzcActivityGroup.getMoney().subtract(new BigDecimal("20")) + "派币】已纳入您的账户。您可以再次同意或者申请其他用户。",
                    pzcActivityGroup.getGroupId(), pzcActivityGroup.getActivityId());

                //历史记录
                wxUtils.insertUserHistory(userId, pzcActivityGroup.getActivityId(), 4L, "在【" + pzcActivityGroup.getTitle() + "】活动中途取消组队，违约金 【" + pzcActivityGroup.getMoney() + "】派币", pzcActivityGroup.getMoney().negate());
                wxUtils.insertUserHistory(applyUser.getUserId(), pzcActivityGroup.getActivityId(), 2L, "在【" + pzcActivityGroup.getTitle() + "】活动中途取消组队，违约金所得 【" + pzcActivityGroup.getMoney().subtract(new BigDecimal("20")) + "】派币", pzcActivityGroup.getMoney().subtract(new BigDecimal("20")));

            });

        }

        newSingleThreadExecutor.execute(() -> {
            // 更新 组队状态为已结束
            pzcActivityGroup.setStatus(1); //已结束
            pzcActivityGroupMapper.updateById(pzcActivityGroup);
        });

        return R.ok(iPzcActivityGroupApplyService.updateStatus(applyId.longValue(), -1));//修改状态为 已取消
    }

    /**
     * 我创建的活动的申请列表
     * 思路整理
     * 首先查出所有 GroupId
     * 然后查出groupId 对应的申请列表
     *
     * @return
     */
    @GetMapping("/applyList")
    public R<List<PzcActivityGroupApplyVo>> applyList() {
        PzcActivityGroupBo bo = new PzcActivityGroupBo();
        bo.setUserId(LoginHelper.getUserId());
        List<PzcActivityGroupVo> pzcActivityGroupVos = iPzcActivityGroupService.queryList(bo);
        List<Long> groupIds = pzcActivityGroupVos.stream().map(PzcActivityGroupVo::getGroupId).collect(Collectors.toList());
        if (groupIds.size() == 0) {
            return R.ok();
        }

        List<PzcActivityGroupApplyVo> pzcActivityGroupApplyVos = iPzcActivityGroupApplyService.queryListByGroupIds(groupIds);
        pzcActivityGroupApplyVos.forEach(
            s -> {
                PzcUser pzcUser = pzcUserMapper.selectById(s.getUserId());
                s.setNickName(pzcUser.getNickname());
                s.setAvatar(pzcUser.getAvatar());
                Integer region = pzcActivityGroupVos.stream().filter(s1 -> s1.getGroupId().equals(s.getGroupId())).findFirst().get().getRegion();

                String title = "";
                PzcActivity pzcActivity = pzcActivityMapper.selectById(s.getActivityId());
                if (s.getActivityId() == 0) {
                    PzcRegion pzcRegion = pzcRegionMapper.selectById(region);
                    if (pzcRegion != null) {
                        title = "【" + pzcRegion.getName() + "】";
                    }
                    log.info("申请活动列表 title:{}", title);

                } else {
                    if (pzcActivity != null) {
                        title = pzcActivity.getTitle();
                    } else {
                        title = "【活动已结束】";
                    }

                }

                s.setActivityTitle(title);
                s.setGroupTitle(pzcActivityGroupVos.stream().filter(s1 -> s1.getGroupId().equals(s.getGroupId())).findFirst().get().getTitle());
            }
        );
        List<PzcActivityGroupApplyVo> result = pzcActivityGroupApplyVos.stream().filter(s -> s.getApplyStatus() == 0).collect(Collectors.toList());//过滤掉已取消的
        return R.ok(result);
    }

    @GetMapping("/userInfo") //查看申请人或者发起人信息
    public R userInfo(@RequestParam("userId") Long userId, @RequestParam("groupId") Long groupId) {
        //首先查询该用户是否申请了我的组 申请了 才有资格去查看  这个不应该是申请的 人才能看   双方都能互相看
        PzcActivityGroupApplyVo pzcActivityGroupApplyVo = iPzcActivityGroupApplyService.queryByUserIdAndGroupId(userId, groupId);
        if (pzcActivityGroupApplyVo == null) {
            PzcActivityGroupVo pzcActivityGroupVo = iPzcActivityGroupService.queryById(groupId);
            if (!userId.equals(pzcActivityGroupVo.getUserId())) {
                return R.fail("无权查看该用户信息");
            }
            pzcActivityGroupApplyVo = iPzcActivityGroupApplyService.queryByUserIdAndGroupId(LoginHelper.getUserId(), groupId);

        }
        PzcUser pzcUser = pzcUserMapper.selectById(userId);
        pzcUser.setMoney(null);
        pzcUser.setUserPhoto(pzcUserPhotoMapper.selectList(new QueryWrapper<>(new PzcUserPhoto()).eq("user_id", userId)));

        PzcActivityGroup pzcActivityGroup = pzcActivityGroupMapper.selectById(groupId);
        pzcActivityGroup.setAddress(pzcActivityGroup.getAddress().substring(pzcActivityGroup.getAddress().indexOf("】") + 1));

        pzcActivityGroupApplyVo.setActivityTitle(pzcActivityGroup.getActivityName());
        pzcUser.setPzcActivityGroupApplyVo(pzcActivityGroupApplyVo);
        pzcUser.setPzcActivityGroup(pzcActivityGroup);
        pzcUser.setLiveStatus(concurrentHashMap.get(userId) != null);
        pzcUser.setNotReadCount(pzcUserTalkMapper.selectNotReadCount(userId, LoginHelper.getUserId(), LoginHelper.getUserId()));
        pzcUser.setExemptCancel(pzcUserMapper.selectById(LoginHelper.getUserId()).getExemptCancel()); //获取我的免责取消次数
        return R.ok(pzcUser);
    }

    /**
     * 13 发起方已评价
     * 14 申请方已评价
     * 15 双方已评价
     *
     * @param applyId
     * @return
     */

    @PostMapping("/pj") //双方评价 （可选）
    @Transactional
    public R pj(@RequestParam("applyId") Integer applyId, @RequestParam("score") Integer score) {
        wxUtils.checkApplyScore(score);
        PzcActivityGroupApplyVo pzcActivityGroupApplyVo = wxUtils.checkApplyPj(applyId.longValue());
        //首先获取我的UserId
        Long userId = LoginHelper.getUserId();
        Long groupId = pzcActivityGroupApplyVo.getGroupId();
        Integer applyStatus = pzcActivityGroupApplyVo.getApplyStatus();
        if (pzcActivityGroupApplyVo.getUserId().equals(userId)) {
            //获取对方 userId 并且修改对方积分
            Long otherUserId = pzcActivityGroupMapper.selectById(groupId).getUserId();
            PzcUser otherUser = pzcUserMapper.selectById(otherUserId);
            if (applyStatus == 13) {
                return R.fail("您已经评价过了 不可重复操作");
            }
            otherUser.setIntegration(otherUser.getIntegration() + score);
            otherUser.setIntegrationNow(otherUser.getIntegrationNow() + score);
            wxUtils.updateUserMsg(otherUser);

            pzcUserMapper.updateById(otherUser);

            if (applyStatus == 14) //发起方评价了
            {
                return R.ok(iPzcActivityGroupApplyService.updateStatus(applyId.longValue(), 15)); //双方都已评价
            }
            return R.ok(iPzcActivityGroupApplyService.updateStatus(applyId.longValue(), 13));//申请方评价
        }
        //判断当前 用户是否为组队发起人 如果不是 直接报错
        PzcActivityGroupVo pzcActivityGroupVo = iPzcActivityGroupService.queryById(groupId);
        if (pzcActivityGroupVo == null) {
            return R.fail("组队不存在");
        }
        if (!pzcActivityGroupVo.getUserId().equals(userId)) {
            return R.fail("你不是组队发起人");
        }
        if (applyStatus == 14) {
            return R.fail("您已经评价过了 不可重复操作");
        }

        // ============================================================================================
        Long otherUserId = pzcActivityGroupApplyVo.getUserId();
        PzcUser otherUser = pzcUserMapper.selectById(otherUserId);
        otherUser.setIntegration(otherUser.getIntegration() + score);
        otherUser.setIntegrationNow(otherUser.getIntegrationNow() + score);
        wxUtils.updateUserMsg(otherUser);
        pzcUserMapper.updateById(otherUser);


        //看看申请方是否评价了
        if (applyStatus == 13) //申请方评价了
        {
            return R.ok(iPzcActivityGroupApplyService.updateStatus(applyId.longValue(), 15)); //双方都已评价
        }

        return R.ok(iPzcActivityGroupApplyService.updateStatus(applyId.longValue(), 14));//发起方评价

    }


    /**
     * 双方都到达了目的地 开始扣手续费
     *
     * @param applyId
     * @return
     */
    @PostMapping("/confirmReach") //确认到达目的地
    @Transactional
    public R confirmReach(@RequestParam("applyId") Integer applyId) {
        PzcActivityGroupApplyVo pzcActivityGroupApplyVo = wxUtils.checkApplyConfirm(applyId.longValue());
        Long userId = LoginHelper.getUserId();
        PzcUser my = pzcUserMapper.selectById(userId);
        Long groupId = pzcActivityGroupApplyVo.getGroupId();
        Integer applyStatus = pzcActivityGroupApplyVo.getApplyStatus();
        if (applyStatus != 2 && applyStatus != 11 && applyStatus != 12) {
            return R.fail("该订单目前状态为【" + wxUtils.applyStatus(applyStatus) + "】不可确认");
        }

        //获取发起方的保证金
        PzcActivityGroupVo pzcActivityGroupVo = iPzcActivityGroupService.queryById(groupId);
        if (pzcActivityGroupVo == null) {
            return R.fail("组队不存在");
        }
        PzcActivity pzcActivity = pzcActivityMapper.selectById(pzcActivityGroupVo.getActivityId());
        if (pzcActivityGroupApplyVo.getUserId().equals(userId)) { //我是申请方
            if (applyStatus == 11) //发起方确认了
            {

                BigDecimal money = pzcActivityGroupVo.getMoney();
                money = money.subtract(new BigDecimal("10"));
                //将保证金还给发起方
                PzcUser pzcUser = pzcUserMapper.selectById(pzcActivityGroupVo.getUserId());
                pzcUser.setMoney(pzcUser.getMoney().add(money));
                pzcUserMapper.updateById(pzcUser);
                //========================================================================
                //获取申请方的保证金
                PzcUser applyUser = pzcUserMapper.selectById(pzcActivityGroupApplyVo.getUserId());
                BigDecimal applyMoney = pzcActivityGroupApplyVo.getMoney();
                applyMoney = applyMoney.subtract(new BigDecimal("10"));
                //将保证金还给申请方
                applyUser.setMoney(applyUser.getMoney().add(applyMoney));
                pzcUserMapper.updateById(applyUser);


                BigDecimal finalMoney = money;
                BigDecimal finalApplyMoney = applyMoney;
                newSingleThreadExecutor.execute(() -> {
                    wxUtils.insertUserHistory(pzcActivityGroupVo.getUserId(), pzcActivityGroupVo.getActivityId(), 2L, "组队完成退还保证金 【" + finalMoney + "】 派币", finalMoney);
                    wxUtils.insertUserHistory(pzcActivityGroupApplyVo.getUserId(), pzcActivityGroupVo.getActivityId(), 2L, "组队完成退还保证金 【" + finalApplyMoney + "】 派币", finalMoney);

                    wxUtils.insertUserHistory(pzcActivityGroupVo.getUserId(), pzcActivityGroupVo.getActivityId(), 1L, JsonUtils.toJsonString(pzcActivity), null);
                    wxUtils.insertUserHistory(pzcActivityGroupApplyVo.getUserId(), pzcActivityGroupVo.getActivityId(), 1L, JsonUtils.toJsonString(pzcActivity), null);


                    PzcActivityGroup pzcActivityGroup = pzcActivityGroupMapper.selectById(pzcActivityGroupApplyVo.getGroupId());
                    // 更新 组队状态为已结束
                    pzcActivityGroup.setStatus(1); //已结束
                    pzcActivityGroupMapper.updateById(pzcActivityGroup);


                    //更新双方积分
                    PzcUser pzcUser1 = pzcUserMapper.selectById(pzcActivityGroup.getUserId());
                    PzcUser pzcUser2 = pzcUserMapper.selectById(pzcActivityGroupApplyVo.getUserId());
                    pzcUser1.setIntegration(pzcUser1.getIntegration() + 3);
                    pzcUser1.setIntegrationNow(pzcUser1.getIntegrationNow() + 3);
                    pzcUser2.setIntegration(pzcUser2.getIntegration() + 3);
                    pzcUser2.setIntegrationNow(pzcUser2.getIntegrationNow() + 3);
                    wxUtils.updateUserMsg(pzcUser1);
                    wxUtils.updateUserMsg(pzcUser2);
                });


                return R.ok(iPzcActivityGroupApplyService.updateStatus(applyId.longValue(), 3)); //双方都已确认
            }
            if (applyStatus == 12) {
                return R.fail("您已经确认过了 不可重复操作");
            }
            //申请方确认了 给发起方推送微信消息
            PzcUser pzcUser = pzcUserMapper.selectById(pzcActivityGroupVo.getUserId());
            Map<String, Map> dataMap = new HashMap<>();
            dataMap.put("thing4", MapBuilder.create().put("value", my.getNickname()).build());
            dataMap.put("thing5", MapBuilder.create().put("value", "对方已到达，您需在活动时间内到达").build());
            dataMap.put("time6", MapBuilder.create().put("value", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")).build());
            log.info("发起方确认到达目的地，给申请方推送微信消息：{}", JsonUtils.toJsonString(dataMap));

            newSingleThreadExecutor.execute(() -> {
                wxUtils.sendArriveMsg(pzcUser.getOpenid(), dataMap);
            });

            return R.ok(iPzcActivityGroupApplyService.updateStatus(applyId.longValue(), 12));//申请方确认
        }
        //判断当前 用户是否为组队发起人 如果不是 直接报错

        if (!pzcActivityGroupVo.getUserId().equals(userId)) {
            return R.fail("你不是组队发起人");
        }
        //看看申请方是否确认了
        if (applyStatus == 12) //我是发起方
        {
            //获取发起方的保证金
            BigDecimal money = pzcActivityGroupVo.getMoney();
            money = money.subtract(new BigDecimal("10"));
            //将保证金还给发起方
            PzcUser pzcUser = pzcUserMapper.selectById(pzcActivityGroupVo.getUserId());
            pzcUser.setMoney(pzcUser.getMoney().add(money));
            pzcUserMapper.updateById(pzcUser);
            //========================================================================
            //获取申请方的保证金
            PzcUser applyUser = pzcUserMapper.selectById(pzcActivityGroupApplyVo.getUserId());
            BigDecimal applyMoney = pzcActivityGroupApplyVo.getMoney();
            applyMoney = applyMoney.subtract(new BigDecimal("10"));
            //将保证金还给申请方
            applyUser.setMoney(applyUser.getMoney().add(applyMoney));
            pzcUserMapper.updateById(applyUser);


            BigDecimal finalMoney = money;
            BigDecimal finalApplyMoney = applyMoney;
            newSingleThreadExecutor.execute(() -> {

                wxUtils.insertUserHistory(pzcActivityGroupVo.getUserId(), pzcActivityGroupVo.getActivityId(), 2L, "组队完成退还保证金 " + finalMoney + " 派币", finalMoney);
                wxUtils.insertUserHistory(pzcActivityGroupApplyVo.getUserId(), pzcActivityGroupVo.getActivityId(), 2L, "组队完成退还保证金 " + finalApplyMoney + " 派币", finalMoney);

                wxUtils.insertUserHistory(pzcActivityGroupVo.getUserId(), pzcActivityGroupVo.getActivityId(), 1L, JsonUtils.toJsonString(pzcActivity), null);
                wxUtils.insertUserHistory(pzcActivityGroupApplyVo.getUserId(), pzcActivityGroupVo.getActivityId(), 1L, JsonUtils.toJsonString(pzcActivity), null);


                PzcActivityGroup pzcActivityGroup = pzcActivityGroupMapper.selectById(pzcActivityGroupApplyVo.getGroupId());
                // 更新 组队状态为已结束
                pzcActivityGroup.setStatus(1); //已结束
                pzcActivityGroupMapper.updateById(pzcActivityGroup);

                //更新双方积分
                PzcUser pzcUser1 = pzcUserMapper.selectById(pzcActivityGroup.getUserId());
                PzcUser pzcUser2 = pzcUserMapper.selectById(pzcActivityGroupApplyVo.getUserId());
                pzcUser1.setIntegration(pzcUser1.getIntegration() + 3);
                pzcUser1.setIntegrationNow(pzcUser1.getIntegrationNow() + 3);
                pzcUser2.setIntegration(pzcUser2.getIntegration() + 3);
                pzcUser2.setIntegrationNow(pzcUser2.getIntegrationNow() + 3);
                wxUtils.updateUserMsg(pzcUser1);
                wxUtils.updateUserMsg(pzcUser2);
            });
            return R.ok(iPzcActivityGroupApplyService.updateStatus(applyId.longValue(), 3)); //双方都已确认
        }
        if (applyStatus == 11) {
            return R.fail("您已经确认过了 不可重复操作");
        }
        //发起方确认了 给申请方推送微信消息
        PzcUser pzcUser = pzcUserMapper.selectById(pzcActivityGroupApplyVo.getUserId());
        Map<String, Map<Object, Object>> dataMap = new HashMap<>();
        dataMap.put("thing4", MapBuilder.create().put("value", my.getNickname()).build());
        dataMap.put("thing5", MapBuilder.create().put("value", "对方已到达，您需在活动时间内到达").build()); //对方已到达约定的见面地点，您需在【"+pzcActivityGroupVo.getActivityTime()+"】前到达目的地，否则，按照违约处理。或您也可以与对方沟通，申请无限制确认到达，以保证组队的正常。
        dataMap.put("time6", MapBuilder.create().put("value", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")).build());
        log.info("申请方确认到达目的地，给发起方推送微信消息：{}", JsonUtils.toJsonString(dataMap));
        newSingleThreadExecutor.execute(() -> {
            wxUtils.sendArriveMsg(pzcUser.getOpenid(), dataMap);
        });

        return R.ok(iPzcActivityGroupApplyService.updateStatus(applyId.longValue(), 11));//发起方确认
    }


    @GetMapping("/applyRole")
    public R applyRole(@RequestParam("applyId") Integer applyId) {
        Long userId = LoginHelper.getUserId();
        PzcActivityGroupApplyVo pzcActivityGroupApplyVo = iPzcActivityGroupApplyService.queryById(applyId.longValue());
        if (pzcActivityGroupApplyVo.getUserId().equals(userId)) {
            return R.ok(0); //我是申请方
        } else {
            return R.ok(1); //我是发起方
        }
    }


    @PostMapping("/confirm") //确认申请 (这里判断 保证金 是否足够缴纳 保证 新人卡 bug)
    @Transactional
    public R confirm(@RequestParam("applyId") Integer applyId) {
        PzcActivityGroupApplyVo pzcActivityGroupApplyVo = wxUtils.checkApplyConfirm(applyId.longValue());
        Long userId = LoginHelper.getUserId();
        Long groupId = pzcActivityGroupApplyVo.getGroupId();
        Integer applyStatus = pzcActivityGroupApplyVo.getApplyStatus();

        if (applyStatus == 2) {
            return R.fail("该订单进行中，不可确认");
        }
        PzcActivityGroupVo group = iPzcActivityGroupService.queryById(groupId);
        //获取双方保证金
        BigDecimal applyMoney = pzcActivityGroupApplyVo.getMoney();
        BigDecimal startMoney = group.getMoney();
        //获取双方
        PzcUser applyUser = pzcUserMapper.selectById(pzcActivityGroupApplyVo.getUserId());
        PzcUser startUser = pzcUserMapper.selectById(group.getUserId());

        //如果可以确认 判断 是那一方确认的
        if (pzcActivityGroupApplyVo.getUserId().equals(userId)) {// 自己是申请方 申请方还得等发起方最后确认时间地点
            if (applyStatus == 10) {
                return R.fail("您已经确认过了 不可重复操作");
            }

            //如果是自己确认的 则修改状态为 已确认
            if (applyStatus == 9) //发起方确认了
            {
                log.info("申请方的余额与保证金 {}----{}", applyUser.getMoney(), applyMoney);
                // 如果有一方保证金不足以缴纳 则报错
                if (applyUser.getMoney().compareTo(applyMoney) < 0) {
                    return R.fail("您的保证金不足以缴纳");
                }


                //双方都已确认 开始 扣除保证金
                //获取申请方保证金
                applyUser.setMoney(applyUser.getMoney().subtract(applyMoney));
                pzcUserMapper.updateById(applyUser);
                //获取发起方保证金
                startUser.setMoney(startUser.getMoney().subtract(startMoney));
                pzcUserMapper.updateById(startUser);

                //存入历史记录
                wxUtils.insertUserHistory(pzcActivityGroupApplyVo.getUserId(), pzcActivityGroupApplyVo.getActivityId(), 3L, "组队开始扣除保证金 【" + applyMoney + "】 派币", applyMoney.negate());
                wxUtils.insertUserHistory(group.getUserId(), pzcActivityGroupApplyVo.getActivityId(), 3L, "组队开始扣除保证金 【" + startMoney + "】 派币", startMoney.negate());

                return R.ok(iPzcActivityGroupApplyService.updateStatus(applyId.longValue(), 2)); //双方都已确认
            } else {
                return R.fail("发起方还未确认最后时间地点,请继续保持沟通哦");
            }

        } else { //自己是发起方
            //判断当前 用户是否为组队发起人 如果不是 直接报错‘
            PzcActivityGroupVo pzcActivityGroupVo = iPzcActivityGroupService.queryById(groupId);
            if (pzcActivityGroupVo == null) {
                return R.fail("组队不存在");
            }
            if (!pzcActivityGroupVo.getUserId().equals(userId)) {
                return R.fail("你不是组队发起人");
            }
            if (applyStatus == 9) {
                return R.fail("您已经确认过了 不可重复操作");
            }

            //看看申请方是否确认了
            if (applyStatus == 10) //申请方确认了
            {

                log.info("发起方的余额与保证金 {}----{}", startUser.getMoney(), startMoney);
                if (startUser.getMoney().compareTo(startMoney) < 0) {
                    return R.fail("您的的保证金不足以缴纳");
                }

                //双方都已确认 开始 扣除保证金
                //获取申请方保证金
                applyUser.setMoney(applyUser.getMoney().subtract(applyMoney));
                pzcUserMapper.updateById(applyUser);
                //获取发起方保证金
                startUser.setMoney(startUser.getMoney().subtract(startMoney));
                pzcUserMapper.updateById(startUser);

                newSingleThreadExecutor.execute(() -> {
                    //存入历史记录
                    wxUtils.insertUserHistory(pzcActivityGroupApplyVo.getUserId(), pzcActivityGroupApplyVo.getActivityId(), 3L, "组队开始扣除保证金 【" + applyMoney + "】 派币", applyMoney.negate());
                    wxUtils.insertUserHistory(group.getUserId(), pzcActivityGroupApplyVo.getActivityId(), 3L, "组队开始扣除保证金 【" + startMoney + "】 派币", startMoney.negate());

                });

                return R.ok(iPzcActivityGroupApplyService.updateStatus(applyId.longValue(), 2)); //双方都已确认
            }
            return R.ok(iPzcActivityGroupApplyService.updateStatus(applyId.longValue(), 9));//发起方确认
        }
    }


    /**
     * 同意用户申请 进入下一阶段
     * 同意用户申请时 先判断对方是否  处于组队进程
     * <p>
     * <p>
     * -1 已取消 0 位于申请列表中 1 申请通过待确认时
     * 2 确认通过进行中 3 组队结束  9发起方已确认
     * 10申请方已确认 11 发起方已打卡 12 申请方已打卡
     * 13 申请方已评价 14 发起方已评价 15 双方已评价
     *
     * @return
     */
    @PostMapping("/apply")
    public R apply(@RequestParam("applyId") Long applyId) {
        //首先查询这个组队是否是我发起的
        PzcActivityGroupApplyVo pzcActivityGroupApplyVo = iPzcActivityGroupApplyService.queryById(applyId);
        if (pzcActivityGroupApplyVo == null) {
            return R.fail("申请不存在");
        }
        Long userId = LoginHelper.getUserId();
        PzcUser my = pzcUserMapper.selectById(userId);

        Long groupId = pzcActivityGroupApplyVo.getGroupId();
        PzcActivityGroupVo pzcActivityGroupVo = iPzcActivityGroupService.queryById(groupId);
        if (pzcActivityGroupVo == null) {
            return R.fail("组队不存在");
        }
        if (!pzcActivityGroupVo.getUserId().equals(userId)) {
            return R.fail("你不是组队发起人");
        }

        //判断对方是否 处于 组队进程 如果是 则不可同意
        Long applyUserId = pzcActivityGroupApplyVo.getUserId();
        //获取活动Id
        Long activityId = pzcActivityGroupVo.getActivityId();
        //获取活动组队列表 除了自己这个队伍外
        List<PzcActivityGroup> groups = pzcActivityGroupMapper.selectList(new QueryWrapper<PzcActivityGroup>().eq("activity_id", activityId));
        List<Long> groupIds = groups.stream().filter(s -> !s.getGroupId().equals(groupId)).map(PzcActivityGroup::getGroupId).collect(Collectors.toList());
        if (groupIds.size() != 0) {
            //然后获取当前对方申请了几个队伍 判断每个队伍的进程 如果有 进程处于 组队状态中 则不可以同意
            List<PzcActivityGroupApply> applies = pzcActivityGroupApplyMapper.selectList(new QueryWrapper<PzcActivityGroupApply>().in("group_id", groupIds).eq("user_id", applyUserId));
            applies.forEach(
                a -> {
                    if (a.getApplyStatus() != 3 && a.getApplyStatus() != 13 && a.getApplyStatus() != 14 && a.getApplyStatus() != 15 && a.getApplyStatus() != -1) {
                        log.info("对方当前进程为 {} 详细信息为 {}", a.getApplyStatus(), JsonUtils.toJsonString(a));
                        throw new RuntimeException("该用户已经处于组队进程中 等待对方结束活动再试哦");
                    }
                }
            );
        }

        //判断一下 我是否在当前活动下 有未完成的组队 如果有就不能 同意其他的申请
        List<PzcActivityGroupApply> applies = pzcActivityGroupApplyMapper.selectList(new QueryWrapper<PzcActivityGroupApply>().eq("group_id", groupId).
            eq("activity_id", activityId).notIn("apply_status",-1,0,3,13,14,15));

        log.info("当前申请人的申请列表为：{}",JSONUtil.toJsonPrettyStr(applies));
        if(applies.size()>1)
        {
            return R.fail("您当前有未完成的组队，无法同意其他申请");
        }

        //修改状态为 已同意
        Integer integer = iPzcActivityGroupApplyService.updateStatus(applyId, 1);
        if (integer == null || integer != 1) {
            return R.fail("修改状态失败");
        }

        //给对方发消息 已经同意了对方的申请 请尽快确认
        PzcUser otherUser = pzcUserMapper.selectById(applyUserId);
        wxUtils.insertPzcOfficialMsg(my.getUserId(), otherUser.getUserId(),"来自" + my.getNickname() + "与您的组队信息：","您的组队申请已经被对方同意，请尽快确认~",groupId,activityId);

        try {
            // 创建一个任务逻辑，接受参数并在任务执行时使用
            ScheduledExecutorUtils.RunnableWithParams task = (params) -> {
                System.out.println("Task executed at: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                if (params.length > 0) {
                    System.out.println("Parameter received: " + params[0]);
                }
                // 在这里添加你的任务逻辑
                PzcActivityGroupApply pzcActivityGroupApply = pzcActivityGroupApplyMapper.selectById((Serializable) params[1]);
                log.info("定时任务执行中，当前申请信息为：{}", JsonUtils.toJsonString(pzcActivityGroupApply));

                if (pzcActivityGroupApply.getApplyStatus() == 1) {
                    //如果对方还未确认 则修改状态为 已取消
                    iPzcActivityGroupApplyService.updateStatus((Long) params[1], -1);
                    //发起方扣除10派币或者免责取消的一次机会
                    PzcUser pzcUser = pzcUserMapper.selectById(pzcActivityGroupVo.getUserId());
                    if (pzcUser.getExemptCancel() == 0) {
                        pzcUser.setMoney(pzcUser.getMoney().subtract(new BigDecimal("10")));
                        pzcUserMapper.updateById(pzcUser);
                        wxUtils.insertUserHistory(pzcActivityGroupVo.getUserId(), pzcActivityGroupVo.getActivityId(), 3L, "组队取消扣除保证金 【10】 派币", new BigDecimal("10").negate());
                    } else {
                        pzcUser.setExemptCancel(pzcUser.getExemptCancel() - 1);
                        pzcUserMapper.updateById(pzcUser);
                    }
                    //修改组队状态为已完成
                    PzcActivityGroup pzcActivityGroup = pzcActivityGroupMapper.selectById(pzcActivityGroupVo.getGroupId());
                    pzcActivityGroup.setStatus(1); //已结束
                    pzcActivityGroupMapper.updateById(pzcActivityGroup);


                    //给对方发消息 对方未确认 申请已取消
                    PzcOfficial pzcOfficial1 = new PzcOfficial();
                    pzcOfficial1.setIsRead(0L);
                    PzcUser otherUser1 = pzcUserMapper.selectById(pzcActivityGroupApply.getUserId());
                    pzcOfficial1.setToUserId(otherUser1.getUserId());
                    pzcOfficial1.setTitle("派之城官方提醒您：");
                    pzcOfficial1.setContent("您好，由于对方未确认地址，您的组队已取消。平台已对对方进行相应惩罚。");
                    pzcOfficial1.setGroupId(groupId);
                    pzcOfficial1.setActivityId(activityId);
                    pzcOfficialMapper.insert(pzcOfficial1);

                    PzcOfficial pzcOfficial2 = new PzcOfficial();
                    pzcOfficial2.setIsRead(0L);
                    pzcOfficial2.setToUserId(pzcActivityGroupVo.getUserId());
                    pzcOfficial2.setTitle("派之城官方提醒您：");
                    pzcOfficial2.setContent("您好，由于您未进行确认地址，您的组队已取消，平台扣除您一次免责取消机会或10派币。(具体根据您免责机会次数确定）");
                    pzcOfficial2.setGroupId(groupId);
                    pzcOfficial2.setActivityId(activityId);
                    pzcOfficialMapper.insert(pzcOfficial2);
                }
                if (pzcActivityGroupApply.getApplyStatus() == 9) { //发起方确认了地址 而申请方未确认
                    //如果对方还未确认 则修改状态为 已取消
                    iPzcActivityGroupApplyService.updateStatus((Long) params[1], -1);
                    //申请方扣除10派币或者免责取消的一次机会
                    PzcUser pzcUser = pzcUserMapper.selectById(pzcActivityGroupApply.getUserId());
                    if (pzcUser.getExemptCancel() == 0) {
                        pzcUser.setMoney(pzcUser.getMoney().subtract(new BigDecimal("10")));
                        pzcUserMapper.updateById(pzcUser);
                        wxUtils.insertUserHistory(pzcActivityGroupApply.getUserId(), pzcActivityGroupApply.getActivityId(), 3L, "组队取消扣除保证金 【10】 派币", new BigDecimal("10").negate());
                    } else {
                        pzcUser.setExemptCancel(pzcUser.getExemptCancel() - 1);
                        pzcUserMapper.updateById(pzcUser);
                    }
                    //修改组队状态为已完成
                    PzcActivityGroup pzcActivityGroup = pzcActivityGroupMapper.selectById(pzcActivityGroupVo.getGroupId());
                    pzcActivityGroup.setStatus(1); //已结束
                    pzcActivityGroupMapper.updateById(pzcActivityGroup);

                    //给对方发消息 对方未确认 申请已取消
                    PzcOfficial pzcOfficial1 = new PzcOfficial();
                    pzcOfficial1.setIsRead(0L);
                    PzcUser otherUser1 = pzcUserMapper.selectById(pzcActivityGroupApply.getUserId()); //申请方
                    pzcOfficial1.setToUserId(otherUser1.getUserId());
                    pzcOfficial1.setTitle("派之城官方提醒您：");
                    pzcOfficial1.setContent("您好，由于您未进行确认地址，您的组队已取消，平台扣除您一次免责取消机会或10派币。(具体根据您免责机会次数确定）");
                    pzcOfficial1.setGroupId(groupId);
                    pzcOfficial1.setActivityId(activityId);
                    pzcOfficialMapper.insert(pzcOfficial1);

                    PzcOfficial pzcOfficial2 = new PzcOfficial();
                    pzcOfficial2.setIsRead(0L);
                    pzcOfficial2.setToUserId(pzcActivityGroupVo.getUserId()); //发起方
                    pzcOfficial2.setTitle("派之城官方提醒您：");
                    pzcOfficial2.setContent("您好，由于对方未确认地址，您的组队已取消。平台已对对方进行相应惩罚。");
                    pzcOfficial2.setGroupId(groupId);
                    pzcOfficial2.setActivityId(activityId);
                    pzcOfficialMapper.insert(pzcOfficial2);
                }
                if (pzcActivityGroupApply.getApplyStatus() == 10) { //申请方确认了地址
                    //如果对方还未确认 则修改状态为 已取消
                    iPzcActivityGroupApplyService.updateStatus((Long) params[1], -1);
                    //修改组队状态为已完成
                    PzcActivityGroup pzcActivityGroup = pzcActivityGroupMapper.selectById(pzcActivityGroupVo.getGroupId());
                    pzcActivityGroup.setStatus(1); //已结束
                    pzcActivityGroupMapper.updateById(pzcActivityGroup);

                    //双方均扣除10派币
                    PzcUser startUser = pzcUserMapper.selectById(pzcActivityGroupVo.getUserId());

                    startUser.setMoney(startUser.getMoney().subtract(new BigDecimal("10")));
                    pzcUserMapper.updateById(startUser);
                    wxUtils.insertUserHistory(pzcActivityGroupVo.getUserId(), pzcActivityGroupVo.getActivityId(), 3L, "组队取消扣除保证金 【10】 派币", new BigDecimal("10").negate());

                    PzcUser applyUser = pzcUserMapper.selectById(pzcActivityGroupApply.getUserId());
                    applyUser.setMoney(applyUser.getMoney().subtract(new BigDecimal("10")));
                    pzcUserMapper.updateById(applyUser);
                    wxUtils.insertUserHistory(pzcActivityGroupApply.getUserId(), pzcActivityGroupApply.getActivityId(), 3L, "组队取消扣除保证金 【10】 派币", new BigDecimal("10").negate());
                }


                if (pzcActivityGroupApply.getApplyStatus() == 11 || pzcActivityGroupApply.getApplyStatus() == 12) {
                    //如果对方还未确认 则修改状态为 已取消
                    iPzcActivityGroupApplyService.updateStatus((Long) params[1], -1);
                    //修改组队状态为已完成
                    PzcActivityGroup pzcActivityGroup = pzcActivityGroupMapper.selectById(pzcActivityGroupVo.getGroupId());
                    pzcActivityGroup.setStatus(1); //已结束
                    pzcActivityGroupMapper.updateById(pzcActivityGroup);
                    if (pzcActivityGroupApply.getApplyStatus() == 11) //发起方打卡了 申请方 全责
                    {
                        BigDecimal money = pzcActivityGroupApply.getMoney();

                        PzcOfficial pzcOfficial1 = new PzcOfficial();
                        pzcOfficial1.setIsRead(0L);
                        pzcOfficial1.setFromUserId(null);
                        pzcOfficial1.setToUserId(pzcActivityGroupApply.getUserId());
                        pzcOfficial1.setTitle("派之城提醒您：");
                        pzcOfficial1.setContent("您好，由于您在活动结束前未进行到约定地方打卡签到，平台已对您进行违约处理，已扣除您全部保障金。");
                        pzcOfficial1.setGroupId(groupId);
                        pzcOfficial1.setActivityId(activityId);
                        pzcOfficialMapper.insert(pzcOfficial1);

                        PzcUser pzcUser1 = pzcUserMapper.selectById(pzcActivityGroupVo.getUserId());
                        pzcUser1.setMoney(pzcUser1.getMoney().add(pzcActivityGroup.getMoney()).add(money.subtract(new BigDecimal("20"))));
                        pzcUserMapper.updateById(pzcUser1); //发起方获得申请方 扣除20派币 后的保证金

                        PzcOfficial pzcOfficial2 = new PzcOfficial();
                        pzcOfficial2.setIsRead(0L);
                        pzcOfficial2.setFromUserId(null);
                        pzcOfficial2.setToUserId(pzcActivityGroupVo.getUserId());
                        pzcOfficial2.setTitle("派之城提醒您：");
                        pzcOfficial2.setContent("您好，您在【" + pzcActivityGroupVo.getTitle() + "】组队活动中，由于对方未在活动结束前进行签到打卡，平台已对对方进行违约处理。对方的违约金【" + money.subtract(new BigDecimal("20")) + "】派币已纳入您的账户。您可以再次同意或申请其他用户。");
                        pzcOfficial2.setGroupId(groupId);
                        pzcOfficial2.setActivityId(activityId);
                        pzcOfficialMapper.insert(pzcOfficial2);

                        wxUtils.insertUserHistory(pzcActivityGroupApply.getUserId(), pzcActivityGroupApply.getActivityId(), 3L, "活动时间到 自动取消组队 扣除保证金 【" + money + "】 派币", money.negate());


                    }
                    if (pzcActivityGroupApply.getApplyStatus() == 12) //申请方打卡了 发起方 全责
                    {

                        BigDecimal money = pzcActivityGroupVo.getMoney();


                        PzcOfficial pzcOfficial1 = new PzcOfficial();
                        pzcOfficial1.setIsRead(0L);
                        pzcOfficial1.setFromUserId(null);
                        pzcOfficial1.setToUserId(pzcActivityGroupVo.getUserId());
                        pzcOfficial1.setTitle("派之城提醒您：");
                        pzcOfficial1.setContent("您好，由于您在活动结束前未进行到约定地方打卡签到，平台已对您进行违约处理，已扣除您全部保障金。");
                        pzcOfficial1.setGroupId(groupId);
                        pzcOfficial1.setActivityId(activityId);
                        pzcOfficialMapper.insert(pzcOfficial1);


                        PzcUser pzcUser1 = pzcUserMapper.selectById(pzcActivityGroupApply.getUserId());
                        pzcUser1.setMoney(pzcUser1.getMoney().add(pzcActivityGroupApply.getMoney()).add(money.subtract(new BigDecimal("20"))));
                        pzcUserMapper.updateById(pzcUser1); //申请方获得发起方 扣除20派币 后的保证金

                        PzcOfficial pzcOfficial2 = new PzcOfficial();
                        pzcOfficial2.setIsRead(0L);
                        pzcOfficial2.setFromUserId(null);
                        pzcOfficial2.setToUserId(pzcActivityGroupApply.getUserId());
                        pzcOfficial2.setTitle("派之城提醒您：");
                        pzcOfficial2.setContent("您好，您在【" + pzcActivityGroupVo.getTitle() + "】组队活动中，由于对方未在活动结束前进行签到打卡，平台已对对方进行违约处理。对方的违约金【" + money.subtract(new BigDecimal("20")) + "】派币已纳入您的账户。您可以再次同意或申请其他用户。");
                        pzcOfficial2.setGroupId(groupId);
                        pzcOfficial2.setActivityId(activityId);
                        pzcOfficialMapper.insert(pzcOfficial2);


                        wxUtils.insertUserHistory(pzcActivityGroupVo.getUserId(), pzcActivityGroupVo.getActivityId(), 3L, "活动时间到 自动取消组队 扣除保证金 【" + money + "】 派币", money.negate());

                    }

                }
            };
            ScheduledExecutorUtils.scheduleTask(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(pzcActivityGroupVo.getActivityTime()), task, groupId, applyId);
        } catch (ParseException e) {
            log.info("创建定时任务失败,活动信息为 {}", JsonUtils.toJsonString(pzcActivityGroupVo));
        }

        return R.ok();
    }


    /**
     * 查询活动组队列表
     */
    @GetMapping("/list")
    public TableDataInfo<PzcActivityGroupVo> list(PzcActivityGroupBo bo, PageQuery pageQuery) {
        log.info("组队大厅 查询条件是： {}", JsonUtils.toJsonString(bo));
        return iPzcActivityGroupService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出活动组队列表
     */
    @SaCheckPermission("system:activityGroup:export")
    @Log(title = "活动组队", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PzcActivityGroupBo bo, HttpServletResponse response) {
        List<PzcActivityGroupVo> list = iPzcActivityGroupService.queryList(bo);
        ExcelUtil.exportExcel(list, "活动组队", PzcActivityGroupVo.class, response);
    }

    /**
     * 获取活动组队详细信息
     *
     * @param groupId 主键
     */
    @GetMapping("/{groupId}")
    public R<PzcActivityGroupVo> getInfo(@NotNull(message = "主键不能为空")
                                         @PathVariable Long groupId) {
        return R.ok(iPzcActivityGroupService.queryById(groupId));
    }


    @Autowired
    private PzcRegionMapper pzcRegionMapper;

    /**
     * 发起活动组队
     */
    @Log(title = "活动组队", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PzcActivityGroupBo bo) {
        Long userId = LoginHelper.getUserId();
        bo.setUserId(userId);

        wxUtils.checkMgc(bo.getTitle());//校验敏感词
        //校验活动是否存在
        if (!iPzcActivityGroupService.checkActivity(bo.getActivityId()) && bo.getActivityId() != 0) { //如果不是城市活动 并且活动id不为0 则校验活动是否存在
            return R.fail("活动不存在");
        }
        PzcRegion pzcRegion = pzcRegionMapper.selectById(bo.getRegion());
        //校验城市是否存在 只对派对生效
        if (pzcRegion == null && pzcActivityMapper.selectById(bo.getActivityId()).getClassify() != 0) {
            log.info("传入的城市Id is {} ", bo.getRegion());
            return R.fail("当前城市不存在");
        }
        if(bo.getMoney().compareTo(new BigDecimal(99))<0)
        {
            return R.fail("派币保证金至少为99");
        }

        //检验自己是否已经在此城市里发起过组队
        if (bo.getActivityId() == 0) {
            //如果是城市活动 则校验是否已经发起过组队
            List<PzcActivityGroup> groups = pzcActivityGroupMapper.selectList(new QueryWrapper<PzcActivityGroup>().
                eq("user_id", userId).eq("activity_id", 0).eq("region", bo.getRegion()).eq("status", 0));
            if (groups.size() != 0) {
                log.info("用户id为：{} 在城市id为：{} 发起过组队 {}", userId, bo.getRegion(), JsonUtils.toJsonString(groups));
                return R.fail("您已经在此城市发起过组队了");
            }
            bo.setActivityName("【" + pzcRegion.getName() + "】");
        } else {
            //如果是活动 则校验是否已经发起过组队 //并且状态不为已结束
            List<PzcActivityGroup> groups = pzcActivityGroupMapper.selectList(new QueryWrapper<PzcActivityGroup>().eq("user_id", userId).eq("activity_id", bo.getActivityId()).eq("status", 0));
            if (groups.size() != 0) {
                log.info("用户id为：{} 在活动id为：{} 发起过组队 {}", userId, bo.getActivityId(), JsonUtils.toJsonString(groups));
                return R.fail("您已经在此活动发起过组队了");
            }
            bo.setActivityName(pzcActivityMapper.selectById(bo.getActivityId()).getTitle());
        }

        // 校验保证金
        PzcUser pzcUser = pzcUserMapper.selectById(userId);
        if (pzcUser.getMoney().compareTo(bo.getMoney()) < 0 || bo.getMoney().compareTo(new BigDecimal(99)) < 0) {
            return R.fail("保证金不足 至少拥有99个派币");
        }


        return toAjax(iPzcActivityGroupService.insertByBo(bo));
    }

    /**
     * 修改活动组队
     */
    @Log(title = "活动组队", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PzcActivityGroupBo bo) {
        bo.setUserId(LoginHelper.getUserId());
        if(bo.getMoney().compareTo(new BigDecimal(99))<0)
        {
            return R.fail("保证金至少为99派币");
        }
        //判断是否在组队进程中 判断组队状态
        //获取我的申请列表
        List<PzcActivityGroupApply> applies = pzcActivityGroupApplyMapper.selectList(new QueryWrapper<PzcActivityGroupApply>().eq("group_id", bo.getGroupId()));
        //判断是否有正在进行中的订单
        applies.forEach(
            a -> {
                if (a.getApplyStatus() != -1 && a.getApplyStatus() != 0 && a.getApplyStatus() != 1) {
                    throw new RuntimeException("当前活动处于" + wxUtils.applyStatus(a.getApplyStatus()) + " 无法修改");
                }
            }
        );

        return toAjax(iPzcActivityGroupService.updateByBo(bo));
    }

    /**
     * 删除活动组队
     *
     * @param groupIds 主键串
     */
    @SaCheckPermission("system:activityGroup:remove")
    @Log(title = "活动组队", businessType = BusinessType.DELETE)
    @DeleteMapping("/{groupIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] groupIds) {
        return toAjax(iPzcActivityGroupService.deleteWithValidByIds(Arrays.asList(groupIds), true));
    }
}
