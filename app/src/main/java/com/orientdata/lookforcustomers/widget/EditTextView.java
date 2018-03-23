package com.orientdata.lookforcustomers.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;


/**
 * Created by wy on 2017/11/16.
 * 字+输入框
 */
public class EditTextView extends RelativeLayout {

    private EditText etContent;
    private TextView tvLeftText;

    public EditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.my_edit_text_view, this, true);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.MyEditTextView);
        initView(attributes);
    }
    private void initView(TypedArray typedArray){
        etContent = (EditText) findViewById(R.id.etContent);
        tvLeftText = (TextView) findViewById(R.id.tvLeftText);
        String leftTextString = typedArray.getString(R.styleable.MyEditTextView_leftText);
        tvLeftText.setText(leftTextString);
        String hintText = typedArray.getString(R.styleable.MyEditTextView_hintText);
        etContent.setHint(hintText);
        etContent.requestFocus();
    }

    /**
     * 设置edit中的hint文字
     *
     * @param str
     */
    public void setHint(String str) {
        etContent.setHint(str);
    }


    /**
     * 获取edittext对象
     *
     * @return
     */
    public EditText getEt() {
        return etContent;
    }

    /**
     * 设置输入长度
     *
     * @param length
     */
    public void setLength(int length) {
        etContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
    }
    /**
     * 设置左边字
     */
    public void setLeftText(String text){
        tvLeftText.setText(text);
    }
    /**
     * 输入框是否单行显示
     */
    public void setEtSingleLine(boolean isSingleLine){
        etContent.setSingleLine(isSingleLine);
    }

    /**
     * 获取输入内容
     * @return
     */
    public String getText(){
        return etContent.getText().toString();
    }
    public void setText(String str){
        etContent.setText(str);
    }
    public void setType(int type){
        etContent.setInputType(type);
    }
}
