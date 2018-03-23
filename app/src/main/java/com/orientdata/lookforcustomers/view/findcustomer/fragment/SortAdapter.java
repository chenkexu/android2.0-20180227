package com.orientdata.lookforcustomers.view.findcustomer.fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.bean.Area;

import java.util.List;

public class SortAdapter extends BaseAdapter implements SectionIndexer {
    private List<AreaSortModel> list = null;
    private Context mContext;

    public SortAdapter(Context mContext, List<AreaSortModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<AreaSortModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final AreaSortModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_select_city, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_city_name);
            view.setTag(viewHolder);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.tv_catagory);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        int section = getSectionForPosition(position);

        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        Area temp = this.list.get(position).getArea();
        if (temp.getStatus() == 0) {//未开通
            viewHolder.tvTitle.setText(this.list.get(position).getArea().getName());
            viewHolder.tvTitle.setTextColor(Color.parseColor("#c7c7c7"));
//            viewHolder.tvTitle.setClickable(false);
            viewHolder.tvTitle.setHint(this.list.get(position).getArea().getCode());
            viewHolder.tvTitle.setHintTextColor(Color.parseColor("#00ffffff"));
        }
        if (temp.getStatus() == 1) {//开通
            viewHolder.tvTitle.setText(this.list.get(position).getArea().getName());
            viewHolder.tvTitle.setTextColor(Color.parseColor("#1c1c1c"));
//            viewHolder.tvTitle.setClickable(true);
            viewHolder.tvTitle.setHint(this.list.get(position).getArea().getCode());
            viewHolder.tvTitle.setHintTextColor(Color.parseColor("#00ffffff"));
        }


        return view;

    }


    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
    }

    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}