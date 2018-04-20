package com.orientdata.lookforcustomers.view.certification;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.bean.BusinessScope;
import com.orientdata.lookforcustomers.bean.BusinessScopeOut;
import com.orientdata.lookforcustomers.bean.CertificationBean;
import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.bean.Industry;
import com.orientdata.lookforcustomers.bean.IndustryBean;
import com.orientdata.lookforcustomers.bean.MustQualify;
import com.orientdata.lookforcustomers.bean.SubIndustryBean;
import com.orientdata.lookforcustomers.bean.TradeCategoryOut;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.runtimepermissions.PermissionsManager;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.util.GlideUtil;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.certification.fragment.ACache;
import com.orientdata.lookforcustomers.view.certification.fragment.PersonalCertificationFragment;
import com.orientdata.lookforcustomers.view.certification.impl.CertificationActivity;
import com.orientdata.lookforcustomers.view.clip.ImageActivity;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.IndustryDialog;
import com.orientdata.lookforcustomers.widget.dialog.SubIndustryDialog;
import com.orientdata.lookforcustomers.widget.dialog.SubmitFeedBackDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * 个人认证上传页面
 * Created by wy on 2017/11/16.
 */
public class PersonalCertificationUploadActivity extends ImageActivity implements View.OnClickListener {
    private MyTitle title;
    private ImageView iv_at_personal_id_reverse_upload;
    private ImageView iv_at_personal_id_front_upload;
    private ImageView iv_at_personal_id_hand_held_upload;
    private LinearLayout industryContainer;
    private LinearLayout uploadContainer;
    //<必选资质名称,view位置>
    private Map<String, Integer> uploadContainerIndexMap;
    //<资质名称,业务范围>
    private Map<String, String> industryAndSubIndexs;
    private View currentImageView;
    private RelativeLayout rlIndustry;
    private RelativeLayout rlSub;
    private TextView industry_tradeId;
    private TextView sub_tradeId;
    private TextView industry_position;
    private TextView sub_position;
    private TextView submitButton;
    private TextView tvIndustry;
    private TextView tvSub;

