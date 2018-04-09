package com.orientdata.lookforcustomers.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
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
import com.orientdata.lookforcustomers.bean.Industry;

import java.util.List;

/**
 * Created by ycs on 2016/8/12.
 * 行业
 */
public class IndustryDialog extends Dialog{
    //点击监听
    private ListView mViewArea;
    private ChangeListener changeListener;
    MyAdapter adapter;
    private TextView tvChooseRemind;


    public IndustryDialog(Context context) {
        super(context);
        init(context);
    }

    protected IndustryDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public IndustryDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }
    public void setOnchangeListener(ChangeListener changeListener){
        this.changeListener = changeListener;
    }

    private Context mContext;

    private void init(Context context) {
        this.mContext = context;
        setContentView(R.layout.area_dialog);
        //设置弹出窗口的位置
        setWindow();
        setUpViews();
        setUpListener();
    }
    public interface ChangeListener{
        void onChangeListener(Industry industry, int position);
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
        mViewArea = (ListView) findViewById(R.id.area);
        tvChooseRemind = (TextView) findViewById(R.id.tvChooseRemind);
        tvChooseRemind.setText(mContext.getResources().getString(R.string.input_industry));
    }

    private void setUpListener() {

    }

    private List<Industry> industries;

    public void setUpData(final List<Industry> industries) {
        this.industries = industries;
        adapter = new MyAdapter(mContext,0,industries);
        mViewArea.setAdapter(adapter);
        mViewArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeListener.onChangeListener(industries.get(position),position);
                adapter.setSelectItem(position);
                adapter.notifyDataSetInvalidated();
            }
        });
    }
        public void setSelect(int position){
        if(adapter!=null){
            adapter.setSelectItem(position);
            mViewArea.setSelection(position);
        }
    }


    class MyAdapter extends ArrayAdapter<Industry> {
        private List<Industry> industries;
        private Context context;

        public MyAdapter(Context context, int resourceId, List<Industry> industries) {
            super(context, resourceId);
            this.industries = industries;
            this.context = context;
        }


        class ViewHolder {
            TextView areaName;
        }

        @Override
        public int getCount() {
            return industries.size();
        }

        @Nullable
        @Override
        public Industry getItem(int position) {
            return industries.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
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
            Industry industry = industries.get(position);
            if (null != industry) {
                viewHolder.areaName.setText(industry.getName());
                if (position == selectItem) {
                    viewHolder.areaName.setTextColor(context.getResources().getColor(R.color.dialog_color));
                }else {
                    viewHolder.areaName.setTextColor(context.getResources().getColor(R.color.c_414141));
                }
            }

            return convertView;
        }
        public  void setSelectItem(int selectItem) {
            this.selectItem = selectItem;
        }
        private int  selectItem=-1;
    }

}
