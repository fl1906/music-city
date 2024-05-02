package top.flya.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.flya.common.core.controller.BaseController;
import top.flya.common.core.domain.PageQuery;
import top.flya.common.core.domain.R;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.helper.LoginHelper;
import top.flya.common.utils.JsonUtils;
import top.flya.system.domain.PzcUser;
import top.flya.system.domain.PzcUserTalk;
import top.flya.system.domain.bo.PzcUserTalkBo;
import top.flya.system.domain.vo.PzcUserTalkVo;
import top.flya.system.mapper.PzcUserMapper;
import top.flya.system.mapper.PzcUserPhotoMapper;
import top.flya.system.mapper.PzcUserTalkMapper;
import top.flya.system.service.IPzcUserTalkService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static top.flya.system.config.ClientCache.concurrentHashMap;

/**
 * 用户聊天
 *
 * @author ruoyi
 * @date 2023-07-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/userTalk")
@Slf4j
public class PzcUserTalkController extends BaseController {

    private final IPzcUserTalkService iPzcUserTalkService;

    private final PzcUserTalkMapper pzcUserTalkMapper;

    private final PzcUserMapper pzcUserMapper;

    private final PzcUserPhotoMapper pzcUserPhotoMapper;


    @PostMapping("/deleteByUserId") //删除聊天记录
    public R deleteByUserId(@RequestParam("userId") String userId, @RequestParam("groupId") Long groupId) {
        Long my = LoginHelper.getUserId();
        pzcUserTalkMapper.delete(new QueryWrapper<PzcUserTalk>().
            eq("group_id", groupId).
            and(
                i -> i.eq("user_id", my).
                    eq("from_user_id", userId).or().
                    eq("user_id", my).eq("to_user_id", userId)));
        return R.ok();
    }

    /**
     * 用户在线状态
     *
     * @param userId
     * @return
     */
    @GetMapping("/live")
    public R liveStatus(@RequestParam("userId") String userId) {
        Boolean liveStatus = false;
        if (concurrentHashMap.get(userId) != null) {
            liveStatus = true;
        }

        PzcUser pzcUser = pzcUserMapper.selectById(userId);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("liveStatus", liveStatus.toString());
        result.put("userId", userId);
        result.put("nickName", pzcUser.getNickname());
        result.put("address", pzcUser.getAddress());
        result.put("sex", String.valueOf(pzcUser.getSex()));
        result.put("info", pzcUser.getIntro());
        result.put("avatar", pzcUser.getAvatar());
        result.put("age", pzcUser.getAge());
        result.put("level", pzcUser.getUserLevel());
        // 查询用户相册
        result.put("photo", pzcUserPhotoMapper.selectList(new QueryWrapper<top.flya.system.domain.PzcUserPhoto>().eq("user_id", userId)).stream().map(top.flya.system.domain.PzcUserPhoto::getUrl).collect(Collectors.toList()));
        return R.ok(result);
    }

    /**
     * 一键已读
     *
     * @param userId
     * @return
     */
    @PostMapping("/read")
    public R<Boolean> read(@RequestParam("userId") String userId) {
        List<PzcUserTalk> pzcUserTalks = pzcUserTalkMapper.selectList(new QueryWrapper<PzcUserTalk>().eq("from_user_id", userId).eq("to_user_id", LoginHelper.getUserId())).stream()
            .filter(pzcUserTalk -> pzcUserTalk.getMessageStatus() == 0)
            .peek(pzcUserTalk -> pzcUserTalk.setMessageStatus(1L))
            .collect(Collectors.toList());
        log.info("pzcUserTalks:{}", JsonUtils.toJsonString(pzcUserTalks));
        if (pzcUserTalks.size() == 0) {
            return R.ok(true);
        }
        boolean b = pzcUserTalkMapper.updateBatchById(pzcUserTalks);
        return R.ok(b);
    }


    /**
     * 我的聊天列表
     * <p>
     * 1. 按照 最后聊天的时间倒序排列
     * 2. 展示 最新一条聊天记录 以及未读消息条数
     */
    @GetMapping("/myList")
    public TableDataInfo<PzcUserTalkVo> myList(PzcUserTalkBo bo, PageQuery pageQuery) {
        return iPzcUserTalkService.queryMyPageList(bo, pageQuery);
    }


    /**
     * 查询用户聊天列表
     */
    @GetMapping("/list")
    public TableDataInfo<PzcUserTalkVo> list(PzcUserTalkBo bo, PageQuery pageQuery) {
        bo.setFromUserId(LoginHelper.getUserId());
        bo.setUserId(LoginHelper.getUserId());
        pageQuery.setIsAsc("desc");
        pageQuery.setOrderByColumn("create_time");
        return iPzcUserTalkService.queryPageList(bo, pageQuery);
    }

}
