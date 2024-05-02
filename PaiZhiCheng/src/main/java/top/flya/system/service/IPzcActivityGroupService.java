package top.flya.system.service;

import top.flya.system.domain.PzcActivityGroup;
import top.flya.system.domain.vo.PzcActivityGroupVo;
import top.flya.system.domain.bo.PzcActivityGroupBo;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 活动组队Service接口
 *
 * @author ruoyi
 * @date 2023-07-10
 */
public interface IPzcActivityGroupService {

    /**
     * 查询活动组队
     */
    PzcActivityGroupVo queryById(Long groupId);

    /**
     * 查询活动组队列表
     */
    TableDataInfo<PzcActivityGroupVo> queryPageList(PzcActivityGroupBo bo, PageQuery pageQuery);

    /**
     * 查询活动组队列表
     */
    List<PzcActivityGroupVo> queryList(PzcActivityGroupBo bo);

    /**
     * 新增活动组队
     */
    Boolean insertByBo(PzcActivityGroupBo bo);

    /**
     * 修改活动组队
     */
    Boolean updateByBo(PzcActivityGroupBo bo);

    /**
     * 校验并批量删除活动组队信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    boolean checkActivity(Long activityId);

    boolean checkGroup(Long userId, Long activityId);
}
