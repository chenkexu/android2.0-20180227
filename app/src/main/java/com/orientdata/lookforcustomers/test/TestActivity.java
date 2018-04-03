package com.orientdata.lookforcustomers.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.widget.MyTitle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.orientdata.lookforcustomers.R.id.tvNum;

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.titleMsg)
    MyTitle titleMsg;

    @BindView(R.id.et_enterprise_signature)
    EditText etEnterpriseSignature;

    @BindView(R.id.tv_message_sign)
    TextView tvMessageSign;

    @BindView(R.id.tv_message_content_hint)
    TextView tvMessageContentHint;

    @BindView(R.id.etMsgContent)
    EditText etMsgContent;

    @BindView(R.id.tv_unsubscribe)
    TextView tvUnsubscribe;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_task);
        ButterKnife.bind(this);


        etEnterpriseSignature.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Logger.d("count:" + count);
                String signContent = s.toString();
                Logger.d("返回企业签名字符串:" + signContent);
                etEnterpriseSignature.removeTextChangedListener(this);
                if (!signContent.startsWith("【")&&signContent.endsWith("】")) {
                    Logger.d("没有【");
                    etEnterpriseSignature.setText("【" + signContent);
                } else if (!signContent.startsWith("【")&&!signContent.endsWith("】")) {
                    Logger.d("没有【】");
                    etEnterpriseSignature.setText("【" + signContent + "】");
                } else if (signContent.startsWith("【")&&!signContent.endsWith("】")) {
                    Logger.d("没有】");
                    etEnterpriseSignature.setText(signContent+"】");
                } else{
                    Logger.d("都有");
                    etEnterpriseSignature.setText(signContent);
                }

                etEnterpriseSignature.setSelection(etEnterpriseSignature.getText().toString().length()-1);

                //设置只能输入2-4位汉字
                String str = stringFilter1(signContent);
                if (!signContent.equals(str)) {
                    //设置内容
                    etEnterpriseSignature.setText(str);
                    etEnterpriseSignature.setSelection(str.length());
                }

                if (etEnterpriseSignature.getText().toString().equals("")) {
                    Logger.d("企业签名为空");
                    tvMessageSign.setText("【请输入企业签名2-4个汉字】");
                    SpannableStringBuilder span = new SpannableStringBuilder("请输入短信内容"+etMsgContent.getText());
                    span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 12,
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    etMsgContent.setHint(span);

                } else {
                    Logger.d("企业签名不为空");
                    tvMessageSign.setText(etEnterpriseSignature.getText().toString());
                    SpannableStringBuilder span = new SpannableStringBuilder("请输入短信内容"+etMsgContent.getText());
                    span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 8,
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    etMsgContent.setHint(span);
                }

                //设置输入短信的长度
                etEnterpriseSignature.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        //输入短信内容时
        etMsgContent.addTextChangedListener(new TextWatcher() {
            private int temp;

            @Override
            public void beforeTextChanged(CharSequence s/*之前的文字内容*/, int start/*添加文字的位置(从0开始)*/, int count, int after/*添加的文字总数*/) {

            }

            @Override
            public void onTextChanged(CharSequence s/*之后的文字内容 */, int start/*添加文字的位置(从0开始)*/, int before/*之前的文字总数*/, int count) {
                if (s.toString() != null) {
                    String messageContent = s.toString();
                    Logger.d("返回的短信的内容：" + messageContent);
                    int length = s.toString().length();
                    etMsgContent.removeTextChangedListener(this);
                    etMsgContent.setText("              "+messageContent);

                    tvMessageContentHint.setVisibility(View.GONE);
                    tvMessageSign.setVisibility(View.GONE);
                    if (messageContent.equals(tvMessageSign.getText().toString())) { //如果只有企业签名
                        tvMessageContentHint.setVisibility(View.VISIBLE);
                        tvMessageSign.setVisibility(View.VISIBLE);
                        etMsgContent.setText("");
                    } else if (!s.toString().contains(tvMessageSign.getText().toString())) {
                        etMsgContent.setText(tvMessageSign.getText().toString() + s.toString());
                        etMsgContent.setSelection(etMsgContent.getText().toString().length());
                    } else {
                        etMsgContent.setText(s.toString());
                        etMsgContent.setSelection(etMsgContent.getText().toString().length());
                    }
                    etMsgContent.addTextChangedListener(this);
                }
            }



            @Override
            public void afterTextChanged (Editable s/*之后的文字内容*/){

            }
        });

    }




    private String stringFilter1(String signContent) {
        //只允许汉字
        String regEx = "[^\u4E00-\u9FA5]{2,7}";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(signContent);
        return m.replaceAll("").trim();
    }
}
