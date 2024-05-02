package top.flya.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;
import top.flya.system.domain.PzcActivityGroup;
import top.flya.system.domain.bo.PzcActivityGroupBo;
import top.flya.system.domain.vo.PzcActivityGroupVo;
import top.flya.common.core.mapper.BaseMapperPlus;

/**
 * 活动组队Mapper接口
 *
 * @author ruoyi
 * @date 2023-07-10
 */
public interface PzcActivityGroupMapper extends BaseMapperPlus<PzcActivityGroupMapper, PzcActivityGroup, PzcActivityGroupVo> {

    Page<PzcActivityGroupVo> selectDetailsList(Page<Object> build,@Param("bo") PzcActivityGroupBo bo);

    PzcActivityGroupVo selectVoByIdDIY(@Param("groupId") Long groupId);
}
