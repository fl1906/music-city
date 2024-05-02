package top.flya.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
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
import top.flya.system.domain.PzcActivityGroup;
import top.flya.system.domain.PzcActivityGroupApply;
import top.flya.system.domain.PzcUser;
import top.flya.system.domain.bo.PzcActivityGroupApplyBo;
import top.flya.system.domain.bo.WxzApplyBo;
import top.flya.system.domain.vo.PzcActivityGroupApplyVo;
import top.flya.system.mapper.PzcActivityGroupApplyMapper;
import top.flya.system.mapper.PzcActivityGroupMapper;
import top.flya.system.mapper.PzcUserMapper;
import top.flya.system.service.IPzcActivityGroupApplyService;
import top.flya.system.utils.ActivityUtils;
import top.flya.system.utils.WxUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 活动组队申请列表
 *
 * @author ruoyi
 * @date 2023-07-10
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/activityGroupApply")
@Slf4j
public class PzcActivityGroupApplyController extends BaseController {

    private final IPzcActivityGroupApplyService iPzcActivityGroupApplyService;

    private final ActivityUtils activityUtils;

    private final PzcActivityGroupApplyMapper pzcActivityGroupApplyMapper;

    private final PzcUserMapper pzcUserMapper;

    private final PzcActivityGroupMapper pzcActivityGroupMapper;


    private final StringRedisTemplate stringRedisTemplate;

    private final WxUtils wxUtils;

    private final ExecutorService newSingleThreadExecutor = new ThreadPoolExecutor(10, 20, 200L,
        TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(100));


    @PostMapping("/wxzApply")
    @Transactional
    public R wxzApply(@RequestParam("applyId") Integer applyId, @RequestParam("wxz") Integer wxz) {  //wxz 0 未选择 1 选择
        log.info("applyId:{},wxz:{}", applyId, wxz);
        if (wxz != 0 && wxz != 1) {
            return R.fail("参数错误");
        }
        PzcActivityGroupApply pzcActivityGroupApply = pzcActivityGroupApplyMapper.selectById(applyId);
        if (pzcActivityGroupApply == null) {
            return R.fail("申请不存在");
        }

        if (pzcActivityGroupApply.getApplyStatus() != 2 && pzcActivityGroupApply.getApplyStatus() != 9 && pzcActivityGroupApply.getApplyStatus() != 10 && pzcActivityGroupApply.getApplyStatus() != 11 && pzcActivityGroupApply.getApplyStatus() != 12) {
            return R.fail("当前状态为【" + wxUtils.applyStatus(pzcActivityGroupApply.getApplyStatus()) + "】，不能进行此操作");
        }
        //这里取消redis缓存
        Long userId = LoginHelper.getUserId();
        String result = stringRedisTemplate.opsForValue().get("officialMessage:" + userId);
        log.info("result:{}", result);
        if (result != null) {
            WxzApplyBo wxzApplyBo = JsonUtils.parseObject(result, WxzApplyBo.class);
            if (wxzApplyBo == null) {
                return R.fail("转换JSON异常");
            }
            if (!wxzApplyBo.getApplyId().equals(applyId)) {
                return R.fail("申请方请求不存在");
            }
            stringRedisTemplate.delete("officialMessage:" + userId);
        } else {
            return R.fail("申请方请求不存在");
        }

        if(wxz==0)
        {
            return R.ok();
        }

        pzcActivityGroupApply.setWxz(wxz);
        pzcActivityGroupApply.setApplyStatus(3); //直接是组队结束状态
        pzcActivityGroupApplyMapper.updateById(pzcActivityGroupApply);

        //退还双方保证金

        newSingleThreadExecutor.execute(() -> {
            // 更新 组队状态为已结束
            PzcActivityGroup pzcActivityGroup = pzcActivityGroupMapper.selectById(pzcActivityGroupApply.getGroupId());

            PzcUser applyUser = pzcUserMapper.selectById(pzcActivityGroupApply.getUserId());
            PzcUser groupUser = pzcUserMapper.selectById(pzcActivityGroup.getUserId());
            applyUser.setMoney(applyUser.getMoney().add(pzcActivityGroupApply.getMoney().subtract(new BigDecimal("10"))));
            groupUser.setMoney(groupUser.getMoney().add(pzcActivityGroup.getMoney().subtract(new BigDecimal("10")))); //退还发起人的保证金
            //无限制确认到达加3分
            applyUser.setIntegration(applyUser.getIntegration() + 3);
            applyUser.setIntegrationNow(applyUser.getIntegrationNow() + 3);
            groupUser.setIntegration(groupUser.getIntegration() + 3);
            groupUser.setIntegrationNow(groupUser.getIntegrationNow() + 3);
            pzcUserMapper.updateById(applyUser);
            pzcUserMapper.updateById(groupUser);
            //更新余额变动
            wxUtils.insertUserHistory(applyUser.getUserId(), pzcActivityGroup.getActivityId(),2L, "组队结束，退还保证金 并收取活动费用 10派币", pzcActivityGroupApply.getMoney().subtract(new BigDecimal("10")));
            wxUtils.insertUserHistory(groupUser.getUserId(), pzcActivityGroup.getActivityId(),2L, "组队结束，退还保证金 并收取活动费用 10派币", pzcActivityGroupApply.getMoney().subtract(new BigDecimal("10")));



            pzcActivityGroup.setStatus(1); //已结束
            pzcActivityGroupMapper.updateById(pzcActivityGroup);
        });


        return R.ok();
    }

