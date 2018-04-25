package com.orientdata.lookforcustomers.view.certification;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.bean.QualificationCertificationUser;
import com.orientdata.lookforcustomers.presenter.CertificatePresent;
import com.orientdata.lookforcustomers.util.GlideUtil;
import com.orientdata.lookforcustomers.view.ImagePagerActivity;
import com.orientdata.lookforcustomers.widget.MyTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * 资质查看
 * Created by wy on 2017/11/16.
 */

public class WathchCertificationActivity extends BaseActivity<ICertificateView, CertificatePresent<ICertificateView>> implements ICertificateView {
    private MyTitle qulityTitle;
    private TextView tv_status;
    private TextView tv_type_name;
    private TextView tv_industry_name;
    private LinearLayout ll_id_certification_container;
    private LinearLayout ll_industry_certification_container;
    private TextView tv_certification_topic;
//    private List<String> photoUrls ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_certification);
        initView();
        initTitle();
        Intent intent = getIntent();
        if (intent != null) {
            CertificationOut c = (CertificationOut) intent.getSerializableExtra("CertificationOut");

            if (c != null) {
                //tv_status.setText(c.getAuthStatus());//int	认证状态 1审核中 2审核通过 3审核拒绝 4审核中
                switch (c.getAuthStatus()) {
                    case 1:
                        tv_status.setText("审核中");
                        break;
                    case 2:
                        tv_status.setText("已认证");
                        break;
                    case 3:
                        tv_status.setText("审核拒绝");
                        break;
                    case 4:
                        tv_status.setText("审核中");
                        break;
                }
                List<QualificationCertificationUser> ulist = c.getUlist();
                if (1 == c.getType()) {//1-企业，2-个人
                    tv_type_name.setText("《营业执照》或《组织机构代码证》");
                    tv_industry_name.setText(c.getTradeOneName() + "——" + c.getTradeTwoName());
                    View view = null;
                    ImageView imageView = null;
                    QualificationCertificationUser q = null;
                    if (ulist.size() >= 1) {
                        q = ulist.get(0);
                        view = LayoutInflater.from(this).inflate(R.layout.customize_creadential_watch_view, null, true);
                        imageView = view.findViewById(R.id.iv_customize_creadential_upload);
                        setImageBitmap(imageView, q.getCertificationImgid());
                        ll_id_certification_container.addView(view);
                        if (ulist.size() >= 2) {
                            tv_certification_topic.setVisibility(View.VISIBLE);
                            //行业资质
                            for (int i = 1; i < ulist.size(); i++) {
                                q = ulist.get(i);
                                view = LayoutInflater.from(this).inflate(R.layout.customize_creadential_watch_view, null, true);
                                imageView = view.findViewById(R.id.iv_customize_creadential_upload);
                                setImageBitmap(imageView, q.getCertificationImgid());
                                ll_industry_certification_container.addView(view);
                            }
                        } else {
                            tv_certification_topic.setVisibility(View.GONE);
                        }

                    }


                }
                if (2 == c.getType()) {//1-企业，2-个人
                    tv_type_name.setText("身份证照片");
                    tv_industry_name.setText(c.getTradeOneName() + "——" + c.getTradeTwoName());
                    View view = null;
                    ImageView imageView = null;
                    QualificationCertificationUser q = null;
                    if (ulist.size() >= 3) {
                        for (int i = 0; i < 3; i++) {
                            q = ulist.get(i);
                            view = LayoutInflater.from(this).inflate(R.layout.customize_creadential_watch_view2, null, true);
                            imageView = view.findViewById(R.id.iv_customize_creadential_upload);
//                            setImageBitmap(imageView, q.getCertificationImgid());
                            setImageBitmapPersonId(imageView,q.getCertificationImgid());
                            ll_id_certification_container.addView(view);

                        }
                        if (ulist.size() >= 4) {
                            tv_certification_topic.setVisibility(View.VISIBLE);
                            //行业资质
                            for (int i = 3; i < ulist.size(); i++) {
                                q = ulist.get(i);
                                view = LayoutInflater.from(this).inflate(R.layout.customize_creadential_watch_view, null, true);
                                imageView = view.findViewById(R.id.iv_customize_creadential_upload);
//                                setImageBitmap(imageView, q.getCertificationImgid());
                                setImageBitmapPersonId(imageView,q.getCertificationImgid());
                                ll_industry_certification_container.addView(view);
                            }
                        } else {
                            tv_certification_topic.setVisibility(View.GONE);
                        }


                    }


                }
            }
        }
    }

    //设置个人图片
    public void setImageBitmapPersonId(final ImageView imageView, final String path) {
        if (!TextUtils.isEmpty(path)) {
            GlideUtil.getInstance().loadCertificateImageId(this,imageView,path,true);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBigPhoto(imageView,path);
                }
            });
//            Glide.with(this).load(path).into(imageView);
        }
    }

    private void showBigPhoto(ImageView view,String path){
        ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add(path);
        ImagePagerActivity.startImagePagerActivity(this,photoUrls,0,imageSize);
    }


    //设置企业图片
    public void setImageBitmap(final ImageView imageView, final String path) {
        if (!TextUtils.isEmpty(path)) {
            GlideUtil.getInstance().loadCertificateImage(this,imageView,path,true);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBigPhoto(imageView,path);
                }
            });
//            Glide.with(this).load(path).into(imageView);
        }
    }

    private void initView() {
        qulityTitle = (MyTitle) findViewById(R.id.ac_watch_certification_title_id);
        tv_status = findViewById(R.id.tv_status);
        tv_type_name = findViewById(R.id.tv_type_name);
        tv_industry_name = findViewById(R.id.tv_industry_name);
        ll_id_certification_container = findViewById(R.id.ll_id_certification_container);
        ll_industry_certification_container = findViewById(R.id.ll_industry_certification_container);
        tv_certification_topic = findViewById(R.id.tv_certification_topic);
    }

    /**
     * 初始化title
     */
    private void initTitle() {
        qulityTitle.setTitleName("认证资质");
        qulityTitle.setImageBack(this);
        //qulityTitle.setRightText(getString(R.string.cancel));
    }

    @Override
    public void showLoading() {
        showDefaultLoading();
    }

    @Override
    public void hideLoading() {
        hideDefaultLoading();
    }

    @Override
    public void getProvinceCity(List<AreaOut> areaOuts) {

    }

    @Override
    public void getCertificateMsg(CertificationOut certificationOut) {

    }

    @Override
    protected CertificatePresent<ICertificateView> createPresent() {
        return new CertificatePresent<>(this);
    }

    public CertificatePresent<ICertificateView> getPresent() {
        return mPresent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
