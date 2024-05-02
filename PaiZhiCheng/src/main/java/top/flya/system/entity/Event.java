package top.flya.system.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Event {
    @JsonProperty("one_way_exit_permits_flag")
    private int oneWayExitPermitsFlag;  // 单向出口许可标志

    @JsonProperty("sale_start_time")
    private long saleStartTime;  // 销售开始时间

    @JsonProperty("music_type")
    private int musicType;  // 音乐类型

    @JsonProperty("post_flag")
    private int postFlag;  // 发布标志

    @JsonProperty("logo")
    private String logo;  // 标志图片

    @JsonProperty("id")
    private int id;  // ID

    @JsonProperty("ticket_type")
    private int ticketType;  // 票务类型

    @JsonProperty("end_time")
    private String endTime;  // 结束时间

    @JsonProperty("email_flag")
    private int emailFlag;  // 电子邮件标志

    @JsonProperty("cover_image")
    private String coverImage;  // 封面图片

    @JsonProperty("state")
    private int state;  // 状态

    @JsonProperty("telephone")
    private String telephone;  // 电话号码

    @JsonProperty("passport_flag")
    private int passportFlag;  // 护照标志

    @JsonProperty("address")
    private String address;  // 地址

    @JsonProperty("miniapp_code_image")
    private String miniappCodeImage;  // 小程序码图片

    @JsonProperty("one_time_notify_flag")
    private int oneTimeNotifyFlag;  // 一次性通知标志

    @JsonProperty("recommend_tickets")
    private List<Object> recommendTickets;  // 推荐票务

    @JsonProperty("star_flag")
    private int starFlag;  // 星标标志

    @JsonProperty("merchant_id")
    private int merchantId;  // 商户ID

    @JsonProperty("notice_list")
    private List<String> noticeList;  // 通知列表

    @JsonProperty("artist_list")
    private List<Object> artistList;  // 艺术家列表

    @JsonProperty("withdraw_state")
    private int withdrawState;  // 提款状态

    @JsonProperty("intro_list")
    private List<Intro> introList;  // 简介列表

    @JsonProperty("type")
    private int type;  // 类型

    @JsonProperty("music_type_text")
    private String musicTypeText;  // 音乐类型文本

    @JsonProperty("ad_flag")
    private int adFlag;  // 广告标志

    @JsonProperty("end_date")
    private String endDate;  // 结束日期

    @JsonProperty("payment_method")
    private int paymentMethod;  // 支付方式

    @JsonProperty("id_card_flag")
    private int idCardFlag;  // 身份证标志

    @JsonProperty("payment_state")
    private int paymentState;  // 支付状态

    @JsonProperty("offline_payment_flag")
    private int offlinePaymentFlag;  // 线下支付标志

    @JsonProperty("name")
    private String name;  // 名称

    @JsonProperty("organizer")
    private Organizer organizer;
    @JsonProperty("longitude")
    private String longitude;  // 经度

    @JsonProperty("price")
    private List<Price> price;  // 价格列表

    @JsonProperty("start_date")
    private String startDate;  // 开始日期

    @JsonProperty("sub_state")
    private int subState;  // 子状态

    @JsonProperty("rec_flag")
    private int recFlag;  // 推荐标志

    @JsonProperty("start_time")
    private String startTime;  // 开始时间

    @JsonProperty("bytedance_code_image")
    private String bytedanceCodeImage;  // 字节跳动码图片

    @JsonProperty("city_id")
    private int cityId;  // 城市ID

    @JsonProperty("sale_end_time")
    private String innerImage;  // 活动详情主图

    @JsonProperty("check_code_image")
    private String checkCodeImage;  // 检查码图片

    @JsonProperty("show_time")
    private String showTime;  // 展示时间

    @JsonProperty("latitude")
    private String latitude;  // 纬度
}