    @GetMapping("/myHistory") //我的历史活动
    public R<List<PzcActivityGroupApply>> myHistory() {
        //我申请 并处于进行中的活动
        Long userId = LoginHelper.getUserId();
        List<PzcActivityGroupApply> step1 = pzcActivityGroupApplyMapper.selectList(
            new QueryWrapper<PzcActivityGroupApply>()
                .eq("user_id", userId).in("apply_status", 3,13,14,15));
        step1.forEach(
            p -> {
                PzcActivityGroup pzcActivityGroup = pzcActivityGroupMapper.selectById(p.getGroupId());
                PzcUser my = pzcUserMapper.selectById(p.getUserId());
                PzcUser other = pzcUserMapper.selectById(pzcActivityGroup.getUserId());
                p.setOtherMoney(pzcActivityGroup.getMoney());
                p.setOtherName(other.getNickname());
                p.setOtherAvatar(other.getAvatar());
                p.setOtherUserId(String.valueOf(other.getUserId()));
                p.setOtherLevel(Math.toIntExact(other.getUserLevel()));
                p.setMyAvatar(my.getAvatar());
                p.setTitle(pzcActivityGroup.getTitle());
            }
        );
        List<PzcActivityGroupApply> result = new java.util.ArrayList<>();

        //申请我的 并处于进行中的活动
        //1 找出所有我创建的组
        List<PzcActivityGroup> pzcActivityGroups = pzcActivityGroupMapper.selectList(new QueryWrapper<PzcActivityGroup>().eq("user_id", userId));
        List<Long> groupIds = pzcActivityGroups.stream().map(PzcActivityGroup::getGroupId).collect(java.util.stream.Collectors.toList());
        if (groupIds.size() != 0) {
            List<PzcActivityGroupApply> step2 = pzcActivityGroupApplyMapper.selectList(new QueryWrapper<>(new PzcActivityGroupApply()).in("group_id", groupIds).in("apply_status", 3,14,13,15));
            step2.forEach(
                p -> {
                    PzcActivityGroup pzcActivityGroup = pzcActivityGroupMapper.selectById(p.getGroupId());
                    PzcUser other = pzcUserMapper.selectById(p.getUserId());
                    PzcUser my = pzcUserMapper.selectById(pzcActivityGroup.getUserId());
                    p.setOtherMoney(pzcActivityGroup.getMoney());
                    p.setOtherName(other.getNickname());
                    p.setOtherAvatar(other.getAvatar());
                    p.setOtherUserId(String.valueOf(other.getUserId()));
                    p.setOtherLevel(Math.toIntExact(other.getUserLevel()));
                    p.setMyAvatar(my.getAvatar());
                    p.setTitle(pzcActivityGroup.getTitle());
                }
            );
            result.addAll(step2);
        }
        result.addAll(step1);

        //按照更新时间倒序排列
        List<PzcActivityGroupApply> collect = result.stream().sorted((o1, o2) -> o2.getUpdateTime().compareTo(o1.getUpdateTime())).collect(Collectors.toList());

        return R.ok(collect);
    }

