package top.flya.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.flya.system.domain.bo.PzcActivityConnArtistBo;
import top.flya.system.domain.vo.PzcActivityConnArtistVo;
import top.flya.system.domain.PzcActivityConnArtist;
import top.flya.system.mapper.PzcActivityConnArtistMapper;
import top.flya.system.service.IPzcActivityConnArtistService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 活动关联艺人Service业务层处理
 *
 * @author ruoyi
 * @date 2023-06-02
 */
@RequiredArgsConstructor
@Service
public class PzcActivityConnArtistServiceImpl implements IPzcActivityConnArtistService {

    private final PzcActivityConnArtistMapper baseMapper;

    /**
     * 查询活动关联艺人
     */
    @Override
    public PzcActivityConnArtistVo queryById(Integer activityConnArtistId){
        return baseMapper.selectVoById(activityConnArtistId);
    }

    /**
     * 查询活动关联艺人列表
     */
    @Override
    public TableDataInfo<PzcActivityConnArtistVo> queryPageList(PzcActivityConnArtistBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PzcActivityConnArtist> lqw = buildQueryWrapper(bo);
        Page<PzcActivityConnArtistVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询活动关联艺人列表
     */
    @Override
    public List<PzcActivityConnArtistVo> queryList(PzcActivityConnArtistBo bo) {
        LambdaQueryWrapper<PzcActivityConnArtist> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PzcActivityConnArtist> buildQueryWrapper(PzcActivityConnArtistBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PzcActivityConnArtist> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getActivityId() != null, PzcActivityConnArtist::getActivityId, bo.getActivityId());
        lqw.eq(bo.getArtistId() != null, PzcActivityConnArtist::getArtistId, bo.getArtistId());
        lqw.eq(bo.getCreateTime() != null, PzcActivityConnArtist::getCreateTime, bo.getCreateTime());
        lqw.eq(bo.getUpdateTime() != null, PzcActivityConnArtist::getUpdateTime, bo.getUpdateTime());
        return lqw;
    }

    /**
     * 新增活动关联艺人
     */
    @Override
    public Boolean insertByBo(PzcActivityConnArtistBo bo) {
        PzcActivityConnArtist add = BeanUtil.toBean(bo, PzcActivityConnArtist.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setActivityConnArtistId(add.getActivityConnArtistId());
        }
        return flag;
    }

    /**
     * 修改活动关联艺人
     */
    @Override
    public Boolean updateByBo(PzcActivityConnArtistBo bo) {
        PzcActivityConnArtist update = BeanUtil.toBean(bo, PzcActivityConnArtist.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PzcActivityConnArtist entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除活动关联艺人
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
