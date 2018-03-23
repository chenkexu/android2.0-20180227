package com.orientdata.lookforcustomers.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.bean.InterestCategory;
import com.orientdata.lookforcustomers.bean.PhoneModelTag;

import java.util.List;
import java.util.Map;

/**
 * Created by wy on 2017/11/22.
 * 多选
 */
public class MultipleSelectDialog extends Dialog implements View.OnClickListener{
    //点击监听
    private ListView mStringView;
    private TextView tvRevert;
    private TextView tvConfirm;
    private SelectListener selectListener;
    MyAdapter adapter;
    List<String> modelList;
//    private TextView tvChooseRemind;
//    private String remindString = "";


    public MultipleSelectDialog(Context context, String remindString) {
        super(context);
//        this.remindString = remindString;
        init(context);
    }

    protected MultipleSelectDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public MultipleSelectDialog(Context context, int themeResId,List<String> modelList) {
        super(context, themeResId);
        this.modelList = modelList;
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
            case R.id.tvCacel:
                for(PhoneModelTag phone : data) {
                    if (phone.isChecked() || phone.isClicked()) {
                        phone.setChecked(false);
                        phone.setClicked(false);
                    }
                }
                adapter.notifyDataSetChanged();
                selectListener.onCancel();
                break;
            case R.id.tvConfirm:
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
        void onSelectListener(List<PhoneModelTag>  data);
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

    private List<PhoneModelTag> data;

    public void setUpData(final List<PhoneModelTag> datas) {
        this.data = datas;
        if(data!=null){
            for(PhoneModelTag phoneModelTag:data){
                phoneModelTag.setClicked(phoneModelTag.isChecked());
            }
        }
        updateEnableStatus();
        adapter = new MyAdapter(mContext,0,data);
        mStringView.setAdapter(adapter);
//        mStringView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                updateData(position);
//            }
//        });
    }
    private  void updateEnableStatus(){
        if(modelList!=null){
            if(modelList.size() == 6){
                //其余未选择选项不可点
                for(PhoneModelTag phoneModelTag : data){
                    if(modelList.contains(phoneModelTag.getTagName())){
                        phoneModelTag.setEnable(true);
                    }else{
                        phoneModelTag.setEnable(false);
                    }
                }
            }else{
                for(PhoneModelTag phoneModelTag : data){
                    phoneModelTag.setEnable(true);
                }
            }
        }

    }
    private void updateData(int position){
        //记住点击状态
        data.get(position).setClicked(!(data.get(position).isClicked()));
        updateEnable();
    }
        public void setSelect(int position){
        if(adapter!=null){
            adapter.setSelectItem(position);
            mStringView.setSelection(position);
        }
    }
    private void updateEnable(){
        int size = 0;
        for(PhoneModelTag phoneModelTag : data){
            if(phoneModelTag.isClicked()){
                size++;
            }
            if(modelList!=null && modelList.contains(phoneModelTag.getTagName())){
                size--;
            }
        }
        int modelSize = 0;
        if(modelList!=null){
            modelSize = modelList.size();
        }

        if(modelSize + size == 6){
            //其余未选择选项不可点
            for(PhoneModelTag phoneModelTag : data){
                if(!phoneModelTag.isClicked()){
                    phoneModelTag.setEnable(false);
                }else{
                    phoneModelTag.setEnable(true);
                }
            }
        }else{
            for(PhoneModelTag phoneModelTag : data){
                phoneModelTag.setEnable(true);
            }
        }
    }

    class MyAdapter extends ArrayAdapter<PhoneModelTag> {
        private List<PhoneModelTag> datas;
        private Context context;


        public MyAdapter(Context context, int resourceId, List<PhoneModelTag> datas) {
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
        public PhoneModelTag getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

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
            final PhoneModelTag data = datas.get(position);
            viewHolder.areaName.setText(data.getTagName());
            if (position == selectItem || data.isClicked()) {
                viewHolder.areaName.setTextColor(context.getResources().getColor(R.color.c_09B6F2));
            }else {
                viewHolder.areaName.setTextColor(context.getResources().getColor(R.color.c_414141));
            }
            viewHolder.areaName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(data.isEnable()){
                        if(((TextView)v).getCurrentTextColor() == context.getResources().getColor(R.color.c_09B6F2)){
                            ((TextView)v).setTextColor(context.getResources().getColor(R.color.c_414141));
                        }else{
                            ((TextView)v).setTextColor(context.getResources().getColor(R.color.c_09B6F2));
                        }
                        updateData(position);
                    }else{
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
