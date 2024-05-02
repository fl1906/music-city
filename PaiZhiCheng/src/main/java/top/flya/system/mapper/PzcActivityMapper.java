package top.flya.system.mapper;

import org.apache.ibatis.annotations.Param;
import top.flya.common.core.mapper.BaseMapperPlus;
import top.flya.system.domain.PzcActivity;
import top.flya.system.domain.vo.PzcActivityVo;

import java.util.List;

/**
 * 活动Mapper接口
 *
 * @author ruoyi
 * @date 2023-06-02
 */
public interface PzcActivityMapper extends BaseMapperPlus<PzcActivityMapper, PzcActivity, PzcActivityVo> {

    List<PzcActivity> selectActivityByActivityIds(@Param("activityIds") List<String> collect, @Param("classify") Integer type);
}
