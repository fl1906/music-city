package top.flya.system.service;

import top.flya.system.domain.PzcArtist;
import top.flya.system.domain.vo.PzcArtistVo;
import top.flya.system.domain.bo.PzcArtistBo;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 艺人Service接口
 *
 * @author flya
 * @date 2023-06-01
 */
public interface IPzcArtistService {

    /**
     * 查询艺人
     */
    PzcArtistVo queryById(Long artistId);

    /**
     * 查询艺人列表
     */
    TableDataInfo<PzcArtistVo> queryPageList(PzcArtistBo bo, PageQuery pageQuery);

    /**
     * 查询艺人列表
     */
    List<PzcArtistVo> queryList(PzcArtistBo bo);

    /**
     * 新增艺人
     */
    Boolean insertByBo(PzcArtistBo bo);

    /**
     * 修改艺人
     */
    Boolean updateByBo(PzcArtistBo bo);

    /**
     * 校验并批量删除艺人信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
