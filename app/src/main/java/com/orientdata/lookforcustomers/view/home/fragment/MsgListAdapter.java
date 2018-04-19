package com.orientdata.lookforcustomers.view.home.fragment;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.bean.Result;
import com.orientdata.lookforcustomers.util.CommonUtils;

import java.util.List;

/**
 * Created by wy on 2017/12/9.
 * 消息
 */
public class MsgListAdapter extends BaseAdapter implements View.OnClickListener{
    private Context mContext;
    private List<Result> mList;
    private List<Result> mListChoose;//已经选择的

    public MsgListAdapter(Context context, List<Result> mList,List<Result> mListChoose)
    {
        mContext = context;
        this.mList = mList;
        this.mListChoose = mListChoose;
    }
    public void setTaskData(List<Result> mList)
    {
        this.mList = mList;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (null != mList)
        {
            count = mList.size();
        }
        return count;
    }

    @Override
    public Result getItem(int position) {
        Result item = null;

        if (null != mList)
        {
            item = mList.get(position);
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
        if (null == convertView)
        {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.item_msg, null);

            viewHolder.ivStatus = (ImageView) convertView.findViewById(R.id.ivStatus);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.toggleButton = (ToggleButton) convertView.findViewById(R.id.toggle_button);
            viewHolder.linear = (RelativeLayout) convertView.findViewById(R.id.linear);
            viewHolder.relative = (RelativeLayout) convertView.findViewById(R.id.relative);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Result result = getItem(position);
        if (null != result)
        {
            if(result.isEdit()){
                viewHolder.toggleButton.setVisibility(View.VISIBLE);
                viewHolder.ivStatus.setVisibility(View.GONE);
                viewHolder.toggleButton.setTag(position);
//                viewHolder.toggleButton.setOnClickListener(new ToggleClickListener(viewHolder.toggleButton));
            }else{
                viewHolder.toggleButton.setVisibility(View.GONE);
                if(result.getObjectId()==1){
                    //未读
                    viewHolder.ivStatus.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.ivStatus.setVisibility(View.GONE);
                }
            }
            viewHolder.relative.setTag(position);
            viewHolder.relative.setOnClickListener(new ToggleClickListener(viewHolder.toggleButton));

            viewHolder.tvTitle.setText(result.getTitle());
            if(!TextUtils.isEmpty(result.getCreateTime())){
                viewHolder.tvDate.setText(CommonUtils.getTimeInterval(result.getCreateTime(),"yyyy-MM-dd HH:mm:ss","yyyy.MM.dd HH:mm:ss"));
            }else if(!TextUtils.isEmpty(result.getUpdateTime())){
                viewHolder.tvDate.setText(CommonUtils.getTimeInterval(result.getUpdateTime(),"yyyy-MM-dd HH:mm:ss","yyyy.MM.dd HH:mm:ss"));
            }



//            if (null != mListChoose && mListChoose.size() > 0) {
//                for (int i = 0; i < mListChoose.size(); i++) {
//                    if ((mListChoose.get(i).getPushMessageId() == mList.get(position).getPushMessageId())) {
//                        viewHolder.toggleButton.setChecked(true);
//                        break;
//                    } else {
//                        viewHolder.toggleButton.setChecked(false);
//                    }
//                }
//            } else {
//                viewHolder.toggleButton.setChecked(false);
//            }
            if(mListChoose == null){
                viewHolder.toggleButton.setChecked(false);
            }else if(mListChoose.size() == 0){
                viewHolder.toggleButton.setChecked(false);
            }else if(mListChoose.size() > 0){
                for (int i = 0; i < mListChoose.size(); i++) {
                    if ((mListChoose.get(i).getPushMessageId() == mList.get(position).getPushMessageId())) {
                        viewHolder.toggleButton.setChecked(true);
                        break;
                    } else {
                        viewHolder.toggleButton.setChecked(false);
                    }
                }
            }
        }

        return convertView;
    }
    private class ToggleClickListener implements View.OnClickListener {

        ToggleButton tbutton;

        public ToggleClickListener(ToggleButton toggleButton) {
            tbutton = toggleButton;
        }

        @Override
        public void onClick(View view) {
            if (view instanceof ToggleButton) {
                ToggleButton toggleButton = (ToggleButton) view;
                int position = (Integer) toggleButton.getTag();
                if (mList != null && mOnItemClickListener != null && position < mList.size()) {
                    mOnItemClickListener.onItemClick(position, tbutton);
                }
            } else if (view instanceof RelativeLayout) {
                RelativeLayout toggle = (RelativeLayout) view;
                int position = (Integer) toggle.getTag();
                if (mList != null && mOnItemClickListener != null && position < mList.size()) {
                    mOnItemClickListener.onItemClick(position, tbutton);
                }
            }
        }
    }

    private static class ViewHolder {
        public ImageView ivStatus;
        public TextView tvTitle;
        public TextView tvDate;
        public ToggleButton toggleButton;
        public RelativeLayout linear;
        public RelativeLayout relative;

    }
    @Override
    public void onClick(View v) {

    }
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }
    public interface OnItemClickListener1 {
        void onItemClick(RelativeLayout view, int position, ToggleButton tb);
    }
    public interface OnItemClickListener {
        void onItemClick(int position, ToggleButton tb);
    }
}
