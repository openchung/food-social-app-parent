package com.dc.commons.constant;

import lombok.Getter;

@Getter
public enum RedisKeyConstant {

    verify_code("verify_code:", "驗證碼"),
    seckill_vouchers("seckill_vouchers:", "秒殺券的key"),
    lock_key("lockby:", "分布式鎖的key"),
    following("following:", "關注集合Key"),
    followers("followers:", "粉絲集合key"),
    following_feeds("following_feeds:", "我關注的好友的FeedsKey"),
    diner_points("diner:points", "diner用戶的積分Key"),
    diner_location("diner:location", "diner地理位置Key"),
    restaurants("restaurants:", "餐廳的Key"),
    restaurant_new_reviews("restaurant:new:reviews:", "餐廳評論Key"),
    ;

    private String key;
    private String desc;

    RedisKeyConstant(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

}