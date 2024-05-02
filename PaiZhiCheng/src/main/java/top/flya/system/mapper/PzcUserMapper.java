package top.flya.system.mapper;

import org.apache.ibatis.annotations.Param;
import top.flya.common.core.mapper.BaseMapperPlus;
import top.flya.system.domain.PzcUser;
import top.flya.system.domain.bo.UpdateMoneyBo;
import top.flya.system.domain.vo.PzcUserVo;

/**
 * 用户Mapper接口
 *
 * @author ruoyi
 * @date 2023-07-09
 */
public interface PzcUserMapper extends BaseMapperPlus<PzcUserMapper, PzcUser, PzcUserVo> {

    int updateMoney(@Param("bo") UpdateMoneyBo bo);
}
