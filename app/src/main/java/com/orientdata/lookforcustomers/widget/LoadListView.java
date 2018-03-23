package com.orientdata.lookforcustomers.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.orientdata.lookforcustomers.R;

import java.util.List;


/**
 * Created by wy on 2017/12/6.
 */
public class LoadListView extends ListView {

    private OnScrollListener onScrollListener = null;
    private PageEnableListener pageEnableListener = null;
    LoadingView loadListView = null;
    private boolean isLoading;
    private boolean hasMoreItem;
    private int lastVisibleItem;                                //最后一个可见的项
    private int totalItemCount;                                 //总的项数


    public LoadListView(Context context) {
        super(context);
        init();
    }

    public LoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setPageEnableListener(PageEnableListener pageEnableListener) {
        this.pageEnableListener = pageEnableListener;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public boolean isHasMoreItem() {
        return hasMoreItem;
    }

    public void setHasMoreItem(boolean hasMoreItem) {
        this.hasMoreItem = hasMoreItem;
        if (!this.hasMoreItem) {  //如果没有更多项，移除底部
            removeFooterView(loadListView);
        } else if (findViewById(R.id.loading_view) == null) {
            addFooterView(loadListView);
            ListAdapter adapter = ((HeaderViewListAdapter) getAdapter()).getWrappedAdapter();
            setAdapter(adapter);
        }
    }

    /**
     * 在下载任务完成之后去调用这个方法
     *
     * @param hasMoreItem 是否还有更多项
     * @param newItems    新的项
     */
    public void onFinsihLoading(boolean hasMoreItem, List<? extends Object> newItems) {
        setHasMoreItem(hasMoreItem);
        setIsLoading(false);  //下载任务完成后，把loading设置成false
        if (newItems != null && newItems.size() > 0) {
            ListAdapter adapter = ((HeaderViewListAdapter) getAdapter()).getWrappedAdapter();  //获取这个listview的adapter
            if (adapter instanceof PagingBaseAdapter) {
                ((PagingBaseAdapter) adapter).addMoreItems(newItems);  //添加项目，包含notify方法
            }
        }
    }

    /**
     * 初始化listview的操作
     */
    private void init() {
        isLoading = false;
        loadListView = new LoadingView(getContext());
        addFooterView(loadListView);
        super.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (onScrollListener != null) {
                    onScrollListener.onScrollStateChanged(absListView, scrollState);
                }
                /**
                 * 当你的listview移动到底部的时候，即你看到的最后一项等于总的项数，已经停止滚动 没有正在加载并且还有更多项
                 * 的时候会被执行
                 */
                if (lastVisibleItem == totalItemCount && scrollState == SCROLL_STATE_IDLE && !isLoading && hasMoreItem) {
                    if (pageEnableListener != null) {
                        isLoading = true;  //执行之后的状态就是loading
                        pageEnableListener.onLoadMoreItems();  //调用回调方法
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totleItem) {
                //Dispatch to child OnScrollListener
                if (onScrollListener != null) {
                    onScrollListener.onScroll(absListView, firstVisibleItem, visibleItemCount, totleItem);
                }
                lastVisibleItem = firstVisibleItem + visibleItemCount;  //最后看到的一项
                totalItemCount = totleItem;  //总的项数
            }
        });

    }

    @Override
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public interface PageEnableListener {
        public void onLoadMoreItems();
    }

}