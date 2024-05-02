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
import top.flya.system.domain.bo.PzcViewPagerBo;
import top.flya.system.domain.vo.PzcViewPagerVo;
import top.flya.system.domain.PzcViewPager;
import top.flya.system.mapper.PzcViewPagerMapper;
import top.flya.system.service.IPzcViewPagerService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 轮播图Service业务层处理
 *
 * @author ruoyi
 * @date 2023-05-23
 */
@RequiredArgsConstructor
@Service
public class PzcViewPagerServiceImpl implements IPzcViewPagerService {

    private final PzcViewPagerMapper baseMapper;

    private final BatchUtils batchUtils;

    /**
     * 查询轮播图
     */
    @Override
    public PzcViewPagerVo queryById(Integer viewPagerId){
        return baseMapper.selectVoById(viewPagerId);
    }

    /**
     * 查询轮播图列表
     */
    @Override
    public TableDataInfo<PzcViewPagerVo> queryPageList(PzcViewPagerBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PzcViewPager> lqw = buildQueryWrapper(bo);
        Page<PzcViewPagerVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        result.setRecords(batchUtils.transformToPzcViewPagerVo(result.getRecords()));
        return TableDataInfo.build(result);
    }

    /**
     * 查询轮播图列表
     */
    @Override
    public List<PzcViewPagerVo> queryList(PzcViewPagerBo bo) {
        LambdaQueryWrapper<PzcViewPager> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PzcViewPager> buildQueryWrapper(PzcViewPagerBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PzcViewPager> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), PzcViewPager::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getImageUrl()), PzcViewPager::getImageUrl, bo.getImageUrl());
        lqw.eq(StringUtils.isNotBlank(bo.getLinkUrl()), PzcViewPager::getLinkUrl, bo.getLinkUrl());
        lqw.eq(bo.getState() != null, PzcViewPager::getState, bo.getState());
        lqw.eq(bo.getActivityId() != null, PzcViewPager::getActivityId, bo.getActivityId());
        return lqw;
    }

    /**
     * 新增轮播图
     */
    @Override
    public Boolean insertByBo(PzcViewPagerBo bo) {
        PzcViewPager add = BeanUtil.toBean(bo, PzcViewPager.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setViewPagerId(add.getViewPagerId());
        }
        return flag;
    }

    /**
     * 修改轮播图
     */
    @Override
    public Boolean updateByBo(PzcViewPagerBo bo) {
        PzcViewPager update = BeanUtil.toBean(bo, PzcViewPager.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PzcViewPager entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除轮播图
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
