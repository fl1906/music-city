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
import top.flya.system.common.BatchUtils;
import top.flya.system.domain.bo.PzcTagBo;
import top.flya.system.domain.vo.PzcTagVo;
import top.flya.system.domain.PzcTag;
import top.flya.system.mapper.PzcTagMapper;
import top.flya.system.service.IPzcTagService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 活动标签Service业务层处理
 *
 * @author ruoyi
 * @date 2023-06-02
 */
@RequiredArgsConstructor
@Service
public class PzcTagServiceImpl implements IPzcTagService {

    private final PzcTagMapper baseMapper;
    private final BatchUtils batchUtils;

    /**
     * 查询活动标签
     */
    @Override
    public PzcTagVo queryById(Long tagId){
        return baseMapper.selectVoById(tagId);
    }

    /**
     * 查询活动标签列表
     */
    @Override
    public TableDataInfo<PzcTagVo> queryPageList(PzcTagBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PzcTag> lqw = buildQueryWrapper(bo);
        Page<PzcTagVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        result.setRecords(batchUtils.transformToPzcTagVo(result.getRecords()));
        return TableDataInfo.build(result);
    }

    /**
     * 查询活动标签列表
     */
    @Override
    public List<PzcTagVo> queryList(PzcTagBo bo) {
        LambdaQueryWrapper<PzcTag> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PzcTag> buildQueryWrapper(PzcTagBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PzcTag> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), PzcTag::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getImageUrl()), PzcTag::getImageUrl, bo.getImageUrl());
        lqw.eq(bo.getCreateTime() != null, PzcTag::getCreateTime, bo.getCreateTime());
        lqw.eq(bo.getUpdateTime() != null, PzcTag::getUpdateTime, bo.getUpdateTime());
        return lqw;
    }

    /**
     * 新增活动标签
     */
    @Override
    public Boolean insertByBo(PzcTagBo bo) {
        PzcTag add = BeanUtil.toBean(bo, PzcTag.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setTagId(add.getTagId());
        }
        return flag;
    }

    /**
     * 修改活动标签
     */
    @Override
    public Boolean updateByBo(PzcTagBo bo) {
        PzcTag update = BeanUtil.toBean(bo, PzcTag.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PzcTag entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除活动标签
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
