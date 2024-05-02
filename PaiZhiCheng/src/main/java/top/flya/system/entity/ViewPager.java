package top.flya.system.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pzc_view_pager") //轮播图
public class ViewPager extends FLBaseEntity {

    @TableId(value = "view_pager_id", type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private Integer viewPagerId; //轮播图id

    private String name; //轮播图名称

    private String imageUrl; //轮播图图片Url

    private String linkUrl; //轮播图链接Url

    private Integer activityId; //活动id 如果关联活动则不为空

}
