package com.orientdata.lookforcustomers.bean;

import java.util.List;

/**
 * Created by wy on 2017/12/19.
 * 佣金列表
 */

public class CommissionListBean extends ErrBean{
    private List<Bh> result;
    private int totalCount;
    private int totalPages;
    private boolean hasMore;

    public List<Bh> getResult() {
        return result;
    }

    public void setResult(List<Bh> result) {
        this.result = result;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}
