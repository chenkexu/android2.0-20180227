package com.orientdata.lookforcustomers.base;


import com.orientdata.lookforcustomers.util.ViewEventListener;

public interface IViewHolder<T> {

    void setViewEventListener(ViewEventListener<T> viewEventListener);

    void setItem(T item);

    void setPosition(int position);
}
