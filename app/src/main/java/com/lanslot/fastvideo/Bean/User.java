package com.lanslot.fastvideo.Bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {
    private Long id;

    private String mobile;

    private String password;

    private Integer level;

    private Long inviteUserId;

    private String inviteCode;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Date expireTime;

    private String inviteUrl;

    private static final long serialVersionUID = 1L;

}