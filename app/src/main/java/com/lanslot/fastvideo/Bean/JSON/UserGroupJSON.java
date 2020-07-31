package com.lanslot.fastvideo.Bean.JSON;

import com.lanslot.fastvideo.Bean.User;

import java.util.ArrayList;

import lombok.Data;

@Data
public class UserGroupJSON extends BaseJSONBean {
    private ArrayList<User> datas;
}
