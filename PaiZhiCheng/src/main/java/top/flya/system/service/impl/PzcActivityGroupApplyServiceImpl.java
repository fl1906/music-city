package top.flya.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.flya.system.domain.bo.PzcActivityGroupApplyBo;
import top.flya.system.domain.vo.PzcActivityGroupApplyVo;
import top.flya.system.domain.PzcActivityGroupApply;
import top.flya.system.mapper.PzcActivityGroupApplyMapper;
import top.flya.system.service.IPzcActivityGroupApplyService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 活动组队申请列Service业务层处理
 *
 * @author ruoyi
 * @date 2023-07-10
 */
@RequiredArgsConstructor
@Service
public class PzcActivityGroupApplyServiceImpl implements IPzcActivityGroupApplyService {

    private final PzcActivityGroupApplyMapper baseMapper;


    /**
     * 查询活动组队申请列
     */
    @Override
    public PzcActivityGroupApplyVo queryById(Long applyId){
        return baseMapper.selectVoById(applyId);
    }

    /**
     * 查询活动组队申请列列表
     */
    @Override
    public TableDataInfo<PzcActivityGroupApplyVo> queryPageList(PzcActivityGroupApplyBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PzcActivityGroupApply> lqw = buildQueryWrapper(bo);
        Page<PzcActivityGroupApplyVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询活动组队申请列列表
     */
    @Override
    public List<PzcActivityGroupApplyVo> queryList(PzcActivityGroupApplyBo bo) {
        LambdaQueryWrapper<PzcActivityGroupApply> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PzcActivityGroupApply> buildQueryWrapper(PzcActivityGroupApplyBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PzcActivityGroupApply> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getApplyId() != null, PzcActivityGroupApply::getApplyId, bo.getApplyId());
        lqw.eq(bo.getUserId() != null, PzcActivityGroupApply::getUserId, bo.getUserId());
        lqw.eq(bo.getActivityId() != null, PzcActivityGroupApply::getActivityId, bo.getActivityId());
        lqw.eq(bo.getGroupId() != null, PzcActivityGroupApply::getGroupId, bo.getGroupId());
        lqw.eq(bo.getGroupType() != null, PzcActivityGroupApply::getGroupType, bo.getGroupType());
        lqw.eq(bo.getMoney() != null, PzcActivityGroupApply::getMoney, bo.getMoney());
//        lqw.eq(bo.getApplyStatus() != null, PzcActivityGroupApply::getApplyStatus, bo.getApplyStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getMessage()), PzcActivityGroupApply::getMessage, bo.getMessage());
        lqw.eq(bo.getCreateTime() != null, PzcActivityGroupApply::getCreateTime, bo.getCreateTime());
        lqw.eq(bo.getUpdateTime() != null, PzcActivityGroupApply::getUpdateTime, bo.getUpdateTime());
        lqw.eq(bo.getWxz()!=null,PzcActivityGroupApply::getWxz,bo.getWxz());
        return lqw;
    }

    /**
     * 新增活动组队申请列
     */
    @Override
    public Boolean insertByBo(PzcActivityGroupApplyBo bo) {
        PzcActivityGroupApply add = BeanUtil.toBean(bo, PzcActivityGroupApply.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setApplyId(add.getApplyId());
        }
        return flag;
    }

    /**
     * 修改活动组队申请列
     */
    @Override
    public Boolean updateByBo(PzcActivityGroupApplyBo bo) {
        PzcActivityGroupApply update = BeanUtil.toBean(bo, PzcActivityGroupApply.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PzcActivityGroupApply entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除活动组队申请列
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public boolean queryByUserIdAndActivityId(Long userId, Long activityId) {
        LambdaQueryWrapper<PzcActivityGroupApply> lqw = Wrappers.lambdaQuery();
        lqw.eq(PzcActivityGroupApply::getUserId, userId);
        lqw.eq(PzcActivityGroupApply::getActivityId, activityId);
        List<PzcActivityGroupApply> list = baseMapper.selectList(lqw);
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }


    @Override
    public Integer updateStatus(Long applyId, int i) {
        PzcActivityGroupApply apply = baseMapper.selectById(applyId);
        if (apply != null) {
            apply.setApplyStatus(i);
            return baseMapper.updateById(apply);
        }
        return null;
    }

    @Override
    public List<PzcActivityGroupApplyVo> queryListByGroupIds(List<Long> groupIds) {
        LambdaQueryWrapper<PzcActivityGroupApply> lqw = Wrappers.lambdaQuery();
        lqw.in(PzcActivityGroupApply::getGroupId, groupIds);
        lqw.orderByDesc(PzcActivityGroupApply::getCreateTime);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public PzcActivityGroupApplyVo queryByUserIdAndGroupId(Long userId, Long groupId) {
        LambdaQueryWrapper<PzcActivityGroupApply> lqw = Wrappers.lambdaQuery();
        lqw.eq(PzcActivityGroupApply::getUserId, userId);
        lqw.eq(PzcActivityGroupApply::getGroupId, groupId);
        return baseMapper.selectVoOne(lqw);
    }
}
