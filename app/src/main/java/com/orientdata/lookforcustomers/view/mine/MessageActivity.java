package com.orientdata.lookforcustomers.view.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.MsgListBean;
import com.orientdata.lookforcustomers.bean.Result;
import com.orientdata.lookforcustomers.event.MsgListEvent;
import com.orientdata.lookforcustomers.event.MsgUpdateEvent;
import com.orientdata.lookforcustomers.presenter.MsgPresent;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.home.IMsgView;
import com.orientdata.lookforcustomers.view.home.fragment.MsgListAdapter;
import com.orientdata.lookforcustomers.view.home.imple.MsgDetailActivity;
import com.orientdata.lookforcustomers.view.xlistview.XListView;
import com.orientdata.lookforcustomers.view.xlistview.XListViewFooter;
import com.orientdata.lookforcustomers.widget.MyTitle;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息界面
 */
public class MessageActivity extends BaseActivity<IMsgView, MsgPresent<IMsgView>> implements IMsgView,XListView.IXListViewListener,View.OnClickListener {


    private XListView xListView;
    private MyTitle title;
    private boolean isEdit = false;//是否在编辑状态
    private List<Result> results = null;
    private ArrayList<Result> resultChoose = null;//选择的消息的集合

    private MsgListAdapter adapter = null;
    private LinearLayout rl_bottom;
    private TextView tvChoose,tvDelete;
    private LinearLayout ll_no_content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.fragment_message, null, false);
        setContentView(view);
        initView(view);
        initTitle();
        updateData();
    }






    private void initView(View view){
        resultChoose = new ArrayList<>();
        xListView = view.findViewById(R.id.xListView);
        title = view.findViewById(R.id.title);
        tvChoose = view.findViewById(R.id.tvChoose);
        tvDelete = view.findViewById(R.id.tvDelete1);
        rl_bottom = view.findViewById(R.id.rl_bottom);
        ll_no_content = view.findViewById(R.id.ll_no_content);
        tvDelete.setOnClickListener(this);
        tvChoose.setOnClickListener(this);
        xListView.setPullRefreshEnable(false);
        xListView.setXListViewListener(this);
    }


    public void updateData() {
        Logger.d("获取消息列表的内容");
        mPresent.msgList();
    }

    private void initTitle(){
        title.setTitleName("消息");
    }


    @Subscribe
    public void msgList(MsgListEvent msgListEvent){
        MsgListBean msgListBean = msgListEvent.msgListBean;

        if(msgListBean!=null) {
            xListView.setLoadState(XListViewFooter.STATE_NO_MORE);

            if(results == null){
                results = new ArrayList<>();
            }else{
                results.clear();
            }

            results = msgListBean.getResult();

            if (results==null) {  //消息数据为空
                ll_no_content.setVisibility(View.VISIBLE);
                xListView.setVisibility(View.GONE);
                Logger.d("results消息数据为空2");
                rl_bottom.setVisibility(View.GONE);
                title.setRightText("");
            }else{
                ll_no_content.setVisibility(View.GONE);
                xListView.setVisibility(View.VISIBLE);
                if(results!=null && isEdit){
                    //编辑状态
                    for(Result result:results){
                        result.setEdit(true);
                    }
                }
                if(results!=null && results.size()>0){
                    if(isEdit){
                        title.setRightText("取消");
                        updateDeleteView();
                    }else{
                        title.setRightText("编辑");
                    }
                    title.setRightTextClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(resultChoose!=null){
                                resultChoose.clear();
                            }
                            if(!isEdit){
                                updateEditStatus(true);
                            }else{
                                updateEditStatus(false);
                            }
                        }
                    });
                }else{
                    title.setRightText("");
                    rl_bottom.setVisibility(View.GONE);
                }
            }

        }
        adapter = new MsgListAdapter(this,results,resultChoose);
        xListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        initListener();
    }


    private void initListener(){
        adapter.setOnItemClickListener(new MsgListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, ToggleButton tbutton) {
                if(isEdit){
                    if(resultChoose.size() == 0) {
                        tbutton.setChecked(true);
                        resultChoose.add(results.get(position));
                        if(resultChoose.size() == results.size()){
                            tvChoose.setText("取消全选");
                        }
                    }else{
                        for(int i=0;i<resultChoose.size();i++){
                            if((resultChoose.get(i).getPushMessageId()== results.get(position).getPushMessageId())){
                                //已经选择了
                                tbutton.setChecked(false);
                                resultChoose.remove(i);
                                tvChoose.setText("全选");
                                break;
                            }
                            if(i == resultChoose.size()-1){
                                //最后一个 已选择list中 无此项
                                tbutton.setChecked(true);
                                resultChoose.add(results.get(position));
                                if(resultChoose.size() == results.size()){
                                    tvChoose.setText("取消全选");
                                }
                                break;
                            }
                        }
                    }
                    updateDeleteView();
                }else{
                    Intent intent= new Intent(MessageActivity.this, MsgDetailActivity.class);
                    intent.putExtra("pushMessageId",results.get(position).getPushMessageId());
                    startActivityForResult(intent,0);
                }
            }
        });
    }
    /**
     * 更新 删除按钮 是否可点
     */
    private void updateDeleteView(){
        if(resultChoose == null || resultChoose.size() == 0){
            tvDelete.setText("取消");
        }else{
            tvDelete.setText("删除("+resultChoose.size()+")");
        }
    }

    private void updateEditStatus(boolean isEdit){
        if(results!=null && results.size()>0){
            for(int i = 0;i< results.size();i++){
                results.get(i).setEdit(isEdit);
            }
            if(isEdit){
                this.isEdit = true;
                rl_bottom.setVisibility(View.VISIBLE);
                title.setRightText("取消");
                tvDelete.setText("取消");
                tvChoose.setText("全选");
            }else{
                this.isEdit = false;
                title.setRightText("编辑");
                rl_bottom.setVisibility(View.GONE);
            }
            adapter.notifyDataSetChanged();
        }
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
    protected MsgPresent<IMsgView> createPresent() {
        return new MsgPresent<>(this);    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvDelete1: //取消和删除
                //删除
                if("取消".equals(tvDelete.getText().toString())){  //点击的是取消
                    updateEditStatus(false);
                    if(resultChoose!=null){
                        resultChoose.clear();
                    }
                }else if(tvDelete.getText().toString().contains("删除")){
                    String ids = "";
                    for(Result result:resultChoose){
                        ids  += result.getPushMessageId()+",";
                    }
                    ids = ids.substring(0,ids.length()-1);
                    mPresent.updateMsg(ids);
                }
                break;
            case R.id.tvChoose://全选 取消全选
                chooseAll();
                break;
        }

    }


    private void chooseAll(){
        String str = tvChoose.getText().toString();
        if("全选".equals(str)){
            if(resultChoose!=null && resultChoose.size()>0){
                resultChoose.clear();
            }
            resultChoose.addAll(results);
            tvChoose.setText("取消全选");
        }else{
            if(resultChoose!=null && resultChoose.size()>0){
                resultChoose.clear();
            }
            tvChoose.setText("全选");
        }
        updateDeleteView();
        adapter.notifyDataSetChanged();
    }


    //收到消息删除成功的广播
    @Subscribe
    public void updateMsg(MsgUpdateEvent msgUpdateEvent){
        if(msgUpdateEvent.errBean.getCode() == 0){
            ToastUtils.showShort("删除成功！");
            if(resultChoose!=null){
                resultChoose.clear();
            }
            updateData();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0 ){
            updateData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
