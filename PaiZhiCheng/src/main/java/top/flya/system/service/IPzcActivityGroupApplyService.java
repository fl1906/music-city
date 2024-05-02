package top.flya.system.service;

import top.flya.common.core.domain.PageQuery;
import top.flya.common.core.page.TableDataInfo;
import top.flya.system.domain.bo.PzcActivityGroupApplyBo;
import top.flya.system.domain.vo.PzcActivityGroupApplyVo;

import java.util.Collection;
import java.util.List;

/**
 * 活动组队申请列Service接口
 *
 * @author ruoyi
 * @date 2023-07-10
 */
public interface IPzcActivityGroupApplyService {

    /**
     * 查询活动组队申请列
     */
    PzcActivityGroupApplyVo queryById(Long applyId);

    /**
     * 查询活动组队申请列列表
     */
    TableDataInfo<PzcActivityGroupApplyVo> queryPageList(PzcActivityGroupApplyBo bo, PageQuery pageQuery);

    /**
     * 查询活动组队申请列列表
     */
    List<PzcActivityGroupApplyVo> queryList(PzcActivityGroupApplyBo bo);

    /**
     * 新增活动组队申请列
     */
    Boolean insertByBo(PzcActivityGroupApplyBo bo);

    /**
     * 修改活动组队申请列
     */
    Boolean updateByBo(PzcActivityGroupApplyBo bo);

    /**
     * 校验并批量删除活动组队申请列信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    boolean queryByUserIdAndActivityId(Long userId, Long activityId);

    Integer updateStatus(Long applyId, int i);

    List<PzcActivityGroupApplyVo> queryListByGroupIds(List<Long> groupIds);

    PzcActivityGroupApplyVo queryByUserIdAndGroupId(Long userId, Long groupId);
}
