package com.orientdata.lookforcustomers.view.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseFragment;
import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.bean.SearchListBean;
import com.orientdata.lookforcustomers.bean.Task;
import com.orientdata.lookforcustomers.event.SearchListEvent;
import com.orientdata.lookforcustomers.presenter.HomePresent;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.certification.fragment.ACache;
import com.orientdata.lookforcustomers.view.findcustomer.CreateFindCustomerActivity;
import com.orientdata.lookforcustomers.view.findcustomer.TaskDetailActivity;
import com.orientdata.lookforcustomers.view.home.IHomeView;
import com.orientdata.lookforcustomers.view.home.imple.HomeActivity;
import com.orientdata.lookforcustomers.view.xlistview.XListView;
import com.orientdata.lookforcustomers.view.xlistview.XListViewFooter;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.SettingStringDialog;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

/**
 * Created by wy on 2017/10/30.
 *
 * 寻客Fragment
 */

public class SearchFragment extends WangrunBaseFragment<IHomeView, HomePresent<IHomeView>> implements View.OnClickListener,XListView.IXListViewListener,IHomeView {
    private LinearLayout typeChoose1,typeChoose2;
    private RelativeLayout linearCreateSearch;
    private HomePresent mHomePresent;
    private TextView chooseText1,chooseText2;
    private ArrayList<String> listStr = null;
    private ArrayList<String> listStatus = null;
    private MyTitle titleSearch;

    private XListView mListView;
    private MyAdapter mAdapter;
    private static List<Task> searchList = null;
    int page = 1;
    int size = 10;
    private ACache aCache = null;//数据缓存



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initData();
        intiView(view);
        updateData();
        return view;
    }








    public void updateData() {
        Logger.d("获取寻客管理的内容");
        //类型，状态，第几页，size
        mHomePresent.getSearchList(choosePosition1,typeChoose,page,size);
    }

    private void intiView(View view){
        aCache = ACache.get(getContext());
        mHomePresent = ((HomeActivity)getActivity()).getPresent();
        typeChoose1 = view.findViewById(R.id.typeChoose1);
        mListView = view.findViewById(R.id.xListView);
        titleSearch = view.findViewById(R.id.titleSearch);
        typeChoose2 = view.findViewById(R.id.typeChoose2);
        chooseText1 = typeChoose1.findViewById(R.id.tvLeftText);
        chooseText2 = typeChoose2.findViewById(R.id.tvLeftText);
        linearCreateSearch = view.findViewById(R.id.linearCreateSearch);
        typeChoose1.setOnClickListener(this);
        typeChoose2.setOnClickListener(this);
        linearCreateSearch.setOnClickListener(this);
        titleSearch.setTitleName("寻客管理");
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (searchList!=null && searchList.size()>0 && position>0) {
                    //进入详情页
                    Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                    intent.putExtra("taskId",searchList.get(position-1).getTaskId());
                    startActivity(intent);
                }
            }
        });
    }

    private void initData(){
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
        switch (v.getId()){
            case R.id.typeChoose1:
                showStringDialog();
                break;
            case R.id.typeChoose2:
                showStringDialog2();
                break;
            case R.id.linearCreateSearch:
                double userStatus = Double.parseDouble(aCache.getAsString(SharedPreferencesTool.USER_STATUS));
                if(userStatus == 2.0){
                    //黑名单
                    ToastUtils.showShort("账户异常，请联系客服");
                }else{
                    aCache.remove(SharedPreferencesTool.DIRECTION_HISTORY);
                    startActivity(new Intent(getContext(), CreateFindCustomerActivity.class));
                }
                break;
        }
    }
    private int choosePosition1 = 0;
    private int choosePosition2 = 0;
    private int typeChoose = -1;//选择的类型 status的值
    private void showStringDialog(){
        final SettingStringDialog dialog = new SettingStringDialog(getContext(),R.style.Theme_Light_Dialog);
        dialog.setOnchangeListener(new SettingStringDialog.ChangeListener() {
            @Override
            public void onChangeListener(String data, int position) {
                dialog.dismiss();
                page = 1;
                chooseText1.setText(listStr.get(position));
                choosePosition1 = position;
                updateData();
                isLoadMore = false;
            }
        });
        dialog.setUpData(listStr);
        dialog.setSelect(choosePosition1);
        dialog.show();
    }


    private int getStatus(String type){
        int  status = -1;
        if("审核中".equals(type)){
            status = 1;
        }else if("审核失败".equals(type)){
            status = 2;
        }else if("待投放".equals(type)){
            status = 6;
        }else if("投放中".equals(type)){
            status = 7;
        }else if("投放结束".equals(type)){
            status = 8;
        }
        return status;
    }
    boolean isLoadMore = false;

    private void showStringDialog2(){
        final SettingStringDialog dialog = new SettingStringDialog(getContext(),R.style.Theme_Light_Dialog);
        dialog.setOnchangeListener(new SettingStringDialog.ChangeListener() {
            @Override
            public void onChangeListener(String data, int position) {
                dialog.dismiss();
                page = 1;
                chooseText2.setText(listStatus.get(position));
                choosePosition2 = position;
                typeChoose = getStatus(listStatus.get(position));
                updateData();
                isLoadMore = false;
            }
        });
        dialog.setUpData(listStatus);
        dialog.setSelect(choosePosition2);
        dialog.show();
    }


    SearchListBean searchListBean;



    @Subscribe
    public void searchListResult(SearchListEvent searchListEvent) {
        searchListBean = searchListEvent.searchListBean;
        if(searchListBean!=null) {
            if(searchListBean.isHasMore()){ //有更多数据
                mListView.setLoadState(XListViewFooter.STATE_NORMAL);
            }else{              //没有更多数据
                mListView.setLoadState(XListViewFooter.STATE_NO_MORE);
            }
            if(page == 1){
                //refresh
                searchList = searchListBean.getResult();
            }else{
                //loadMore
                searchList.addAll(searchList.size(),searchListBean.getResult());
            }
        }
        mAdapter = new MyAdapter(getContext(),searchList);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        onLoad();
    }




    private void setData(boolean isRefresh, List data) {
        page++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
//            cAdapter.setNewData(data);
        } else {
            if (size > 0) {
//                cAdapter.addData(data);
            }
        }
        if (size < PAGE_SIZE) {
            //第一页如果不够一页就不显示没有更多数据布局
//            cAdapter.loadMoreEnd(isRefresh);
//            T.showShort(getApplicationContext(),"没有更多数据");
        } else {
//            cAdapter.loadMoreComplete();
        }
    }




    @Override
    public void onRefresh() {
        if(searchList!=null){
            searchList.clear();
        }
        page = 1;
        updateData();
    }

    @Override
    public void onPause() {
        super.onPause();
//        page = 1;
    }

    @Override
    public void onLoadMore() {
        page++;
        updateData();
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

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void getCertificateMsg(CertificationOut certificationOut, boolean isCertificate) {

    }
}
