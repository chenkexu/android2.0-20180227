package com.orientdata.lookforcustomers.view.home.imple;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.Result;
import com.orientdata.lookforcustomers.bean.ResultBean;
import com.orientdata.lookforcustomers.event.MsgInfoEvent;
import com.orientdata.lookforcustomers.presenter.MsgPresent;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.view.MyTextView;
import com.orientdata.lookforcustomers.view.home.IMsgView;
import com.orientdata.lookforcustomers.widget.MyTitle;

import org.greenrobot.eventbus.Subscribe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wy on 2017/12/19.
 */

public class MsgDetailActivity extends BaseActivity<IMsgView, MsgPresent<IMsgView>> implements IMsgView, View.OnClickListener{
    private TextView tvContent;
    private MyTitle title;
    private TextView tvDate;
    private String content = "";
    private String date = "";
    private int pushMessageId = -1;
    private TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_detail);
        initView();
        initTitle();
    }
    private void initView(){
//        date = getIntent().getStringExtra("date");
//        content = getIntent().getStringExtra("content");
        pushMessageId = getIntent().getIntExtra("pushMessageId",-1);
        tvDate = findViewById(R.id.tvDate);
        tvTitle = findViewById(R.id.tvTitle);
        title = findViewById(R.id.title);
        tvContent = findViewById(R.id.tvContent);
        mPresent.msgInfo(pushMessageId);
    }
    private void initTitle(){
        title.setTitleName("消息详情");
        title.setImageBack(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onPause() {
        setResult(1);
        super.onPause();
    }

    @Override
    public void showLoading() {
        showDefaultLoading();
    }

    @Override
    public void hideLoading() {
        hideDefaultLoading();
    }
    @Subscribe
    public void msgInfo(MsgInfoEvent msgInfoEvent){
        if(msgInfoEvent!=null){
            ResultBean result = msgInfoEvent.result;
            if(result!=null && result.getCode() == 0){
                Result result1 = result.getResult();
                String date = "";
                if(!TextUtils.isEmpty(result1.getCreateTime())){
                    date = result1.getCreateTime();
                }else if(!TextUtils.isEmpty(result1.getUpdateTime())){
                    date = result1.getUpdateTime();
                }
                updateView(result1.getTitle(),result1.getText(),date);
            }
        }
    }
    private void updateView(String title,String content,String date){
//        tvContent.setText(stringFilter(ToDBC(content)));
        tvContent.setText(content);
        tvContent.getViewTreeObserver().addOnGlobalLayoutListener(new OnTvGlobalLayoutListener());

        tvTitle.setText(title);
        if(!TextUtils.isEmpty(date)){
            tvDate.setText(CommonUtils.getTimeInterval(date,"yyyy-MM-dd HH:mm:ss","yyyy.MM.dd HH:mm:ss"));
        }
    }
    private class OnTvGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
         @Override
         public void onGlobalLayout() {
             tvContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                         final String newText = autoSplitText(tvContent);
                         if (!TextUtils.isEmpty(newText)) {
                             tvContent.setText(newText);
                            }
         }
     }
    private String autoSplitText(final TextView tv) {
                 final String rawText = tv.getText().toString(); //原始文本
                 final Paint tvPaint = tv.getPaint(); //paint，包含字体等信息
                 final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight(); //控件可用宽度

                 //将原始文本按行拆分
                 String [] rawTextLines = rawText.replaceAll("\r", "").split("\n");
                 StringBuilder sbNewText = new StringBuilder();
                for (String rawTextLine : rawTextLines) {
                        if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                                 //如果整行宽度在控件可用宽度之内，就不处理了
                                 sbNewText.append(rawTextLine);
                             } else {
                                 //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                                 float lineWidth = 0;
                                 for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                                         char ch = rawTextLine.charAt(cnt);
                                         lineWidth += tvPaint.measureText(String.valueOf(ch));
                                         if (lineWidth <= tvWidth) {
                                                 sbNewText.append(ch);
                                             } else {
                                                 sbNewText.append("\n");
                                                 lineWidth = 0;
                                                 --cnt;
                                             }
                                     }
                             }
                         sbNewText.append("\n");
                     }

                 //把结尾多余的\n去掉
                 if (!rawText.endsWith("\n")) {
                         sbNewText.deleteCharAt(sbNewText.length() - 1);
                     }

                 return sbNewText.toString();
             }

    @Override
    protected MsgPresent<IMsgView> createPresent() {
        return new MsgPresent<>(this);
    }
}
