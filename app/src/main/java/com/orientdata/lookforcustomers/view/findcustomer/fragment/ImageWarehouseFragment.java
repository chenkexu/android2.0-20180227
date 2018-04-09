package com.orientdata.lookforcustomers.view.findcustomer.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseFragment;
import com.orientdata.lookforcustomers.bean.AdPage;
import com.orientdata.lookforcustomers.bean.AdPagesBean;
import com.orientdata.lookforcustomers.bean.Area;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.NextStepCheckBean;
import com.orientdata.lookforcustomers.bean.PreOut;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.presenter.CityPickPresent;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.util.MyOpenActivityUtils;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.agreement.MyWebViewActivity;
import com.orientdata.lookforcustomers.view.findcustomer.CreateAdActivity;
import com.orientdata.lookforcustomers.view.findcustomer.CreateFindCustomerActivity;
import com.orientdata.lookforcustomers.view.findcustomer.ICityPickView;
import com.orientdata.lookforcustomers.view.findcustomer.impl.MessageTaskActivity;
import com.orientdata.lookforcustomers.view.findcustomer.impl.PageTaskActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.OkHttpClientManager;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

import static android.app.Activity.RESULT_OK;

/**
 * Created by wy on 2017/11/16.
 * 任务库List页面
 */

public class ImageWarehouseFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private SwipeMenuListView listView;
    private AdPagesBean mAdPageBeans;
    private MyImageWarehouseViewAdapter mAdapter;
    private List<AdPage> mAdPageLists;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        //getData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
           getData();
        } else {
            //不可见时执行的操作
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_warehouse, container, false);
        initView(view);
//        initEvent();
        //getProvinceCity();
        context = getActivity();
        /**
         * 创建SwipeMenuCreator
         */
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem openItem = new SwipeMenuItem(context);
                openItem.setBackground(new ColorDrawable(Color.parseColor("#7A7C85")));
                openItem.setWidth(dp2px(75));
                openItem.setTitle("取消");
                openItem.setTitleSize(14);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);


                SwipeMenuItem deleteItem = new SwipeMenuItem(context);
                deleteItem.setBackground(new ColorDrawable(Color.parseColor("#FF5C64")));
                deleteItem.setWidth(dp2px(75));
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(14);
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);
//                //可以加入图片
//                SwipeMenuItem deleteItem1 = new SwipeMenuItem(context);
//                deleteItem1.setBackground(new ColorDrawable(Color.LTGRAY));
//                deleteItem1.setWidth(dp2px(90));
//                deleteItem1.setIcon(android.R.drawable.ic_delete);
//                menu.addMenuItem(deleteItem1);
            }
        };

        /**
         * 实例化
         */

        listView.setMenuCreator(creator);
        /**
         * 监听事件
         */
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                //index的值就是在SwipeMenu依次添加SwipeMenuItem顺序值，类似数组的下标。
                //从0开始，依次是：0、1、2、3...
                switch (index) {
                    case 0:
                        //Toast.makeText(context, "取消:" + position, Toast.LENGTH_SHORT).show();
                        mAdapter.notifyDataSetChanged();
                        break;

                    case 1:
                        //Toast.makeText(context, "删除:" + position, Toast.LENGTH_SHORT).show();
                        ((CreateAdActivity) getActivity()).showDefaultLoading();
                        MDBasicRequestMap map = new MDBasicRequestMap();
                        map.put("userId", UserDataManeger.getInstance().getUserId());
                        map.put("pageTemplateId", mAdPageLists.get(position).getPageTemplateId() + "");

                        //下一步验证
                        OkHttpClientManager.postAsyn(HttpConstant.DEL_USER_PAGE, new OkHttpClientManager.ResultCallback<ErrBean>() {
                            @Override
                            public void onError(Exception e) {
                                ((CreateAdActivity) getActivity()).hideDefaultLoading();
                                ToastUtils.showShort(e.getMessage());
                           /* startActivity(data);
                            finish();*/
                            }

                            @Override
                            public void onResponse(ErrBean response) {
                                ((CreateAdActivity) getActivity()).hideDefaultLoading();
                                if (response.getCode() == 0) {
                                    ToastUtils.showShort("删除成功！");

                                    mAdPageLists.remove(position);
                                    mAdapter.notifyDataSetChanged();
                                }

                            }
                        }, map);
                        break;
                }

                // false : 当用户触发其他地方的屏幕时候，自动收起菜单。
                // true : 不改变已经打开菜单的样式，保持原样不收起。
                return false;
            }
        });

        // 监测用户在ListView的SwipeMenu侧滑事件。
        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int pos) {
                Log.d("位置:" + pos, "开始侧滑...");
            }

            @Override
            public void onSwipeEnd(int pos) {
                Log.d("位置:" + pos, "侧滑结束.");
            }
        });

       /* *//**
         * 添加一点死数据
         *//*
        String[] data = new String[15];
        for (int i = 0; i < data.length; i++) {
            data[i] = "消息:" + i;
        }

        *//**
         * 绑定adapter
         *//*
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.fragment_image_warehouse_item, data);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data);

        listView.setAdapter(adapter);*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                MyOpenActivityUtils.openCommonWebView(getActivity(),mAdPageLists.get(position).getName(),mAdPageLists.get(position).getFenxiangUrl());
                Intent intent = new Intent(getActivity(),MyWebViewActivity.class);
                intent.putExtra("url",mAdPageLists.get(position).getFenxiangUrl());
                intent.putExtra("title",mAdPageLists.get(position).getName());
                getActivity().startActivity(intent);
            }
        });


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
        listView = (SwipeMenuListView) view.findViewById(R.id.listView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    public int dp2px(float dipValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public void getData() {
        ((CreateAdActivity) getActivity()).showDefaultLoading();
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        OkHttpClientManager.postAsyn(HttpConstant.SELECT_AD_PAGES, new OkHttpClientManager.ResultCallback<AdPagesBean>() {
            @Override
            public void onError(Exception e) {
                ((CreateAdActivity) getActivity()).hideDefaultLoading();
                ToastUtils.showShort(e.getMessage());
                           /* startActivity(data);
                            finish();*/
            }

            @Override
            public void onResponse(AdPagesBean response) {
                ((CreateAdActivity) getActivity()).hideDefaultLoading();
                if (response.getCode() == 0) {
                    mAdPageBeans = response;

                    if (mAdPageBeans.getResult() == null) {
                        ((CreateAdActivity) getActivity()).hideDefaultLoading();
                        return;
                    }
                    mAdPageLists = mAdPageBeans.getResult();
                    if (mAdapter == null) {
                        mAdapter = new MyImageWarehouseViewAdapter(getActivity());
                        listView.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }

                }

            }
        }, map);

    }

    class MyImageWarehouseViewAdapter extends BaseAdapter {

        private Context context = null;

        public MyImageWarehouseViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return mAdPageLists.size();
        }

        @Override
        public Object getItem(int position) {
            return mAdPageLists.get(position);
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
                convertView = inflater.inflate(R.layout.fragment_image_warehouse_item, null, true);
                mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            AdPage adPage = mAdPageLists.get(position);
            //TODO
            if (adPage != null) {

                mHolder.tv_name.setText(adPage.getName());
            } else {
                mHolder.tv_name.setText("获取名字失败");
            }

            return convertView;
        }

        class ViewHolder {
            private TextView tv_name;
        }
    }

}
