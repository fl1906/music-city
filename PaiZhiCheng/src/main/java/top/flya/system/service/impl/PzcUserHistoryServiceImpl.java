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
import top.flya.common.utils.DateUtils;
import top.flya.system.domain.bo.PzcUserHistoryBo;
import top.flya.system.domain.vo.PzcUserHistoryVo;
import top.flya.system.domain.PzcUserHistory;
import top.flya.system.mapper.PzcUserHistoryMapper;
import top.flya.system.service.IPzcUserHistoryService;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 用户操作历史记录Service业务层处理
 *
 * @author ruoyi
 * @date 2023-07-06
 */
@RequiredArgsConstructor
@Service
public class PzcUserHistoryServiceImpl implements IPzcUserHistoryService {

    private final PzcUserHistoryMapper baseMapper;

    /**
     * 查询用户操作历史记录
     */
    @Override
    public PzcUserHistoryVo queryById(Long historyId){
        return baseMapper.selectVoById(historyId);
    }

    /**
     * 查询用户操作历史记录列表
     */
    @Override
    public TableDataInfo<PzcUserHistoryVo> queryPageList(PzcUserHistoryBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PzcUserHistory> lqw = buildQueryWrapper(bo);
        Page<PzcUserHistoryVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户操作历史记录列表
     */
    @Override
    public List<PzcUserHistoryVo> queryList(PzcUserHistoryBo bo) {
        LambdaQueryWrapper<PzcUserHistory> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PzcUserHistory> buildQueryWrapper(PzcUserHistoryBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PzcUserHistory> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, PzcUserHistory::getUserId, bo.getUserId());
        lqw.eq(bo.getActivityId() != null, PzcUserHistory::getActivityId, bo.getActivityId());
        lqw.in(bo.getType() != null, PzcUserHistory::getType, bo.getType());
        lqw.eq(StringUtils.isNotBlank(bo.getMessage()), PzcUserHistory::getMessage, bo.getMessage());
        //获取本月开始时间和结束时间
        if(StringUtils.isNotBlank(bo.getNowTime())&&bo.getNowTime().length()>0)
        {
            try {
                lqw.between(true, PzcUserHistory::getCreateTime,DateUtils.getMonthStartAndEnd(bo.getNowTime())[0], DateUtils.getMonthStartAndEnd(bo.getNowTime())[1]);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        return lqw;
    }

    /**
     * 新增用户操作历史记录
     */
    @Override
    public Boolean insertByBo(PzcUserHistoryBo bo) {
        PzcUserHistory add = BeanUtil.toBean(bo, PzcUserHistory.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setHistoryId(add.getHistoryId());
        }
        return flag;
    }

    /**
     * 修改用户操作历史记录
     */
    @Override
    public Boolean updateByBo(PzcUserHistoryBo bo) {
        PzcUserHistory update = BeanUtil.toBean(bo, PzcUserHistory.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PzcUserHistory entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除用户操作历史记录
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
