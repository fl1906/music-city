package top.flya.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.flya.common.core.domain.PageQuery;
import top.flya.common.core.page.TableDataInfo;
import top.flya.system.domain.PzcRegion;
import top.flya.system.domain.bo.PzcRegionBo;
import top.flya.system.domain.vo.PzcRegionVo;
import top.flya.system.mapper.PzcRegionMapper;
import top.flya.system.service.IPzcRegionService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 地区Service业务层处理
 *
 * @author ruoyi
 * @date 2023-07-22
 */
@RequiredArgsConstructor
@Service
public class PzcRegionServiceImpl implements IPzcRegionService {

    private final PzcRegionMapper baseMapper;

    /**
     * 查询地区
     */
    @Override
    public PzcRegionVo queryById(Long regionId){
        return baseMapper.selectVoById(regionId);
    }

    /**
     * 查询地区列表
     */
    @Override
    public TableDataInfo<PzcRegionVo> queryPageList(PzcRegionBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PzcRegion> lqw = buildQueryWrapper(bo);
        Page<PzcRegionVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }


    /**
     *  不分页全查
     */

    /**
     * 查询地区列表
     */
    @Override
    public List<PzcRegionVo> queryList(PzcRegionBo bo) {
        LambdaQueryWrapper<PzcRegion> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PzcRegion> buildQueryWrapper(PzcRegionBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PzcRegion> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getBase()), PzcRegion::getBase, bo.getBase());
        lqw.like(StringUtils.isNotBlank(bo.getName()), PzcRegion::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getImgUrl()), PzcRegion::getImgUrl, bo.getImgUrl());
        lqw.eq(bo.getCreateTime() != null, PzcRegion::getCreateTime, bo.getCreateTime());
        lqw.eq(bo.getUpdateTime() != null, PzcRegion::getUpdateTime, bo.getUpdateTime());
        return lqw;
    }

    /**
     * 新增地区
     */
    @Override
    public Boolean insertByBo(PzcRegionBo bo) {
        PzcRegion add = BeanUtil.toBean(bo, PzcRegion.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setRegionId(add.getRegionId());
        }
        return flag;
    }

    /**
     * 修改地区
     */
    @Override
    public Boolean updateByBo(PzcRegionBo bo) {
        PzcRegion update = BeanUtil.toBean(bo, PzcRegion.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PzcRegion entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除地区
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
