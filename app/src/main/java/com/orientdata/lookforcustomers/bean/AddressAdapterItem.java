package com.orientdata.lookforcustomers.bean;
import com.orientdata.lookforcustomers.view.adapter.SeachAddressAdapter;

/**
 * Created by ckx on 2018/5/24.
 */

public class AddressAdapterItem extends SeachAddressAdapter.AdapterItem<AddressSearchRecode> {


    public AddressAdapterItem(AddressSearchRecode source) {
        super(source);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getAddress() {
        return null;
    }

    @Override
    public Double getLongitude() {
        return null;
    }

    @Override
    public Double getLatitude() {
        return null;
    }
}
