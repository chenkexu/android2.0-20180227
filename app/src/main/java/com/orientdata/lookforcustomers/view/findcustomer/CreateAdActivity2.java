package com.orientdata.lookforcustomers.view.findcustomer;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.bean.ErrBean;
import com.orientdata.lookforcustomers.event.AdEvent;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.runtimepermissions.PermissionsManager;
import com.orientdata.lookforcustomers.util.GlideUtil;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.widget.MyTitle;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.OkHttpClientManager;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;
import vr.md.com.mdlibrary.utils.ImageUtils2;


/**
 * Created by wy on 2017/11/16.
 * 创建落地页-保存
 */

public class CreateAdActivity2 extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_shop_name;
    private TextView tv_phone_no;
    private TextView tv_address;
    private TextView tv_save;

    private ImageView iv_upload_show;//上传的图片

    private RelativeLayout rl_show_baidumap;

    private ImageView iv_show_baidumap;//百度地图片的截图

    private LinearLayout ll_clip_layout;

    private String baiduMapPath;//百度地图截图路径
    private String longitude;//经度
    private String dimension;//维度
    private String name;
    private String sopsName;
    private String sopsIphone;
    private String imagePath; //图片路径
    private String address;
    private List<File> fileLists;
    private Dialog progressDialog;
    private MyTitle title;
    private Bitmap bm;


    //private String filePath;

    //rivate String adImagePath;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ad2);
        initView();
        initTitle();
        Intent data = getIntent();
        if (data != null) {

            Logger.d("百度地图截图路径："+baiduMapPath);
            baiduMapPath = data.getStringExtra("baiduMapPath");
            longitude = data.getStringExtra("longitude");
            dimension = data.getStringExtra("dimension");
            name = data.getStringExtra("name");
            sopsName = data.getStringExtra("sopsName");
            sopsIphone = data.getStringExtra("sopsIphone");
            imagePath = data.getStringExtra("imagePath");//广告位图片
            Logger.d("广告位图片路径："+imagePath);
            address = data.getStringExtra("address");
            //adImagePath = data.getStringExtra("adImagePath");
        }
        if (!TextUtils.isEmpty(imagePath)) {
            //iv_upload_show.setBackground(Drawable.createFromPath(imagePath));
//            Glide.with(this).load(imagePath).into(iv_upload_show);
            GlideUtil.getInstance().loadCertificateImage(this,iv_upload_show,imagePath,true);
        } else {
            iv_upload_show.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(baiduMapPath)) {
            //iv_show_baidumap.setBackground(Drawable.createFromPath(baiduMapPath));
//            Glide.with(this).load(baiduMapPath).into(iv_show_baidumap);
            GlideUtil.getInstance().loadCertificateImage(this,iv_show_baidumap,baiduMapPath,true);

        }
        if (!TextUtils.isEmpty(sopsName)) {
            tv_shop_name.setText(sopsName);
        }
        if (!TextUtils.isEmpty(sopsIphone)) {
            tv_phone_no.setText(sopsIphone);
        }
        if (!TextUtils.isEmpty(address)) {
            tv_address.setText(address);
        }

    }

    /**
     * 初始化title
     */
    private void initTitle() {
        title.setTitleName("落地页制作");
        title.setImageBack(this);
        title.setLeftImage(R.mipmap.back_white, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initView() {
        title = findViewById(R.id.mt_title);
        iv_upload_show = (ImageView) findViewById(R.id.iv_upload_show);
        iv_show_baidumap = (ImageView) findViewById(R.id.iv_show_baidumap);
        tv_shop_name = (TextView) findViewById(R.id.tv_shop_name);
        tv_phone_no = (TextView) findViewById(R.id.tv_phone_no);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_save.setOnClickListener(this);
        rl_show_baidumap = (RelativeLayout) findViewById(R.id.rl_show_baidumap);
        ll_clip_layout = (LinearLayout) findViewById(R.id.ll_clip_layout);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:

                if (TextUtils.isEmpty(name)
                        || TextUtils.isEmpty(sopsName)
                        || TextUtils.isEmpty(sopsIphone)
                        || TextUtils.isEmpty(longitude)
                        || TextUtils.isEmpty(dimension)
                    //|| TextUtils.isEmpty(adImagePath)
                    //|| TextUtils.isEmpty(imagePath)
                        ) {
                    ToastUtils.showShort("信息不完整！");
                    return;
                }
                final MDBasicRequestMap map = new MDBasicRequestMap();
                map.put("userId", UserDataManeger.getInstance().getUserId());
                map.put("token", UserDataManeger.getInstance().getUserToken());
                map.put("name", name);//页面名称
                map.put("sopsName", sopsName);//店铺名称
                map.put("sopsIphone", sopsIphone);//店铺电话
                map.put("longitude", longitude);//经度
                map.put("dimension", dimension);//维度
                map.put("throwAdress", address);//落地页投放位置

                String url = HttpConstant.INSERT_USER_PAGE;//用户制作落地页请求url

                File[] submitfiles = null; //要提交的图片文件

                if (!TextUtils.isEmpty(imagePath)) { //如果上传的图片不为空
                    fileLists = new ArrayList<>();
                    //  TODO 文件压缩
                    fileLists.add(ImageUtils2.compressFile(imagePath));
//                  fileLists.add(new File(imagePath));
                    submitfiles = new File[fileLists.size()];
                    //fileLists.add(new File(adImagePath));

                    String[] submitFileKeys = new String[fileLists.size()];
                    for (int i = 0; i < fileLists.size(); i++) {
                        submitfiles[i] = fileLists.get(i);//图片数组添加提交的图片
                        submitFileKeys[i] = i + "";
                    }
                    List<File> fileList = new ArrayList<>();
                    for (int i = 0; i < fileLists.size(); i++) {
                        fileList.add(fileLists.get(i));
                    }
                }else {  //如果上传的图片为空
                    fileLists = new ArrayList<>();
                    //  TODO
                    String path = Environment.getExternalStorageDirectory() + "/ClipPhoto/cache/";// + System.currentTimeMillis() + ".png";
                    File dir = new File(path);
                    if (!dir.exists()){
                        dir.mkdirs();
                    }
                    path = path + System.currentTimeMillis() + ".png";
                    File file = new File(path);
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    fileLists.add(file);
                    submitfiles = new File[fileLists.size()];
                    //fileLists.add(new File(adImagePath));
                    String[] submitFileKeys = new String[fileLists.size()];
                    for (int i = 0; i < fileLists.size(); i++) {
                        submitfiles[i] = fileLists.get(i);
                        submitFileKeys[i] = i + "";
                    }
                    List<File> fileList = new ArrayList<>();

                    for (int i = 0; i < fileLists.size(); i++) {
                        fileList.add(fileLists.get(i));
                    }
                }
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
                            if (response.getCode() == 0) {
                                ToastUtils.showShort("保存成功！");
                                //setResult(RESULT_OK);
                                AdEvent adEvent = new AdEvent();
                                adEvent.complete = true;
                                EventBus.getDefault().post(adEvent);
                                finish();
                            }
                        }
                    }, submitfiles, "file", map);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

    }














    /**
     * 显示默认的进度条
     */
    protected void showDefaultLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        } else {
            progressDialog = null;
        }

        progressDialog = new Dialog(this, R.style.loadingDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.content__roll_loading, null);
        progressDialog.setContentView(view);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    /**
     * 隐藏默认的进度条
     */
    protected void hideDefaultLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }























    //确认，生成图片
    public void confirm(View view, String path) {
        bm = loadBitmapFromView(view);
        try {
            bm.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(path));
//            Toast.makeText(this, "图片已保存至：SD卡根目录/imageWithText.jpg", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //mPresent.uploadImg(2,filePath);

    }

    //以图片形式获取View显示的内容（类似于截图）
    public static Bitmap loadBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * 是否有拍照的权限
     */
    @TargetApi(23)
    public boolean hasPermisson() {
        boolean b1 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean b2 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return b1 && b2;
    }

    /**
     * 请求拍照的权限
     */
    @TargetApi(23)
    public void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                0);
    }


}
