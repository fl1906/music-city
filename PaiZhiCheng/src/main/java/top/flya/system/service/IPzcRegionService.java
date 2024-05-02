package top.flya.system.service;

import top.flya.common.core.domain.PageQuery;
import top.flya.common.core.page.TableDataInfo;
import top.flya.system.domain.bo.PzcRegionBo;
import top.flya.system.domain.vo.PzcRegionVo;

import java.util.Collection;
import java.util.List;

/**
 * 地区Service接口
 *
 * @author ruoyi
 * @date 2023-07-22
 */
public interface IPzcRegionService {

    /**
     * 查询地区
     */
    PzcRegionVo queryById(Long regionId);

    /**
     * 查询地区列表
     */
    TableDataInfo<PzcRegionVo> queryPageList(PzcRegionBo bo, PageQuery pageQuery);

    /**
     * 查询地区列表
     */
    List<PzcRegionVo> queryList(PzcRegionBo bo);

    /**
     * 新增地区
     */
    Boolean insertByBo(PzcRegionBo bo);

    /**
     * 修改地区
     */
    Boolean updateByBo(PzcRegionBo bo);

    /**
     * 校验并批量删除地区信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
