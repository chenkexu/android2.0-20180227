package com.orientdata.lookforcustomers.view.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.MessageAndNoticeBean;
import com.orientdata.lookforcustomers.presenter.MsgAndNoticePresent;
import com.orientdata.lookforcustomers.view.adapter.MessageAndToticeAdapter;
import com.orientdata.lookforcustomers.view.home.imple.IMsgAndNoticeView;
import com.orientdata.lookforcustomers.view.home.imple.MsgDetailActivity;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.qiniu.android.common.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 消息界面
 */
public class MessageAndNoticeActivity extends BaseActivity<IMsgAndNoticeView, MsgAndNoticePresent<IMsgAndNoticeView>> implements IMsgAndNoticeView {


    @BindView(R.id.title)
    MyTitle title;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private MessageAndToticeAdapter messageAndToticeAdapter;
    private List<MessageAndNoticeBean> messageAndNoticeBeanList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.fragment_message_new, null, false);
        setContentView(view);
        ButterKnife.bind(this);
        title.setTitleName("消息");
        title.setImageBack(this);


        messageAndNoticeBeanList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAndToticeAdapter = new MessageAndToticeAdapter(messageAndNoticeBeanList);
        mRecyclerView.setAdapter(messageAndToticeAdapter);
        messageAndToticeAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                setData();
            }
        });


        messageAndToticeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MessageAndNoticeActivity.this, MsgDetailActivity.class);
                List<MessageAndNoticeBean> data = messageAndToticeAdapter.getData();
                intent.putExtra(Constants.MessageAndNoticeBean, data.get(position));
                startActivity(intent);

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private void setData(){
        mPresent.selectMsgAndAnnouncement();
    }



    @Override
    public void selectMsgAndAnnouncement(List<MessageAndNoticeBean> messageAndNoticeBeanList) {
        messageAndToticeAdapter.setNewData(messageAndNoticeBeanList);
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
    protected MsgAndNoticePresent<IMsgAndNoticeView> createPresent() {
        return new MsgAndNoticePresent<>(this);
    }



}
