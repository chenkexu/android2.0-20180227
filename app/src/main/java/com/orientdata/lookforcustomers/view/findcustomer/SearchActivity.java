package com.orientdata.lookforcustomers.view.findcustomer;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
import android.widget.LinearLayout;
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
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.adapter.SeachAddressAdapter;
import com.orientdata.lookforcustomers.widget.ClearableEditText;
import com.orientdata.lookforcustomers.widget.dialog.ConfirmDialog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static org.litepal.crud.DataSupport.findAll;


/**
 * Created by wy on 2017/11/25.
 * 搜索
 */

public class SearchActivity extends BaseActivity<IDirectionalSettingView, DirectionalSettingPresent<IDirectionalSettingView>> implements
        IDirectionalSettingView, View.OnClickListener, OnGetPoiSearchResultListener {

    @BindView(R.id.address_list)
    RecyclerView rvAddressList;
    @BindView(R.id.tv_clear_history)
    TextView tvClearHistory;
    @BindView(R.id.rl_title_search)
    LinearLayout rlTitleSearch;
    @BindView(R.id.rl_clear)
    RelativeLayout rlClear;
    @BindView(R.id.tv_history)
    TextView tvHistory;
    @BindView(R.id.tv_collection)
    TextView tvCollection;


    private ImageView right_btn;//关闭
    private ClearableEditText clearEdit;
    private ImageView rightText;//搜索
    private ImageView back_btn;//返回
    List<AddressSearchRecode> history;

    private PoiSearch mPoiSearch = null;
    private SeachAddressAdapter addressAdapter = null;
    private View view;


    @OnClick({R.id.tv_history, R.id.tv_collection, R.id.iv_back, R.id.tv_clear_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_history://历史记录
                clickHistory();
                // TODO: 2018/5/24 查看所有的历史记录
                history = DataSupport.limit(15).order("id desc").find(AddressSearchRecode.class);
                if (history.size() == 0 || history == null) {
                    addressAdapter.setNewData(null);
                    addressAdapter.setEmptyView(R.layout.layout_no_content, (ViewGroup) rvAddressList.getParent());
                } else {
                    updateAddressSearchResult(history, 2);
                }
                break;
            case R.id.tv_collection://收藏
                clickCollect();
                // TODO: 2018/5/30 请求接口
                mPresent.getAllAddress();
                break;
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.tv_clear_history://清空历史记录
                showDialog();
                break;
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //绑定activity
        ButterKnife.bind(this);
        clearEdit = findViewById(R.id.clearEdit);
        rightText = findViewById(R.id.rightText);
        rightText.setOnClickListener(this);

//        view = getLayoutInflater().inflate(R.layout.rv_footer_search, (ViewGroup) rvAddressList.getParent(), false);
        // POI搜索API
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        final List<AddressSearchRecode> addressSearchRecodes = new ArrayList<>();
        addressAdapter = new SeachAddressAdapter(addressSearchRecodes);
        rvAddressList.setItemAnimator(new DefaultItemAnimator());
        rvAddressList.setLayoutManager(new LinearLayoutManager(this));
        addressAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        addressAdapter.isFirstOnly(false);
        rvAddressList.setAdapter(addressAdapter);


        clickHistory();

        // TODO: 2018/5/24 查看所有的历史记录
        history = DataSupport.limit(15).order("id desc").find(AddressSearchRecode.class);
        updateAddressSearchResult(history, 2);

        addressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<AddressSearchRecode> addressInfo = addressAdapter.getData();
                AddressSearchRecode addressSearchRecode = addressInfo.get(position);
                //保存到数据库
                addressSearchRecode.saveOrUpdate("name=?", addressSearchRecode.getName());
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
                String s1 = s.toString();
                if (TextUtils.isEmpty(s1)) {
                    rlTitleSearch.setVisibility(View.VISIBLE);
                    updateAddressSearchResult(history, 2);
                } else {
                    rlTitleSearch.setVisibility(View.GONE);
                    addressAdapter.setNewData(null);
                    mPoiSearch.searchInCity((new PoiCitySearchOption()).city("北京").keyword(clearEdit.getText().toString()));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        addressAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<AddressSearchRecode> addressInfo = addressAdapter.getData();
                AddressSearchRecode addressSearchRecode = addressInfo.get(position);
                showDeleteDialog(addressSearchRecode);
            }
        });
    }







    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rightText://搜索
                if (TextUtils.isEmpty(clearEdit.getText().toString().trim())) {
                    ToastUtils.showShort("搜索内容不能为空");
                    return;
                }
                addressAdapter.setNewData(null);
                mPoiSearch.searchInCity((new PoiCitySearchOption()).city("北京").keyword(clearEdit.getText().toString()));
                break;

            case R.id.back_btn:
                finish();
                break;
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
                DataSupport.deleteAll(AddressSearchRecode.class);
                history = findAll(AddressSearchRecode.class);
                updateAddressSearchResult(history, 2);
                dialog.dismiss();
            }
        });
    }

    private void showDeleteDialog(final AddressSearchRecode addressSearchRecode) {
        final ConfirmDialog dialog = new ConfirmDialog(this, "您确定要取消收藏地址吗？");
        dialog.show();
        dialog.setClickListenerInterface(new ConfirmDialog.ClickListenerInterface() {
            @Override
            public void doCancel() {
                dialog.dismiss();
            }

            @Override
            public void doConfirm() {
                mPresent.appAddressDelete(addressSearchRecode.getAddressId());
                dialog.dismiss();
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
            rlClear.setVisibility(View.GONE);
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
            updateAddressSearchResult(results, 1);
            return;
        }
    }

    private void clickHistory(){
        tvHistory.setTextColor(getResources().getColor(R.color.text_gray_new));
        tvCollection.setTextColor(getResources().getColor(R.color.no_selec));
        Drawable img_history, img_collect;
        Resources res = getResources();
        img_history = res.getDrawable(R.mipmap.icon_history_sel);
        img_collect = res.getDrawable(R.mipmap.icon_collect);
        img_history.setBounds(0, 0, img_history.getMinimumWidth(), img_history.getMinimumHeight());

        img_collect.setBounds(0, 0, img_collect.getMinimumWidth(), img_collect.getMinimumHeight());

        tvHistory.setCompoundDrawables(img_history, null, null, null); //设置左图标
        tvCollection.setCompoundDrawables(img_collect, null, null, null); //设置左图标
    }


    private void clickCollect(){

        tvHistory.setTextColor(getResources().getColor(R.color.no_selec));
        tvCollection.setTextColor(getResources().getColor(R.color.text_gray_new));

        Drawable img_history, img_collect;
        Resources res = getResources();
        img_history = res.getDrawable(R.mipmap.icon_history);
        img_collect = res.getDrawable(R.mipmap.icon_collect_sel);
        img_history.setBounds(0, 0, img_history.getMinimumWidth(), img_history.getMinimumHeight());

        img_collect.setBounds(0, 0, img_collect.getMinimumWidth(), img_collect.getMinimumHeight());

        tvHistory.setCompoundDrawables(img_history, null, null, null); //设置左图标
        tvCollection.setCompoundDrawables(img_collect, null, null, null); //设置左图标
    }

    //更新搜索的结果
    private void updateAddressSearchResult(List<AddressSearchRecode> results, int type) {

        if (type == 2 && results.size() > 0) {
            rlClear.setVisibility(View.VISIBLE);
        } else {
            rlClear.setVisibility(View.GONE);
        }
        addressAdapter.setDataType(type);
        if (results.size() > 0) {
            Logger.d(results.get(0).getAddress());
            addressAdapter.setNewData(results);
        } else {
            addressAdapter.setNewData(null);
            addressAdapter.setEmptyView(R.layout.layout_no_content, (ViewGroup) rvAddressList.getParent());
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

        if (addressCollectInfo == null || addressCollectInfo.size() == 0) {
            rlClear.setVisibility(View.GONE);
            addressAdapter.setNewData(null);
            addressAdapter.setEmptyView(R.layout.layout_no_content, (ViewGroup) rvAddressList.getParent());
        } else {
            history.clear();
            for (AddressCollectInfo addressCollectInfo1 : addressCollectInfo) {
                AddressSearchRecode addressSearchRecode = new AddressSearchRecode(Double.parseDouble(addressCollectInfo1.getLongitude()),
                        Double.parseDouble(addressCollectInfo1.getLatitude()), "", addressCollectInfo1.getAddress(),addressCollectInfo1.getId());
                history.add(addressSearchRecode);
            }
            updateAddressSearchResult(history, 3);
        }
    }


    @Override
    public void AddAddressSucess() {

    }

    //添加收藏失败
    @Override
    public void AddAddressError() {

    }

    @Override
    public void deleteAddressSucess() {
        ToastUtils.showShort("删除成功");
        mPresent.getAllAddress();
    }

    @Override
    public void deleteAddressError() {
        ToastUtils.showShort("删除失败");
    }

    @Override
    protected DirectionalSettingPresent<IDirectionalSettingView> createPresent() {
        return new DirectionalSettingPresent<>(this);
    }
}
