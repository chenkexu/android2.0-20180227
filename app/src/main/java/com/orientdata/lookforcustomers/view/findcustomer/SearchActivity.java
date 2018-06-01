package com.orientdata.lookforcustomers.view.findcustomer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.AddressCollectInfo;
import com.orientdata.lookforcustomers.bean.AddressSearchRecode;
import com.orientdata.lookforcustomers.bean.SettingOut;
import com.orientdata.lookforcustomers.presenter.DirectionalSettingPresent;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.adapter.SeachAddressAdapter;
import com.orientdata.lookforcustomers.widget.ClearableEditText;
import com.orientdata.lookforcustomers.widget.FluidLayout;
import com.orientdata.lookforcustomers.widget.dialog.ConfirmDialog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by wy on 2017/11/25.
 * 搜索
 */

public class SearchActivity extends BaseActivity<IDirectionalSettingView, DirectionalSettingPresent<IDirectionalSettingView>> implements
        IDirectionalSettingView, View.OnClickListener,OnGetPoiSearchResultListener {

    @BindView(R.id.address_list)
    RecyclerView rvAddressList;
    private RelativeLayout rlRemind;//搜索记录和删除按钮
    private ImageView ivDelete;
    private ImageView right_btn;//关闭
    private ClearableEditText clearEdit;
    private TextView rightText;//搜索
    private ImageView back_btn;//返回
    List<AddressSearchRecode> history;

    //    private FluidLayout flowlayout;
    int column = 0;
    private PoiSearch mPoiSearch = null;
    private SeachAddressAdapter addressAdapter = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //绑定activity
        ButterKnife.bind(this);
        rlRemind = findViewById(R.id.rlRemind);
        ivDelete = findViewById(R.id.ivDelete);
        clearEdit = findViewById(R.id.clearEdit);
        rightText = findViewById(R.id.rightText);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        rightText.setOnClickListener(this);
        // POI搜索API
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        List<AddressSearchRecode> addressSearchRecodes = new ArrayList<>();

        addressAdapter = new SeachAddressAdapter(addressSearchRecodes);
        rvAddressList.setItemAnimator(new DefaultItemAnimator());
        rvAddressList.setLayoutManager(new LinearLayoutManager(this));
        addressAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        addressAdapter.isFirstOnly(false);
        rvAddressList.setAdapter(addressAdapter);


        // TODO: 2018/5/24 查看所有的历史记录
        history = DataSupport.findAll(AddressSearchRecode.class);

        if (history == null || history.size() == 0 ) {
            addressAdapter.setNewData(null);
            addressAdapter.setEmptyView(R.layout.layout_no_content,(ViewGroup) rvAddressList.getParent());
        }else{
            updateAddressSearchResult(history);
        }


        addressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<AddressSearchRecode> addressInfo = addressAdapter.getData();
                AddressSearchRecode addressSearchRecode = addressInfo.get(position);
                //保存到数据库
                addressSearchRecode.saveOrUpdate("name=?",addressSearchRecode.getName());


                Intent intent = new Intent();
                intent.putExtra("searchValue", addressSearchRecode);
                setResult(RESULT_OK, intent);
                finish();
                Logger.d(addressInfo.get(position).getAddress());
            }
        });

        clearEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addressAdapter.setNewData(null);
                mPoiSearch.searchInCity((new PoiCitySearchOption()).city("北京").keyword(clearEdit.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    protected DirectionalSettingPresent<IDirectionalSettingView> createPresent() {
        return new DirectionalSettingPresent<>(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rightText://搜索
                if (TextUtils.isEmpty(clearEdit.getText().toString().trim())) {
                    ToastUtils.showShort("搜索内容不能为空");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("searchValue", clearEdit.getText().toString().trim());
                setResult(RESULT_OK, intent);
                finish();
                break;

            case R.id.back_btn:
                finish();
                break;
            case R.id.ivDelete:
                break;
        }
    }


    /**
     * 更新搜索历史列表
     */
    private void updateFluidView() {
        history = SharedPreferencesTool.getInstance().getDataList(SharedPreferencesTool.SEARCH_HISTORY);
        if (history == null || history.size() == 0) {
//            flowlayout.removeAllViews();
            ivDelete.setVisibility(View.GONE);
            return;
        }
        ivDelete.setVisibility(View.VISIBLE);
        for (int i = 0; i < history.size(); i++) {
            final TextView textView = new TextView(this);
            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(15, 15, 15, 15);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            //设置最大字数为10，多余的显示...
            textView.setMaxEms(10);
            textView.setSingleLine(true);
            textView.setPadding(15, 15, 15, 15);
            textView.setBackgroundResource(R.drawable.shape_bg_gray_round);

            final String his = history.get(i).getAddress();
            textView.setText(his);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showShort(his);
                    Intent intent = new Intent();
                    intent.putExtra("searchValue", his);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
//            flowlayout.addView(textView, params);
        }
    }


    private void showDialog() {
        final ConfirmDialog dialog = new ConfirmDialog(this, getString(R.string.confirm_remind));
        dialog.show();
        dialog.setClickListenerInterface(new ConfirmDialog.ClickListenerInterface() {
            @Override
            public void doCancel() {
                dialog.dismiss();
            }

            @Override
            public void doConfirm() {
                SharedPreferencesTool.getInstance().remove(SharedPreferencesTool.SEARCH_HISTORY);
                dialog.dismiss();
                updateFluidView();
            }
        });

    }


    @Override
    public void onGetPoiResult(PoiResult result) {
        List<AddressSearchRecode> results = new ArrayList<>();
        if (clearEdit.getText().toString().equals("")) {
            return;
        }
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            ToastUtils.showShort("未找到结果");
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            List<PoiInfo> poiInfos = result.getAllPoi();
            if (poiInfos == null) {
                return;
            }

            for (PoiInfo poiInfo : poiInfos) {
                if (poiInfo == null || poiInfo.location == null) {
                    continue;
                }
                AddressSearchRecode addressResult = new AddressSearchRecode(poiInfo);
                results.add(addressResult);
            }
            updateAddressSearchResult(results);
            return;
        }
    }


    //更新搜索的结果
    private void updateAddressSearchResult(List<AddressSearchRecode> results) {
        if (results.size() > 0) {
            Logger.d(results.get(0).getAddress());
            addressAdapter.setNewData(results);
        } else {
            ToastUtils.showShort("没有搜索结果");
        }
    }


    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            ToastUtils.showShort("抱歉，未找到结果");
        } else {
            ToastUtils.showShort(result.getName() + ": " + result.getAddress());
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @OnClick({R.id.tv_history, R.id.tv_collection})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_history://历史记录
                // TODO: 2018/5/24 查看所有的历史记录
                history = DataSupport.findAll(AddressSearchRecode.class);
                if (history.size() == 0 || history == null) {
                    addressAdapter.setEmptyView(R.layout.layout_no_content,(ViewGroup) rvAddressList.getParent());
                }else{
                    updateAddressSearchResult(history);
                }
                break;
            case R.id.tv_collection://收藏
                // TODO: 2018/5/30 请求接口
                mPresent.getAllAddress();
                break;
        }
    }


    private void get(){

    }



    @Override
    public void showLoading() {
        showDefaultLoading();
    }

    @Override
    public void hideLoading() {
        hideDefaultLoading();
    }

    @Override
    public void getSelectSetting(SettingOut settingOuts) {

    }

    @Override
    public void getAllCollectionAddress(List<AddressCollectInfo> addressCollectInfo) {
        if (addressCollectInfo == null || addressCollectInfo.size() == 0 ) {
            addressAdapter.setNewData(null);
            addressAdapter.setEmptyView(R.layout.layout_no_content,(ViewGroup) rvAddressList.getParent());
        }else{
            history.clear();
            for(AddressCollectInfo addressCollectInfo1:addressCollectInfo){
                AddressSearchRecode addressSearchRecode = new AddressSearchRecode(Double.parseDouble(addressCollectInfo1.getLongitude()),
                        Double.parseDouble(addressCollectInfo1.getLatitude()), "", addressCollectInfo1.getAddress());
                history.add(addressSearchRecode);
            }
            updateAddressSearchResult(history);
        }
    }

    @Override
    public void AddAddressSucess() {

    }

    //操作失败
    @Override
    public void AddAddressError() {

    }



}
