package com.orientdata.lookforcustomers.view.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.bean.MessageAndNoticeBean;

import java.util.List;

/**
 * Created by ckx on 2018/6/29.
 */

public class MessageAndToticeAdapter extends BaseQuickAdapter<MessageAndNoticeBean,BaseViewHolder> {


    public MessageAndToticeAdapter(@Nullable List<MessageAndNoticeBean> data) {
        super(R.layout.item_msg, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageAndNoticeBean item) {

        helper.setText(R.id.tvTitle, item.getTitle());

        if (item.getCreateDate() != null) {
            helper.setText(R.id.tvDate, item.getCreateDate());
        }
        if (item.getCreateTime() != null) {
            helper.setText(R.id.tvDate, item.getCreateTime());
        }

        if (item.getObjectId() == 1) { //未读
            helper.setVisible(R.id.ivStatus, true);
        } else {     //2.已经读了
            helper.setVisible(R.id.ivStatus, false);
        }


        if (item.getLookStatus() == 1) { //未读
            helper.setVisible(R.id.ivStatus, false);
        } else if (item.getLookStatus() == 0) {
            helper.setVisible(R.id.ivStatus, true);
        }
    }

}
