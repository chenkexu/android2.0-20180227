package com.orientdata.lookforcustomers.view.findcustomer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseFragment;
import com.orientdata.lookforcustomers.bean.IndustryTemplate;
import com.orientdata.lookforcustomers.bean.TradeSelfout;
import com.orientdata.lookforcustomers.event.ImgClipResultEvent;
import com.orientdata.lookforcustomers.presenter.ImgPresent;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.findcustomer.ImageMakingActivity;
import com.orientdata.lookforcustomers.view.findcustomer.impl.AddAdvertiseImgActivity;
import com.orientdata.lookforcustomers.widget.DrawableCenterTextView;
import com.orientdata.lookforcustomers.widget.dialog.SettingStringDialog;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wy on 2017/11/16.
 * 模板制作
 */

public class TemplateMakingFragment extends BaseFragment implements View.OnClickListener{
    private ImgPresent mImgPresent;
    private List<TradeSelfout> modelList = null;
    private RelativeLayout chooseLuodi;
    private TextView tvLeftText;
    private ListView listMode;
    private List<IndustryTemplate> modelImgList = null;
    int modelImgListSize = 0;
    private List<IndustryTemplate> modelImgListChoose = null;//随机的三张图片
    ImgAdapter adapter;
    int selectPosition = -1;
    private IndustryTemplate selectTrade = null;
    private TextView tvImgMake;
    private DrawableCenterTextView tvChange;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && (getActivity())!=null){
            ((AddAdvertiseImgActivity)getActivity()).setCancelVisible(View.GONE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_template_making,container,false);
        initView(view);
        mImgPresent.getModelList();
        return view;
    }
    private void initView(View view){
        mImgPresent = ((AddAdvertiseImgActivity) getActivity()).getPresent();
        chooseLuodi = view.findViewById(R.id.chooseLuodi);
        listMode = view.findViewById(R.id.listMode);
        tvImgMake = view.findViewById(R.id.tvImgMake);
        tvChange = view.findViewById(R.id.tvChange);
        tvLeftText = chooseLuodi.findViewById(R.id.tvLeftText);
        tvLeftText.setText("请选择落地页");
        chooseLuodi.setOnClickListener(this);
        tvImgMake.setOnClickListener(this);
        tvChange.setOnClickListener(this);
        adapter = new ImgAdapter(getContext());
        listMode.setAdapter(adapter);
        listMode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取选中的参数
                selectPosition = position;
                adapter.notifyDataSetChanged();
                selectTrade = modelImgListChoose.get(position);
            }
        });
    }

    /**
     * 随机选取 3张不重复图片 第一张
     * @return
     */
    private int getFirstRandomData(){
        return new Random().nextInt(modelImgListSize);//产生 0-(size-1)
    }

    /**
     * 随机选取 3张不重复图片 第二张
     * @param first
     * @return
     */
    private int getSecondRandomData(int first){
        int second = new Random().nextInt(modelImgListSize);
        while(first == second){
            second = new Random().nextInt(modelImgListSize);
        }
        return second;
    }

    /**
     * 随机选取 3张不重复图片 第三张
     * @param first
     * @param second
     * @return
     */
    private int getThirdRandomData(int first,int second){
        int third = new Random().nextInt(modelImgListSize);
        while(first == third || second == third){
            third = new Random().nextInt(modelImgListSize);
        }
        return third;
    }


    public static TemplateMakingFragment newInstance() {
        Bundle args = new Bundle();
        TemplateMakingFragment fragment = new TemplateMakingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chooseLuodi:
                showLodiDialog();
                break;
            case R.id.tvChange:
                //换一换
                updateModelImgListChooseData();
                break;
            case R.id.tvImgMake:
                if(selectTrade != null){
                    Intent intent = new Intent(getContext(),ImageMakingActivity.class);
                    intent.putExtra("url",selectTrade.getImageId());
                    startActivity(intent);
                }else{
                    ToastUtils.showShort("请选择模板！");
                }

                break;
        }

    }

    private int choosePositio = 0;

    /**
     * 选择行业
     */
    private void showLodiDialog(){
        if(modelList !=null && modelList.size() >0){
            List<String> list = new ArrayList<>();
            for(TradeSelfout tradeSelfout : modelList){
                list.add(tradeSelfout.getName());
            }
            final SettingStringDialog dialog = new SettingStringDialog(getContext(),R.style.Theme_Light_Dialog);
            dialog.setOnchangeListener(new SettingStringDialog.ChangeListener() {
                @Override
                public void onChangeListener(String data, int position) {
                    tvLeftText.setText(data);
                    dialog.dismiss();
                    choosePositio = position;
                    showImgList(position);
                }
            });
            dialog.setUpData(list);
            dialog.setSelect(choosePositio);
            dialog.show();
        }else{
            ToastUtils.showShort("获取行业信息失败！");
        }

    }

    /**
     * 相应行业的图片列表
     * @param position
     */
    private void showImgList(int position){
        if(modelImgList !=null)
            modelImgList.clear();
        modelImgList = modelList.get(position).getItl();
        updateModelImgListChooseData();
    }

    /**
     * 随机显示三张不同图片
     */
    private void updateModelImgListChooseData(){
        if(modelImgListChoose == null){
            modelImgListChoose = new ArrayList<>();
        }else{
            modelImgListChoose.clear();
        }
        if(modelImgList == null || modelImgList.size() == 0){
//            ToastUtils.showShort("请上传此行业的模版图片");
        }else{
            modelImgListSize = modelImgList.size();
            int first = getFirstRandomData();
            int second = getSecondRandomData(first);
            int third = getThirdRandomData(first,second);
            modelImgListChoose.add(modelImgList.get(first));
            modelImgListChoose.add(modelImgList.get(second));
            modelImgListChoose.add(modelImgList.get(third));
        }

        adapter.notifyDataSetChanged();
    }

    /**
     * 模板制作列表
     * @param imgClipResultEvent
     */
    @Subscribe
    public void imgClipResult(ImgClipResultEvent imgClipResultEvent) {
        if(imgClipResultEvent.modelList !=null)
            this.modelList = imgClipResultEvent.modelList;
            if(modelList!=null && modelList.size()>0){
                tvLeftText.setText(modelList.get(0).getName());
                showImgList(0);
            }
    }
    class ImgAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public ImgAdapter() {
        }

        public ImgAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return modelImgListChoose == null ? 0 : modelImgListChoose.size();
        }

        @Override
        public IndustryTemplate getItem(int position) {
            return modelImgListChoose.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //在外面先定义，ViewHolder静态类
        class ViewHolder {
            private ImageView img;
            private RadioButton radioButton;
        }

        //然后重写getView
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_template_make, null);
                holder.img =  convertView.findViewById(R.id.img);
                holder.radioButton =  convertView.findViewById(R.id.radioButton);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            IndustryTemplate industryTemplate = modelImgListChoose.get(position);
            Glide.with(getContext()).load(industryTemplate.getImageId()).into(holder.img);
            if(selectPosition == position){
                holder.radioButton.setChecked(true);
            }
            else{
                holder.radioButton.setChecked(false);
            }
            return convertView;
        }
    }
}
