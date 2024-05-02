package top.flya.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.flya.common.core.domain.PageQuery;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.utils.JsonUtils;
import top.flya.system.domain.PzcActivityGroup;
import top.flya.system.domain.PzcActivityGroupApply;
import top.flya.system.domain.PzcRegion;
import top.flya.system.domain.PzcUserPhoto;
import top.flya.system.domain.bo.PzcActivityGroupBo;
import top.flya.system.domain.vo.PzcActivityGroupVo;
import top.flya.system.domain.vo.PzcUserVo;
import top.flya.system.mapper.*;
import top.flya.system.service.IPzcActivityGroupService;
import top.flya.system.utils.gaode.GaoDeMapUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 活动组队Service业务层处理
 *
 * @author ruoyi
 * @date 2023-07-10
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class PzcActivityGroupServiceImpl implements IPzcActivityGroupService {

    private final PzcActivityGroupMapper baseMapper;

    private final PzcActivityMapper pzcActivityMapper;

    private final PzcUserPhotoMapper pzcUserPhotoMapper;


    private final PzcActivityGroupApplyMapper pzcActivityGroupApplyMapper;

    private final PzcRegionMapper pzcRegionMapper;

    private final GaoDeMapUtil gaoDeMapUtil;

    /**
     * 查询活动组队
     */
    @Override
    public PzcActivityGroupVo queryById(Long groupId) {
        PzcActivityGroupVo pzcActivityGroupVo = baseMapper.selectVoByIdDIY(groupId);
        if (pzcActivityGroupVo == null) {
            return null;
        }
        if (pzcActivityGroupVo.getAuth() == 2) {
            log.info("私密组队，不返回用户信息");
            pzcActivityGroupVo.setUser(getPrivateUser(pzcActivityGroupVo.getUser()));
            pzcActivityGroupVo.setPhoto(null);
        } else {
            List<PzcUserPhoto> userPhotos = pzcUserPhotoMapper.selectList(new QueryWrapper<PzcUserPhoto>().eq("user_id", pzcActivityGroupVo.getUserId()));
            pzcActivityGroupVo.setPhoto(userPhotos);
            if (pzcActivityGroupVo.getAuth() == 1) //权限 1只返回一张图片 即头像
            {
                pzcActivityGroupVo.setPhoto(null); //
            }
        }
        if (pzcActivityMapper.selectVoById(pzcActivityGroupVo.getActivityId()) == null) {
            Integer region = pzcActivityGroupVo.getRegion();
            PzcRegion pzcRegion = pzcRegionMapper.selectById(region);
            if (pzcRegion != null) {
                pzcActivityGroupVo.setActivityTitle("【" + pzcRegion.getName() + "】");
            }

        } else {
            pzcActivityGroupVo.setActivityTitle(pzcActivityMapper.selectVoById(pzcActivityGroupVo.getActivityId()).getTitle());
        }
        pzcActivityGroupVo.setAddress(pzcActivityGroupVo.getAddress().substring(pzcActivityGroupVo.getAddress().indexOf("】") + 1));


        return pzcActivityGroupVo;
    }

    public PzcUserVo getPrivateUser(PzcUserVo pzcUser)
    {
        PzcUserVo pzcUserVo = new PzcUserVo();
        pzcUserVo.setUserId(pzcUser.getUserId());
        pzcUserVo.setOpenid(pzcUser.getOpenid());
        pzcUserVo.setMoney(null);
        pzcUserVo.setUserLevel(pzcUser.getUserLevel());
        pzcUserVo.setIntegration(null);
        pzcUserVo.setIntegrationNow(null);
        pzcUserVo.setRealname(null);
        pzcUserVo.setNickname("匿名用户");
        pzcUserVo.setSex(null);
        pzcUserVo.setPhone(null);
        pzcUserVo.setAvatar("https://img.flya.top/mac/202309261521392.png");
        pzcUserVo.setAddress(null);
        pzcUserVo.setIntro(null);
        pzcUserVo.setAge(null);
        pzcUserVo.setConstellation(null);
        pzcUserVo.setMbti(null);
        pzcUserVo.setHobby(null);
        pzcUserVo.setSchool(null);
        pzcUserVo.setOccupation(null);
        pzcUserVo.setMusicStyle(null);
        pzcUserVo.setState(null);
        pzcUserVo.setExemptCancel(null);
        return pzcUserVo;
    }

    /**
     * 查询活动组队列表
     */
    @Override
    public TableDataInfo<PzcActivityGroupVo> queryPageList(PzcActivityGroupBo bo, PageQuery pageQuery) {

        log.info("查询活动组队列表 bo is {}", JsonUtils.toJsonString(bo));
        if(bo.getUserLevel()!=null)
        {
            pageQuery.setIsAsc("desc");
            pageQuery.setOrderByColumn("u.user_level");
        }


        Page<PzcActivityGroupVo> result = baseMapper.selectDetailsList(pageQuery.build(), bo);
        ArrayList<PzcActivityGroupVo> pzcActivityGroupVos = new ArrayList<>();
        Pattern pattern = Pattern.compile("【(.*?)】(.*?)");
        result.getRecords().forEach(
            pzcActivityGroupVo -> {
                pzcActivityGroupVos.add(pzcActivityGroupVo);
                if (pzcActivityGroupVo.getAuth() == 2) {
                    log.info("私密组队，返回用户信息的部分信息");
                    pzcActivityGroupVo.setUser(getPrivateUser(pzcActivityGroupVo.getUser()));
                }//如果是私密组队，不返回用户信息
                //查询当前组队 是否正在进行中 如果是 则 不进入组队大厅
                Long groupId = pzcActivityGroupVo.getGroupId();
                Long count = pzcActivityGroupApplyMapper.selectCount(new QueryWrapper<PzcActivityGroupApply>().eq("group_id", groupId).notIn("apply_status", -1, 0, 13, 14, 15));
                if (count > 0) {
                    if(bo.getUserId()==null) //只是不对大厅展示
                    {
                        //从列表中移除这个对象、
                        log.info("当前组队正在进行中，不返回组队信息 groupId is {}", pzcActivityGroupVo.getGroupId());
                        pzcActivityGroupVos.remove(pzcActivityGroupVo);
                    }

                }
                if (bo.getLongitudeAndLatitude() != null && !bo.getLongitudeAndLatitude().isEmpty()) {
                    //计算距离

                    String jwdFromAddress = pzcActivityGroupVo.getAddress();

                    Matcher matcher = pattern.matcher(jwdFromAddress);
                    if (matcher.find()) {
                        // 获取经纬度
                        log.info("从数据库地址经纬度 is {}", jwdFromAddress);
                        jwdFromAddress = matcher.group(1);
                        pzcActivityGroupVo.setAddress(pzcActivityGroupVo.getAddress().substring(pzcActivityGroupVo.getAddress().indexOf("】") + 1));
                    } else {
                        log.info("调用API获取经纬度 is {}", jwdFromAddress);
                        jwdFromAddress = gaoDeMapUtil.getLonLat(pzcActivityGroupVo.getAddress()).getData().toString(); //经纬度
                    }
                    String distance = gaoDeMapUtil.getDistance(bo.getLongitudeAndLatitude(), jwdFromAddress).getData().toString();
                    log.info("离我【{}】米", distance);
                    //计算距离
                    BigDecimal distanceStr = new BigDecimal(distance).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP); //保留两位小数
                    pzcActivityGroupVo.setDistance(distanceStr);
                }
            }
        );
        if (bo.getDistance() != null) {
//            log.info("按照距离排序前： {}",JSONUtil.toJsonPrettyStr(pzcActivityGroupVos));
            pzcActivityGroupVos.sort(Comparator.comparing(PzcActivityGroupVo::getDistance)); //按照距离远到近排序
//            log.info("按照距离排序后： {}",JSONUtil.toJsonPrettyStr(pzcActivityGroupVos));
        }

        //查询当前组队 是否正在进行中 如果是 则 不进入组队大厅
        return TableDataInfo.build(pzcActivityGroupVos);
    }

    /**
     * 查询活动组队列表
     */
    @Override
    public List<PzcActivityGroupVo> queryList(PzcActivityGroupBo bo) {
        LambdaQueryWrapper<PzcActivityGroup> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PzcActivityGroup> buildQueryWrapper(PzcActivityGroupBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PzcActivityGroup> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getActivityId() != null, PzcActivityGroup::getActivityId, bo.getActivityId());
        lqw.eq(bo.getUserId() != null, PzcActivityGroup::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), PzcActivityGroup::getTitle, bo.getTitle());
        lqw.eq(bo.getMoney() != null, PzcActivityGroup::getMoney, bo.getMoney());
        lqw.eq(bo.getGroupType() != null, PzcActivityGroup::getGroupType, bo.getGroupType());
        lqw.eq(StringUtils.isNotBlank(bo.getAddress()), PzcActivityGroup::getAddress, bo.getAddress());
        lqw.eq(bo.getActivityTime() != null, PzcActivityGroup::getActivityTime, bo.getActivityTime());
        lqw.eq(bo.getAuth() != null, PzcActivityGroup::getAuth, bo.getAuth());
        lqw.eq(bo.getCreateTime() != null, PzcActivityGroup::getCreateTime, bo.getCreateTime());
        lqw.eq(bo.getUpdateTime() != null, PzcActivityGroup::getUpdateTime, bo.getUpdateTime());
        return lqw;
    }

    /**
     * 新增活动组队
     */
    @Override
    public Boolean insertByBo(PzcActivityGroupBo bo) {
        PzcActivityGroup add = BeanUtil.toBean(bo, PzcActivityGroup.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setGroupId(add.getGroupId());
        }
        return flag;
    }

    /**
     * 修改活动组队
     */
    @Override
    public Boolean updateByBo(PzcActivityGroupBo bo) {
        PzcActivityGroup update = BeanUtil.toBean(bo, PzcActivityGroup.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PzcActivityGroup entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除活动组队
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public boolean checkActivity(Long activityId) {
        return pzcActivityMapper.selectVoById(activityId) != null;
    }

    @Override
    public boolean checkGroup(Long userId, Long activityId) {
        LambdaQueryWrapper<PzcActivityGroup> lqw = Wrappers.lambdaQuery();
        lqw.eq(PzcActivityGroup::getUserId, userId);
        lqw.eq(PzcActivityGroup::getActivityId, activityId);
        //
        return baseMapper.selectCount(lqw) > 0;
    }
}
