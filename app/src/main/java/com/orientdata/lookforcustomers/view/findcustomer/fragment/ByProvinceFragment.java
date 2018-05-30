package com.orientdata.lookforcustomers.view.findcustomer.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseFragment;
import com.orientdata.lookforcustomers.bean.Area;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.presenter.CityPickPresent;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.findcustomer.ICityPickView;
import com.orientdata.lookforcustomers.widget.abslistview.CommonAdapter;
import com.orientdata.lookforcustomers.widget.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by wy on 2017/11/16.
 * 按照省选择
 */

public class ByProvinceFragment extends BaseFragment implements View.OnClickListener, ICityPickView {

    private int currentItem;

    private ListView lv_fm_by_province_left;
    private ListView lv_fm_by_province_right;

    private CityPickPresent mCityPickPresent;


    private List<AreaOut> mAreaOuts;

//    private ArrayAdapter<String> mLeftAdapter;
    private LeftAdapter mLeftAdapter;


    private List<String> mProvinceNames;
    List<CityListItemMode> mcityNames;
    private MyRightListViewAdapter mMyRightListViewAdapter;
    private List<Area> areas;


    private int provincePosition = -1;
    private int cityPosition = -1;
    private String mProvinceName;
    //    public static final String AREA_KEY = "Area";//存储获取的省市信息


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_by_province, container, false);
        initView(view);
//        initEvent();
        getProvinceCity();

        return view;
    }


    private void updateLeftView() {
        if (mAreaOuts != null) {
            if (mProvinceNames == null) {
                mProvinceNames = new ArrayList<>();
            } else {
                mProvinceNames.clear();
            }

            for (int i = 0; i < mAreaOuts.size(); i++) {
                mProvinceNames.add(mAreaOuts.get(i).getName());
            }
            if (mLeftAdapter == null) {
                mLeftAdapter = new LeftAdapter(getActivity(), R.layout.fragment_by_province_left_item, mProvinceNames);
//                mLeftAdapter = new ArrayAdapter<String>(getActivity(), R.layout.fragment_by_province_left_item, mProvinceNames);
                lv_fm_by_province_left.setAdapter(mLeftAdapter);
            } else {
                //mLeftAdapter = (ArrayAdapter) lv_fm_by_province_left.getAdapter();
                mLeftAdapter.notifyDataSetChanged();
            }
            lv_fm_by_province_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    currentItem = position;
                    mLeftAdapter.notifyDataSetChanged();
                    updateRightView(parent, view, position);
                    mProvinceName = mProvinceNames.get(position);
                }
            });
