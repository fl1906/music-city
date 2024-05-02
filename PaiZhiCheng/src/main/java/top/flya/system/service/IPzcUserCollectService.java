package top.flya.system.service;

import top.flya.system.domain.PzcUserCollect;
import top.flya.system.domain.vo.PzcUserCollectVo;
import top.flya.system.domain.bo.PzcUserCollectBo;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 用户收藏活动Service接口
 *
 * @author ruoyi
 * @date 2023-07-08
 */
public interface IPzcUserCollectService {

    /**
     * 查询用户收藏活动
     */
    PzcUserCollectVo queryById(Long collectId);

    /**
     * 查询用户收藏活动列表
     */
    TableDataInfo<PzcUserCollectVo> queryPageList(PzcUserCollectBo bo, PageQuery pageQuery);

    /**
     * 查询用户收藏活动列表
     */
    List<PzcUserCollectVo> queryList(PzcUserCollectBo bo);

    /**
     * 新增用户收藏活动
     */
    Boolean insertByBo(PzcUserCollectBo bo);

    /**
     * 修改用户收藏活动
     */
    Boolean updateByBo(PzcUserCollectBo bo);

    /**
     * 校验并批量删除用户收藏活动信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
