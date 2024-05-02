package top.flya.system.service;

import top.flya.system.domain.PzcOrder;
import top.flya.system.domain.vo.PzcOrderVo;
import top.flya.system.domain.bo.PzcOrderBo;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 订单Service接口
 *
 * @author ruoyi
 * @date 2023-07-09
 */
public interface IPzcOrderService {

    /**
     * 查询订单
     */
    PzcOrderVo queryById(Long orderId);

    /**
     * 查询订单列表
     */
    TableDataInfo<PzcOrderVo> queryPageList(PzcOrderBo bo, PageQuery pageQuery);

    /**
     * 查询订单列表
     */
    List<PzcOrderVo> queryList(PzcOrderBo bo);

    /**
     * 新增订单
     */
    Boolean insertByBo(PzcOrderBo bo);

    /**
     * 修改订单
     */
    Boolean updateByBo(PzcOrderBo bo);

    /**
     * 校验并批量删除订单信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
