package top.flya.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.flya.system.common.BatchUtils;
import top.flya.system.domain.bo.PzcArtistBo;
import top.flya.system.domain.vo.PzcArtistVo;
import top.flya.system.domain.PzcArtist;
import top.flya.system.mapper.PzcArtistMapper;
import top.flya.system.service.IPzcArtistService;

import java.util.List;
import java.util.Map;
import java.util.Collection;


/**
 * 艺人Service业务层处理
 *
 * @author flya
 * @date 2023-06-01
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class PzcArtistServiceImpl implements IPzcArtistService {

    private final PzcArtistMapper baseMapper;

    private final BatchUtils batchUtils;


    /**
     * 查询艺人
     */
    @Override
    public PzcArtistVo queryById(Long artistId){
        return baseMapper.selectVoById(artistId);
    }

    /**
     * 查询艺人列表
     */
    @Override
    public TableDataInfo<PzcArtistVo> queryPageList(PzcArtistBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PzcArtist> lqw = buildQueryWrapper(bo);
        Page<PzcArtistVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        result.setRecords(batchUtils.transformToPzcArtistVo(result.getRecords()));
        return TableDataInfo.build(result);
    }

    /**
     * 查询艺人列表
     */
    @Override
    public List<PzcArtistVo> queryList(PzcArtistBo bo) {
        LambdaQueryWrapper<PzcArtist> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PzcArtist> buildQueryWrapper(PzcArtistBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PzcArtist> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), PzcArtist::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getImageUrl()), PzcArtist::getImageUrl, bo.getImageUrl());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), PzcArtist::getDescription, bo.getDescription());
        return lqw;
    }

    /**
     * 新增艺人
     */
    @Override
    public Boolean insertByBo(PzcArtistBo bo) {
        PzcArtist add = BeanUtil.toBean(bo, PzcArtist.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setArtistId(add.getArtistId());
        }
        return flag;
    }

    /**
     * 修改艺人
     */
    @Override
    public Boolean updateByBo(PzcArtistBo bo) {
        PzcArtist update = BeanUtil.toBean(bo, PzcArtist.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PzcArtist entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除艺人
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
