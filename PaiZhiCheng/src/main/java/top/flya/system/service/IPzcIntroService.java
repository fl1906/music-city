package top.flya.system.service;

import top.flya.common.core.domain.PageQuery;
import top.flya.common.core.page.TableDataInfo;
import top.flya.system.domain.bo.PzcIntroBo;
import top.flya.system.domain.vo.PzcIntroVo;

import java.util.Collection;
import java.util.List;

/**
 * 活动介绍Service接口
 *
 * @author ruoyi
 * @date 2023-08-04
 */
public interface IPzcIntroService {

    /**
     * 查询活动介绍
     */
    PzcIntroVo queryById(Long introId);

    /**
     * 查询活动介绍列表
     */
    TableDataInfo<PzcIntroVo> queryPageList(PzcIntroBo bo, PageQuery pageQuery);

    /**
     * 查询活动介绍列表
     */
    List<PzcIntroVo> queryList(PzcIntroBo bo);

    /**
     * 新增活动介绍
     */
    Boolean insertByBo(PzcIntroBo bo);

    /**
     * 修改活动介绍
     */
    Boolean updateByBo(PzcIntroBo bo);

    /**
     * 校验并批量删除活动介绍信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
