package com.orientdata.lookforcustomers.view.findcustomer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.widget.ClearableEditText;
import com.orientdata.lookforcustomers.widget.FluidLayout;
import com.orientdata.lookforcustomers.widget.dialog.ConfirmDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wy on 2017/11/25.
 * 搜索
 */

public class SearchActivity extends WangrunBaseActivity implements View.OnClickListener {
    private RelativeLayout rlRemind;//搜索记录和删除按钮
    private ImageView ivDelete;
    private ImageView right_btn;//关闭
    private ClearableEditText clearEdit;
    private TextView rightText;//搜索
    private ImageView back_btn;//返回
    List<String> history;
    private FluidLayout flowlayout;

    int column = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        rlRemind = findViewById(R.id.rlRemind);
        ivDelete = findViewById(R.id.ivDelete);
        clearEdit = findViewById(R.id.clearEdit);
        rightText = findViewById(R.id.rightText);
        back_btn = findViewById(R.id.back_btn);
//        right_btn = findViewById(R.id.right_btn);
        flowlayout = findViewById(R.id.flowlayout);
        updateFluidView();
//        right_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
//      right_btn
        rightText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rightText://搜索
                //将字符串存到share中
                history = SharedPreferencesTool.getInstance().getDataList(SharedPreferencesTool.SEARCH_HISTORY);
                if (history == null) {
                    history = new ArrayList<>();
                }
                history.add(clearEdit.getText().toString());
                SharedPreferencesTool.getInstance().setDataList(SharedPreferencesTool.SEARCH_HISTORY, history);
                Intent intent = new Intent();
                intent.putExtra("searchValue", clearEdit.getText().toString().trim());
                setResult(RESULT_OK, intent);
                finish();
                break;

            case R.id.back_btn:
                finish();
                break;
            case R.id.ivDelete:
                showDialog();
                break;
        }
    }

    /**
     * 更新搜索历史列表
     */
    private void updateFluidView() {
        history = SharedPreferencesTool.getInstance().getDataList(SharedPreferencesTool.SEARCH_HISTORY);
        if (history == null || history.size() == 0) {
            flowlayout.removeAllViews();
            ivDelete.setVisibility(View.GONE);
            return;
        }
        ivDelete.setVisibility(View.VISIBLE
        );
        for (int i = 0; i < history.size(); i++) {
            final TextView textView = new TextView(this);
            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(15, 15, 15, 15);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            //设置最大字数为10，多余的显示...
            textView.setMaxEms(10);
            textView.setSingleLine(true);
            textView.setPadding(15, 15, 15, 15);
            textView.setBackgroundResource(R.drawable.shape_bg_gray_round);
            final String his = history.get(i);
            textView.setText(his);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showShort(his);
                    Intent intent = new Intent();
                    intent.putExtra("searchValue", his);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            flowlayout.addView(textView, params);
        }
    }

    private void showDialog() {
        final ConfirmDialog dialog = new ConfirmDialog(this, getString(R.string.confirm_remind));
        dialog.show();
        dialog.setClickListenerInterface(new ConfirmDialog.ClickListenerInterface() {
            @Override
            public void doCancel() {
                dialog.dismiss();
            }

            @Override
            public void doConfirm() {
                SharedPreferencesTool.getInstance().remove(SharedPreferencesTool.SEARCH_HISTORY);
                dialog.dismiss();
                updateFluidView();
            }
        });

    }

}
