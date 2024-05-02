package top.flya.system.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import top.flya.common.core.mapper.BaseMapperPlus;
import top.flya.system.domain.PzcUserTalk;
import top.flya.system.domain.bo.PzcUserTalkBo;
import top.flya.system.domain.vo.PzcUserTalkVo;

import java.util.List;

/**
 * 用户聊天Mapper接口
 *
 * @author ruoyi
 * @date 2023-07-16
 */
public interface PzcUserTalkMapper extends BaseMapperPlus<PzcUserTalkMapper, PzcUserTalk, PzcUserTalkVo> {

    PzcUserTalkVo selectVoPageV1 (@Param("otherUser")Long otherUser, @Param("userId") Long myUserId);

    Integer selectNotReadCount(@Param("fromUserId") Long userId, @Param("toUserId") Long toUserId,@Param("userId")Long myUserId);

    Page<PzcUserTalkVo> selectVoPageV2(Page<Object> build, @Param("bo") PzcUserTalkBo bo);

    List<Long> selectMyTalkUserIds(@Param("my") Long userId);

    List<Long> selectMyTalkUserIdsV2(@Param("my") Long userId);
}
