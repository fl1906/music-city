package top.flya.system.service;

import top.flya.system.domain.PzcUserHistory;
import top.flya.system.domain.vo.PzcUserHistoryVo;
import top.flya.system.domain.bo.PzcUserHistoryBo;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 用户操作历史记录Service接口
 *
 * @author ruoyi
 * @date 2023-07-06
 */
public interface IPzcUserHistoryService {

    /**
     * 查询用户操作历史记录
     */
    PzcUserHistoryVo queryById(Long historyId);

    /**
     * 查询用户操作历史记录列表
     */
    TableDataInfo<PzcUserHistoryVo> queryPageList(PzcUserHistoryBo bo, PageQuery pageQuery);

    /**
     * 查询用户操作历史记录列表
     */
    List<PzcUserHistoryVo> queryList(PzcUserHistoryBo bo);

    /**
     * 新增用户操作历史记录
     */
    Boolean insertByBo(PzcUserHistoryBo bo);

    /**
     * 修改用户操作历史记录
     */
    Boolean updateByBo(PzcUserHistoryBo bo);

    /**
     * 校验并批量删除用户操作历史记录信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
