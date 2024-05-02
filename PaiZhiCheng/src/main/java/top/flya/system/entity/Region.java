package top.flya.system.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pzc_region") //地区
public class Region extends FLBaseEntity {

    @TableId(value = "regionId", type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private Integer regionId; //地区id

    private String name; //地区名称

}
