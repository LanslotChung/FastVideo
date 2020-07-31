package com.lanslot.fastvideo.Bean;

import java.util.ArrayList;

import lombok.Data;

@Data
public class IndexInfo {
    private ArrayList<Notice> noticeList;
    private ArrayList<ImageRotation> imageRotationList;
    private ArrayList<MovieChannel> movieChannelList;
}
