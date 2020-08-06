
package com.lanslot.fastvideo.Bean;

import java.util.Date;

import lombok.Data;

@Data
public class InviteUser {

    private long createTime;
    private String deviceInfo;
    private Date expireTime;
    private Long id;
    private String inviteCode;
    private String inviteUrl;
    private Long inviteUserId;
    private Integer level;
    private String mobile;
    private String password;
    private long status;
    private Date updateTime;

}
