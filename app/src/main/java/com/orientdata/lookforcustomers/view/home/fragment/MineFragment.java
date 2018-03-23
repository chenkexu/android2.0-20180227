package com.orientdata.lookforcustomers.view.home.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseFragment;
import com.orientdata.lookforcustomers.base.WangrunBaseFragment;
import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.bean.MyInfoBean;
import com.orientdata.lookforcustomers.bean.URLBean;
import com.orientdata.lookforcustomers.event.UploadImgEvent;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.presenter.HomePresent;
import com.orientdata.lookforcustomers.runtimepermissions.PermissionsManager;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.util.GlideUtil;
import com.orientdata.lookforcustomers.util.L;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.certification.impl.CertificationActivity;
import com.orientdata.lookforcustomers.view.findcustomer.CreateFindCustomerActivity;
import com.orientdata.lookforcustomers.view.home.IHomeView;
import com.orientdata.lookforcustomers.view.home.InvoiceActivity;
import com.orientdata.lookforcustomers.view.mine.ShareToGetCommissionActivity;
import com.orientdata.lookforcustomers.view.mine.imple.AccountBalanceActivity;
import com.orientdata.lookforcustomers.view.mine.imple.CommissionWithDrawActivity;
import com.orientdata.lookforcustomers.view.mine.imple.MyCommissionActivity;
import com.orientdata.lookforcustomers.view.mine.imple.SettingActivity;
import com.orientdata.lookforcustomers.widget.CircleImageView;
import com.orientdata.lookforcustomers.widget.dialog.RemindDialog;
import com.orientdata.lookforcustomers.widget.toggleButton.RoundImageView;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.OkHttpClientManager;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

import static android.app.Activity.RESULT_OK;

/**
 * Created by wy on 2017/10/30.
 * 我的页面Fragment
 */

