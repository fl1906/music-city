package top.flya.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.flya.system.common.BatchUtils;
import top.flya.system.domain.bo.PzcUserCollectBo;
import top.flya.system.domain.vo.PzcUserCollectVo;
import top.flya.system.domain.PzcUserCollect;
import top.flya.system.mapper.PzcActivityMapper;
import top.flya.system.mapper.PzcUserCollectMapper;
import top.flya.system.service.IPzcUserCollectService;

import java.util.*;

/**
 * 用户收藏活动Service业务层处理
 *
 * @author ruoyi
 * @date 2023-07-08
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class PzcUserCollectServiceImpl implements IPzcUserCollectService {

    private final PzcUserCollectMapper baseMapper;

    private final PzcActivityMapper pzcActivityMapper;


    private final BatchUtils batchUtils;
    /**
     * 查询用户收藏活动
     */
    @Override
    public PzcUserCollectVo queryById(Long collectId){
        return baseMapper.selectVoById(collectId);
    }

    /**
     * 查询用户收藏活动列表
     */
    @Override
    public TableDataInfo<PzcUserCollectVo> queryPageList(PzcUserCollectBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PzcUserCollect> lqw = buildQueryWrapper(bo);
        Page<PzcUserCollectVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<PzcUserCollectVo> records = new ArrayList<>();


            result.getRecords().forEach(
                vo -> {
                    vo.setActivity(pzcActivityMapper.selectById(vo.getActivityId()));

                    if(vo.getActivity()!=null)
                    {
                        log.info("qaq: {} vo.getActivity().getClassify() is {} bo.getType() is {}",vo.getActivity().getClassify().equals(bo.getType()),vo.getActivity().getClassify(),bo.getType());
                        if(vo.getActivity().getClassify().equals(bo.getType()))
                        {
                            log.info("vo.getActivity() is:"+vo.getActivity().getClassify());
                            vo.getActivity().setCoverImage(vo.getActivity().getCoverImage().contains("http")?vo.getActivity().getCoverImage(): batchUtils.getNewImageUrls(Collections.singletonList(vo.getActivity().getCoverImage())).get(Long.parseLong(vo.getActivity().getCoverImage())));
//                            vo.getActivity().setShareImage(vo.getActivity().getShareImage().contains("http")?vo.getActivity().getShareImage(): batchUtils.getNewImageUrls(Collections.singletonList(vo.getActivity().getShareImage())).get(Long.parseLong(vo.getActivity().getShareImage())));
                            records.add(vo);
                        }

                    }
                }
            );

        return TableDataInfo.build(records);
    }

    /**
     * 查询用户收藏活动列表
     */
    @Override
    public List<PzcUserCollectVo> queryList(PzcUserCollectBo bo) {
        LambdaQueryWrapper<PzcUserCollect> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PzcUserCollect> buildQueryWrapper(PzcUserCollectBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PzcUserCollect> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, PzcUserCollect::getUserId, bo.getUserId());
        lqw.eq(bo.getActivityId() != null, PzcUserCollect::getActivityId, bo.getActivityId());
        return lqw;
    }

    /**
     * 新增用户收藏活动
     */
    @Override
    public Boolean insertByBo(PzcUserCollectBo bo) {
        PzcUserCollect add = BeanUtil.toBean(bo, PzcUserCollect.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setCollectId(add.getCollectId());
        }
        return flag;
    }

    /**
     * 修改用户收藏活动
     */
    @Override
    public Boolean updateByBo(PzcUserCollectBo bo) {
        PzcUserCollect update = BeanUtil.toBean(bo, PzcUserCollect.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PzcUserCollect entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除用户收藏活动
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