    /**
     * -1 已取消
     * 0 位于申请列表中
     * 1 申请通过待确认时
     * 2 确认通过进行中
     * 3 组队结束
     * 9发起方已确认
     * 10申请方已确认
     * 11 发起方已打卡
     * 12 申请方已打卡
     * 13 发起方已评价
     * 14 申请方已评价
     * 15 双方已评价
     *
     * @return
     */
    @GetMapping("/myTrips") //我的行程
    public R<List<PzcActivityGroupApply>> myTrips() {
        //我申请 并处于进行中的活动
        Long userId = LoginHelper.getUserId();
        List<PzcActivityGroupApply> step1 = pzcActivityGroupApplyMapper.selectList(
            new QueryWrapper<PzcActivityGroupApply>()
                .eq("user_id", userId).in("apply_status", 1, 2, 3, 9, 10, 11, 12, 14)); //, 14 我评价过了就从行程中移除
        step1.forEach(
            p -> {
                PzcActivityGroup pzcActivityGroup = pzcActivityGroupMapper.selectById(p.getGroupId());
                PzcUser my = pzcUserMapper.selectById(p.getUserId());
                PzcUser other = pzcUserMapper.selectById(pzcActivityGroup.getUserId());
                p.setOtherMoney(pzcActivityGroup.getMoney());
                p.setOtherName(other.getNickname());
                p.setOtherAvatar(other.getAvatar());
                p.setOtherUserId(String.valueOf(other.getUserId()));
                p.setOtherLevel(Math.toIntExact(other.getUserLevel()));
                p.setMyAvatar(my.getAvatar());
                p.setTitle(pzcActivityGroup.getTitle());
            }
        );
        List<PzcActivityGroupApply> result = new java.util.ArrayList<>();

        //申请我的 并处于进行中的活动
        //1 找出所有我创建的组
        List<PzcActivityGroup> pzcActivityGroups = pzcActivityGroupMapper.selectList(new QueryWrapper<PzcActivityGroup>().eq("user_id", userId));
        List<Long> groupIds = pzcActivityGroups.stream().map(PzcActivityGroup::getGroupId).collect(java.util.stream.Collectors.toList());
        if (groupIds.size() != 0) {
            List<PzcActivityGroupApply> step2 = pzcActivityGroupApplyMapper.selectList(new QueryWrapper<>(new PzcActivityGroupApply()).in("group_id", groupIds).in("apply_status", 1, 2, 3, 9, 10, 11, 12,  13));//13, 评价了就移除
            step2.forEach(
                p -> {
                    PzcActivityGroup pzcActivityGroup = pzcActivityGroupMapper.selectById(p.getGroupId());
                    PzcUser other = pzcUserMapper.selectById(p.getUserId());
                    PzcUser my = pzcUserMapper.selectById(pzcActivityGroup.getUserId());
                    p.setOtherMoney(pzcActivityGroup.getMoney());
                    p.setOtherName(other.getNickname());
                    p.setOtherAvatar(other.getAvatar());
                    p.setOtherUserId(String.valueOf(other.getUserId()));
                    p.setOtherLevel(Math.toIntExact(other.getUserLevel()));
                    p.setMyAvatar(my.getAvatar());
                    p.setTitle(pzcActivityGroup.getTitle());
                }
            );
            result.addAll(step2);
        }
        result.addAll(step1);

        //按照更新时间倒序排列
        List<PzcActivityGroupApply> collect = result.stream().sorted((o1, o2) -> o2.getUpdateTime().compareTo(o1.getUpdateTime())).collect(Collectors.toList());

        return R.ok(collect);
    }

