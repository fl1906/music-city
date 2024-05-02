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
import top.flya.system.domain.bo.PzcOrderBo;
import top.flya.system.domain.vo.PzcOrderVo;
import top.flya.system.domain.PzcOrder;
import top.flya.system.mapper.PzcOrderMapper;
import top.flya.system.service.IPzcOrderService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 订单Service业务层处理
 *
 * @author ruoyi
 * @date 2023-07-09
 */
@RequiredArgsConstructor
@Service
public class PzcOrderServiceImpl implements IPzcOrderService {

    private final PzcOrderMapper baseMapper;

    /**
     * 查询订单
     */
    @Override
    public PzcOrderVo queryById(Long orderId){
        return baseMapper.selectVoById(orderId);
    }

    /**
     * 查询订单列表
     */
    @Override
    public TableDataInfo<PzcOrderVo> queryPageList(PzcOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PzcOrder> lqw = buildQueryWrapper(bo);
        Page<PzcOrderVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询订单列表
     */
    @Override
    public List<PzcOrderVo> queryList(PzcOrderBo bo) {
        LambdaQueryWrapper<PzcOrder> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PzcOrder> buildQueryWrapper(PzcOrderBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PzcOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, PzcOrder::getUserId, bo.getUserId());
        lqw.eq(bo.getActivityId() != null, PzcOrder::getActivityId, bo.getActivityId());
        lqw.eq(bo.getMoney() != null, PzcOrder::getMoney, bo.getMoney());
        lqw.eq(bo.getOrderStatus() != null, PzcOrder::getOrderStatus, bo.getOrderStatus());
        lqw.eq(bo.getType() != null, PzcOrder::getType, bo.getType());
        lqw.eq(StringUtils.isNotBlank(bo.getOutOrderNum()), PzcOrder::getOutOrderNum, bo.getOutOrderNum());
        lqw.eq(StringUtils.isNotBlank(bo.getIntro()), PzcOrder::getIntro, bo.getIntro());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), PzcOrder::getTitle, bo.getTitle());
        lqw.eq(bo.getCreateTime() != null, PzcOrder::getCreateTime, bo.getCreateTime());
        lqw.eq(bo.getUpdateTime() != null, PzcOrder::getUpdateTime, bo.getUpdateTime());
        return lqw;
    }

    /**
     * 新增订单
     */
    @Override
    public Boolean insertByBo(PzcOrderBo bo) {
        PzcOrder add = BeanUtil.toBean(bo, PzcOrder.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setOrderId(add.getOrderId());
        }
        return flag;
    }

    /**
     * 修改订单
     */
    @Override
    public Boolean updateByBo(PzcOrderBo bo) {
        PzcOrder update = BeanUtil.toBean(bo, PzcOrder.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PzcOrder entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除订单
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
