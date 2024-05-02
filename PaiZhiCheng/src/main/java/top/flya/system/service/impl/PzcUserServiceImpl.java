package top.flya.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.flya.common.core.domain.PageQuery;
import top.flya.common.core.page.TableDataInfo;
import top.flya.system.common.BatchUtils;
import top.flya.system.domain.PzcUser;
import top.flya.system.domain.bo.PzcUserBo;
import top.flya.system.domain.bo.UpdateMoneyBo;
import top.flya.system.domain.vo.PzcUserVo;
import top.flya.system.mapper.PzcUserMapper;
import top.flya.system.service.IPzcUserService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户Service业务层处理
 *
 * @author ruoyi
 * @date 2023-07-09
 */
@RequiredArgsConstructor
@Service
public class PzcUserServiceImpl implements IPzcUserService {

    private final PzcUserMapper baseMapper;

    private final BatchUtils batchUtils;

    /**
     * 查询用户
     */
    @Override
    public PzcUserVo queryById(Long userId){
        return baseMapper.selectVoById(userId);
    }

    /**
     * 查询用户列表
     */
    @Override
    public TableDataInfo<PzcUserVo> queryPageList(PzcUserBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PzcUser> lqw = buildQueryWrapper(bo);
        Page<PzcUserVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        result.setRecords(batchUtils.transformToPzcUserVo(result.getRecords()));
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户列表
     */
    @Override
    public List<PzcUserVo> queryList(PzcUserBo bo) {
        LambdaQueryWrapper<PzcUser> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PzcUser> buildQueryWrapper(PzcUserBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PzcUser> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getOpenid()), PzcUser::getOpenid, bo.getOpenid());
        lqw.eq(bo.getMoney() != null, PzcUser::getMoney, bo.getMoney());
        lqw.eq(bo.getUserLevel() != null, PzcUser::getUserLevel, bo.getUserLevel());
        lqw.eq(bo.getIntegration() != null, PzcUser::getIntegration, bo.getIntegration());
        lqw.eq(bo.getIntegrationNow() != null, PzcUser::getIntegrationNow, bo.getIntegrationNow());
        lqw.like(StringUtils.isNotBlank(bo.getRealname()), PzcUser::getRealname, bo.getRealname());
        lqw.like(StringUtils.isNotBlank(bo.getNickname()), PzcUser::getNickname, bo.getNickname());
        lqw.eq(bo.getSex() != null, PzcUser::getSex, bo.getSex());
        lqw.eq(StringUtils.isNotBlank(bo.getPhone()), PzcUser::getPhone, bo.getPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getAvatar()), PzcUser::getAvatar, bo.getAvatar());
        lqw.eq(StringUtils.isNotBlank(bo.getAddress()), PzcUser::getAddress, bo.getAddress());
        lqw.eq(StringUtils.isNotBlank(bo.getIntro()), PzcUser::getIntro, bo.getIntro());
        lqw.eq(bo.getAge() != null, PzcUser::getAge, bo.getAge());
        lqw.eq(StringUtils.isNotBlank(bo.getConstellation()), PzcUser::getConstellation, bo.getConstellation());
        lqw.eq(StringUtils.isNotBlank(bo.getMbti()), PzcUser::getMbti, bo.getMbti());
        lqw.eq(bo.getHobby() != null, PzcUser::getHobby, bo.getHobby());
        lqw.eq(StringUtils.isNotBlank(bo.getSchool()), PzcUser::getSchool, bo.getSchool());
        lqw.eq(StringUtils.isNotBlank(bo.getOccupation()), PzcUser::getOccupation, bo.getOccupation());
        lqw.eq(StringUtils.isNotBlank(bo.getMusicStyle()), PzcUser::getMusicStyle, bo.getMusicStyle());
        lqw.eq(bo.getState() != null, PzcUser::getState, bo.getState());
        return lqw;
    }

    /**
     * 新增用户
     */
    @Override
    public Boolean insertByBo(PzcUserBo bo) {
        PzcUser add = BeanUtil.toBean(bo, PzcUser.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setUserId(add.getUserId());
        }
        return flag;
    }

    /**
     * 修改用户
     */
    @Override
    public Boolean updateByBo(PzcUserBo bo) {
        PzcUser update = BeanUtil.toBean(bo, PzcUser.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PzcUser entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除用户
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public int updateMoney(UpdateMoneyBo bo) {

        return baseMapper.updateMoney(bo);
    }
}
