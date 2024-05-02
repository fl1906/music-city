package top.flya.system.service;

import top.flya.system.domain.PzcUserPhoto;
import top.flya.system.domain.vo.PzcUserPhotoVo;
import top.flya.system.domain.bo.PzcUserPhotoBo;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 用户资料相册Service接口
 *
 * @author ruoyi
 * @date 2023-07-11
 */
public interface IPzcUserPhotoService {

    /**
     * 查询用户资料相册
     */
    PzcUserPhotoVo queryById(Long photoId);

    /**
     * 查询用户资料相册列表
     */
    TableDataInfo<PzcUserPhotoVo> queryPageList(PzcUserPhotoBo bo, PageQuery pageQuery);

    /**
     * 查询用户资料相册列表
     */
    List<PzcUserPhotoVo> queryList(PzcUserPhotoBo bo);

    /**
     * 新增用户资料相册
     */
    Boolean insertByBo(PzcUserPhotoBo bo);

    /**
     * 修改用户资料相册
     */
    Boolean updateByBo(PzcUserPhotoBo bo);

    /**
     * 校验并批量删除用户资料相册信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
