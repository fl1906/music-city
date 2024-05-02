package top.flya.system.service;

import top.flya.system.domain.PzcOrganizerTicket;
import top.flya.system.domain.vo.PzcOrganizerTicketVo;
import top.flya.system.domain.bo.PzcOrganizerTicketBo;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 主办方票务Service接口
 *
 * @author ruoyi
 * @date 2023-07-22
 */
public interface IPzcOrganizerTicketService {

    /**
     * 查询主办方票务
     */
    PzcOrganizerTicketVo queryById(Long organizerTicketId);

    /**
     * 查询主办方票务列表
     */
    TableDataInfo<PzcOrganizerTicketVo> queryPageList(PzcOrganizerTicketBo bo, PageQuery pageQuery);

    /**
     * 查询主办方票务列表
     */
    List<PzcOrganizerTicketVo> queryList(PzcOrganizerTicketBo bo);

    /**
     * 新增主办方票务
     */
    Boolean insertByBo(PzcOrganizerTicketBo bo);

    /**
     * 修改主办方票务
     */
    Boolean updateByBo(PzcOrganizerTicketBo bo);

    /**
     * 校验并批量删除主办方票务信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
