package com.orientdata.lookforcustomers.widget;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wy on 2017/12/6.
 */
public abstract class PagingBaseAdapter<T> extends BaseAdapter {

    protected List<T> items = null;
    protected Context context;

    public PagingBaseAdapter(List<T> items,Context context) {
        this.items = items;
        this.context = context;
    }

    public PagingBaseAdapter() {
        this.items = new ArrayList<>();
    }

    public void addMoreItems(List<T> items) {
        this.items.addAll(items);  //把新的项添加到listview里面
        notifyDataSetChanged();  //更新布局
    }

}