//            lv_fm_by_province_left.setAdapter(mLeftAdapter);
        }

    }

    private void updateRightView(AdapterView<?> parent, View view, int position) {
        if (mAreaOuts != null && mAreaOuts.size() > 0) {
            AreaOut areaout = mAreaOuts.get(position);
            List<Area> citys = areaout.getList();
            if (citys == null) {
                return;
            }
            if (mcityNames == null) {
                mcityNames = new ArrayList<>();
            } else {
                mcityNames.clear();
            }

            for (int i = 0; i < citys.size(); i++) {
                if (i == citys.size() - 1) {//List<CityListItemMode> mcityNames;
                    mcityNames.add(new CityListItemMode(citys.get(i)));
                } else {
                    mcityNames.add(new CityListItemMode(citys.get(i), citys.get(i + 1)));
                    i++;
                }
            }
            //TODO
            if (mMyRightListViewAdapter == null) {
                mMyRightListViewAdapter = new MyRightListViewAdapter(getActivity());
                lv_fm_by_province_right.setAdapter(mMyRightListViewAdapter);
            } else {
                mMyRightListViewAdapter.notifyDataSetChanged();
            }

        }
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
        lv_fm_by_province_left = view.findViewById(R.id.lv_fm_by_province_left);
        lv_fm_by_province_right = view.findViewById(R.id.lv_fm_by_province_right);
        lv_fm_by_province_right.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivChooseProvince:
            case R.id.tvChooseProvince:
                //showProvinceWheel(areaOuts);
                break;
            case R.id.ivChooseCity:
            case R.id.tvChooseCity:
                if (provincePosition == -1) {
                    ToastUtils.showShort("请先选择省");
                } else {
                    // showCityWheel();
                }
                break;
            case R.id.btNext:
                //next();
                break;
        }
    }

    /**
     * 获取省市列表
     */
    private void getProvinceCity() {
        mCityPickPresent.getProvinceCityData();
    }


    @Override
    public void getProvinceCity(List<AreaOut> areaOuts) {
        mAreaOuts = areaOuts;
        updateLeftView();
        updateRightView(null, null, 0);
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


    private class CityListItemMode {

        private Area area1;
        private Area area2;

        public CityListItemMode(Area area1, Area area2) {
            this.area1 = area1;
            this.area2 = area2;
        }

        public CityListItemMode(Area area1) {
            this.area1 = area1;
        }

        public Area getArea1() {
            return area1;
        }

        public Area getArea2() {
            return area2;
        }
    }


    //左边Adapter
    class LeftAdapter extends CommonAdapter<String>{

        public LeftAdapter(Context context, int layoutId, List<String> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder viewHolder, String item, int position) {
            TextView textView = viewHolder.getView(R.id.tv_fm_by_province_left_item_id);
            if (position == currentItem) {
                textView.setTextColor(getResources().getColor(R.color.colorPrimary));
            }else {
                textView.setTextColor(getResources().getColor(R.color.text_gray));
            }
            viewHolder.setText(R.id.tv_fm_by_province_left_item_id, item);
        }
    }



    class MyRightListViewAdapter extends BaseAdapter {

        private Context context = null;

        public MyRightListViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return mcityNames.size();
        }

        @Override
        public Object getItem(int position) {
            return mcityNames.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder;
            if (convertView == null) {
                mHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.fragment_by_province_right_item, null, true);
                mHolder.tv_fm_by_province_right_item_id1 = (TextView) convertView.findViewById(R.id.tv_fm_by_province_right_item_id1);
                mHolder.tv_fm_by_province_right_item_id2 = (TextView) convertView.findViewById(R.id.tv_fm_by_province_right_item_id2);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            Area area1 = mcityNames.get(position).getArea1();
            Area area2 = mcityNames.get(position).getArea2();
            //TODO
            if (area1 != null) {

                mHolder.tv_fm_by_province_right_item_id1.setText(area1.getName());
                mHolder.tv_fm_by_province_right_item_id1.setHint(area1.getCode());
                mHolder.tv_fm_by_province_right_item_id1.setHintTextColor(Color.parseColor("#00ffffff"));
                if (area1.getStatus() == 0) {//0未开通 1已开通
                    mHolder.tv_fm_by_province_right_item_id1.setTextColor(getResources().getColor(R.color.color_FFFFFF));
                    mHolder.tv_fm_by_province_right_item_id1.setBackgroundResource(R.drawable.shape_bg_fm_by_province_city_close);
//                    mHolder.tv_fm_by_province_right_item_id1.setClickable(false);
                    mHolder.tv_fm_by_province_right_item_id1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtils.showShort("当前城市未开通业务,请重新选择!");
                        }
                    });
                }
                if (area1.getStatus() == 1) {//0未开通 1已开通
                    mHolder.tv_fm_by_province_right_item_id1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    mHolder.tv_fm_by_province_right_item_id1.setBackgroundResource(R.drawable.shape_bg_fm_by_province_city_open);
                    mHolder.tv_fm_by_province_right_item_id1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView view = (TextView) v;
//                            ToastUtils.showShort("您选择了" + view.getText());
                            Intent intent = new Intent();
                            intent.putExtra("cityCode", view.getHint());
                            intent.putExtra("cityName", view.getText());
                            intent.putExtra("provinceName",mProvinceName);
                            getActivity().setResult(RESULT_OK, intent);
                            getActivity().finish();
                        }
                    });
                }

            } else {
                mHolder.tv_fm_by_province_right_item_id1.setText("");
                mHolder.tv_fm_by_province_right_item_id1.setBackgroundColor(Color.parseColor("#fafafa"));
            }
            if (area2 != null) {

                mHolder.tv_fm_by_province_right_item_id2.setText(area2.getName());
                mHolder.tv_fm_by_province_right_item_id2.setHint(area2.getCode());
                mHolder.tv_fm_by_province_right_item_id2.setHintTextColor(Color.parseColor("#00ffffff"));
                if (area2.getStatus() == 0) {//0未开通 1已开通
                    mHolder.tv_fm_by_province_right_item_id2.setTextColor(getResources().getColor(R.color.color_FFFFFF));
                    mHolder.tv_fm_by_province_right_item_id2.setBackgroundResource(R.drawable.shape_bg_fm_by_province_city_close);
//                    mHolder.tv_fm_by_province_right_item_id2.setClickable(false);
                    mHolder.tv_fm_by_province_right_item_id2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtils.showShort("当前城市未开通业务,请重新选择!");
                        }
                    });

                }
                if (area2.getStatus() == 1) {//0未开通 1已开通
                    mHolder.tv_fm_by_province_right_item_id2.setTextColor(getResources().getColor(R.color.colorPrimary));
                    mHolder.tv_fm_by_province_right_item_id2.setBackgroundResource(R.drawable.shape_bg_fm_by_province_city_open);
                    mHolder.tv_fm_by_province_right_item_id2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                       /* if (area2.getStatus() == 0) {
                            ToastUtils.showShort(area2.getName() + "暂未开通业务。");
                        } else {
                            // TODO
                            ToastUtils.showShort(area2.getName() + ":" + area2.getCode());
                        }*/
                            TextView view = (TextView) v;
                            //ToastUtils.showShort(view.getHint());
                            Intent intent = new Intent();
                            intent.putExtra("cityCode", view.getHint());
                            intent.putExtra("cityName", view.getText());
                            intent.putExtra("provinceName",mProvinceName);
                            getActivity().setResult(RESULT_OK, intent);
                            getActivity().finish();
                        }
                    });
                }

            } else {
                mHolder.tv_fm_by_province_right_item_id2.setText("");
                mHolder.tv_fm_by_province_right_item_id2.setBackgroundColor(Color.parseColor("#fafafa"));
            }
            return convertView;
        }

        class ViewHolder {
            private TextView tv_fm_by_province_right_item_id1;
            private TextView tv_fm_by_province_right_item_id2;
        }
    }
}
