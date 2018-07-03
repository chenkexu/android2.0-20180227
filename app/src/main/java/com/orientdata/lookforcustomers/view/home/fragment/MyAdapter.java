package com.orientdata.lookforcustomers.view.home.fragment;

/**
 * Created by wy on 2017/12/9.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.bean.Task;

import java.util.List;


public class MyAdapter extends BaseAdapter implements View.OnClickListener{
    private Context mContext;
    private List<Task> mList;
    public MyAdapter(Context context, List<Task> mList)
    {
        mContext = context;
        this.mList = mList;
    }
    public void setTaskData(List<Task> mList)
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
    public Task getItem(int position) {
        Task item = null;

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
            convertView = mInflater.inflate(R.layout.item_search_task, null);
            viewHolder.ivStatus = (ImageView) convertView.findViewById(R.id.ivStatus);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Task task = getItem(position);
        if (null != task) {
            viewHolder.tvTitle.setText(task.getTaskName());

            if(task.getStatus() == 2||task.getStatus()==4){ //审核失败
                viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.shenhe_error));
                viewHolder.ivStatus.setImageResource(R.mipmap.order_error);
            }else if(task.getStatus() == 1){ //审核中
                viewHolder.ivStatus.setImageResource(R.mipmap.order_ing);
                viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.c_9c9c9c));
            }else if(task.getStatus() == 6){ //待投放
                viewHolder.ivStatus.setImageResource(R.mipmap.order_pre);
                viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.shenhe_pre));
            }else if(task.getStatus() == 7){  //投放中
                viewHolder.ivStatus.setImageResource(R.mipmap.order_suc);
                viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.shenhe_sucuess));
            }else if(task.getStatus() == 8){ //投放结束
                viewHolder.ivStatus.setImageResource(R.mipmap.order_over);
                viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.shenhe_over));
            }else{
                viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.gray_small));
            }

            viewHolder.tvStatus.setText(getStatus(task.getStatus()));
        }

        return convertView;
    }

    private static class ViewHolder
    {
        public ImageView ivStatus;
        public TextView tvTitle;
        public TextView tvStatus;
    }


    @Override
    public void onClick(View v) {

    }
    private String getStatus(int status){
        String type = "";
        if(status == 1 || status ==3 ||status == 5 || status == 12){
            type = "审核中";
        }else if(status == 2 || status == 4){
            type = "审核失败";
        }else if(status == 6){
            type = "待投放";
        }else if(status == 7){
            type = "投放中";
        }else if(status == 8){
            type = "投放结束";
        }
        return type;
    }
}
