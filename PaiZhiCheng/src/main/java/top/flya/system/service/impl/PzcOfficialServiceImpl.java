package top.flya.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.flya.common.core.domain.PageQuery;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.helper.LoginHelper;
import top.flya.system.domain.PzcOfficial;
import top.flya.system.domain.bo.PzcOfficialBo;
import top.flya.system.domain.vo.PzcOfficialVo;
import top.flya.system.mapper.PzcOfficialMapper;
import top.flya.system.service.IPzcOfficialService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 官方消息Service业务层处理
 *
 * @author ruoyi
 * @date 2023-07-27
 */
@RequiredArgsConstructor
@Service
public class PzcOfficialServiceImpl implements IPzcOfficialService {

    private final PzcOfficialMapper baseMapper;

    /**
     * 查询官方消息
     */
    @Override
    public PzcOfficialVo queryById(Long officialId){
        return baseMapper.selectVoById(officialId);
    }

    /**
     * 查询官方消息列表
     */
    @Override
    public TableDataInfo<PzcOfficialVo> queryPageList(PzcOfficialBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PzcOfficial> lqw = buildQueryWrapper(bo);
        lqw.eq(true,PzcOfficial::getIsRead,0L);
        Page<PzcOfficialVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询官方消息列表
     */
    @Override
    public List<PzcOfficialVo> queryList(PzcOfficialBo bo) {
        LambdaQueryWrapper<PzcOfficial> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PzcOfficial> buildQueryWrapper(PzcOfficialBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PzcOfficial> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getFromUserId() != null, PzcOfficial::getFromUserId, bo.getFromUserId());
        lqw.in(bo.getToUserId() != null, PzcOfficial::getToUserId, bo.getToUserId(),0); //官方消息 和给我的消息
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), PzcOfficial::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), PzcOfficial::getContent, bo.getContent());
        lqw.eq(bo.getIsRead() != null, PzcOfficial::getIsRead, bo.getIsRead());
        lqw.eq(bo.getGroupId() != null, PzcOfficial::getGroupId, bo.getGroupId());
        lqw.eq(bo.getActivityId() != null, PzcOfficial::getActivityId, bo.getActivityId());
        lqw.eq(bo.getCreateTime() != null, PzcOfficial::getCreateTime, bo.getCreateTime());
        lqw.eq(bo.getUpdateTime() != null, PzcOfficial::getUpdateTime, bo.getUpdateTime());
        return lqw;
    }

    /**
     * 新增官方消息
     */
    @Override
    public Boolean insertByBo(PzcOfficialBo bo) {
        PzcOfficial add = BeanUtil.toBean(bo, PzcOfficial.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setOfficialId(add.getOfficialId());
        }
        return flag;
    }

    /**
     * 修改官方消息
     */
    @Override
    public Boolean updateByBo(PzcOfficialBo bo) {
        PzcOfficial update = BeanUtil.toBean(bo, PzcOfficial.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PzcOfficial entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除官方消息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    @Override
    public Integer read(Integer officialId) {
        if(officialId==null)
        {
            UpdateWrapper<PzcOfficial> set = new UpdateWrapper<PzcOfficial>().eq("to_user_id", LoginHelper.getUserId()).set("is_read", 1);
            return baseMapper.update(new PzcOfficial(),set);
        }else {
            PzcOfficial pzcOfficial = baseMapper.selectById(officialId);
            pzcOfficial.setIsRead(1L);
            return baseMapper.updateById(pzcOfficial);
        }
    }
}
