package top.flya.system.service;

import top.flya.system.domain.PzcActivityConnArtist;
import top.flya.system.domain.vo.PzcActivityConnArtistVo;
import top.flya.system.domain.bo.PzcActivityConnArtistBo;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 活动关联艺人Service接口
 *
 * @author ruoyi
 * @date 2023-06-02
 */
public interface IPzcActivityConnArtistService {

    /**
     * 查询活动关联艺人
     */
    PzcActivityConnArtistVo queryById(Integer activityConnArtistId);

    /**
     * 查询活动关联艺人列表
     */
    TableDataInfo<PzcActivityConnArtistVo> queryPageList(PzcActivityConnArtistBo bo, PageQuery pageQuery);

    /**
     * 查询活动关联艺人列表
     */
    List<PzcActivityConnArtistVo> queryList(PzcActivityConnArtistBo bo);

    /**
     * 新增活动关联艺人
     */
    Boolean insertByBo(PzcActivityConnArtistBo bo);

    /**
     * 修改活动关联艺人
     */
    Boolean updateByBo(PzcActivityConnArtistBo bo);

    /**
     * 校验并批量删除活动关联艺人信息
     */
    Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid);
}