public class MineFragment extends WangrunBaseFragment<IHomeView, HomePresent<IHomeView>>
        implements IHomeView, View.OnClickListener {
    private RelativeLayout rl_invoice;
    private TextView tv_my_commission;
    private TextView tv_balance_account;
    private TextView iv_setting; //设置
    private ImageView blur; //背景图片

    private Map<String, Object> mMapInfoBeans;

    private TextView tv_company_name;//公司名字
    private TextView tv_phone_no;
    private RelativeLayout rl_my_commission;
    private String name;
    private String userHead;
    private String phone;
    private String moreMoney;
    private Double commission;
    private String subCount;
    private Double balance;
    private Double frozenAmount;
    private RelativeLayout rl_commission_withdraw;
    private RelativeLayout rl_service;
    private RelativeLayout rl_share;
    private RelativeLayout linear_company;
    private RoundImageView iv_head_portrait; //用户头像
    private static final int CROP_SMALL_PICTURE = 103;
    public static final String IMAGE_FILE_LOCATION = "file:///sdcard/tempHeadPortrait.jpg";//剪切完的图片所存地址
    public static final String IMAGE_FILE_LOCATION_Path = Environment.getExternalStorageDirectory() + "/tempHeadPortrait.jpg";//剪切完的图片所存地址
    Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(view);
        getData();
        return view;
    }

    /*@Override
    public void onStart() {
        super.onStart();
        getData();
    }*/

    private void initView(View view) {
        rl_invoice = view.findViewById(R.id.rl_invoice);
//        blur = view.findViewById(R.id.blur);
        rl_my_commission = view.findViewById(R.id.rl_my_commission);
        iv_head_portrait = view.findViewById(R.id.iv_head_portrait);
        rl_invoice.setOnClickListener(this);
//        tv_my_commission.setOnClickListener(this);
        tv_company_name = view.findViewById(R.id.tv_company_name);
        rl_commission_withdraw = view.findViewById(R.id.rl_commission_withdraw);
        tv_phone_no = view.findViewById(R.id.tv_phone_no);
        tv_balance_account = view.findViewById(R.id.tv_balance_account);
        iv_setting = view.findViewById(R.id.iv_setting);
        linear_company = view.findViewById(R.id.linear_company);
        rl_share = view.findViewById(R.id.rl_share);
        rl_service = view.findViewById(R.id.rl_service);
        rl_commission_withdraw.setOnClickListener(this);
        tv_balance_account.setOnClickListener(this);
        iv_setting.setOnClickListener(this);
        rl_share.setOnClickListener(this);
        linear_company.setOnClickListener(this);
        rl_service.setOnClickListener(this);
        iv_head_portrait.setOnClickListener(this);
        rl_my_commission.setOnClickListener(this);
    }


    public void getData() {
        showDefaultLoading();
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());


        OkHttpClientManager.postAsyn(HttpConstant.SELECT_SHOW_MY_INFO, new OkHttpClientManager.ResultCallback<MyInfoBean>() {
            @Override
            public void onError(Exception e) {
                ToastUtils.showShort(e.getMessage());
                hideDefaultLoading();
            }

            @Override
            public void onResponse(MyInfoBean response) {
                if (response.getCode() == 0) {
                    if (response.getResult() == null || response.getResult().size() <= 0) {
                        hideDefaultLoading();
                        return;
                    }
                    //TODO
                    mMapInfoBeans = response.getResult();
                    //是 String 名称
                    if (mMapInfoBeans.containsKey("name")) {
                        tv_company_name.setVisibility(View.VISIBLE);
                        name = (String) mMapInfoBeans.get("name");
                        if (!TextUtils.isEmpty(name)) {
                            tv_company_name.setText(name);
                        } else {
                            tv_company_name.setVisibility(View.GONE);
                        }
                    } else {
                        tv_company_name.setVisibility(View.GONE);
                    }

                    /*        userHead
                    是 String 头像路径*/
                    if (mMapInfoBeans.containsKey("userHead")) {
                        userHead = (String) mMapInfoBeans.get("userHead");
                    }
                    if (!TextUtils.isEmpty(userHead)) {
                        L.e("头像地址是："+userHead);
//                        Glide.with(getContext()).load(userHead).bitmapTransform(new BlurTransformation(getContext(), 25),
//                                new CenterCrop(getContext())).into(blur);
//                        Glide.with(getContext()).load(userHead).into(iv_head_portrait);
                        GlideUtil.getInstance().loadImage(getContext(),iv_head_portrait,userHead,true);
//                        Glide.with(getContext()).load(userHead).bitmapTransform(new CropCircleTransformation(getContext()))
//                                .into(iv_head_portrait);
                    } else {
                        Glide.with(getContext()).load(R.mipmap.head_portrait).bitmapTransform(new CropCircleTransformation(getContext()))
                                .into(iv_head_portrait);
                    }
                    /*        phone
                    是 String 手机号*/
                    if (mMapInfoBeans.containsKey("phone")) {
                        phone = (String) mMapInfoBeans.get("phone");
                        if (!TextUtils.isEmpty(phone)) {
                            tv_phone_no.setText(phone);
                        }
                    }
                           /* commission
                    是 bigdecimal 佣金*/
                    if (mMapInfoBeans.containsKey("commission")) {
                        commission = (Double) mMapInfoBeans.get("commission");
                    }
                    /*        moreMoney
                    是 String 最低提现金额*/
                    if (mMapInfoBeans.containsKey("moreMoney")) {
                        moreMoney = (String) mMapInfoBeans.get("moreMoney");
                    }
                    /*        subCount
                    是 String 每日可提现次数*/
                    if (mMapInfoBeans.containsKey("subCount")) {
                        subCount = (String) mMapInfoBeans.get("subCount");
                    }

                    /*        balance
                    是 bigdecimal 余额*/
                    if (mMapInfoBeans.containsKey("balance")) {
                        balance = (Double) mMapInfoBeans.get("balance");
                    }
                    /*        frozenAmount
                    是 bigdecimal 冻结金额*/

                    if (mMapInfoBeans.containsKey("frozenAmount")) {
                        frozenAmount = (Double) mMapInfoBeans.get("frozenAmount");
                    }


                }
                hideDefaultLoading();
            }
        }, map);
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.rl_invoice: //开发票
                intent = new Intent(getActivity(), InvoiceActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_my_commission: //我的佣金
                intent = new Intent(getActivity(), MyCommissionActivity.class);
                intent.putExtra("moreMoney", moreMoney);
                intent.putExtra("commission", commission);
                intent.putExtra("subCount", subCount);
                startActivity(intent);
                break;
            case R.id.rl_commission_withdraw://佣金提现
                //佣金提现
//                if(commission<Integer.parseInt(moreMoney)){
//                    ToastUtils.showShort("余额不足,无法提现！");
//                    return;
//                }
                intent = new Intent(getContext(), CommissionWithDrawActivity.class);
                intent.putExtra("moreMoney", moreMoney);
                intent.putExtra("commission", commission);
                intent.putExtra("subCount", subCount);
                startActivity(intent);
                break;
            case R.id.tv_balance_account: //账户余额
                intent = new Intent(getActivity(), AccountBalanceActivity.class);
                intent.putExtra("balance", balance);
                intent.putExtra("frozenAmount", frozenAmount);
                startActivity(intent);
                break;
            case R.id.iv_setting: //设置
                intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_share: //分享
                //赚取佣金 获取Url
                if (hasSharePermisson()){
                    showDefaultLoading();
                    MDBasicRequestMap map = new MDBasicRequestMap();
                    map.put("userId", UserDataManeger.getInstance().getUserId());
                    OkHttpClientManager.postAsyn(HttpConstant.CREATE_URL, new OkHttpClientManager.ResultCallback<URLBean>() {
                        @Override
                        public void onError(Exception e) {
                            ToastUtils.showShort(e.getMessage());
                            hideDefaultLoading();
                        }

                        @Override
                        public void onResponse(URLBean re) {
                            if (re.getCode() == 0) {
                                String url = re.getResult();
                                if (!TextUtils.isEmpty(url)) {
                                    Intent intent1 = new Intent(getContext(), ShareToGetCommissionActivity.class);
                                    intent1.putExtra("url", url);
                                    startActivity(intent1);
                                } else {
                                    ToastUtils.showShort("获取分享链接失败");
                                }
                            }
                            hideDefaultLoading();
                        }
                    }, map);
                }else {
                    requestSharePermission();
                }


                break;
            case R.id.linear_company: //账户认证
                mPresent.getCertificateMsg(true);
                break;
            case R.id.rl_service://联系客服
                final String url = "mqqwpa://im/chat?chat_type=wpa&uin=2280249239";

                if (checkApkExist(getActivity(), "com.tencent.mobileqq")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } else {
                    ToastUtils.showShort("本机未安装QQ应用,请下载安装。");
                }
                break;
            case R.id.iv_head_portrait: //点击头像
                choosePhoto();
                break;
        }
    }

    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static final int PHOTOZOOM = 0; // 相册/拍照

    //选择照片
    private void choosePhoto() {
        if (CommonUtils.haveSDCard()) {
            if (hasPermisson()) {
                Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);   //创建打开相册的意图对象,选择照片
                openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");//传入数据(图片uri,图片类型)
                startActivityForResult(openAlbumIntent, PHOTOZOOM);   //开启
            } else {
                requestPermission();
            }
        } else {
            Toast.makeText(getActivity(), "没有SD卡!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 请求拍照的权限
     */
    @TargetApi(23)
    public void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission_group.CONTACTS,
                        Manifest.permission_group.MICROPHONE,
                        Manifest.permission_group.SMS},
                0);
    }

    /**
     * 是否有拍照的权限
     */
    @TargetApi(23)
    public boolean hasPermisson() {
        boolean b1 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean b2 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        boolean b3 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.CAMERA);
        boolean b4 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission_group.CONTACTS);
        boolean b5 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission_group.MICROPHONE);
        boolean b6 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission_group.SMS);
        return b1 && b2 && b3 && b4 && b5 && b6;
    }

    Bitmap bitmap1 = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = null;
        switch (requestCode) {
            case PHOTOZOOM://相册
                if (resultCode != RESULT_OK) {
                    return;
                }
                if (data == null) {
                    return;
                }
                cropImageUri(data.getData(), 720, 422, CROP_SMALL_PICTURE);
                break;

            case CROP_SMALL_PICTURE:
                //这里是调用图片库裁剪后的返回
                //可以参照裁剪方法，里面已经指定了uri，所以在这里，直接可以从里面取uri，然后获取bitmap，并且设置到imageview
                try {
                    bitmap1 = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
//                    compressImg(bitmap1);
                    //上传图片
                    if (bitmap1 != null) {
                        File file = new File(IMAGE_FILE_LOCATION_Path);
                        if (file.exists()) {
                            mPresent.uploadImg(2, IMAGE_FILE_LOCATION_Path);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
//        File file = new File(IMAGE_FILE_LOCATION);
//        if(file.exists()){
//            file.delete();
//        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //是否裁剪
        intent.putExtra("crop", "true");
//        //设置xy的裁剪比例
//        intent.putExtra("aspectX", 72);
//        intent.putExtra("aspectY", 42);
        //设置输出的宽高
//        intent.putExtra("outputX", outputX);
//        intent.putExtra("outputY", outputY);
        //是否缩放
        intent.putExtra("scale", false);
        //输入图片的Uri，指定以后，可以在这个uri获得图片
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //是否返回图片数据，可以不用，直接用uri就可以了
        intent.putExtra("return-data", false);
        //设置输入图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //是否关闭面部识别
        intent.putExtra("noFaceDetection", true); // no face detection
        //启动
        startActivityForResult(intent, requestCode);
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
    public void getCertificateMsg(CertificationOut certificationOut, boolean isCertificate) {
        String cerStatus = "";
        String remindString = "";
        int imgResId = 0;
        String btText = "";
        int authStatus = -1;
        if (certificationOut == null) {
            //未认证
            cerStatus = getString(R.string.no_cer);
            remindString = getString(R.string.no_certified);
            imgResId = R.mipmap.no_certified;
            btText = getString(R.string.go_cer);
        } else {
            //认证状态 1审核中 2审核通过 3审核拒绝
            authStatus = certificationOut.getAuthStatus();
            if (authStatus == 1 || authStatus == 4) {
                //审核中
                cerStatus = getString(R.string.cer_ing);
                remindString = getString(R.string.cer_waiting);
                imgResId = R.mipmap.audit;
                btText = getString(R.string.go_watch);
            } else if (authStatus == 3) {
                //审核拒绝
                cerStatus = getString(R.string.no_pass);
                remindString = getString(R.string.not_pass);
                imgResId = R.mipmap.not_pass;
                btText = getString(R.string.re_go_cer);
            }
        }
        if (authStatus != 2) {
            final RemindDialog dialog = new RemindDialog(getContext(), cerStatus, remindString, imgResId, btText);
            dialog.setClickListenerInterface(new RemindDialog.ClickListenerInterface() {
                @Override
                public void doCertificate() {
                    dialog.dismiss();
                    startActivity(new Intent(getContext(), CertificationActivity.class));
                }
            });
            dialog.setCancelable(true);
            dialog.show();
        } else {
            //审核通过
            if (isCertificate) {
                startActivity(new Intent(getContext(), CertificationActivity.class));
            } else {
                startActivity(new Intent(getContext(), CreateFindCustomerActivity.class));
            }
        }
    }

    @Subscribe
    public void uploadHeadPortrait(UploadImgEvent uploadImgEvent) {
        if (uploadImgEvent != null) {
            if (uploadImgEvent.uploadPicBean.getCode() == 0) {
//                ToastUtils.showShort("换头像成功");
            }
        }
    }

    @Override
    protected HomePresent<IHomeView> createPresent() {
        return new HomePresent<>(this);
    }

    /**
     * 是否有拍照的权限
     */
    @TargetApi(23)
    public boolean hasSharePermisson() {
        boolean b1 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.ACCESS_NETWORK_STATE);
        boolean b2 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.ACCESS_WIFI_STATE);
        boolean b3 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.READ_PHONE_STATE);
        boolean b4 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean b5 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        boolean b6 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.INTERNET);
        boolean b7 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        boolean b8 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
        boolean b9 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
        return b1 && b2 && b3 && b4 && b5 && b6 && b7 && b8 && b9;
    }

    /**
     * 请求拍照的权限
     */
    @TargetApi(23)
    public void requestSharePermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS},
                0);
    }

}
