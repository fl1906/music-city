package top.flya.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.flya.common.core.domain.PageQuery;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.helper.LoginHelper;
import top.flya.system.domain.PzcUser;
import top.flya.system.domain.PzcUserTalk;
import top.flya.system.domain.bo.PzcUserTalkBo;
import top.flya.system.domain.vo.PzcUserTalkVo;
import top.flya.system.mapper.PzcUserMapper;
import top.flya.system.mapper.PzcUserTalkMapper;
import top.flya.system.service.IPzcUserTalkService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户聊天Service业务层处理
 *
 * @author ruoyi
 * @date 2023-07-16
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class PzcUserTalkServiceImpl implements IPzcUserTalkService {

    private final PzcUserTalkMapper baseMapper;

    private final PzcUserMapper pzcUserMapper;

    /**
     * 查询用户聊天
     */
    @Override
    public PzcUserTalkVo queryById(Long talkId) {
        return baseMapper.selectVoById(talkId);
    }

    /**
     * 查询用户聊天列表
     */
    @Override
    public TableDataInfo<PzcUserTalkVo> queryPageList(PzcUserTalkBo bo, PageQuery pageQuery) {
        Page<PzcUserTalkVo> result = baseMapper.selectVoPageV2(pageQuery.build(), bo);
        return TableDataInfo.build(result);
    }

    /**
     * 查询我 与朋友的聊天列表
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    @Override
    public TableDataInfo<PzcUserTalkVo> queryMyPageList(PzcUserTalkBo bo, PageQuery pageQuery) {
        List<Long> userIds = baseMapper.selectMyTalkUserIds(LoginHelper.getUserId());
        List<Long> userIds2 = baseMapper.selectMyTalkUserIdsV2(LoginHelper.getUserId());
        List<PzcUserTalkVo> result =new ArrayList<>();
        userIds.addAll(userIds2);

        userIds=userIds.stream().distinct().collect(Collectors.toList());
        log.info("聊天列表 对方 userIds:{}", userIds);
        userIds.forEach(
            userId -> {
                PzcUserTalkVo item = baseMapper.selectVoPageV1(userId, LoginHelper.getUserId());
//                if(item==null)
//                {
//                    return; //TODO
//                }
                Long fromUserId = item.getFromUserId().equals(LoginHelper.getUserId()) ? item.getToUserId() : item.getFromUserId();
                item.setNotReadCount(baseMapper.selectNotReadCount(fromUserId, LoginHelper.getUserId(),LoginHelper.getUserId())); //
                PzcUser pzcUser = pzcUserMapper.selectById(Objects.equals(item.getToUserId(), LoginHelper.getUserId()) ? item.getFromUserId() : item.getToUserId());
                item.setUsername(pzcUser.getNickname());
                item.setAvatar(pzcUser.getAvatar());
                item.setToUserId(item.getToUserId().equals(LoginHelper.getUserId()) ? item.getFromUserId() : item.getToUserId());
                result.add(item);
                }
        );
        List<PzcUserTalkVo> collect = result.stream().sorted(Comparator.comparing(PzcUserTalkVo::getCreateTime).reversed()).collect(Collectors.toList());

        return TableDataInfo.build(collect);
    }


    /**
     * 查询用户聊天列表
     */
    @Override
    public List<PzcUserTalkVo> queryList(PzcUserTalkBo bo) {
        LambdaQueryWrapper<PzcUserTalk> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PzcUserTalk> buildQueryWrapper(PzcUserTalkBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PzcUserTalk> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getFromUserId() != null, PzcUserTalk::getFromUserId, bo.getFromUserId());
        lqw.eq(bo.getToUserId() != null, PzcUserTalk::getToUserId, bo.getToUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getMessage()), PzcUserTalk::getMessage, bo.getMessage());
        lqw.eq(bo.getMessageStatus() != null, PzcUserTalk::getMessageStatus, bo.getMessageStatus());
        lqw.eq(bo.getMessageType() != null, PzcUserTalk::getMessageType, bo.getMessageType());
        lqw.eq(bo.getCreateTime() != null, PzcUserTalk::getCreateTime, bo.getCreateTime());
        lqw.eq(bo.getUpdateTime() != null, PzcUserTalk::getUpdateTime, bo.getUpdateTime());
        return lqw;
    }

    /**
     * 新增用户聊天
     */
    @Override
    public Boolean insertByBo(PzcUserTalkBo bo) {
        PzcUserTalk add = BeanUtil.toBean(bo, PzcUserTalk.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setTalkId(add.getTalkId());
        }
        return flag;
    }

    /**
     * 修改用户聊天
     */
    @Override
    public Boolean updateByBo(PzcUserTalkBo bo) {
        PzcUserTalk update = BeanUtil.toBean(bo, PzcUserTalk.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PzcUserTalk entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除用户聊天
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
