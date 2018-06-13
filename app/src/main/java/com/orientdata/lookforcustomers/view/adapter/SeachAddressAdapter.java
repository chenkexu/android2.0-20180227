package com.orientdata.lookforcustomers.view.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.bean.AddressSearchRecode;

import java.util.List;

/**
 * Created by ckx on 2018/5/24.
 */

public class SeachAddressAdapter extends BaseQuickAdapter<AddressSearchRecode,BaseViewHolder> {
    private int type;

    public SeachAddressAdapter(@Nullable List<AddressSearchRecode> data) {
        super(R.layout.item_rv_search_address, data);
    }



    public void setDataType(int type){
        this.type = type;
    }



    @Override
    protected void convert(BaseViewHolder helper, AddressSearchRecode item) {
        if (item.getName().equals("") || item.getName() == null) {
            helper.setVisible(R.id.tv_address_name, false);
            helper.setImageResource(R.id.iv_icon_address, R.mipmap.ic_radio_button_checked);
        }else{
            helper.setVisible(R.id.tv_address_name, true);
            helper.setText(R.id.tv_address_name,item.getName());
            helper.setImageResource(R.id.iv_icon_address, R.mipmap.geo_fence);
        }

        if (item.getAddress().equals("") || item.getAddress() == null) {
            helper.setVisible(R.id.tv_address, false);
            helper.setImageResource(R.id.iv_icon_address, R.mipmap.ic_radio_button_checked);
        }else{
            helper.setVisible(R.id.tv_address, true);
            helper.setText(R.id.tv_address,item.getAddress());
            helper.setImageResource(R.id.iv_icon_address, R.mipmap.geo_fence);
        }

        if (type == 3) {
            helper.setVisible(R.id.iv_delete, true);
        }else{
            helper.setVisible(R.id.iv_delete, false);
        }


        helper.addOnClickListener(R.id.iv_delete);


    }

    public abstract static class AdapterItem<T> {
        private T source;

        public AdapterItem(T source) {
            this.source = source;
        }

        public abstract String getName();

        public abstract String getAddress();

        public abstract Double getLongitude();

        public abstract Double getLatitude();

        public T getSource() {
            return source;
        }
    }
}
