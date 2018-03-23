package com.orientdata.lookforcustomers.view.home.fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.bean.Task;
import com.orientdata.lookforcustomers.widget.PagingBaseAdapter;

/**
 * Created by wy on 2017/12/6.
 */
public class SearchListAdapter extends PagingBaseAdapter<Task> {

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Task getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Task task= items.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search_task, viewGroup, false);
            viewHolder.ivStatus = convertView.findViewById(R.id.ivStatus);
            viewHolder.tvTitle =  convertView.findViewById(R.id.tvTitle);
            viewHolder.tvStatus = convertView.findViewById(R.id.tvStatus);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(task.getType()==1?R.mipmap.msg_task:R.mipmap.page_task).into(viewHolder.ivStatus);
        viewHolder.tvTitle.setText(task.getTaskName());
        viewHolder.tvStatus.setText(TaskStatus.getName(task.getStatus()));
        return convertView;
    }
    private class ViewHolder {
        public ImageView ivStatus;
        public TextView tvTitle;
        public TextView tvStatus;
    }
}
