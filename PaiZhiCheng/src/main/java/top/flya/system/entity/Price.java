package top.flya.system.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Price {
    @JsonProperty("stock_num")
    private int stockNum;  // 库存数量

    @JsonProperty("ticket_id")
    private int ticketId;  // 票务ID

    @JsonProperty("id")
    private int id;  // ID

    @JsonProperty("date_name")
    private String dateName;  // 日期名称

    @JsonProperty("price")
    private String price;  // 价格

    @JsonProperty("date")
    private List<Object> date;  // 日期列表

    @JsonProperty("limit")
    private int limit;  // 限制

    @JsonProperty("type")
    private int type;  // 类型

    @JsonProperty("num")
    private int num;  // 数量

    @JsonProperty("name")
    private String name;  // 名称

    @JsonProperty("sold_out_flag")
    private int soldOutFlag;  // 售罄标志

}
