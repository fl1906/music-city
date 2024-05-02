package top.flya.system.service;

import top.flya.system.domain.PzcActivityConnIntro;
import top.flya.system.domain.vo.PzcActivityConnIntroVo;
import top.flya.system.domain.bo.PzcActivityConnIntroBo;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 活动介绍与活动关联Service接口
 *
 * @author ruoyi
 * @date 2023-06-02
 */
public interface IPzcActivityConnIntroService {

    /**
     * 查询活动介绍与活动关联
     */
    PzcActivityConnIntroVo queryById(Integer activityConnIntroId);

    /**
     * 查询活动介绍与活动关联列表
     */
    TableDataInfo<PzcActivityConnIntroVo> queryPageList(PzcActivityConnIntroBo bo, PageQuery pageQuery);

    /**
     * 查询活动介绍与活动关联列表
     */
    List<PzcActivityConnIntroVo> queryList(PzcActivityConnIntroBo bo);

    /**
     * 新增活动介绍与活动关联
     */
    Boolean insertByBo(PzcActivityConnIntroBo bo);

    /**
     * 修改活动介绍与活动关联
     */
    Boolean updateByBo(PzcActivityConnIntroBo bo);

    /**
     * 校验并批量删除活动介绍与活动关联信息
     */
    Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid);
}
