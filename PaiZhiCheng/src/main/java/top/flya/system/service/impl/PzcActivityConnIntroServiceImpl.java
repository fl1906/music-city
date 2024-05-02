package top.flya.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.flya.system.domain.bo.PzcActivityConnIntroBo;
import top.flya.system.domain.vo.PzcActivityConnIntroVo;
import top.flya.system.domain.PzcActivityConnIntro;
import top.flya.system.mapper.PzcActivityConnIntroMapper;
import top.flya.system.service.IPzcActivityConnIntroService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 活动介绍与活动关联Service业务层处理
 *
 * @author ruoyi
 * @date 2023-06-02
 */
@RequiredArgsConstructor
@Service
public class PzcActivityConnIntroServiceImpl implements IPzcActivityConnIntroService {

    private final PzcActivityConnIntroMapper baseMapper;

    /**
     * 查询活动介绍与活动关联
     */
    @Override
    public PzcActivityConnIntroVo queryById(Integer activityConnIntroId){
        return baseMapper.selectVoById(activityConnIntroId);
    }

    /**
     * 查询活动介绍与活动关联列表
     */
    @Override
    public TableDataInfo<PzcActivityConnIntroVo> queryPageList(PzcActivityConnIntroBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PzcActivityConnIntro> lqw = buildQueryWrapper(bo);
        Page<PzcActivityConnIntroVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询活动介绍与活动关联列表
     */
    @Override
    public List<PzcActivityConnIntroVo> queryList(PzcActivityConnIntroBo bo) {
        LambdaQueryWrapper<PzcActivityConnIntro> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PzcActivityConnIntro> buildQueryWrapper(PzcActivityConnIntroBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PzcActivityConnIntro> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getActivityConnIntroId() != null, PzcActivityConnIntro::getActivityConnIntroId, bo.getActivityConnIntroId());
        lqw.eq(bo.getActivityId() != null, PzcActivityConnIntro::getActivityId, bo.getActivityId());
        lqw.eq(bo.getIntroId() != null, PzcActivityConnIntro::getIntroId, bo.getIntroId());
        return lqw;
    }

    /**
     * 新增活动介绍与活动关联
     */
    @Override
    public Boolean insertByBo(PzcActivityConnIntroBo bo) {
        PzcActivityConnIntro add = BeanUtil.toBean(bo, PzcActivityConnIntro.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setActivityConnIntroId(add.getActivityConnIntroId());
        }
        return flag;
    }

    /**
     * 修改活动介绍与活动关联
     */
    @Override
    public Boolean updateByBo(PzcActivityConnIntroBo bo) {
        PzcActivityConnIntro update = BeanUtil.toBean(bo, PzcActivityConnIntro.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PzcActivityConnIntro entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除活动介绍与活动关联
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
