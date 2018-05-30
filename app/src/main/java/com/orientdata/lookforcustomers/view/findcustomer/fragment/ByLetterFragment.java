package com.orientdata.lookforcustomers.view.findcustomer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseFragment;
import com.orientdata.lookforcustomers.bean.Area;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.presenter.CityPickPresent;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.findcustomer.ICityPickView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by wy on 2017/11/16.
 * 城市选择- 按字母选择
 */

public class ByLetterFragment extends BaseFragment implements View.OnClickListener, ICityPickView {
    // TODO
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private List<AreaSortModel> SourceDateList;


    private ListView lv_fm_by_province_left;
    private ListView lv_fm_by_province_right;

    private CityPickPresent mCityPickPresent;


    private List<AreaOut> mAreaOuts;
    private ArrayAdapter<String> mLeftAdapter;
    private List<String> mProvinceNames;
    private List<Area> areas;


    private int provincePosition = -1;
    private int cityPosition = -1;
//    public static final String AREA_KEY = "Area";//存储获取的省市信息

    String name;
    String businessLicenseNo;
    String contactPerson;
    String phone;
    String contactCard;
    String cityCode;
    String provinceCode;
    String address;
    String province;
    String city;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_by_letter, container, false);
        initView(view);
//        initEvent();
        getProvinceCity();

        return view;
    }

    public static ByProvinceFragment newInstance() {
        Bundle args = new Bundle();
        ByProvinceFragment fragment = new ByProvinceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initView(View view) {
        mCityPickPresent = new CityPickPresent(this);
        sideBar = (SideBar) view.findViewById(R.id.sidrbar);
        dialog = (TextView) view.findViewById(R.id.dialog);
        sortListView = (ListView) view.findViewById(R.id.country_lvcountry);
        initDatas();
        initEvents();
//        setAdapter();
    }

    private void setAdapter() {
        SourceDateList = filledData(mAreaOuts);
        Collections.sort(SourceDateList, new PinyinComparator());
        adapter = new SortAdapter(getActivity(), SourceDateList);
        //sortListView.addHeaderView(initHeadView());
        sortListView.setAdapter(adapter);
    }

    private List<AreaSortModel> filledData(List<AreaOut> areaOuts) {
        if (areaOuts == null && areaOuts.size() <= 0) {
            return null;
        }
        List<AreaSortModel> mSortList = new ArrayList<AreaSortModel>();
        ArrayList<String> indexString = new ArrayList<String>();
        List<Area> areas = null;
        for (int i = 0; i < areaOuts.size(); i++) {
            areas = areaOuts.get(i).getList();
            if (areas != null && areas.size() > 0) {
                for (int j = 0; j < areas.size(); j++) {
                    AreaSortModel sortModel = new AreaSortModel();
                    Area area = areas.get(j);
                    // if (area.getStatus() == 1) {
                    sortModel.setArea(area);
                    String pinyin = PinyinUtils.getPingYin(area.getName());
                    String sortString = pinyin.substring(0, 1).toUpperCase();
                    if (sortString.matches("[A-Z]")) {
                        sortModel.setSortLetters(sortString.toUpperCase());
                        if (!indexString.contains(sortString)) {
                            indexString.add(sortString);
                        }
                    }
                    mSortList.add(sortModel);
                    //}

                }
            }
        }
        Collections.sort(indexString);
        sideBar.setIndexText(indexString);
        return mSortList;
    }

    private List<CitySortModel> filledData(String[] date) {
        List<CitySortModel> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();

        for (int i = 0; i < date.length; i++) {
            CitySortModel sortModel = new CitySortModel();
            sortModel.setName(date[i]);
            String pinyin = PinyinUtils.getPingYin(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }
            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
        sideBar.setIndexText(indexString);
        return mSortList;
    }

    private void initEvents() {
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position + 1);
                }
            }
        });

        //ListView的点击事件
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //mTvTitle.setText(((CitySortModel) adapter.getItem(position - 1)).getName());
                //Toast.makeText(getActivity().getApplication(), ((CitySortModel) adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
                Area area = ((AreaSortModel) adapter.getItem(position)).getArea();

                if (area.getStatus() == 0) {//未开通
                    ToastUtils.showShort(area.getName() + " 暂未开通业务！");
                    return;
                }
                ToastUtils.showShort("您选择了" + area.getName());
                Intent intent = new Intent();
                intent.putExtra("cityCode", area.getCode());
                intent.putExtra("cityName", area.getName());
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
            }
        });

    }

    private void initDatas() {
        sideBar.setTextView(dialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    /**
     * 获取省市列表
     */
    private void getProvinceCity() {
        showDefaultLoading();
        mCityPickPresent.getProvinceCityData();
    }


    @Override
    public void getProvinceCity(List<AreaOut> areaOuts) {
        mAreaOuts = areaOuts;
        setAdapter();
        hideDefaultLoading();
        //updateLeftView();
    }

    @Override
    public void addAddress(Object object) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
