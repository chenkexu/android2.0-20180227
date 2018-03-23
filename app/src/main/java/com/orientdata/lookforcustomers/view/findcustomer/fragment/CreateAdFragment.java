package com.orientdata.lookforcustomers.view.findcustomer.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.util.ImageTools;
import com.orientdata.lookforcustomers.util.SystemUtils;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.findcustomer.CreateAdActivity;
import com.orientdata.lookforcustomers.view.findcustomer.CreateAdActivity2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.AbstractSequentialList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by wy on 2017/11/16.
 * 创建落地页
 */

public class CreateAdFragment extends Fragment implements View.OnClickListener {
    private ImageView iv_upload;
    private EditText et_shop_name;
    private EditText et_phone_no;
    private TextView tv_address;
    private TextView et_page_name;
    private TextView tv_next_step;
    private TextView tv_page_name_num;
    private RelativeLayout rl_show_baidumap;
    private ImageView iv_show_baidumap;

    private String baiduMapPath;//百度地图截图路径
    private String longitude;//经度
    private String dimension;//维度
    private String imagePath;
    private String address;
    private String mSavePaths;
    //private String adImagePath;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initArguments();

    }

    private void initArguments() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            baiduMapPath = bundle.getString("mapPath");
            longitude = bundle.getString("longitude");
            dimension = bundle.getString("dimension");
            address = bundle.getString("address");
            //adImagePath = bundle.getString("adImagePath");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_ad, container, false);
        initView(view);
        //rl_show_baidumap.setBackground(Drawable.createFromPath(baiduMapPath));
        //iv_upload.setBackground(Drawable.createFromPath(adImagePath));

        return view;
    }


    public static CreateAdFragment newInstance() {
        Bundle args = new Bundle();
        CreateAdFragment fragment = new CreateAdFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initView(View view) {
        iv_show_baidumap = view.findViewById(R.id.iv_show_baidumap);
        if (!TextUtils.isEmpty(baiduMapPath)) {
            Glide.with(getActivity()).load(baiduMapPath).into(iv_show_baidumap);
        }
        iv_upload = view.findViewById(R.id.iv_upload);
        et_shop_name = view.findViewById(R.id.et_shop_name);
        et_phone_no = view.findViewById(R.id.et_phone_no);
        tv_address = view.findViewById(R.id.tv_address);
        if (!TextUtils.isEmpty(address)) {
            tv_address.setText(address);
        }
        et_page_name = view.findViewById(R.id.et_page_name);
        tv_next_step = view.findViewById(R.id.tv_next_step);
        tv_next_step.setOnClickListener(this);
        rl_show_baidumap = view.findViewById(R.id.rl_show_baidumap);
        tv_page_name_num = view.findViewById(R.id.tv_page_name_num);
        iv_upload.setOnClickListener(this);
        et_page_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_page_name_num.setText(s.length() + "/8");
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_upload:
                Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);//创建打开相册的意图对象,选择照片
                openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");//传入数据(图片uri,图片类型)
                startActivityForResult(openAlbumIntent, 0);
                break;
            case R.id.tv_next_step:
                String name = et_page_name.getText().toString().trim();
                String sopsName = et_shop_name.getText().toString().trim();
                String sopsIphone = et_phone_no.getText().toString().trim();

                if (TextUtils.isEmpty(sopsName)) {
                    ToastUtils.showShort("请设置店铺名称");
                    return;
                }
                if (TextUtils.isEmpty(sopsIphone)) {
                    ToastUtils.showShort("请设置联系方式");
                    return;
                }
                if (!CommonUtils.isPhoneNum(sopsIphone)) {
                    ToastUtils.showShort("请正确设置联系方式，不支持特殊符号");
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    ToastUtils.showShort("请设置任务名称");
                    return;
                }

                //截屏
                /*View decorView = this.getWindow().getDecorView();
                decorView.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                //获取屏幕整张图片
                Bitmap bitmap = decorView.getDrawingCache();*/
                Intent intent = new Intent(getActivity(), CreateAdActivity2.class);
                intent.putExtra("name", name);
                intent.putExtra("sopsName", sopsName);
                intent.putExtra("sopsIphone", sopsIphone);
                intent.putExtra("longitude", longitude);
                intent.putExtra("dimension", dimension);
                intent.putExtra("baiduMapPath", baiduMapPath);
                intent.putExtra("imagePath", mSavePaths);
                intent.putExtra("address", address);
                //intent.putExtra("adImagePath", adImagePath);
                //startActivityForResult(intent, 2);
                startActivity(intent);
                getActivity().finish();

                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode != RESULT_OK) {
                    return;
                }
                if (data == null) {
                    return;
                }
                Uri uri = data.getData();
                if (SystemUtils.isMIUI()) {
                    imagePath = getPhotoForMiuiSystem(data);
                } else {
                    imagePath = getPhotoForNormalSystem(data);
                }
                Bitmap bitmap1 = getLoacalBitmap(imagePath);
                if (bitmap1 == null) {
                    return;
                }
                //setImageBitmap(bitmap1, path);
                long size = getBitmapsize(bitmap1);
                if (size > 5 * 1024 * 1024) {
//                filePaths.add(path);
                    ((CreateAdActivity) getActivity()).showDefaultLoading();
                    //压缩保存图片
                    final Bitmap tempBitmap = bitmap1;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //TODO 压缩图片
                            try {
                                String savePath1 = Environment.getExternalStorageDirectory() + "/ClipPhoto/cache/";// + System.currentTimeMillis() + ".png";
                                File dir = new File(savePath1);
                                if (!dir.exists()) {
                                    dir.mkdirs();
                                }
                                savePath1 = savePath1 + System.currentTimeMillis() + ".png";
                                //ImageTools.savePhotoToSDCard(ImageTools.compassBitmap(tempBitmap, 4 * 1024), savePath1);
                                ImageTools.compassBitmap(tempBitmap, 5 * 1024, savePath1);

                                mSavePaths = savePath1;
                            } finally {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((CreateAdActivity) getActivity()).hideDefaultLoading();
                                    }
                                });
                            }

                        }
                    }).start();
                } else {
                    mSavePaths = imagePath;
                }

                Glide.with(getActivity()).load(uri).into(iv_upload);
                break;
            /*case 2:
                //((CreateAdActivity)getActivity()).setCurrentItem(1);
                //((CreateAdActivity)getActivity()).getImageWareHouseData();
                getActivity().finish();
                break;*/
        }
    }

    /**
     * 获取bitmap
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            ToastUtils.showShort("该照片不存在，或为空文件");
            return null;
        }
    }

    /**
     * 获取bitmap大小
     *
     * @param bitmap
     * @return
     */
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

    /**
     * 获取图片-miui系统
     *
     * @param data
     * @return
     */
    private String getPhotoForMiuiSystem(Intent data) {
        Uri localUri = data.getData();
        String scheme = localUri.getScheme();
        String imagePath = "";
        if ("content".equals(scheme)) {
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getActivity().getContentResolver().query(localUri, filePathColumns, null, null, null);
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
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
