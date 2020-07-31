package com.lanslot.fastvideo.DB;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import lombok.Data;

@Data
@Table(name = "UserInfo")
public class UserInfo {
    @Column(name = "id",isId = true,autoGen = false)
    private int id = 0;

    @Column(name = "mobile")
    private String mobile;


    @Column(name = "password")
    private String password;

    @Column(name = "token")
    private String token;
}
