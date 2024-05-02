package top.flya.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.flya.common.core.domain.BaseEntity;


/**
 * 活动介绍对象 pzc_intro
 *
 * @author ruoyi
 * @date 2023-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pzc_intro")
public class PzcIntro extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "intro_id",type = IdType.AUTO)
    private Long introId;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 0 场地舞台介绍 1 更多介绍
     */
    private Long type;
    /**
     * 图片
     */
    private String imageFullUrl;
    /**
     * 删除状态，默认为1表示正常状态
     */
    private Integer state;

}