    /**
     * 查询活动组队申请列表列表
     */
    @GetMapping("/list")
    public TableDataInfo<PzcActivityGroupApplyVo> list(PzcActivityGroupApplyBo bo, PageQuery pageQuery) {
        bo.setUserId(LoginHelper.getUserId());
        return iPzcActivityGroupApplyService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出活动组队申请列表列表
     */
    @Log(title = "活动组队申请列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PzcActivityGroupApplyBo bo, HttpServletResponse response) {
        List<PzcActivityGroupApplyVo> list = iPzcActivityGroupApplyService.queryList(bo);
        ExcelUtil.exportExcel(list, "活动组队申请列表", PzcActivityGroupApplyVo.class, response);
    }

    /**
     * 获取活动组队申请列表详细信息
     *
     * @param applyId 主键
     */
    @GetMapping("/{applyId}")
    public R<PzcActivityGroupApplyVo> getInfo(@NotNull(message = "主键不能为空")
                                              @PathVariable Long applyId) {
        return R.ok(iPzcActivityGroupApplyService.queryById(applyId));
    }

    /**
     * 申请参与组队
     * <p>
     * 1 做校验
     * 1.1 活动是否存在 1.2 活动是否已经开始 1.3 活动是否已经结束 1.4 活动是否已经满员
     * 2 组是否还存在
     * <p>
     * <p>
     * ====================
     * 用户申请活动的时候 判断 是否足够缴纳保证金
     */
    @Log(title = "活动组队申请列表", businessType = BusinessType.INSERT) //
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PzcActivityGroupApplyBo bo) {
        log.info("申请参与组队:{}", JsonUtils.toJsonString(bo));
        if(bo.getMoney().compareTo(new BigDecimal(100))<0)
        {
            return R.fail("申请失败，最低保障金为99派币~");
        }
        if (!activityUtils.allCheck(Math.toIntExact(bo.getActivityId()), bo.getGroupId())) {
            return R.fail("申请失败，活动不存在或者已经结束或者组不存在");
        }
        bo.setUserId(LoginHelper.getUserId());
        if (iPzcActivityGroupApplyService.queryByUserIdAndGroupId(bo.getUserId(), bo.getGroupId()) != null) {
            return R.fail("申请失败，您已经申请过了");
        }

        //======================================================
        PzcUser applyUser = pzcUserMapper.selectById(bo.getUserId()); //我有2个币  申请需要 两个币  我则需要 根据当前他的余额来判断
        log.info("申请参与组队 我目前的余额是： {} 申请需要的余额是：{}", applyUser.getMoney(), bo.getMoney());
        if (applyUser.getMoney().compareTo(bo.getMoney()) < 0 || applyUser.getMoney().compareTo(new BigDecimal(100)) < 0) //100块钱 也没有就需要充值了
        {
            return R.fail("申请失败，最低保障金为99派币~");
        }

        return toAjax(iPzcActivityGroupApplyService.insertByBo(bo));
    }

    /**
     * 修改活动组队申请列表
     */
    @Log(title = "活动组队申请列表", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PzcActivityGroupApplyBo bo) {
        bo.setUserId(LoginHelper.getUserId());
        if (!activityUtils.allCheck(Math.toIntExact(bo.getActivityId()), bo.getGroupId())) {
            return R.fail("修改失败，活动不存在或者已经结束或者组不存在");
        }
        if (!iPzcActivityGroupApplyService.queryByUserIdAndActivityId(bo.getUserId(), bo.getActivityId())) {
            return R.fail("修改失败，您还没有申请过该活动组");
        }
        return toAjax(iPzcActivityGroupApplyService.updateByBo(bo));
    }

    /**
     * 取消活动组队申请列表
     *
     * @param applyIds 主键串
     */
    @Log(title = "活动组队申请列表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{applyIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] applyIds) {
        return toAjax(iPzcActivityGroupApplyService.deleteWithValidByIds(Arrays.asList(applyIds), true));
    }


}
