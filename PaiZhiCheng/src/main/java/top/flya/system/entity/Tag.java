package top.flya.system.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true) //活动标签
@TableName("pzc_tag")
public class Tag extends FLBaseEntity {

    @TableId(value = "tag_id", type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private Integer tagId;  // ID

    private String name;  // 名称

    private String imageUrl;  // 图片

}
