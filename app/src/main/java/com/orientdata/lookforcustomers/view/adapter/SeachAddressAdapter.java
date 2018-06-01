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


    public SeachAddressAdapter(@Nullable List<AddressSearchRecode> data) {
        super(R.layout.item_rv_search_address, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, AddressSearchRecode item) {

        if (item.getName().equals("")||item.getName()==null) {
            helper.setVisible(R.id.tv_address_name, false);
        }else{
            helper.setVisible(R.id.tv_address_name, true);
            helper.setText(R.id.tv_address_name,item.getName());
        }

        helper.setText(R.id.tv_address,item.getAddress());


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
