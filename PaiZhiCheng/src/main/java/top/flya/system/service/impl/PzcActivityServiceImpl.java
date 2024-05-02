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
import org.springframework.transaction.annotation.Transactional;
import top.flya.common.core.domain.PageQuery;
import top.flya.common.core.page.TableDataInfo;
import top.flya.system.common.BatchUtils;
import top.flya.system.domain.*;
import top.flya.system.domain.bo.PzcActivityBo;
import top.flya.system.domain.vo.PzcActivityVo;
import top.flya.system.mapper.*;
import top.flya.system.service.IPzcActivityService;
import top.flya.system.utils.gaode.GaoDeMapUtil;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 活动Service业务层处理
 *
 * @author ruoyi
 * @date 2023-06-02
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class PzcActivityServiceImpl implements IPzcActivityService {

    private final PzcActivityMapper baseMapper;

    private final PzcIntroMapper pzcIntroMapper;

    private final PzcActivityConnIntroMapper pzcActivityConnIntroMapper;

    private final PzcArtistMapper pzcArtistMapper;

    private final PzcActivityConnArtistMapper pzcActivityConnArtistMapper;

    private final PzcTagMapper pzcTagMapper;

    private final PzcActivityConnTagMapper pzcActivityConnTagMapper;

    private final PzcOrganizerMapper pzcOrganizerMapper;

    private final BatchUtils batchUtils;

    private final PzcOrganizerTicketMapper pzcOrganizerTicketMapper;

    private final GaoDeMapUtil gaoDeMapUtil;

    /**
     * 查询活动
     */
    @Override
    public PzcActivityVo queryById(Integer activityId) {
        PzcActivityVo pzcActivityVo = baseMapper.selectVoById(activityId);
        if(pzcActivityVo==null)
        {
            throw new RuntimeException("当前活动不存在或者已过期,请重新选择活动哦~");
        }
        List<PzcActivityVo> pzcActivityVos = new ArrayList<>();
        pzcActivityVos.add(pzcActivityVo);
        pzcActivityVos.forEach(r->{
            r.setInnerImage(pzcActivityVo.getInnerImage().contains("http")?pzcActivityVo.getInnerImage():batchUtils.getNewImageUrls(Collections.singletonList(pzcActivityVo.getInnerImage())).get(Long.parseLong(pzcActivityVo.getInnerImage())));
            r.setCoverImage(pzcActivityVo.getCoverImage().contains("http")?pzcActivityVo.getCoverImage():batchUtils.getNewImageUrls(Collections.singletonList(pzcActivityVo.getCoverImage())).get(Long.parseLong(pzcActivityVo.getCoverImage())));
            r.setShareImage(pzcActivityVo.getShareImage()==null?null:pzcActivityVo.getShareImage().contains("http")?pzcActivityVo.getShareImage():batchUtils.getNewImageUrls(Collections.singletonList(pzcActivityVo.getShareImage())).get(Long.parseLong(pzcActivityVo.getShareImage())));

            log.info("pzcActivityVo.getInnerImage() = {}",pzcActivityVo.getInnerImage());
            ArrayList<PzcIntro> introList = new ArrayList<>();
            ArrayList<PzcArtist> artistList = new ArrayList<>();
            ArrayList<PzcTag> tagList = new ArrayList<>();
            pzcActivityConnIntroMapper.selectList(Wrappers.<PzcActivityConnIntro>lambdaQuery().eq(PzcActivityConnIntro::getActivityId, r.getActivityId())).forEach(c->{
                PzcIntro pzcIntro = pzcIntroMapper.selectById(c.getIntroId());
                if(pzcIntro == null){
                    return;
                }
                if(StringUtils.isEmpty(pzcIntro.getImageFullUrl()))
                {
                    introList.add(pzcIntro);
                    return;
                }
                Map<Long, String> newImageUrls = batchUtils.getNewImageUrls(Collections.singletonList(pzcIntro.getImageFullUrl()));
                pzcIntro.setImageFullUrl(pzcIntro.getImageFullUrl().contains("http")?pzcIntro.getImageFullUrl():newImageUrls.get(Long.parseLong(pzcIntro.getImageFullUrl())));
                introList.add(pzcIntro);
            });

            //筛选introList 中元素 PzcIntro 的type==1
            r.setIntroList(introList.stream().filter(intro -> intro.getType() == 1).collect(Collectors.toList()));
            r.setStageList(introList.stream().filter(intro -> intro.getType() == 0).collect(Collectors.toList()));
            pzcActivityConnArtistMapper.selectList(Wrappers.<PzcActivityConnArtist>lambdaQuery().eq(PzcActivityConnArtist::getActivityId, r.getActivityId())).forEach(c->{
                PzcArtist pzcArtist = pzcArtistMapper.selectById(c.getArtistId());
                if(pzcArtist == null){
                    return;
                }
                if(StringUtils.isEmpty(pzcArtist.getImageUrl()))
                {
                    artistList.add(pzcArtist);
                    return;
                }
                Map<Long, String> newImageUrls = batchUtils.getNewImageUrls(Collections.singletonList(pzcArtist.getImageUrl()));
                pzcArtist.setImageUrl(pzcArtist.getImageUrl().contains("http")?pzcArtist.getImageUrl():newImageUrls.get(Long.parseLong(pzcArtist.getImageUrl())));
                artistList.add(pzcArtist);
            });
            r.setArtistList(artistList);
            pzcActivityConnTagMapper.selectVoList(Wrappers.<PzcActivityConnTag>lambdaQuery().eq(PzcActivityConnTag::getActivityId, r.getActivityId())).forEach(c->{
                PzcTag pzcTag = pzcTagMapper.selectById(c.getTagId());
                if(pzcTag == null){
                    return;
                }
                if(StringUtils.isEmpty(pzcTag.getImageUrl()))
                {
                    tagList.add(pzcTag);
                    return;
                }
                Map<Long, String> newImageUrls = batchUtils.getNewImageUrls(Collections.singletonList(pzcTag.getImageUrl()));
                pzcTag.setImageUrl(pzcTag.getImageUrl().contains("http")?pzcTag.getImageUrl():newImageUrls.get(Long.parseLong(pzcTag.getImageUrl())));
                tagList.add(pzcTag);
            });
            r.setTagList(tagList);
            PzcOrganizer pzcOrganizer = pzcOrganizerMapper.selectOne(Wrappers.<PzcOrganizer>lambdaQuery().eq(PzcOrganizer::getOrganizerId, r.getOrganizerId()));

            pzcOrganizer.setLogo(pzcOrganizer.getLogo().contains("http")?pzcOrganizer.getLogo():batchUtils.getNewImageUrls(Collections.singletonList(pzcOrganizer.getLogo())).get(Long.parseLong(pzcOrganizer.getLogo())));
            List<PzcOrganizerTicket> pzcOrganizerTickets = pzcOrganizerTicketMapper.selectList(Wrappers.<PzcOrganizerTicket>lambdaQuery().eq(PzcOrganizerTicket::getOrganizerId, pzcOrganizer.getOrganizerId()));
            pzcOrganizerTickets.forEach(
                p->{
                    p.setLogoImage(p.getLogoImage().contains("http")?p.getLogoImage():batchUtils.getNewImageUrls(Collections.singletonList(p.getLogoImage())).get(Long.parseLong(p.getLogoImage())));
                    p.setQrImage(p.getQrImage().contains("http")?p.getQrImage():batchUtils.getNewImageUrls(Collections.singletonList(p.getQrImage())).get(Long.parseLong(p.getQrImage())));
                }
            );
            pzcOrganizer.setOrganizerTickets(pzcOrganizerTickets);
            r.setOrganizerList(pzcOrganizer);

//            r.setDistance();
        });
        return pzcActivityVos.get(0);
    }

    /**
     * 查询活动列表
     */
    @Override
    public TableDataInfo<PzcActivityVo> queryPageList(PzcActivityBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PzcActivity> lqw = buildQueryWrapper(bo);
        Page<PzcActivityVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        result.setRecords(batchUtils.transformToPzcActivityVo(result.getRecords()));
        return TableDataInfo.build(result);
    }

    /**
     * 查询活动列表
     */
    @Override
    public TableDataInfo<PzcActivityVo> queryPageListWx(PzcActivityBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PzcActivity> lqw = buildQueryWrapper(bo);
        pageQuery.setIsAsc("asc");
        pageQuery.setOrderByColumn("start_time");
        lqw.ge(PzcActivity::getShowTime, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        Page<PzcActivityVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        result.setRecords(batchUtils.transformToPzcActivityVo(result.getRecords()));
        return TableDataInfo.build(result);
    }


    /**
     * 查询活动列表
     */
    @Override
    public List<PzcActivityVo> queryList(PzcActivityBo bo) {
        LambdaQueryWrapper<PzcActivity> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PzcActivity> buildQueryWrapper(PzcActivityBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PzcActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getAddress()), PzcActivity::getAddress, bo.getAddress());
        lqw.eq(bo.getRegionId() != null, PzcActivity::getRegionId, bo.getRegionId());
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), PzcActivity::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getStartTime()), PzcActivity::getStartTime, bo.getStartTime());
        lqw.eq(StringUtils.isNotBlank(bo.getEndDate()), PzcActivity::getEndDate, bo.getEndDate());
        lqw.eq(bo.getInnerImage() != null, PzcActivity::getInnerImage, bo.getInnerImage());
        lqw.eq(StringUtils.isNotBlank(bo.getShowTime()), PzcActivity::getShowTime, bo.getShowTime());
        lqw.eq(StringUtils.isNotBlank(bo.getCoverImage()), PzcActivity::getCoverImage, bo.getCoverImage());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            PzcActivity::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.between(params.get("beginUpdateTime") != null && params.get("endUpdateTime") != null,
            PzcActivity::getUpdateTime, params.get("beginUpdateTime"), params.get("endUpdateTime"));
        lqw.eq(bo.getState() != null, PzcActivity::getState, bo.getState());
       if(bo.getOrganizerList()!=null)
       {
           lqw.eq(bo.getOrganizerList().getOrganizerId() != null, PzcActivity::getOrganizerId, bo.getOrganizerList().getOrganizerId());
       }
       if(bo.getClassify()!=null)
       {
              lqw.eq(true, PzcActivity::getClassify, bo.getClassify());
       }
       lqw.eq(bo.getRegion()!=null, PzcActivity::getRegion, bo.getRegion());

       return lqw;
    }

    /**
     * 新增活动
     */
    @Override
    @Transactional
    public Boolean insertByBo(PzcActivityBo bo) {
        PzcActivity add = BeanUtil.toBean(bo, PzcActivity.class);
        if (bo.getActivityId() != null) {
            throw new RuntimeException("活动id在创建时不能填写");
        }
        log.info("新增活动 主办方Id为: {}", bo.getOrganizerList().getOrganizerId());
        if(bo.getOrganizerList().getOrganizerId()==null)
        {
           //就根据输入的内容来新建主办方
            PzcOrganizer organizer = new PzcOrganizer();
            organizer.setName(bo.getOrganizerList().getName());
            organizer.setPhone(bo.getOrganizerList().getPhone());
            organizer.setContent(bo.getOrganizerList().getContent());
            organizer.setLogo(bo.getOrganizerList().getLogo());
            pzcOrganizerMapper.insert(organizer);
            log.info("新增活动 新建主办方Id为: {}", organizer.getOrganizerId());
            bo.getOrganizerList().setOrganizerId(organizer.getOrganizerId());
        }
        add.setOrganizerId(bo.getOrganizerList().getOrganizerId());
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setActivityId(add.getActivityId());
        }
        saveActivityConfigs(bo);
        return flag;
    }


    @Transactional //这里关联其他表的保存
    public void saveActivityConfigs(PzcActivityBo bo)
    {

        if(bo.getOrganizerList()!=null) //主办方票务
        {
            List<PzcOrganizerTicket> organizerTickets = bo.getOrganizerList().getOrganizerTickets();
            if(organizerTickets!=null&&organizerTickets.size()!=0)
            {
                List<Integer> ids = new ArrayList<>();
               //校验PzcOrganizerTicket 的 "organizerId": 是否 是当前组织下的
                organizerTickets.forEach(o->{
                    if(o.getOrganizerTicketId()==null)
                    {
                        //新建
                        PzcOrganizerTicket pzcOrganizerTicket = new PzcOrganizerTicket();
                        pzcOrganizerTicket.setOrganizerId(bo.getOrganizerList().getOrganizerId());
                        pzcOrganizerTicket.setName(o.getName());
                        pzcOrganizerTicket.setLogoImage(o.getLogoImage());
                        pzcOrganizerTicket.setQrImage(o.getQrImage());

                        pzcOrganizerTicketMapper.insert(pzcOrganizerTicket);
                        o.setOrganizerTicketId(pzcOrganizerTicket.getOrganizerTicketId());
                        o.setOrganizerId(pzcOrganizerTicket.getOrganizerId());
                        ids.add(Math.toIntExact(o.getOrganizerTicketId()));
                        return;
                    }
                    ids.add(Math.toIntExact(o.getOrganizerTicketId()));
                    PzcOrganizerTicket pzcOrganizerTicket = pzcOrganizerTicketMapper.selectById(o.getOrganizerTicketId());
                    pzcOrganizerTicketMapper.update(o, Wrappers.<PzcOrganizerTicket>lambdaQuery().eq(PzcOrganizerTicket::getOrganizerTicketId,o.getOrganizerTicketId()));
                    if(!Objects.equals(bo.getOrganizerList().getOrganizerId(),pzcOrganizerTicket.getOrganizerId()))
                    {
                        log.info("bo.getOrganizerList().getOrganizerId() is {} , pzcOrganizerTicket.getOrganizerId() is {}",bo.getOrganizerList().getOrganizerId(),pzcOrganizerTicket.getOrganizerId());
                        throw new RuntimeException("票务组织者id不是当前组织者id");
                    }
                });
                //删除
                if(ids.size()>0)
                {
                    LambdaQueryWrapper<PzcOrganizerTicket> lqw = Wrappers.lambdaQuery();
                    lqw.eq(PzcOrganizerTicket::getOrganizerId,bo.getOrganizerList().getOrganizerId());
                    lqw.notIn(PzcOrganizerTicket::getOrganizerTicketId,ids);
                    pzcOrganizerTicketMapper.delete(lqw);
                }

            }
        }

        if(bo.getStageList().size()!=0) //舞台介绍
        {
            bo.getStageList().forEach(stage-> {
                if(stage.getIntroId()==null)
                {
                    //新建
                    PzcIntro pzcIntro = new PzcIntro();
                    pzcIntro.setContent(stage.getContent());
                    pzcIntro.setImageFullUrl(stage.getImageFullUrl());
                    pzcIntro.setType(stage.getType());
                    pzcIntro.setTitle(stage.getTitle());
                    pzcIntroMapper.insert(pzcIntro);

                    stage.setIntroId(pzcIntro.getIntroId());
                }

                //首先查询这个介绍是否存在
                LambdaQueryWrapper<PzcIntro> lqw = Wrappers.lambdaQuery();
                lqw.eq(PzcIntro::getIntroId, stage.getIntroId());
                PzcIntro pzcIntro = pzcIntroMapper.selectOne(lqw);
                if (pzcIntro == null) {
                    throw new RuntimeException("舞台介绍不存在 id is " + stage.getIntroId());
                }
                LambdaQueryWrapper<PzcActivityConnIntro> lqw3 = Wrappers.lambdaQuery();
                lqw3.eq(PzcActivityConnIntro::getActivityId, bo.getActivityId());
                List<PzcActivityConnIntro> pzcActivityConnIntros = pzcActivityConnIntroMapper.selectList(lqw3);
                if(pzcActivityConnIntros.size()!=0)
                {
                    pzcActivityConnIntros.forEach(
                        p->{
                            Integer introId = p.getIntroId();
                            PzcIntro pzcIntro1 = pzcIntroMapper.selectById(introId);
                            if(pzcIntro1==null)
                            {
                                return;
                            }
                            if(pzcIntro1.getType()==0)
                            {
                                pzcActivityConnIntroMapper.delete(new QueryWrapper<PzcActivityConnIntro>().eq("intro_id",introId).eq("activity_id",bo.getActivityId())); //删除之后重新添加
                            }
                        }
                    );
                }

                //介绍表关联活动表 先查询关联关系是否存在
                LambdaQueryWrapper<PzcActivityConnIntro> lqw2 = Wrappers.lambdaQuery();
                lqw2.eq(PzcActivityConnIntro::getActivityId, bo.getActivityId());
                lqw2.eq(PzcActivityConnIntro::getIntroId, stage.getIntroId());
                PzcActivityConnIntro pzcActivityConnIntro1 = pzcActivityConnIntroMapper.selectOne(lqw2);
                if (pzcActivityConnIntro1 != null) {
                    return;
                }



                PzcActivityConnIntro pzcActivityConnIntro = new PzcActivityConnIntro();
                pzcActivityConnIntro.setActivityId(bo.getActivityId());
                pzcActivityConnIntro.setIntroId(Math.toIntExact(stage.getIntroId()));
                pzcActivityConnIntroMapper.insert(pzcActivityConnIntro);
            });
        }

        if (bo.getIntroList().size() != 0) { //介绍
            bo.getIntroList().forEach(intro -> {

                if(intro.getIntroId()==null)
                {
                    //新建
                    PzcIntro pzcIntro = new PzcIntro();
                    pzcIntro.setContent(intro.getContent());
                    pzcIntro.setImageFullUrl(intro.getImageFullUrl());
                    pzcIntro.setType(intro.getType());
                    pzcIntro.setTitle(intro.getTitle());
                    pzcIntroMapper.insert(pzcIntro);

                    intro.setIntroId(pzcIntro.getIntroId());
                }


                //首先查询这个介绍是否存在
                LambdaQueryWrapper<PzcIntro> lqw = Wrappers.lambdaQuery();
                lqw.eq(PzcIntro::getIntroId, intro.getIntroId());
                PzcIntro pzcIntro = pzcIntroMapper.selectOne(lqw);
                if (pzcIntro == null) {
                    throw new RuntimeException("介绍不存在 id is " + intro.getIntroId());
                }


                LambdaQueryWrapper<PzcActivityConnIntro> lqw3 = Wrappers.lambdaQuery();
                lqw3.eq(PzcActivityConnIntro::getActivityId, bo.getActivityId());
                List<PzcActivityConnIntro> pzcActivityConnIntros = pzcActivityConnIntroMapper.selectList(lqw3);
                if(pzcActivityConnIntros.size()!=0)
                {
                    pzcActivityConnIntros.forEach(
                        p->{
                            Integer introId = p.getIntroId();
                            PzcIntro pzcIntro1 = pzcIntroMapper.selectById(introId);
                            if(pzcIntro1==null)
                            {
                                return;
                            }
                            if(pzcIntro1.getType()==1)
                            {
                                pzcActivityConnIntroMapper.delete(new QueryWrapper<PzcActivityConnIntro>().eq("intro_id",introId).eq("activity_id",bo.getActivityId())); //删除之后重新添加
                            }
                        }
                    );
                }

                //介绍表关联活动表 先查询关联关系是否存在
                LambdaQueryWrapper<PzcActivityConnIntro> lqw2 = Wrappers.lambdaQuery();
                lqw2.eq(PzcActivityConnIntro::getActivityId, bo.getActivityId());
                lqw2.eq(PzcActivityConnIntro::getIntroId, intro.getIntroId());
                PzcActivityConnIntro pzcActivityConnIntro1 = pzcActivityConnIntroMapper.selectOne(lqw2);
                if (pzcActivityConnIntro1 != null) {
                    return;
                }


                PzcActivityConnIntro pzcActivityConnIntro = new PzcActivityConnIntro();
                pzcActivityConnIntro.setActivityId(bo.getActivityId());
                pzcActivityConnIntro.setIntroId(Math.toIntExact(intro.getIntroId()));
                pzcActivityConnIntroMapper.insert(pzcActivityConnIntro);

            });

        }
        if (bo.getArtistList().size() != 0) { //艺人
            List<Integer> artistIds = new ArrayList<>();
            bo.getArtistList().forEach(artist -> {
                artistIds.add(Math.toIntExact(artist.getArtistId()));
                if(artist.getArtistId()==null)
                {
                    //新建
                    PzcArtist pzcArtist = new PzcArtist();

                    pzcArtist.setName(artist.getName());
                    pzcArtist.setImageUrl(artist.getImageUrl());
                    pzcArtist.setDescription(artist.getDescription());
                    pzcArtistMapper.insert(pzcArtist);

                    artist.setArtistId(pzcArtist.getArtistId());
                }
                //首先查询这个艺人是否存在
                LambdaQueryWrapper<PzcArtist> lqw = Wrappers.lambdaQuery();
                lqw.eq(PzcArtist::getArtistId, artist.getArtistId());
                PzcArtist pzcArtist = pzcArtistMapper.selectOne(lqw);
                if (pzcArtist == null) {
                    throw new RuntimeException("艺术家不存在 id is " + artist.getArtistId());
                }
                //介绍表关联活动表 先查询关联关系是否存在
                LambdaQueryWrapper<PzcActivityConnArtist> lqw2 = Wrappers.lambdaQuery();
                lqw2.eq(PzcActivityConnArtist::getActivityId, bo.getActivityId());
                lqw2.eq(PzcActivityConnArtist::getArtistId, artist.getArtistId());
                PzcActivityConnArtist pzcActivityConnArtist1 = pzcActivityConnArtistMapper.selectOne(lqw2);
                if (pzcActivityConnArtist1 != null) {
                    return;
                }
                PzcActivityConnArtist pzcActivityConnArtist = new PzcActivityConnArtist();
                pzcActivityConnArtist.setActivityId(bo.getActivityId());
                pzcActivityConnArtist.setArtistId(Math.toIntExact(artist.getArtistId()));
                pzcActivityConnArtistMapper.insert(pzcActivityConnArtist);
            });

            if(artistIds.size()>0)
            {
                //这里删除多余的
                LambdaQueryWrapper<PzcActivityConnArtist> lqw3 = Wrappers.lambdaQuery();
                lqw3.eq(PzcActivityConnArtist::getActivityId, bo.getActivityId());
                lqw3.notIn(PzcActivityConnArtist::getArtistId, artistIds);
                pzcActivityConnArtistMapper.delete(lqw3);
            }


        }
        if (bo.getTagList().size() != 0) { //标签
            List<Integer> tagIds = new ArrayList<>();
            bo.getTagList().forEach(tag -> {
                tagIds.add(Math.toIntExact(tag.getTagId()));
                if(tag.getTagId()==null)
                {
                    //新建
                    PzcTag pzcTag = new PzcTag();
                    pzcTag.setName(tag.getName());
                    pzcTag.setImageUrl(tag.getImageUrl());
                    pzcTagMapper.insert(pzcTag);

                    tag.setTagId(pzcTag.getTagId());
                }

                //首先查询这个标签是否存在
                LambdaQueryWrapper<PzcTag> lqw = Wrappers.lambdaQuery();
                lqw.eq(PzcTag::getTagId, tag.getTagId());
                PzcTag pzcTag = pzcTagMapper.selectOne(lqw);
                if (pzcTag == null) {
                    throw new RuntimeException("标签不存在 id is " + tag.getTagId());
                }
                //介绍表关联活动表 先查询关联关系是否存在
                LambdaQueryWrapper<PzcActivityConnTag> lqw2 = Wrappers.lambdaQuery();
                lqw2.eq(PzcActivityConnTag::getActivityId, bo.getActivityId());
                lqw2.eq(PzcActivityConnTag::getTagId, tag.getTagId());
                PzcActivityConnTag pzcActivityConnTag1 = pzcActivityConnTagMapper.selectOne(lqw2);
                if (pzcActivityConnTag1 != null) {
//                    throw new RuntimeException("标签已经关联 id is " + tag.getTagId() + "无需重复关联");
                    return;
                }

                PzcActivityConnTag pzcActivityConnTag = new PzcActivityConnTag();
                pzcActivityConnTag.setActivityId(bo.getActivityId());
                pzcActivityConnTag.setTagId(Math.toIntExact(tag.getTagId()));
                pzcActivityConnTagMapper.insert(pzcActivityConnTag);

            });
            if(tagIds.size()>0)
            {
                //这里删除多余的
                LambdaQueryWrapper<PzcActivityConnTag> lqw3 = Wrappers.lambdaQuery();
                lqw3.eq(PzcActivityConnTag::getActivityId, bo.getActivityId());
                lqw3.notIn(PzcActivityConnTag::getTagId, tagIds);
                pzcActivityConnTagMapper.delete(lqw3);
            }


        }
    }

    /**
     * 修改活动
     */
    @Override
    @Transactional
    public Boolean updateByBo(PzcActivityBo bo) {
        PzcActivity update = BeanUtil.toBean(bo, PzcActivity.class);
        update.setOrganizerId(bo.getOrganizerList().getOrganizerId());

        validEntityBeforeSave(update);

        boolean flag = baseMapper.updateById(update) > 0;
        saveActivityConfigs(bo);
        return flag;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PzcActivity entity) {
        log.info("数据校验开始entity.getOrganizerId is {}", entity.getOrganizerId());
        //TODO 做一些数据校验,如唯一约束
        if (entity.getOrganizerId() != null) {
            //首先查询这个组织是否存在
            LambdaQueryWrapper<PzcOrganizer> lqw = Wrappers.lambdaQuery();
            lqw.eq(PzcOrganizer::getOrganizerId, entity.getOrganizerId());
            PzcOrganizer pzcOrganizer = pzcOrganizerMapper.selectOne(lqw);
            if (pzcOrganizer == null) {
                throw new RuntimeException("活动组织者不存在 id is " + entity.getOrganizerId());
            }

        }
    }

    /**
     * 批量删除活动
     */
    @Override
    @Transactional
    public Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        //删除活动与其他表的关联关系
            LambdaQueryWrapper<PzcActivityConnIntro> lqw = Wrappers.lambdaQuery();
            lqw.in(PzcActivityConnIntro::getActivityId, ids);
            pzcActivityConnIntroMapper.delete(lqw);

            LambdaQueryWrapper<PzcActivityConnArtist> lqw2 = Wrappers.lambdaQuery();
            lqw2.in(PzcActivityConnArtist::getActivityId, ids);
            pzcActivityConnArtistMapper.delete(lqw2);

            LambdaQueryWrapper<PzcActivityConnTag> lqw3 = Wrappers.lambdaQuery();
            lqw3.in(PzcActivityConnTag::getActivityId, ids);
            pzcActivityConnTagMapper.delete(lqw3);
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
