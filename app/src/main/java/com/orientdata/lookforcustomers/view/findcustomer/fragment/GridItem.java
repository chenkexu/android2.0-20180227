package com.orientdata.lookforcustomers.view.findcustomer.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.orientdata.lookforcustomers.R;

public class GridItem extends RelativeLayout implements Checkable {

    private Context mContext;
    private boolean mChecked;
    private ImageView iv_main = null;
    //private ImageView mSecletView = null;
    private ToggleButton tb_choose;

    public GridItem(Context context) {
        this(context, null, 0);
    }

    public GridItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.grid_item, this);
        iv_main = findViewById(R.id.iv_main);
        //mSecletView = (ImageView) findViewById(R.id.select);
        tb_choose = findViewById(R.id.tb_choose);
    }

    @Override
    public void setChecked(boolean checked) {
        // TODO Auto-generated method stub
        mChecked = checked;
        tb_choose.setChecked(checked);
        /*setBackgroundDrawable(checked ? getResources().getDrawable(
                R.drawable.background) : null);
        mSecletView.setVisibility(checked ? View.VISIBLE : View.GONE);*/
    }

    @Override
    public boolean isChecked() {
        // TODO Auto-generated method stub
        return mChecked;
    }

    @Override
    public void toggle() {
        // TODO Auto-generated method stub
        setChecked(!mChecked);
    }

    public void setImgResId(int resId) {
        if (iv_main != null) {
            iv_main.setBackgroundResource(resId);
        }
    }

}
