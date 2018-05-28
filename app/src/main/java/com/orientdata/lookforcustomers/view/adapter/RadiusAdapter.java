package com.orientdata.lookforcustomers.view.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orientdata.lookforcustomers.R;

import java.util.List;

/**
 * Created by ckx on 2018/5/24.
 */

public class RadiusAdapter extends BaseQuickAdapter<String,BaseViewHolder> {


    public RadiusAdapter(@Nullable List<String> data) {
        super(R.layout.item_lv_radio, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {
       helper.setText(R.id.tv_radio,item);
    }


}
