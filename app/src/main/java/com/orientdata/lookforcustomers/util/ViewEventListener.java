package com.orientdata.lookforcustomers.util;

import android.view.View;

public interface ViewEventListener<T> {
   void onViewEvent(int actionId, T item, int position, View view);
}