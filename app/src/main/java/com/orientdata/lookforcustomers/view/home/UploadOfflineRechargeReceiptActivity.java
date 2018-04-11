package com.orientdata.lookforcustomers.view.home;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.runtimepermissions.PermissionsManager;
import com.orientdata.lookforcustomers.util.GlideUtil;
import com.orientdata.lookforcustomers.util.ImageTools;
import com.orientdata.lookforcustomers.util.SystemUtils;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.certification.EnterpriseCertificationUploadActivity;
import com.orientdata.lookforcustomers.view.certification.impl.CertificationActivity;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.ConfirmSubmitDialog;
import com.orientdata.lookforcustomers.widget.dialog.OfflineRechargeConfirmSubmitDialog;
import com.orientdata.lookforcustomers.widget.dialog.SubmitFeedBackDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.OkHttpClientManager;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * 上传充值凭证
 */
public class UploadOfflineRechargeReceiptActivity extends WangrunBaseActivity implements View.OnClickListener {
    private MyTitle title;

    private EditText et_bank_name;
    private EditText et_serial_number;
    private EditText et_account_name;
    private EditText et_account_number;//
    private EditText et_money;//金额
    private ImageView iv_upload;
    private TextView tv_submit;
    private String savePath1;
    private String openAccountBank;
    private String serialNumber;
    private String accountName;
    private String accountNumber;
    private String money;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_offline_recharge_receipt);
        initView();
        initTitle();
    }

    private void initView() {
        title = findViewById(R.id.mt_title);
        et_bank_name = findViewById(R.id.et_bank_name);
        et_serial_number = findViewById(R.id.et_serial_number);
        et_account_name = findViewById(R.id.et_account_name);
        et_account_number = findViewById(R.id.et_account_number);
        et_money = findViewById(R.id.et_money);
        iv_upload = findViewById(R.id.iv_upload);
        iv_upload.setOnClickListener(this);
        tv_submit = findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(this);
    }

    private void initTitle() {
        title.setTitleName("上传凭证");
        title.setImageBack(this);
       /* title.setRightText("上传充值凭证");
        title.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getBaseContext(), UploadOfflineRechargeReceiptActivity.class), 1);
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_upload:
                if (hasPermisson()) {
                    Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);   //创建打开相册的意图对象,选择照片
                    openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");//传入数据(图片uri,图片类型)
                    startActivityForResult(openAlbumIntent, 0);   //开启
                } else {
                    requestPermission();
                }
                break;
            case R.id.tv_submit:
                showRemindDialog();
                break;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = null;
        switch (requestCode) {
            case 0://相册
                if (resultCode != RESULT_OK) {
                    return;
                }
                if (data == null) {
                    return;
                }
                uri = data.getData();
                String path;
                if (SystemUtils.isMIUI()) {
                    path = getPhotoForMiuiSystem(data);
                } else {
                    path = getPhotoForNormalSystem(data);
                }
//                Intent intent3 = new Intent(this, ClipActivity.class);
//                intent3.putExtra("path", path);
//                startActivityForResult(intent3, IMAGE_COMPLETE);
                Bitmap bitmap1 = getLoacalBitmap(path);
                if (bitmap1 == null) {
                    return;
                }
                //setImageBitmap(bitmap1, path);
                long size = getBitmapsize(bitmap1);
                if (size > 5 * 1024 * 1024) {
//                filePaths.add(path);
                    showDefaultLoading();
                    //压缩保存图片
                    final Bitmap tempBitmap = bitmap1;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //TODO 压缩图片
                            savePath1 = Environment.getExternalStorageDirectory() + "/ClipPhoto/cache/";// + System.currentTimeMillis() + ".png";
                            File dir = new File(savePath1);
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            savePath1 = savePath1 + System.currentTimeMillis() + ".png";
                            //ImageTools.savePhotoToSDCard(ImageTools.compassBitmap(tempBitmap, 4 * 1024), savePath1);
                            ImageTools.compassBitmap(tempBitmap, 5 * 1024, savePath1);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    GlideUtil.getInstance().loadAdImage(UploadOfflineRechargeReceiptActivity.this,iv_upload,savePath1,true);
//                                  Glide.with(UploadOfflineRechargeReceiptActivity.this).load(savePath1).into(iv_upload);
                                    hideDefaultLoading();
                                }
                            });
                        }
                    }).start();
                } else {
                    savePath1 = path;
//                    Glide.with(UploadOfflineRechargeReceiptActivity.this).load(savePath1).into(iv_upload);
                    GlideUtil.getInstance().loadAdImage(UploadOfflineRechargeReceiptActivity.this,iv_upload,savePath1,true);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showRemindDialog() {
        openAccountBank = et_bank_name.getText().toString().trim();
        serialNumber = et_serial_number.getText().toString().trim();
        accountName = et_account_name.getText().toString().trim();
        accountNumber = et_account_number.getText().toString().trim();
        money = et_money.getText().toString().trim();
//
//        if (TextUtils.isEmpty(openAccountBank)
//                || TextUtils.isEmpty(serialNumber)
//                || TextUtils.isEmpty(accountName)
//                || TextUtils.isEmpty(accountNumber)
//                || TextUtils.isEmpty(money)
//                ) {
//            ToastUtils.showShort("信息填写不完整！");
//            return;
//        }
        if (TextUtils.isEmpty(openAccountBank)) {
            ToastUtils.showShort("开户行不能为空！");
            return;
        }
        if (TextUtils.isEmpty(serialNumber)) {
            ToastUtils.showShort("流水号不能为空！");
            return;
        }
        if (TextUtils.isEmpty(accountName)) {
            ToastUtils.showShort("账户名不能为空！");
            return;
        }
        if (TextUtils.isEmpty(accountNumber)) {
            ToastUtils.showShort("账号不能为空！");
            return;
        }
        if (TextUtils.isEmpty(money)) {
            ToastUtils.showShort("金额不能为空！");
            return;
        }


        if (TextUtils.isEmpty(savePath1)) {
            ToastUtils.showShort("请上传凭证！");
            return;
        }
        String info = "请注意：\n1.为了您的资金安全请勿重复提交!\n2.改提交超过3个工作日未到账后被退回，您可以确认到账后再次提交。";
        String head = "开户行：" + openAccountBank + "\n"
                + "流水号：" + serialNumber + "\n"
                + "账户名：" + accountName + "\n"
                + "账    号：" + accountNumber + "\n"
                + "金    额：" + money;
        final OfflineRechargeConfirmSubmitDialog dialog = new OfflineRechargeConfirmSubmitDialog(this, head, info);
        dialog.setClickListenerInterface(new OfflineRechargeConfirmSubmitDialog.ClickListenerInterface() {
            @Override
            public void doCancel() {
                dialog.dismiss();
            }

            @Override
            public void doConfirm() {
                dialog.dismiss();
                submit();
            }
        });
        dialog.show();
    }

    private void submit() {
        MDBasicRequestMap map = new MDBasicRequestMap();


        map.put("userId", UserDataManeger.getInstance().getUserId());
        map.put("openAccountBank", openAccountBank);
        map.put("serialNumber", serialNumber);
        map.put("accountName", accountName);
        map.put("accountNumber", accountNumber);
        map.put("money", money);


        // map.put("content", content);
        File[] submitfiles = new File[1];
        String[] submitFileKeys = new String[1];
        submitfiles[0] = new File(savePath1);
        submitFileKeys[0] = 1 + "";
        List<File> fileList = new ArrayList<>();
        fileList.add(new File(savePath1));
        try {
            showDefaultLoading();
            OkHttpClientManager.postAsyn(HttpConstant.SELECT_ADD_OFFLINE_CHARGE, new OkHttpClientManager.ResultCallback<ErrBean>() {
                @Override
                public void onError(Exception e) {
                    hideDefaultLoading();
                    ToastUtils.showShort(e == null ? "服务器小哥正在修复，请稍后重试..." : e.getMessage());
                }

                @Override
                public void onResponse(ErrBean response) {
                    hideDefaultLoading();
                    //ToastUtils.showShort("创建成功");
                    showSubmitFeedbackDialog(response.getCode());
//                    finish();
                }
            }, submitfiles, "file", map);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void showSubmitFeedbackDialog(final int code) {
        String submitStatus = "";
        String confirmText = "";
        int imgRes = 0;
        if (code == 0) {
            //成功
            submitStatus = getString(R.string.sub_suc);
            confirmText = getString(R.string.sub_confirm);
            imgRes = R.mipmap.submit_suc;

        } else {
            //失败
            submitStatus = getString(R.string.sub_fail);
            confirmText = getString(R.string.sub_fail_txt);
            imgRes = R.mipmap.sub_fail;

        }
        final SubmitFeedBackDialog submitFeedBackDialog = new SubmitFeedBackDialog(this, submitStatus, confirmText, imgRes);
        submitFeedBackDialog.show();
        submitFeedBackDialog.setClickListenerInterface(new SubmitFeedBackDialog.ClickListenerInterface() {
            @Override
            public void doCertificate() {
                //消失所有的页面 到首页
                if (code == 0) {
                    submitFeedBackDialog.dismiss();
                    finish();
                } else {
                    submitFeedBackDialog.dismiss();
                }

                // closeActivity(UploadOfflineRechargeReceiptActivity.class,CertificationActivity.class);
            }
        });
    }

    private String getPhotoForMiuiSystem(Intent data) {
        Uri localUri = data.getData();
        String scheme = localUri.getScheme();
        String imagePath = "";
        if ("content".equals(scheme)) {
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(localUri, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imagePath = c.getString(columnIndex);
            c.close();
        } else if ("file".equals(scheme)) {//小米4选择云相册中的图片是根据此方法获得路径
            imagePath = localUri.getPath();
        }
        return imagePath;
    }


    /**
     * 其他系统的相册选择
     *
     * @param data
     */
    private String getPhotoForNormalSystem(Intent data) {
        String filePath = getRealPathFromURI(data.getData());
        return filePath;
    }

    /**
     * 解析Intent.getdata()得到的uri为String型的filePath
     *
     * @param contentUri
     * @return
     */
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            ToastUtils.showShort("该照片不存在，或为空文件");
            return null;
        }
    }

    public long getBitmapsize(Bitmap bitmap) {
        if (bitmap == null) {
            return 0L;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
