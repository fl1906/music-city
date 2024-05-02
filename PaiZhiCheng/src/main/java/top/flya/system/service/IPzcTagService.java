package top.flya.system.service;

import top.flya.system.domain.PzcTag;
import top.flya.system.domain.vo.PzcTagVo;
import top.flya.system.domain.bo.PzcTagBo;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 活动标签Service接口
 *
 * @author ruoyi
 * @date 2023-06-02
 */
public interface IPzcTagService {

    /**
     * 查询活动标签
     */
    PzcTagVo queryById(Long tagId);

    /**
     * 查询活动标签列表
     */
    TableDataInfo<PzcTagVo> queryPageList(PzcTagBo bo, PageQuery pageQuery);

    /**
     * 查询活动标签列表
     */
    List<PzcTagVo> queryList(PzcTagBo bo);

    /**
     * 新增活动标签
     */
    Boolean insertByBo(PzcTagBo bo);

    /**
     * 修改活动标签
     */
    Boolean updateByBo(PzcTagBo bo);

    /**
     * 校验并批量删除活动标签信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
