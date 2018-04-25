package com.orientdata.lookforcustomers.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.bean.InterestCategory;

import java.util.List;

/**
 * Created by wy on 2017/11/22.
 * 多选
 */
public class HobbyMultipleSelectDialog extends Dialog implements View.OnClickListener{
    //点击监听
    private ListView mStringView;
    private TextView tvRevert;
    private TextView tvConfirm;
    private SelectListener selectListener;
    MyAdapter adapter;
    List<String> hobbyList;
//    private TextView tvChooseRemind;
//    private String remindString = "";


    public HobbyMultipleSelectDialog(Context context, String remindString) {
        super(context);
//        this.remindString = remindString;
        init(context);
    }

    protected HobbyMultipleSelectDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }


    //兴趣点
    public HobbyMultipleSelectDialog(Context context, int themeResId,List<String> hobbyList) {
        super(context, themeResId);
        this.hobbyList =  hobbyList;
        init(context);
    }
    public void setOnchangeListener(SelectListener selectListener){
        this.selectListener = selectListener;
    }

    private Context mContext;

    private void init(Context context) {
        this.mContext = context;
        setContentView(R.layout.multiple_select_dialog);
        //设置弹出窗口的位置
        setWindow();
        setUpViews();
        setUpListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvCacel: //重置
                for(InterestCategory interestCategory : data) {
                    if (interestCategory.isChecked()|| interestCategory.isClicked()) {
                        interestCategory.setChecked(false);
                        interestCategory.setClicked(false);
                    }
                }
                adapter.notifyDataSetChanged();
                selectListener.onCancel();
                break;
            case R.id.tvConfirm: //点击确定
                //更新 点击的数据
                for(int i=0;i<data.size();i++){
                    data.get(i).setChecked(data.get(i).isClicked());
                    data.get(i).setClicked(false);
                }
                selectListener.onSelectListener(data);
                break;
        }
    }

    public interface SelectListener{
        void onSelectListener(List<InterestCategory> data);
        void onCancel();
    }

    //从底部弹出
    private void setWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        //设置一个弹出动画
        window.setWindowAnimations(R.style.popwin_anim_style);
        window.getDecorView().setPadding(0, 0, 0, 0);
    }

    private void setUpViews() {
        mStringView = (ListView) findViewById(R.id.area);
        tvRevert = findViewById(R.id.tvCacel);
        tvConfirm = findViewById(R.id.tvConfirm);
        tvRevert.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
    }

    private void setUpListener() {

    }

    private List<InterestCategory> data;

    public void setUpData(final List<InterestCategory> datas) {
        this.data = datas;
        if(data!=null){
            for(InterestCategory interestCategory:data){
                interestCategory.setClicked(interestCategory.isChecked());
            }
        }
        updateEnableStatus();
        adapter = new MyAdapter(mContext,0,data);
        mStringView.setAdapter(adapter);
        mStringView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                updateData(position);
//                adapter.notifyDataSetInvalidated();
            }
        });
    }
    private void updateData(int position){
        data.get(position).setClicked(!(data.get(position).isClicked()));
        updateEnable();
    }

    private void updateEnable(){
        int size = 0;
        for(InterestCategory interestCategory : data){
            if(interestCategory.isClicked()){
                size++;
            }
            if(hobbyList!=null && hobbyList.contains(interestCategory.getIndustryName())){
                size--;
            }
        }
        int hobbySize = 0;
        if(hobbyList!=null){
            hobbySize = hobbyList.size();
        }

        if(hobbySize + size == 6){
            //其余未选择选项不可点
            for(InterestCategory interestCategory : data){
                if(!interestCategory.isClicked()){
                    interestCategory.setEnable(false);
                }else{
                    interestCategory.setEnable(true);
                }
            }
        }else{
            for(InterestCategory interestCategory : data){
                interestCategory.setEnable(true);
            }
        }
    }
    private  void updateEnableStatus(){
        if(hobbyList!=null){
            if(hobbyList.size() == 6){
                //其余未选择选项不可点
                for(InterestCategory interestCategory : data){
                    if(hobbyList.contains(interestCategory.getIndustryName())){
                        interestCategory.setEnable(true);
                    }else{
                        interestCategory.setEnable(false);
                    }
                }
            }else{
                for(InterestCategory interestCategory : data){
                    interestCategory.setEnable(true);
                }
            }
        }

    }
        public void setSelect(int position){
        if(adapter!=null){
            adapter.setSelectItem(position);
            mStringView.setSelection(position);
        }
    }

    class MyAdapter extends ArrayAdapter<InterestCategory> {
        private List<InterestCategory> datas;
        private Context context;


        public MyAdapter(Context context, int resourceId, List<InterestCategory> datas) {
            super(context, resourceId);
            this.datas = datas;
            this.context = context;
        }


        class ViewHolder {
            TextView areaName;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public InterestCategory getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

//        @Override
//        public boolean isEnabled(int position) {
//            Log.e("==","enable == "+datas.get(position).isEnable());
//            return datas.get(position).isEnable();
//        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater mInflater = LayoutInflater.from(context);
                convertView = mInflater.inflate(R.layout.area_item, null);
                viewHolder.areaName = (TextView) convertView.findViewById(R.id.areaName);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final InterestCategory data = datas.get(position);
            viewHolder.areaName.setText(data.getIndustryName());
            if (position == selectItem || data.isClicked()) {
                viewHolder.areaName.setTextColor(context.getResources().getColor(R.color.dialog_color));
            }else {
                viewHolder.areaName.setTextColor(context.getResources().getColor(R.color.c_414141));
            }

            viewHolder.areaName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(data.isEnable()){
                        if(((TextView)v).getCurrentTextColor() == context.getResources().getColor(R.color.dialog_color)){
                            ((TextView)v).setTextColor(context.getResources().getColor(R.color.c_414141));
                        }else{
                            ((TextView)v).setTextColor(context.getResources().getColor(R.color.dialog_color));
                        }
                        updateData(position);
                    }else{
                        Log.e("==","return");
                        return;
                    }
                }
            });
            return convertView;
        }
        public  void setSelectItem(int selectItem) {
            this.selectItem = selectItem;
        }
        private int  selectItem=-1;
    }

}
