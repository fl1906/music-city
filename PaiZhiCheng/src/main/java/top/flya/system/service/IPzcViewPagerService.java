package top.flya.system.service;

import top.flya.system.domain.PzcViewPager;
import top.flya.system.domain.vo.PzcViewPagerVo;
import top.flya.system.domain.bo.PzcViewPagerBo;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 轮播图Service接口
 *
 * @author ruoyi
 * @date 2023-05-23
 */
public interface IPzcViewPagerService {

    /**
     * 查询轮播图
     */
    PzcViewPagerVo queryById(Integer viewPagerId);

    /**
     * 查询轮播图列表
     */
    TableDataInfo<PzcViewPagerVo> queryPageList(PzcViewPagerBo bo, PageQuery pageQuery);

    /**
     * 查询轮播图列表
     */
    List<PzcViewPagerVo> queryList(PzcViewPagerBo bo);

    /**
     * 新增轮播图
     */
    Boolean insertByBo(PzcViewPagerBo bo);

    /**
     * 修改轮播图
     */
    Boolean updateByBo(PzcViewPagerBo bo);

    /**
     * 校验并批量删除轮播图信息
     */
    Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid);
}
