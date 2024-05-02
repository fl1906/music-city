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
import top.flya.system.domain.PzcOrganizerTicket;
import top.flya.system.domain.bo.PzcOrganizerTicketBo;
import top.flya.system.domain.vo.PzcOrganizerTicketVo;
import top.flya.system.mapper.PzcOrganizerTicketMapper;
import top.flya.system.service.IPzcOrganizerTicketService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 主办方票务Service业务层处理
 *
 * @author ruoyi
 * @date 2023-07-22
 */
@RequiredArgsConstructor
@Service
public class PzcOrganizerTicketServiceImpl implements IPzcOrganizerTicketService {

    private final PzcOrganizerTicketMapper baseMapper;

    /**
     * 查询主办方票务
     */
    @Override
    public PzcOrganizerTicketVo queryById(Long organizerTicketId){
        return baseMapper.selectVoById(organizerTicketId);
    }

    /**
     * 查询主办方票务列表
     */
    @Override
    public TableDataInfo<PzcOrganizerTicketVo> queryPageList(PzcOrganizerTicketBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PzcOrganizerTicket> lqw = buildQueryWrapper(bo);
        Page<PzcOrganizerTicketVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询主办方票务列表
     */
    @Override
    public List<PzcOrganizerTicketVo> queryList(PzcOrganizerTicketBo bo) {
        LambdaQueryWrapper<PzcOrganizerTicket> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PzcOrganizerTicket> buildQueryWrapper(PzcOrganizerTicketBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PzcOrganizerTicket> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), PzcOrganizerTicket::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getQrImage()), PzcOrganizerTicket::getQrImage, bo.getQrImage());
        lqw.eq(StringUtils.isNotBlank(bo.getLogoImage()), PzcOrganizerTicket::getLogoImage, bo.getLogoImage());
        lqw.eq(bo.getOrganizerId() != null, PzcOrganizerTicket::getOrganizerId, bo.getOrganizerId());
        return lqw;
    }

    /**
     * 新增主办方票务
     */
    @Override
    public Boolean insertByBo(PzcOrganizerTicketBo bo) {
        PzcOrganizerTicket add = BeanUtil.toBean(bo, PzcOrganizerTicket.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setOrganizerTicketId(add.getOrganizerTicketId());
        }
        return flag;
    }

    /**
     * 修改主办方票务
     */
    @Override
    public Boolean updateByBo(PzcOrganizerTicketBo bo) {
        PzcOrganizerTicket update = BeanUtil.toBean(bo, PzcOrganizerTicket.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PzcOrganizerTicket entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除主办方票务
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
