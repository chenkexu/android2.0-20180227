package com.orientdata.lookforcustomers.view.mine;

/**
 * Created by wy on 2017/12/9.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.bean.Bh;
import com.orientdata.lookforcustomers.util.CommonUtils;

import java.util.List;

/**
 * 我的佣金列表
 */
public class MyCommissionListItemAdapter extends BaseAdapter{
    private Context mContext;
    private List<Bh> mList;
    public MyCommissionListItemAdapter(Context context, List<Bh> mList)
    {
        mContext = context;
        this.mList = mList;
    }
    public void setBhData(List<Bh> mList)
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
    public Bh getItem(int position) {
        Bh item = null;

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
            convertView = mInflater.inflate(R.layout.item_commission_list, null);

            viewHolder.tvMoney = (TextView) convertView.findViewById(R.id.tvMoney);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Bh bh = getItem(position);
        if (null != bh)
        {
            String str = "";
            boolean isPlus = bh.getTransactionType() == 105?true:false;
            if(isPlus){
                str = "+";
                viewHolder.tvMoney.setTextColor(mContext.getResources().getColor(R.color.c_b31a1f));
            }else{
                str = "-";
                viewHolder.tvMoney.setTextColor(mContext.getResources().getColor(R.color.c_414141));
            }
            viewHolder.tvMoney.setText(str+bh.getOccurMoney()+"元");
            viewHolder.tvDate.setText(CommonUtils.getDateStr(bh.getCreateDate(),"yyyy/MM/dd"));
        }

        return convertView;
    }

    private static class ViewHolder
    {
        public TextView tvMoney;
        public TextView tvDate;
    }

}
