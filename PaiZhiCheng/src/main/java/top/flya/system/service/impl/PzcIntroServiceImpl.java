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
import top.flya.system.common.BatchUtils;
import top.flya.system.domain.PzcIntro;
import top.flya.system.domain.bo.PzcIntroBo;
import top.flya.system.domain.vo.PzcIntroVo;
import top.flya.system.mapper.PzcIntroMapper;
import top.flya.system.service.IPzcIntroService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 活动介绍Service业务层处理
 *
 * @author ruoyi
 * @date 2023-08-04
 */
@RequiredArgsConstructor
@Service
public class PzcIntroServiceImpl implements IPzcIntroService {

    private final PzcIntroMapper baseMapper;

    private final BatchUtils batchUtils;
    /**
     * 查询活动介绍
     */
    @Override
    public PzcIntroVo queryById(Long introId){
        return baseMapper.selectVoById(introId);
    }

    /**
     * 查询活动介绍列表
     */
    @Override
    public TableDataInfo<PzcIntroVo> queryPageList(PzcIntroBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PzcIntro> lqw = buildQueryWrapper(bo);
        Page<PzcIntroVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        result.setRecords(batchUtils.transformToPzcIntroVo(result.getRecords()));
        return TableDataInfo.build(result);
    }

    /**
     * 查询活动介绍列表
     */
    @Override
    public List<PzcIntroVo> queryList(PzcIntroBo bo) {
        LambdaQueryWrapper<PzcIntro> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PzcIntro> buildQueryWrapper(PzcIntroBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PzcIntro> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), PzcIntro::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), PzcIntro::getContent, bo.getContent());
        lqw.eq(bo.getType() != null, PzcIntro::getType, bo.getType());
        lqw.eq(StringUtils.isNotBlank(bo.getImageFullUrl()), PzcIntro::getImageFullUrl, bo.getImageFullUrl());
        lqw.eq(bo.getCreateTime() != null, PzcIntro::getCreateTime, bo.getCreateTime());
        lqw.eq(bo.getUpdateTime() != null, PzcIntro::getUpdateTime, bo.getUpdateTime());
        return lqw;
    }

    /**
     * 新增活动介绍
     */
    @Override
    public Boolean insertByBo(PzcIntroBo bo) {
        PzcIntro add = BeanUtil.toBean(bo, PzcIntro.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setIntroId(add.getIntroId());
        }
        return flag;
    }

    /**
     * 修改活动介绍
     */
    @Override
    public Boolean updateByBo(PzcIntroBo bo) {
        PzcIntro update = BeanUtil.toBean(bo, PzcIntro.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PzcIntro entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除活动介绍
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
