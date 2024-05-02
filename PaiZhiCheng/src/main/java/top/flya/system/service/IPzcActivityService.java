package top.flya.system.service;

import top.flya.system.domain.PzcActivity;
import top.flya.system.domain.vo.PzcActivityVo;
import top.flya.system.domain.bo.PzcActivityBo;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 活动Service接口
 *
 * @author ruoyi
 * @date 2023-06-02
 */
public interface IPzcActivityService {

    /**
     * 查询活动
     */
    PzcActivityVo queryById(Integer activityId);

    /**
     * 查询活动列表
     */
    TableDataInfo<PzcActivityVo> queryPageList(PzcActivityBo bo, PageQuery pageQuery);


    /**
     * 查询活动列表 微信
     *
     */
  TableDataInfo<PzcActivityVo> queryPageListWx(PzcActivityBo bo, PageQuery pageQuery);

    /**
     * 查询活动列表
     */
    List<PzcActivityVo> queryList(PzcActivityBo bo);

    /**
     * 新增活动
     */
    Boolean insertByBo(PzcActivityBo bo);

    /**
     * 修改活动
     */
    Boolean updateByBo(PzcActivityBo bo);

    /**
     * 校验并批量删除活动信息
     */
    Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid);
}
