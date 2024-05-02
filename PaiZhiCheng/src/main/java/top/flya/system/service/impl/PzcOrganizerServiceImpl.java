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
import top.flya.system.domain.bo.PzcOrganizerBo;
import top.flya.system.domain.vo.PzcOrganizerVo;
import top.flya.system.domain.PzcOrganizer;
import top.flya.system.mapper.PzcOrganizerMapper;
import top.flya.system.service.IPzcOrganizerService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 活动主办方Service业务层处理
 *
 * @author ruoyi
 * @date 2023-06-02
 */
@RequiredArgsConstructor
@Service
public class PzcOrganizerServiceImpl implements IPzcOrganizerService {

    private final PzcOrganizerMapper baseMapper;
    private final BatchUtils batchUtils;

    /**
     * 查询活动主办方
     */
    @Override
    public PzcOrganizerVo queryById(Long organizerId){
        return baseMapper.selectVoById(organizerId);
    }

    /**
     * 查询活动主办方列表
     */
    @Override
    public TableDataInfo<PzcOrganizerVo> queryPageList(PzcOrganizerBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PzcOrganizer> lqw = buildQueryWrapper(bo);
        Page<PzcOrganizerVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        result.setRecords(batchUtils.transformToPzcOrganizerVo(result.getRecords()));
        return TableDataInfo.build(result);
    }

    /**
     * 查询活动主办方列表
     */
    @Override
    public List<PzcOrganizerVo> queryList(PzcOrganizerBo bo) {
        LambdaQueryWrapper<PzcOrganizer> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PzcOrganizer> buildQueryWrapper(PzcOrganizerBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PzcOrganizer> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getPhone()), PzcOrganizer::getPhone, bo.getPhone());
        lqw.like(StringUtils.isNotBlank(bo.getName()), PzcOrganizer::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getLogo()), PzcOrganizer::getLogo, bo.getLogo());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), PzcOrganizer::getContent, bo.getContent());
        return lqw;
    }

    /**
     * 新增活动主办方
     */
    @Override
    public Boolean insertByBo(PzcOrganizerBo bo) {
        PzcOrganizer add = BeanUtil.toBean(bo, PzcOrganizer.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setOrganizerId(add.getOrganizerId());
        }
        return flag;
    }

    /**
     * 修改活动主办方
     */
    @Override
    public Boolean updateByBo(PzcOrganizerBo bo) {
        PzcOrganizer update = BeanUtil.toBean(bo, PzcOrganizer.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PzcOrganizer entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除活动主办方
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
