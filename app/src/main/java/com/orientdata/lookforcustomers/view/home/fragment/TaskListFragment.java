package com.orientdata.lookforcustomers.view.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.LazyLoadFragment;
import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.bean.SearchListBean;
import com.orientdata.lookforcustomers.bean.Task;
import com.orientdata.lookforcustomers.event.SearchListEvent;
import com.orientdata.lookforcustomers.presenter.HomePresent;
import com.orientdata.lookforcustomers.view.certification.fragment.ACache;
import com.orientdata.lookforcustomers.view.findcustomer.TaskDetailActivity;
import com.orientdata.lookforcustomers.view.home.IHomeView;
import com.orientdata.lookforcustomers.view.xlistview.XListView;
import com.orientdata.lookforcustomers.view.xlistview.XListViewFooter;
import com.orientdata.lookforcustomers.widget.dialog.SettingStringDialog;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ckx on 2018/6/20.
 */

public class TaskListFragment extends LazyLoadFragment<IHomeView, HomePresent<IHomeView>> implements View.OnClickListener, XListView.IXListViewListener, IHomeView {
    private  int status;
    XListView mListView;


    private ArrayList<String> listStr = null;
    private ArrayList<String> listStatus = null;
    private MyAdapter mAdapter;
    private static List<Task> searchList = null;
    int page = 1;
    int size = 10;
    private ACache aCache = null;//数据缓存
    private Context context;
    private TextView textView;


    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_search2;
    }

    @Override
    protected void lazyLoad() {
        AddData();
        intiView();
        updateData(getStatus(fragmentTitle));
    }




    public void updateData(int typeChoose) {
        Logger.d("获取寻客管理的内容" + typeChoose);
        //类型，状态，第几页，size
        mPresent.getSearchList(1, typeChoose, page, size);
    }



    private void intiView() {
        mListView = findViewById(R.id.xListView);
        textView = findViewById(R.id.tv_null);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (searchList != null && searchList.size() > 0 && position > 0 && (position - 1) < searchList.size()) {
                    //进入详情页
                    Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                    intent.putExtra("taskId", searchList.get(position - 1).getTaskId());
                    startActivity(intent);
                }
            }
        });
    }

    private void AddData() {
        listStr = new ArrayList<>();
        listStr.add("全部");
        listStr.add("短信");
        listStr.add("页面");
        listStatus = new ArrayList<>();
        listStatus.add("全部");
        listStatus.add("审核中");
        listStatus.add("审核失败");
        listStatus.add("待投放");
        listStatus.add("投放中");
        listStatus.add("投放结束");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.typeChoose1:
                showStringDialog();
                break;
            case R.id.typeChoose2:
                showStringDialog2();
                break;
        }
    }


    private int choosePosition1 = 0;
    private int typeChoose = -1;//选择的类型 status的值

    private void showStringDialog() {
        final SettingStringDialog dialog = new SettingStringDialog(getContext(), R.style.Theme_Light_Dialog);
        dialog.setOnchangeListener(new SettingStringDialog.ChangeListener() {
            @Override
            public void onChangeListener(String data, int position) {
                dialog.dismiss();
                page = 1;
//                chooseText1.setText(listStr.get(position));
                choosePosition1 = position;
//                updateData();
                isLoadMore = false;
            }
        });
        dialog.setUpData(listStr);
        dialog.setSelect(choosePosition1);
        dialog.show();
    }


    public static int getStatus(String type) {
        int status = -1;
        if ("审核中".equals(type)) {
            status = 1;
        } else if ("审核失败".equals(type)) {
            status = 2;
        } else if ("待投放".equals(type)) {
            status = 6;
        } else if ("投放中".equals(type)) {
            status = 7;
        } else if ("投放结束".equals(type)) {
            status = 8;
        }
        return status;
    }

    boolean isLoadMore = false;


    private void showStringDialog2() {
        final SettingStringDialog dialog = new SettingStringDialog(getContext(), R.style.Theme_Light_Dialog);
        dialog.setOnchangeListener(new SettingStringDialog.ChangeListener() {
            @Override
            public void onChangeListener(String data, int position) {
                dialog.dismiss();
                page = 1;
//                chooseText2.setText(listStatus.get(position));
                typeChoose = getStatus(listStatus.get(position));
//                updateData();
                isLoadMore = false;
            }
        });
        dialog.setUpData(listStatus);
        dialog.show();
    }

    SearchListBean searchListBean;

    @Subscribe
    public void searchListResult(SearchListEvent searchListEvent) {
        searchListBean = searchListEvent.searchListBean;
        if (searchListBean != null) {
            if (searchListBean.getTotalCount() == 0) {
                mListView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
            }else{
                textView.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                if (searchListBean.isHasMore()) { //有更多数据
                    mListView.setLoadState(XListViewFooter.STATE_NORMAL);
                } else {              //没有更多数据
                    mListView.setLoadState(XListViewFooter.STATE_NO_MORE);
                }
                if (page == 1) {
                    //refresh
                    searchList = searchListBean.getResult();
                } else {
                    //loadMore
                    searchList.addAll(searchList.size(), searchListBean.getResult());
                }
            }

        }
        mAdapter = new MyAdapter(getContext(), searchList);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        onLoad();
    }


    @Override
    public void onRefresh() {
        if (searchList != null) {
            searchList.clear();
        }
        page = 1;
        updateData(getStatus(fragmentTitle));
    }


    @Override
    public void onLoadMore() {
        page++;
        updateData(getStatus(fragmentTitle));
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime("刚刚");
    }


    /*下面没有用*/
    @Override
    protected HomePresent<IHomeView> createPresent() {
        return new HomePresent<>(this);
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
    public void getCertificateMsg(CertificationOut certificationOut, boolean isCertificate) {

    }

    public static TaskListFragment getInstance() {
        TaskListFragment sf = new TaskListFragment();
        return sf;
    }

}
