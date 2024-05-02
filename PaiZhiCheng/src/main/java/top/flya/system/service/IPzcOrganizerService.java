package top.flya.system.service;

import top.flya.system.domain.PzcOrganizer;
import top.flya.system.domain.vo.PzcOrganizerVo;
import top.flya.system.domain.bo.PzcOrganizerBo;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 活动主办方Service接口
 *
 * @author ruoyi
 * @date 2023-06-02
 */
public interface IPzcOrganizerService {

    /**
     * 查询活动主办方
     */
    PzcOrganizerVo queryById(Long organizerId);

    /**
     * 查询活动主办方列表
     */
    TableDataInfo<PzcOrganizerVo> queryPageList(PzcOrganizerBo bo, PageQuery pageQuery);

    /**
     * 查询活动主办方列表
     */
    List<PzcOrganizerVo> queryList(PzcOrganizerBo bo);

    /**
     * 新增活动主办方
     */
    Boolean insertByBo(PzcOrganizerBo bo);

    /**
     * 修改活动主办方
     */
    Boolean updateByBo(PzcOrganizerBo bo);

    /**
     * 校验并批量删除活动主办方信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
