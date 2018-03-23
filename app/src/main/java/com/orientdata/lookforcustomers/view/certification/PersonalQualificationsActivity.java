package com.orientdata.lookforcustomers.view.certification;

import android.os.Bundle;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.presenter.CertificatePresent;
import com.orientdata.lookforcustomers.widget.MyTitle;
import java.util.List;

/**
 * 个人资质上传
 * Created by wy on 2017/11/16.
 */

public class PersonalQualificationsActivity extends BaseActivity<ICertificateView, CertificatePresent<ICertificateView>> implements ICertificateView{
    MyTitle qulityTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_quality);
        initView();
        initTitle();
    }

    private void initView(){
        qulityTitle = (MyTitle) findViewById(R.id.qulityTitle);
    }
    /**
     * 初始化title
     */
    private void initTitle(){
        qulityTitle.setTitleName(getString(R.string.personal_authentication));
        qulityTitle.setImageBack(this);
        qulityTitle.setRightText(getString(R.string.cancel));
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
}
