package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wy on 2017/11/20.
 * 寻客列表
 */

public class SearchListBean extends ErrBean implements Serializable {

    private List<Task> result;
    private int totalCount;
    private boolean hasMore;
    private int totalPages;

    public List<Task> getResult() {
        return result;
    }

    public void setResult(List<Task> result) {
        this.result = result;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
