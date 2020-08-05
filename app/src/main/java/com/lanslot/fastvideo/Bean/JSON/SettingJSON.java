package com.lanslot.fastvideo.Bean.JSON;

import com.lanslot.fastvideo.Bean.Setting;

import java.util.ArrayList;

import lombok.Data;

@Data
public class SettingJSON extends BaseJSONBean {
    private ArrayList<Setting> datas;
}
