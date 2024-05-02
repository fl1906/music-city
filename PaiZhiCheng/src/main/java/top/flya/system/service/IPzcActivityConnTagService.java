package top.flya.system.service;

import top.flya.system.domain.PzcActivityConnTag;
import top.flya.system.domain.vo.PzcActivityConnTagVo;
import top.flya.system.domain.bo.PzcActivityConnTagBo;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 活动标签与活动关联Service接口
 *
 * @author ruoyi
 * @date 2023-06-03
 */
public interface IPzcActivityConnTagService {

    /**
     * 查询活动标签与活动关联
     */
    PzcActivityConnTagVo queryById(Integer activityConnTagId);

    /**
     * 查询活动标签与活动关联列表
     */
    TableDataInfo<PzcActivityConnTagVo> queryPageList(PzcActivityConnTagBo bo, PageQuery pageQuery);

    /**
     * 查询活动标签与活动关联列表
     */
    List<PzcActivityConnTagVo> queryList(PzcActivityConnTagBo bo);

    /**
     * 新增活动标签与活动关联
     */
    Boolean insertByBo(PzcActivityConnTagBo bo);

    /**
     * 修改活动标签与活动关联
     */
    Boolean updateByBo(PzcActivityConnTagBo bo);

    /**
     * 校验并批量删除活动标签与活动关联信息
     */
    Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid);
}
