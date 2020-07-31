package com.lanslot.fastvideo.Bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Notice implements Serializable {
    private Long id;

    private String title;

    private String content;

    private Byte status;

    private Date createdTime;

    private Date updatedTime;

    private String createTimeStr;

    private String updatedTimeStr;

    private static final long serialVersionUID = 1L;
}