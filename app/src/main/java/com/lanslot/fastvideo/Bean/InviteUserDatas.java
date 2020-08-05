
package com.lanslot.fastvideo.Bean;

import java.util.ArrayList;

import lombok.Data;

@Data
public class InviteUserDatas {
    private long endRow;
    private long firstPage;
    private Boolean hasNextPage;
    private Boolean hasPreviousPage;
    private Boolean isFirstPage;
    private Boolean isLastPage;
    private long lastPage;
    private ArrayList<InviteUser> inviteUser;
    private long navigateFirstPage;
    private long navigateLastPage;
    private long navigatePages;
    private ArrayList<Long> navigatepageNums;
    private long nextPage;
    private long pageNum;
    private long pageSize;
    private long pages;
    private long prePage;
    private long size;
    private long startRow;
    private long total;

}
