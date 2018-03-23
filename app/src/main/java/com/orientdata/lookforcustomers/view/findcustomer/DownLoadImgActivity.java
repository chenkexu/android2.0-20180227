package com.orientdata.lookforcustomers.view.findcustomer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.event.DownloadImgEvent;
import com.orientdata.lookforcustomers.network.okhttp.bean.OkError;
import com.orientdata.lookforcustomers.network.okhttp.callback.IResponseCallback;
import com.orientdata.lookforcustomers.network.okhttp.manager.NetManager;
import com.orientdata.lookforcustomers.network.okhttp.manager.OkClient;
import com.orientdata.lookforcustomers.network.okhttp.manager.ParamManager;
import com.orientdata.lookforcustomers.network.okhttp.progress.ProgressListener;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.certification.EnterpriseCertificationUploadActivity;
import com.orientdata.lookforcustomers.view.certification.impl.CertificationActivity;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.dialog.SubmitFeedBackDialog;
import com.squareup.okhttp.internal.http.Transport;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by wy on 2017/12/4.
 * 模板图片下载
 */

public class DownLoadImgActivity extends WangrunBaseActivity{
    private static  final int DOWN_START  = 1;
    private static  final int DOWN_SUCCESS  = 2;
    private static  final int DOWN_FAILED  = 3;
    private static  final int DOWN_PROGRESS  = 4;
    private int totlefile = 0;
    String[] str = null;
    private TextView tvProgress;
    private ProgressBar progressBar;
    private MyTitle titleImg;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_download);
        str = getIntent().getStringArrayExtra("urls");
        totlefile = str.length;
        progressBar = findViewById(R.id.progressBar);
        tvProgress = findViewById(R.id.tvProgress);
        titleImg = findViewById(R.id.titleImg);
        initTitle();
        if(str!=null && str.length>0){
            downloadfile(str);
        }
    }
    private void initTitle() {
        titleImg.setTitleName("下载");
        titleImg.setImageBack(this);
    }

        /**
         * 下载文件
         *
         * @param downloadurls
         */
    private int nowfile = 0;
    public void downloadfile(String downloadurls[]) {

        OkClient.download(3, ParamManager.download(downloadurls[nowfile]), null, new IResponseCallback() {
            @Override
            public void onSuccess(int tag, Object object) {
                Log.e("DownLoadImgActivity", "SUCCESS");
                handler.sendEmptyMessage(DOWN_SUCCESS);
            }

            @Override
            public void onError(int tag, OkError error) {
                Log.i("DownLoadImgActivity", "downFailed:异常： " + error);
                handler.sendEmptyMessage(DOWN_FAILED);
            }
        }, new ProgressListener() {
            @Override
            public void onProgress(long bytesWritten, long contentLength, long percent) {
                Log.e("DownLoadImgActivity", percent + "%");
                Message msg = new Message();
                msg.what = DOWN_PROGRESS;
                msg.arg1 = (int) percent;
                handler.sendMessage(msg);

            }
        });

    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DOWN_START:
//                    downDialog.updateView(0, "准备下载");
//                    ToastUtils.showShort("准备下载");
                    tvProgress.setText("图片"+nowfile+"正在拼命下载中。。。");
                    break;
                case DOWN_PROGRESS:
                    int progress = msg.arg1;
//                    downDialog.updateView(progress, (nowfile + 1) + "/" + totlefile);
//                    ToastUtils.showShort(progress+"  --  "+(nowfile + 1) + "/" + totlefile);
                    progressBar.setProgress(progress);

                    break;
                case DOWN_SUCCESS:
                    nowfile = nowfile + 1;
//                    if (htmltype == 4) {
//                        downloadthumb();
//                    }
                    if (nowfile == totlefile) {
                        Log.i("DownLoadImgActivity", "下载全部完成" + nowfile);
                        ToastUtils.showShort("图片"+nowfile+"下载全部完成");
//                        downDialog.dissmiss();
//                        downthumb = null;
//                        downVideoUrls = null;
                        nowfile=0;
                        totlefile=0;
//                        finish();
                        progressBar.setProgress(100);
                        showDialog();

                    } else {
//                        downDialog.updateView(100, "");
//                        downDialog.updateView(0, "准备下载");
                        downloadfile(str);

                    }

                    break;
                case 3:
//                    downDialog.updateView(0, "下载异常");
                    ToastUtils.showShort("下载异常");
                    break;
            }

        }
    };
    private void showDialog(){
        final SubmitFeedBackDialog submitFeedBackDialog = new SubmitFeedBackDialog(this,"下载完成！","确定",R.mipmap.submit_suc);
        submitFeedBackDialog.show();
        submitFeedBackDialog.setClickListenerInterface(new SubmitFeedBackDialog.ClickListenerInterface() {
            @Override
            public void doCertificate() {
                submitFeedBackDialog.dismiss();
                closeActivity(DownLoadImgActivity.class);
                DownloadImgEvent downloadImgEvent = new DownloadImgEvent();
                downloadImgEvent.isSucDownload = true;
                EventBus.getDefault().post(downloadImgEvent);
            }
        });
    }
    /**
     * 取消请求
     *
     * @param tag
     */
    public static void cancelRequest(int... tag) {
        NetManager.getInstance().cancelRequest(tag);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelRequest();
    }
}
