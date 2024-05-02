package top.flya.system.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("pzc_artist")
@EqualsAndHashCode(callSuper = true) //艺人
public class Artist extends FLBaseEntity{

    @TableId(value = "artist_id", type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private Integer artistId;  // ID

    private String name;  // 名称

    private String imageUrl;  // 图片

    private String description;  // 描述
}
