package top.flya.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.flya.system.domain.bo.PzcUserPhotoBo;
import top.flya.system.domain.vo.PzcUserPhotoVo;
import top.flya.system.domain.PzcUserPhoto;
import top.flya.system.mapper.PzcUserPhotoMapper;
import top.flya.system.service.IPzcUserPhotoService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 用户资料相册Service业务层处理
 *
 * @author ruoyi
 * @date 2023-07-11
 */
@RequiredArgsConstructor
@Service
public class PzcUserPhotoServiceImpl implements IPzcUserPhotoService {

    private final PzcUserPhotoMapper baseMapper;

    /**
     * 查询用户资料相册
     */
    @Override
    public PzcUserPhotoVo queryById(Long photoId){
        return baseMapper.selectVoById(photoId);
    }

    /**
     * 查询用户资料相册列表
     */
    @Override
    public TableDataInfo<PzcUserPhotoVo> queryPageList(PzcUserPhotoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PzcUserPhoto> lqw = buildQueryWrapper(bo);
        Page<PzcUserPhotoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户资料相册列表
     */
    @Override
    public List<PzcUserPhotoVo> queryList(PzcUserPhotoBo bo) {
        LambdaQueryWrapper<PzcUserPhoto> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PzcUserPhoto> buildQueryWrapper(PzcUserPhotoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PzcUserPhoto> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, PzcUserPhoto::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getUrl()), PzcUserPhoto::getUrl, bo.getUrl());
        lqw.eq(StringUtils.isNotBlank(bo.getMessage()), PzcUserPhoto::getMessage, bo.getMessage());
        lqw.eq(bo.getCreateTime() != null, PzcUserPhoto::getCreateTime, bo.getCreateTime());
        lqw.eq(bo.getUpdateTime() != null, PzcUserPhoto::getUpdateTime, bo.getUpdateTime());
        return lqw;
    }

    /**
     * 新增用户资料相册
     */
    @Override
    public Boolean insertByBo(PzcUserPhotoBo bo) {
        PzcUserPhoto add = BeanUtil.toBean(bo, PzcUserPhoto.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setPhotoId(add.getPhotoId());
        }
        return flag;
    }

    /**
     * 修改用户资料相册
     */
    @Override
    public Boolean updateByBo(PzcUserPhotoBo bo) {
        PzcUserPhoto update = BeanUtil.toBean(bo, PzcUserPhoto.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PzcUserPhoto entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除用户资料相册
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
