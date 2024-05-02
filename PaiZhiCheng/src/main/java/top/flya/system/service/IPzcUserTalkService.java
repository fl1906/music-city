package top.flya.system.service;

import top.flya.system.domain.PzcUserTalk;
import top.flya.system.domain.vo.PzcUserTalkVo;
import top.flya.system.domain.bo.PzcUserTalkBo;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 用户聊天Service接口
 *
 * @author ruoyi
 * @date 2023-07-16
 */
public interface IPzcUserTalkService {

    /**
     * 查询用户聊天
     */
    PzcUserTalkVo queryById(Long talkId);

    /**
     * 查询用户聊天列表
     */
    TableDataInfo<PzcUserTalkVo> queryPageList(PzcUserTalkBo bo, PageQuery pageQuery);

    /**
     * 查询用户聊天列表
     */
    List<PzcUserTalkVo> queryList(PzcUserTalkBo bo);

    /**
     * 新增用户聊天
     */
    Boolean insertByBo(PzcUserTalkBo bo);

    /**
     * 修改用户聊天
     */
    Boolean updateByBo(PzcUserTalkBo bo);

    /**
     * 校验并批量删除用户聊天信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);


    TableDataInfo<PzcUserTalkVo> queryMyPageList(PzcUserTalkBo bo, PageQuery pageQuery);
}
