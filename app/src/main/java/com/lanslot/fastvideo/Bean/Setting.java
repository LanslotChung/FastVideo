package com.lanslot.fastvideo.Bean;

import java.util.Date;

import lombok.Data;

@Data
public class Setting {
    private Long id;
    private String key;
    private String value;
    private String remark;
    private Date createTime;
    private Date updateTime;

    public enum Key {
        USER_RECHANG_URL,
        INTERFACE_MOVICE_URL,
        VIP_USER_MONTH,
        VIP_USER_OVER,
        USER_SHARE_URL,
        USER_INTEGRAL_PRIZE,
        USER_EXTEND_DAY,
        USER_RECHANG_WX_URL,
        USER_RECHANG_ZFB_URL
    }
}
