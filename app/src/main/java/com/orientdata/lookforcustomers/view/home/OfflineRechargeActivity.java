package com.orientdata.lookforcustomers.view.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.bean.OfflineRechargeBean;
import com.orientdata.lookforcustomers.bean.OfflineRechargeListBean;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.util.DateTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.home.fragment.MyAdapter;
import com.orientdata.lookforcustomers.view.xlistview.XListView;
import com.orientdata.lookforcustomers.view.xlistview.XListView1;
import com.orientdata.lookforcustomers.view.xlistview.XListViewFooter;
import com.orientdata.lookforcustomers.view.xlistview.XListViewFooter1;
import com.orientdata.lookforcustomers.widget.MyTitle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.OkHttpClientManager;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * 线下充值
 */
public class OfflineRechargeActivity extends AppCompatActivity implements View.OnClickListener, XListView1.IXListViewListener {
    private MyTitle title;
    private XListView1 mListView;
    int mPage = 1;
    int mSize = 10;
    private List<OfflineRechargeBean> mOfflineRechargeBeans = null;
    private MyOfflineRechargeListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_recharge);
        mOfflineRechargeBeans = new ArrayList<>();
        initView();
        initTitle();
        //getData(mPage, mSize);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData(mPage, mSize);
    }

    public void getData(int page, int size) {
        String url = HttpConstant.SELECT_OFFLINE_CHARGE_LISTS;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());
        if (page != -1) {
            map.put("page", page + "");
            map.put("size", size + "");
        }
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<OfflineRechargeListBean>() {
            @Override
            public void onError(Exception e) {
                ToastUtils.showShort(e.getMessage());
            }

            @Override
            public void onResponse(OfflineRechargeListBean res) {
                if (res == null) {
                    return;
                }
                if (res.getCode() == 0) {
//                    if (res.getResult() == null || res.getResult().size() <= 0) {
//                        return;
//                    }

                    if (res.isHasMore()) {
                        mListView.setLoadState(XListViewFooter1.STATE_NORMAL);
                    } else {
                        mListView.setLoadState(XListViewFooter1.STATE_NO_MORE);
                    }
                    if (mPage == 1) {
                        //refresh
                        mOfflineRechargeBeans = res.getResult();
                    } else {
                        //loadMore
                        mOfflineRechargeBeans.addAll(mOfflineRechargeBeans.size(), res.getResult());
                    }
                    if (mAdapter == null) {
                        mAdapter = new MyOfflineRechargeListAdapter(getBaseContext());
                        mListView.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                    onLoad();
                }
            }
        }, map);
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime("刚刚");
    }

    private void initView() {
        title = findViewById(R.id.mt_title);
        mListView = findViewById(R.id.xListView);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //进入详情页
                Intent intent = new Intent(OfflineRechargeActivity.this, OfflineRechargeReceiptDetailActivity.class);
                //intent.putExtra("taskId",searchList.get(position).getTaskId());
                intent.putExtra("data", mOfflineRechargeBeans.get(position - 1));
                startActivity(intent);
            }
        });

    }

    private void initTitle() {
        title.setTitleName("线下充值");
        title.setImageBack(this);
        title.setRightText("上传充值凭证");
        title.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getBaseContext(), UploadOfflineRechargeReceiptActivity.class), 1);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onRefresh() {
        if (mOfflineRechargeBeans != null) {
            mOfflineRechargeBeans.clear();
        }
        mPage = 1;
        getData(mPage, mSize);

    }

    @Override
    public void onLoadMore() {
        mPage++;
        getData(mPage, mSize);
    }

    class MyOfflineRechargeListAdapter extends BaseAdapter {
        private Context mContext;

        public MyOfflineRechargeListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            int count = 0;
            if (null != mOfflineRechargeBeans) {
                count = mOfflineRechargeBeans.size();
            }
            return count;
        }

        @Override
        public OfflineRechargeBean getItem(int position) {
            OfflineRechargeBean item = null;

            if (null != mOfflineRechargeBeans) {
                item = mOfflineRechargeBeans.get(position);
            }

            return item;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (null == convertView) {
                viewHolder = new ViewHolder();
                LayoutInflater mInflater = LayoutInflater.from(mContext);
                convertView = mInflater.inflate(R.layout.item_offline_recharge_list, null, false);
                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
                viewHolder.tv_bank_name = (TextView) convertView.findViewById(R.id.tv_bank_name);
                viewHolder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            OfflineRechargeBean item = getItem(position);
            if (null != item) {
                viewHolder.tv_money.setText(item.getMoney() + "");
                viewHolder.tv_bank_name.setText(item.getAccountName());
                //viewHolder.tv_status.setText(item.getStatus());
                //是 int 状态 1待处理 2未到账 3已充值 4 已驳回
                switch (item.getStatus()) {
                    case 1:
                        viewHolder.tv_status.setText("待处理");
                        viewHolder.tv_status.setTextColor(Color.parseColor("#999999"));
                        break;
                    case 2:
                        viewHolder.tv_status.setText("未到账");
                        viewHolder.tv_status.setTextColor(Color.parseColor("#db010b"));
                        break;
                    case 3:
                        viewHolder.tv_status.setText("已完成");
                        viewHolder.tv_status.setTextColor(Color.parseColor("#09b6f2"));
                        break;
                    case 4:
                        viewHolder.tv_status.setText("已驳回");
                        viewHolder.tv_status.setTextColor(Color.parseColor("#db010b"));
                        break;
                    default:
                        viewHolder.tv_status.setText("");
                        break;
                }
                Date date = item.getCreateDate();
                if (date != null) {
                    viewHolder.tv_time.setText(DateTool.parseDate2Str(date, "yyyy.MM.dd HH:mm:ss"));
                } else {
                    viewHolder.tv_time.setText("");
                }
            }

            return convertView;
        }

        class ViewHolder {
            public ImageView iv_icon;
            public TextView tv_money;
            public TextView tv_bank_name;
            public TextView tv_status;
            public TextView tv_time;
        }

    }


}
