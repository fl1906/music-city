package top.flya.system.service;

import top.flya.common.core.domain.PageQuery;
import top.flya.common.core.page.TableDataInfo;
import top.flya.system.domain.bo.PzcOfficialBo;
import top.flya.system.domain.vo.PzcOfficialVo;

import java.util.Collection;
import java.util.List;

/**
 * 官方消息Service接口
 *
 * @author ruoyi
 * @date 2023-07-27
 */
public interface IPzcOfficialService {

    /**
     * 查询官方消息
     */
    PzcOfficialVo queryById(Long officialId);

    /**
     * 查询官方消息列表
     */
    TableDataInfo<PzcOfficialVo> queryPageList(PzcOfficialBo bo, PageQuery pageQuery);

    /**
     * 查询官方消息列表
     */
    List<PzcOfficialVo> queryList(PzcOfficialBo bo);

    /**
     * 新增官方消息
     */
    Boolean insertByBo(PzcOfficialBo bo);

    /**
     * 修改官方消息
     */
    Boolean updateByBo(PzcOfficialBo bo);

    /**
     * 校验并批量删除官方消息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    Integer read(Integer officialId);
}
