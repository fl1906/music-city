package top.flya.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.flya.common.annotation.Log;
import top.flya.common.core.controller.BaseController;
import top.flya.common.core.domain.PageQuery;
import top.flya.common.core.domain.R;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.enums.BusinessType;
import top.flya.common.utils.JsonUtils;
import top.flya.common.utils.poi.ExcelUtil;
import top.flya.system.common.BatchUtils;
import top.flya.system.domain.PzcActivity;
import top.flya.system.domain.bo.PzcUserCollectBo;
import top.flya.system.domain.vo.PzcUserCollectVo;
import top.flya.system.mapper.PzcActivityMapper;
import top.flya.system.service.IPzcUserCollectService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * 用户收藏活动
 *
 * @author ruoyi
 * @date 2023-07-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/userCollect")
@Slf4j
public class PzcUserCollectController extends BaseController {

    private final IPzcUserCollectService iPzcUserCollectService;

    private final PzcActivityMapper pzcActivityMapper;


    private final StringRedisTemplate stringRedisTemplate;

    private final BatchUtils batchUtils;

    /**
     * 查询用户收藏活动列表
     */
    @GetMapping("/list")
    public R<List<PzcActivity>> list(PzcUserCollectBo bo, PageQuery pageQuery) {
        Set<String> members = stringRedisTemplate.opsForSet().members("collect:" + getUserId());
        if(members==null||members.isEmpty())
        {
            return R.ok();
        }
        List<String> collect = new ArrayList<>(members);
        List<PzcActivity> pzcActivities = pzcActivityMapper.selectActivityByActivityIds(collect, bo.getType());
        log.info("用户收藏活动列表 {}", JsonUtils.toJsonString(pzcActivities));
        pzcActivities.stream().forEach(
                pzcActivity -> {
                    pzcActivity.setCoverImage(pzcActivity.getCoverImage().contains("http")?pzcActivity.getCoverImage():
                        (batchUtils.getNewImageUrls(Collections.singletonList(pzcActivity.getCoverImage())).get(Long.parseLong((pzcActivity.getCoverImage())))));
                }
        );
        return R.ok(pzcActivities);
    }

    /**
     * 导出用户收藏活动列表
     */
    @SaCheckPermission("system:userCollect:export")
    @Log(title = "用户收藏活动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PzcUserCollectBo bo, HttpServletResponse response) {
        List<PzcUserCollectVo> list = iPzcUserCollectService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户收藏活动", PzcUserCollectVo.class, response);
    }

    /**
     * 获取用户收藏活动详细信息
     *
     * @param collectId 主键
     */
    @GetMapping("/{collectId}")
    public R<PzcUserCollectVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long collectId) {
        return R.ok(iPzcUserCollectService.queryById(collectId));
    }

    @GetMapping("/status")
    public R<Boolean> status(@RequestParam("activityId") Long activityId) {
        return R.ok(Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember("collect:" + getUserId(), activityId.toString())));
    }
    /**
     * 新增用户收藏活动 这里改为存入Redis 加快响应速度
     */
    @Log(title = "用户收藏/取消活动", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PzcUserCollectBo bo) {
        log.info("用户收藏/取消活动 {}", JsonUtils.toJsonString(bo));
        if(Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember("collect:" + getUserId(), bo.getActivityId().toString())))
        {
            //取消收藏活动
            stringRedisTemplate.opsForSet().remove("collect:" + getUserId(),bo.getActivityId().toString());
        }else {
            stringRedisTemplate.opsForSet().add("collect:" + getUserId(),bo.getActivityId().toString());
        }
        return R.ok("1");
    }

}