    private int industryPosition = -1;//一级行业选择的position
    private int subPosition = -1;//子类选择的position
    private String currentIndustryName;
    private String currentSubName;
    private Bitmap companyBitmap;
    private Context mContext;
    private boolean isChoose = true;
    private String filePath;
    private List<Industry> industries = null;
    private List<TradeCategoryOut> subIndustries;
    private static final String ENTERPRISE_INDUSTRY = "enterprise_industry";//存储获取企业一级行业
    ACache aCache = null;//数据缓存
    private int totalImageToUpload;
    List<RadioGroup> radioGroups;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_certification_upload);
        this.mContext = this;
        initView();
        //初始化Title
        initTitle();
        getIndustryData();
        uploadContainerIndexMap = new HashMap<String, Integer>();
        industryAndSubIndexs = new TreeMap<String, String>();
        //上传身份证正面照片
        iv_at_personal_id_front_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentImageView = view;
                choosePhoto();
            }
        });
        //上传身份证反面照片
        iv_at_personal_id_reverse_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentImageView = view;
                choosePhoto();
            }
        });
        //上传手持身份证照片
        iv_at_personal_id_hand_held_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentImageView = view;
                choosePhoto();
            }
        });
    }

    private void initView() {
        aCache = ACache.get(this);
        title = findViewById(R.id.fm_home_title_id);
        iv_at_personal_id_front_upload = findViewById(R.id.iv_at_personal_id_front_upload);
        iv_at_personal_id_reverse_upload = findViewById(R.id.iv_at_personal_id_reverse_upload);
        iv_at_personal_id_hand_held_upload = findViewById(R.id.iv_at_personal_id_hand_held_upload);
        industryContainer = findViewById(R.id.ll_at_personal_certification_industry_container);
        uploadContainer = findViewById(R.id.ll_at_personal_certification_upload_container);
        rlIndustry = findViewById(R.id.rlIndustry);
        rlSub = findViewById(R.id.rlSub);
        industry_tradeId = findViewById(R.id.industry_tradeId);
        sub_tradeId = findViewById(R.id.sub_tradeId);
        sub_tradeId = findViewById(R.id.sub_tradeId);
        industry_position = findViewById(R.id.industry_position);
        sub_position = findViewById(R.id.sub_position);
        tvIndustry = findViewById(R.id.tvIndustry);
        tvSub = findViewById(R.id.tvSub);
        rlIndustry.setOnClickListener(this);
        rlSub.setOnClickListener(this);
        submitButton = findViewById(R.id.tv_personal_certification_upload);
        submitButton.setOnClickListener(this);
        radioGroups = new ArrayList<>();
    }

    /**
     * 选择照片
     */
    private void choosePhoto() {
        if (CommonUtils.haveSDCard()) {
            if (hasPermisson()) {
                tanKuang();
            } else {
                requestPermission();
            }
        } else {
            Toast.makeText(this, "没有SD卡!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlIndustry:
                if (industries != null && industries.size() > 0) {
                    showIndustryWheel(industries);
                } else {
                    ToastUtils.showShort("获取一级行业失败");
                }
                break;
            case R.id.rlSub:
                getSubData();
                break;
            case R.id.tv_personal_certification_upload:
                submit();
                break;
        }
    }

    /**
     * 提交资料认证
     */
    private int curCatch = -1;//此用户缓存的position
    List<CertificationBean> dataListCatch = null;

    private void submit() {
        CertificationBean certificationBean = null;
        String url = HttpConstant.SUBMIT_CERTIFICATION_URL;

        //使用getAsObject()，直接进行强转
        dataListCatch = (ArrayList<CertificationBean>) aCache.getAsObject(SharedPreferencesTool.CERTIFICATE_KEY_PER);
        if (dataListCatch != null) {
            for (int i = 0; i < dataListCatch.size(); i++) {
                String id = dataListCatch.get(i).getUserId() + "";
                //id 并且是企业认证
                if (UserDataManeger.getInstance().getUserId().equals(id) && dataListCatch.get(i).getType() == 2) {
                    curCatch = i;
                    certificationBean = dataListCatch.get(i);
                }
            }
        }
        if (certificationBean == null) {
            ToastUtils.showShort("无首页认证信息！");
            return;
        }
        MDBasicRequestMap map = new MDBasicRequestMap();
        if (filePaths == null) {
            ToastUtils.showShort(R.string.input_remind);
            return;
        }
        if (filePaths.size() < 3) {
            ToastUtils.showShort("请上传身份证照片");
            return;
        }
        if (industryPosition == -1 || subPosition == -1){
            ToastUtils.showShort("请选择所属行业");
            return;
        }

        if (filePaths.size() >= 3 && filePaths.size() < totalImageToUpload + 3) {
            ToastUtils.showShort("请上传行业资质照片");
            return;
        }

        if (radioGroups != null && radioGroups.size() > 0) {
            for (RadioGroup radioGroup : radioGroups) {
                if (radioGroup != null && radioGroup.getChildCount() > 1) {
                    if (radioGroup.getCheckedRadioButtonId() == -1) {
                        ToastUtils.showShort("请选择资质照片类型");
                        return;
                    }
                }
            }
        }
        if (industries == null || subIndustries == null || certificationBean == null) {
            ToastUtils.showShort(R.string.input_remind);
            return;
        }

        map.put("userId", certificationBean.getUserId() + "");
        map.put("token", UserDataManeger.getInstance().getUserToken());
        map.put("name", certificationBean.getName());
        map.put("type", certificationBean.getType() + "");
//        map.put("businessLicenseNo", certificationBean.getBusinessLicenseNo());
//        map.put("contactPerson", certificationBean.getContactPerson());
        map.put("contactPhone", certificationBean.getContactPhone());
        map.put("contactCard", certificationBean.getContactCard());
        map.put("cityCode", certificationBean.getCityCode());
        map.put("provinceCode", certificationBean.getProvinceCode());
        map.put("address", certificationBean.getAddress());

        map.put("address", certificationBean.getAddress());
        Industry submitIndustry = industries.get(industryPosition);
        TradeCategoryOut submitTCO = subIndustries.get(subPosition);
        map.put("tradeOneId", submitIndustry.getTradeId() + "");
        map.put("tradeTwoId", submitTCO.getTradeId() + "");
        map.put("tradeOneName", submitIndustry.getName());
        map.put("tradeTwoName", submitTCO.getName());
        File[] submitfiles = new File[filePaths.size()];
        String[] submitFileKeys = new String[filePaths.size()];
        for (int i = 0; i < filePaths.size(); i++) {
            submitfiles[i] = new File(filePaths.get(i));
            submitFileKeys[i] = filePaths.get(i);
        }
//        map.put("file", submitfiles);

        showDefaultLoading();
        try {
            OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<ErrBean>() {
                @Override
                public void onError(Exception e) {
                    hideDefaultLoading();
                    ToastUtils.showShort(e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
                }

                @Override
                public void onResponse(ErrBean response) {
                    hideDefaultLoading();
                    showSubmitFeedbackDialog(response.getCode());
                }
            }, submitfiles, "file", map);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * show 提交反馈 dialog
     *
     * @param code
     */
    private void showSubmitFeedbackDialog(final int code) {
        String submitStatus = "";
        String confirmText = "";
        int imgRes = 0;
        if (code == 0) {
            //成功
            submitStatus = getString(R.string.sub_suc);
            confirmText = getString(R.string.sub_confirm);
            imgRes = R.mipmap.icon_dialog_success;
            //清掉此用户的认证缓存数据
//            if(curCatch!=-1){
//                dataListCatch.remove(curCatch);
//                SharedPreferencesTool.getInstance().setDataList(SharedPreferencesTool.CERTIFICATE_KEY_PER,dataListCatch);
//            }
        } else {
            //失败
            submitStatus = getString(R.string.sub_fail);
            confirmText = getString(R.string.sub_fail_txt);
            imgRes = R.mipmap.icon_dialog_error;

        }
        final SubmitFeedBackDialog submitFeedBackDialog = new SubmitFeedBackDialog(this, submitStatus, confirmText, imgRes);
        submitFeedBackDialog.show();
        submitFeedBackDialog.setClickListenerInterface(new SubmitFeedBackDialog.ClickListenerInterface() {
            @Override
            public void doCertificate() {
                if (code == 0) {
                    submitFeedBackDialog.dismiss();
                    finish();
                } else {
                    submitFeedBackDialog.dismiss();
                }
                //消失所有的页面 到首页
                closeActivity(PersonalCertificationUploadActivity.class, CertificationActivity.class);
            }
        });
    }


    /**
     * 获取一级行业
     */
    private void getIndustryData() {
        if (industries != null && industries.size() >= 0) {
            industries.clear();
        }
        String url = HttpConstant.SELECT_INDUSTRY;
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("type", "1");//个人
        showDefaultLoading();
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<IndustryBean>() {
            @Override
            public void onError(Exception e) {
                hideDefaultLoading();
                ToastUtils.showShort(e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
            }

            @Override
            public void onResponse(IndustryBean response) {
                hideDefaultLoading();
                if (response.getCode() == 0) {
//                        SharedPreferencesTool.setObjectToShare(mContext, response.getResult(), ENTERPRISE_INDUSTRY);
                    industries = response.getResult();
//                        showIndustryWheel(industries);
                }
            }
        }, map);
    }

    /**
     * 获取二级行业
     */
    private void getSubData() {
//        showWaitDialog();
        String industryTradeId = industry_tradeId.getText().toString();
        if (!TextUtils.isEmpty(industryTradeId)) {
            String url = HttpConstant.SELECT_SUB_STRY;
            MDBasicRequestMap map = new MDBasicRequestMap();
            map.put("parentId", industryTradeId);
            OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<SubIndustryBean>() {
                @Override
                public void onError(Exception e) {
                    ToastUtils.showShort(e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
                }

                @Override
                public void onResponse(SubIndustryBean response) {
                    if (response.getCode() == 0) {
                        subIndustries = response.getResult();
                        showSubIndustryWheel();
                    }
                }
            }, map);
        } else {
            ToastUtils.showShort("请选择一级行业");
        }
    }

    /**
     * 一级行业
     */
    private void showIndustryWheel(List<Industry> industries) {
        final IndustryDialog dialog = new IndustryDialog(mContext, R.style.Theme_Light_Dialog);
        dialog.setUpData(industries);
        dialog.show();
        dialog.setCancelable(true);
        dialog.setOnchangeListener(new IndustryDialog.ChangeListener() {
            @Override
            public void onChangeListener(Industry industry, int position) {
                if (isChange(position)) {
                    sub_position.setText("");
                    subPosition = -1;
                    sub_tradeId.setText("");
                    tvSub.setText(getString(R.string.input_sub_industry));
                }
                tvIndustry.setText(industry.getName());
                industry_position.setText(position + "");
                industry_tradeId.setText(industry.getTradeId() + "");
                industryPosition = position;
                currentIndustryName = industry.getName();
                dialog.dismiss();
            }
        });
        String str = industry_position.getText().toString();
        if (!TextUtils.isEmpty(str)) {
            dialog.setSelect(Integer.parseInt(str));
        } else {
            tvIndustry.setText(getString(R.string.input_industry));
        }
    }

    /**
     * 动态创建认证view
     */
    private void dynamicCreateCertificationView() {
        totalImageToUpload = 0;
        if (subPosition != -1) {
            industryContainer.setVisibility(View.VISIBLE);
            industryContainer.removeAllViews();
            uploadContainer.setVisibility(View.VISIBLE);
            uploadContainer.removeAllViews();
            // 获取二级行业
            TradeCategoryOut tco = subIndustries.get(subPosition);
            //二级行业必选资质。
            List<MustQualify> mustQualifies = tco.getMu();
            //动态创建必须资质
            if (mustQualifies != null && mustQualifies.size() > 0) {
                //uploadContainer.setVisibility(View.VISIBLE);
                //uploadContainer.removeAllViews();
                for (MustQualify m : mustQualifies) {
                    String name = m.getQualifyName();
                    if (name.contains("||")) {
                        CustomizeCredentialUploadView tempView = new CustomizeCredentialUploadView(this);
                        name = name.replace("||", "#");
                        String[] names = name.split("#");
                        RadioGroup radioGroup = tempView.initRadioGroup(this, names);

                        // TODO: 2018/4/20
                        ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
                        radioGroups.add(radioGroup); //下面的圆圈按钮
                        tempView.setUploadOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                currentImageView = v;
                                choosePhoto();
                            }
                        });
                        uploadContainerIndexMap.put(name, uploadContainer.getChildCount());
                        uploadContainer.addView(tempView);
                        totalImageToUpload++;
                    } else if (name.contains("3份")) {
                        CustomizeCredentialUploadView2 tempView2 = new CustomizeCredentialUploadView2(PersonalCertificationUploadActivity.this);
                        tempView2.initRadioGroup(PersonalCertificationUploadActivity.this, new String[]{name});
                        tempView2.setUploadOnClickListener1(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                currentImageView = v;
                                choosePhoto();
                            }
                        });
                        tempView2.setUploadOnClickListener2(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                currentImageView = v;
                                choosePhoto();
                            }
                        });
                        tempView2.setUploadOnClickListener3(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                currentImageView = v;
                                choosePhoto();
                            }
                        });
                        uploadContainerIndexMap.put(name, uploadContainer.getChildCount());
                        uploadContainer.addView(tempView2);
                        totalImageToUpload += 3;
                    } else {
                        CustomizeCredentialUploadView tempView = new CustomizeCredentialUploadView(PersonalCertificationUploadActivity.this);
                        tempView.initRadioGroup(PersonalCertificationUploadActivity.this, new String[]{name});
                        tempView.setUploadOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                currentImageView = v;
                                choosePhoto();
                            }
                        });
                        uploadContainerIndexMap.put(name, uploadContainer.getChildCount());
                        uploadContainer.addView(tempView);
                        totalImageToUpload++;
                    }
                }
            }else{
                // TODO: 2018/4/20 清空要选择的照片。
//                radioGroups.clear();
//                Logger.d("没有可选的资质。。。。");
            }
            //营业范围、动态创建checkbox，多选，以及创建对应的CustomizecredentialUploadView
            List<BusinessScopeOut> bsos = tco.getYib();
            if (bsos != null && bsos.size() > 0) {
                //industryContainer.setVisibility(View.VISIBLE);
                //uploadContainer.setVisibility(View.VISIBLE);
                //清除所有的子view
                //industryContainer.removeAllViews();
                //uploadContainer.removeAllViews();
                //遍历业务范围，

                //上面的方框checkbox按钮。。。
                for (final BusinessScopeOut bso : bsos) {
                    CheckBox checkBox = (CheckBox) getLayoutInflater().inflate(R.layout.activity_enterprise_certification_upload_checkbox_item, null, false);
                    //业务名称
                    final String subBusinessName = bso.getScopeName();
                    checkBox.setText(subBusinessName);
                    industryContainer.addView(checkBox);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                //获取二级业务范围列表
                                List<BusinessScope> bss = bso.getErb();
                                //遍历二级业务
                                if (bss != null && bss.size() > 0) {
                                    for (BusinessScope bs : bss) {
                                        String name = bs.getScopeName();
                                        if (name.contains("||")) {
                                            CustomizeCredentialUploadView tempView = new CustomizeCredentialUploadView(PersonalCertificationUploadActivity.this);
                                            name = name.replace("||", "#");
                                            String[] names = name.split("#");
                                            RadioGroup radioGroup = tempView.initRadioGroup(PersonalCertificationUploadActivity.this, names);
                                            radioGroups.add(radioGroup);
                                            tempView.setUploadOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    currentImageView = v;
                                                    choosePhoto();
                                                }
                                            });
                                            industryAndSubIndexs.put(name, subBusinessName);
                                            uploadContainerIndexMap.put(name, uploadContainer.getChildCount());
                                            uploadContainer.addView(tempView);
                                            totalImageToUpload++;
                                        } else if (name.contains("3份")) {
                                            CustomizeCredentialUploadView2 tempView2 = new CustomizeCredentialUploadView2(PersonalCertificationUploadActivity.this);
                                            tempView2.initRadioGroup(PersonalCertificationUploadActivity.this, new String[]{name});
                                            tempView2.setUploadOnClickListener1(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    currentImageView = v;
                                                    choosePhoto();
                                                }
                                            });
                                            tempView2.setUploadOnClickListener2(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    currentImageView = v;
                                                    choosePhoto();
                                                }
                                            });
                                            tempView2.setUploadOnClickListener3(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    currentImageView = v;
                                                    choosePhoto();
                                                }
                                            });
                                            industryAndSubIndexs.put(name, subBusinessName);
                                            uploadContainerIndexMap.put(name, uploadContainer.getChildCount());
                                            uploadContainer.addView(tempView2);
                                            totalImageToUpload += 3;

                                        } else {
                                            CustomizeCredentialUploadView tempView = new CustomizeCredentialUploadView(PersonalCertificationUploadActivity.this);
                                            tempView.initRadioGroup(PersonalCertificationUploadActivity.this, new String[]{name});
                                            tempView.setUploadOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    currentImageView = v;
                                                    choosePhoto();
                                                }
                                            });
                                            industryAndSubIndexs.put(name, subBusinessName);
                                            uploadContainerIndexMap.put(name, uploadContainer.getChildCount());
                                            uploadContainer.addView(tempView);
                                            totalImageToUpload++;
                                        }
                                    }
                                }
                            } else {
                                String subScope = null;
                                String scope = null;
                                Iterator iter = industryAndSubIndexs.entrySet().iterator();
                                List<String> temp = new ArrayList<String>();
                                while (iter.hasNext()) {

                                    TreeMap.Entry entry = (TreeMap.Entry) iter.next();
                                    // 二级营业范围
                                    subScope = (String) entry.getKey();
                                    //营业范围
                                    scope = (String) entry.getValue();

                                    if (scope == subBusinessName) {
                                        temp.add(subScope);
                                        // 获取value
                                       /* int index = uploadContainerIndexMap.get(subScope);
                                        uploadContainer.removeViewAt(index);
                                        uploadContainerIndexMap.remove(subScope);
                                        industryAndSubIndexs.remove(subScope);*/
                                    }
                                }
                                totalImageToUpload -= temp.size();

                                for (String s : temp) {
                                    if (uploadContainerIndexMap != null) {
                                        int index = uploadContainerIndexMap.get(s);
                                        if (uploadContainer != null && uploadContainer.getChildCount() >= (index + 1)) {
                                            uploadContainer.removeViewAt(index);
                                            if (uploadContainerIndexMap != null && uploadContainerIndexMap.containsKey(s)) {
                                                uploadContainerIndexMap.remove(s);
                                            }
                                            if (industryAndSubIndexs != null && industryAndSubIndexs.containsKey(s)) {
                                                industryAndSubIndexs.remove(s);
                                            }

                                            for (Map.Entry<String, Integer> entry : uploadContainerIndexMap.entrySet()) {
                                                String key = entry.getKey();
                                                int value = entry.getValue();
                                                if (value > index) {
                                                    value--;
                                                    uploadContainerIndexMap.put(key, value);
                                                }
                                                //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            }

        }
    }

    /**
     * 二级行业
     */
    private void showSubIndustryWheel() {
        if (subIndustries != null) {
            final SubIndustryDialog dialog = new SubIndustryDialog(mContext, R.style.Theme_Light_Dialog);
            dialog.setUpData(subIndustries);
            dialog.show();
            dialog.setCancelable(true);
            dialog.setOnchangeListener(new SubIndustryDialog.ChangeListener() {
                @Override
                public void onChangeListener(TradeCategoryOut tradeCategoryOut, int position) {
                    // TODO: 2018/4/20 清空缓存数据啊啊啊
                    radioGroups.clear();
                    tvSub.setText(tradeCategoryOut.getName());
                    sub_position.setText(position + "");
                    sub_tradeId.setText(tradeCategoryOut.getTradeId() + "");
                    subPosition = position;
                    currentSubName = tradeCategoryOut.getName();
                    dialog.dismiss();

                    dynamicCreateCertificationView();
                }
            });
            String str = sub_position.getText().toString();
            if (!TextUtils.isEmpty(str)) {
                dialog.setSelect(Integer.parseInt(str));
            } else {
                tvSub.setText(getString(R.string.input_sub_industry));
            }
        }
    }

    /**
     * 判断是否为空
     */
    private void checkIsUpload() {


    }

    /**
     * 一级行业选择是否发生变化
     *
     * @param position
     * @return
     */
    private boolean isChange(int position) {
        String str = industry_position.getText().toString();
        if (!TextUtils.isEmpty(str)) {
            if (Integer.parseInt(str) != position) {
                return true;
            }
        }
        return false;
    }

    /**
     * 初始化title
     */
    private void initTitle() {
        title.setTitleName(getString(R.string.personal_authentication));
        title.setImageBack(this);
        title.setRightText(getString(R.string.cancel));
        title.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity(PersonalCertificationUploadActivity.class, CertificationActivity.class);
            }
        });
//        title.setLeftImage(R.mipmap.back_white, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
    }

    @Override
    public void setImageBitmap(Bitmap bitmap, String path) {
        if (bitmap != null) {
            companyBitmap = bitmap;
//            Glide.with(this).load(path).into((ImageView) currentImageView);
            GlideUtil.getInstance().loadCertificateImage(this,(ImageView) currentImageView,path,true);
        }
    }

    /**
     * 是否有拍照的权限
     */
    @TargetApi(23)
    public boolean hasPermisson() {
        boolean b1 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean b2 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        boolean b3 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.CAMERA);
        boolean b4 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission_group.CONTACTS);
        boolean b5 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission_group.MICROPHONE);
        boolean b6 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission_group.SMS);
        return b1 && b2 && b3 && b4 && b5 && b6;
    }

    /**
     * 请求拍照的权限
     */
    @TargetApi(23)
    public void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission_group.CONTACTS,
                        Manifest.permission_group.MICROPHONE,
                        Manifest.permission_group.SMS},
                0);
    }

}
