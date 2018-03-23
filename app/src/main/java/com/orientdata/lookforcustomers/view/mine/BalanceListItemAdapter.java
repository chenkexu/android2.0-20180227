package com.orientdata.lookforcustomers.view.mine;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.bean.Bh;
import com.orientdata.lookforcustomers.util.CommonUtils;

import java.util.List;

/**
 * Created by wy on 2017/12/9.
 * 余额列表
 */
public class BalanceListItemAdapter extends BaseAdapter{
    private Context mContext;
    private List<Bh> mList;
    public BalanceListItemAdapter(Context context, List<Bh> mList)
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
            convertView = mInflater.inflate(R.layout.item_balance_list, null);

            viewHolder.ivType = (ImageView) convertView.findViewById(R.id.ivType);
            viewHolder.tvType = (TextView) convertView.findViewById(R.id.tvType);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.tvMoney = (TextView) convertView.findViewById(R.id.tvMoney);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Bh bh = getItem(position);
        if (null != bh)
        {
            //101支付宝充值 102微信充值 103线下充值 104佣金转余额 201余额支出
            int type =  bh.getTransactionType();
            if(type == 101){
                viewHolder.ivType.setImageResource(R.mipmap.img_alipay);
            }else if(type == 102){
                viewHolder.ivType.setImageResource(R.mipmap.img_weixin);
            }else if(type == 103){
                viewHolder.ivType.setImageResource(R.mipmap.img_line);
            }else if(type == 104){
                viewHolder.ivType.setImageResource(R.mipmap.img_line);
            }else if(type == 201){
                viewHolder.ivType.setImageResource(R.mipmap.img_line);
            }else{
                viewHolder.ivType.setImageResource(R.mipmap.img_line);
            }
            viewHolder.tvType.setText(bh.getRemark());
            viewHolder.tvDate.setText(CommonUtils.getDateStr(bh.getCreateDate(),"yyyy/MM/dd HH:mm:ss"));

            String str = "";
            boolean isPlus = bh.getTransactionType() == 201 ?false:true;
            if(isPlus){
                str = "+";
//                viewHolder.tvMoney.setTextColor(mContext.getResources().getColor(R.color.c_DB010B));
            }else{
                str = "-";
//                viewHolder.tvMoney.setTextColor(mContext.getResources().getColor(R.color.c_414141));
            }
            viewHolder.tvMoney.setText(str+bh.getOccurMoney());
        }

        return convertView;
    }

    private static class ViewHolder
    {
        public ImageView ivType;
        public TextView tvType;
        public TextView tvDate;
        public TextView tvMoney;

    }

}
