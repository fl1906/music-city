package top.flya.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.flya.system.domain.bo.PzcActivityConnTagBo;
import top.flya.system.domain.vo.PzcActivityConnTagVo;
import top.flya.system.domain.PzcActivityConnTag;
import top.flya.system.mapper.PzcActivityConnTagMapper;
import top.flya.system.service.IPzcActivityConnTagService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 活动标签与活动关联Service业务层处理
 *
 * @author ruoyi
 * @date 2023-06-03
 */
@RequiredArgsConstructor
@Service
public class PzcActivityConnTagServiceImpl implements IPzcActivityConnTagService {

    private final PzcActivityConnTagMapper baseMapper;

    /**
     * 查询活动标签与活动关联
     */
    @Override
    public PzcActivityConnTagVo queryById(Integer activityConnTagId){
        return baseMapper.selectVoById(activityConnTagId);
    }

    /**
     * 查询活动标签与活动关联列表
     */
    @Override
    public TableDataInfo<PzcActivityConnTagVo> queryPageList(PzcActivityConnTagBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PzcActivityConnTag> lqw = buildQueryWrapper(bo);
        Page<PzcActivityConnTagVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询活动标签与活动关联列表
     */
    @Override
    public List<PzcActivityConnTagVo> queryList(PzcActivityConnTagBo bo) {
        LambdaQueryWrapper<PzcActivityConnTag> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PzcActivityConnTag> buildQueryWrapper(PzcActivityConnTagBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PzcActivityConnTag> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getActivityConnTagId() != null, PzcActivityConnTag::getActivityConnTagId, bo.getActivityConnTagId());
        lqw.eq(bo.getActivityId() != null, PzcActivityConnTag::getActivityId, bo.getActivityId());
        lqw.eq(bo.getTagId() != null, PzcActivityConnTag::getTagId, bo.getTagId());
        lqw.eq(bo.getCreateTime() != null, PzcActivityConnTag::getCreateTime, bo.getCreateTime());
        lqw.eq(bo.getUpdateTime() != null, PzcActivityConnTag::getUpdateTime, bo.getUpdateTime());
        return lqw;
    }

    /**
     * 新增活动标签与活动关联
     */
    @Override
    public Boolean insertByBo(PzcActivityConnTagBo bo) {
        PzcActivityConnTag add = BeanUtil.toBean(bo, PzcActivityConnTag.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setActivityConnTagId(add.getActivityConnTagId());
        }
        return flag;
    }

    /**
     * 修改活动标签与活动关联
     */
    @Override
    public Boolean updateByBo(PzcActivityConnTagBo bo) {
        PzcActivityConnTag update = BeanUtil.toBean(bo, PzcActivityConnTag.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PzcActivityConnTag entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除活动标签与活动关联
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
