package com.orientdata.lookforcustomers.view.findcustomer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseFragment;
import com.orientdata.lookforcustomers.bean.Area;
import com.orientdata.lookforcustomers.bean.AreaOut;
import com.orientdata.lookforcustomers.bean.CertificationBean;
import com.orientdata.lookforcustomers.bean.CertificationOut;
import com.orientdata.lookforcustomers.bean.PicListBean;
import com.orientdata.lookforcustomers.bean.UploadPicBean;
import com.orientdata.lookforcustomers.bean.UserPicStore;
import com.orientdata.lookforcustomers.event.DownloadImgEvent;
import com.orientdata.lookforcustomers.event.ImgClipResultEvent;
import com.orientdata.lookforcustomers.event.PicDeleteResultEvent;
import com.orientdata.lookforcustomers.event.PicListResultEvent;
import com.orientdata.lookforcustomers.network.okhttp.bean.OkError;
import com.orientdata.lookforcustomers.network.okhttp.callback.IResponseCallback;
import com.orientdata.lookforcustomers.network.okhttp.manager.OkClient;
import com.orientdata.lookforcustomers.network.okhttp.manager.ParamManager;
import com.orientdata.lookforcustomers.network.okhttp.progress.ProgressListener;
import com.orientdata.lookforcustomers.network.util.AppConfig;
import com.orientdata.lookforcustomers.presenter.ImgPresent;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.findcustomer.DownLoadImgActivity;
import com.orientdata.lookforcustomers.view.findcustomer.IImgView;
import com.orientdata.lookforcustomers.view.findcustomer.impl.AddAdvertiseImgActivity;
import com.orientdata.lookforcustomers.widget.dialog.ConfirmSubmitDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by wy on 2017/11/16.
 * 图片库
 */

public class PictureLibraryFragment extends BaseFragment implements IImgView, View.OnClickListener,AddAdvertiseImgActivity.ClickCancel {
    private GridView pictureList;
    private ImgPresent mImgPresent;
    private ArrayList<UserPicStore> picList = null;//数据
    private ArrayList<UserPicStore> picListChoose = null;//选择的集合
    private TextView tvChoose;
    private ImageView tvDowload;
    private AlbumGridViewAdapter adapter;
    private TextView tv;
    private ImageView iv_delete;
    private RelativeLayout rl_bottom;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture_library,container,false);
        initView(view);
        mImgPresent.getPictureList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView(View view){
        picListChoose = new ArrayList<>();
        mImgPresent = ((AddAdvertiseImgActivity) getActivity()).getPresent();
        pictureList = view.findViewById(R.id.pictureList);
        tvChoose = view.findViewById(R.id.tvChoose);
        tvDowload = view.findViewById(R.id.tvDowload);
        rl_bottom = view.findViewById(R.id.rl_bottom);
        iv_delete = view.findViewById(R.id.iv_delete);
        tvChoose.setOnClickListener(this);
        tvDowload.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        tv = (TextView) view.findViewById(R.id.myText);
        pictureList.setEmptyView(tv);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && (getActivity())!=null){
            ((AddAdvertiseImgActivity)getActivity()).setDeleteInterFace(this);
        }
        if(isEdit){
            ((AddAdvertiseImgActivity)getActivity()).setCancelVisible(View.VISIBLE);
            updateDownDeleteView();
        }
    }

    public static PictureLibraryFragment newInstance() {
        Bundle args = new Bundle();
        PictureLibraryFragment fragment = new PictureLibraryFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private  boolean isEdit = false;
    private void initListener() {
        adapter.setOnLongItemClickListener(new AlbumGridViewAdapter.OnLongItemClickListener() {
            @Override
            public void onLongItemClick1(View view) {
                if(!isEdit){
                    for(int i = 0;i< picList.size();i++){
                        picList.get(i).setEdit(true);
                    }
                    adapter.notifyDataSetChanged();
                    isEdit = true;
                    ((AddAdvertiseImgActivity)getActivity()).setCancelVisible(View.VISIBLE);
                    rl_bottom.setVisibility(View.VISIBLE);
                }
            }
        });
        adapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(final RelativeLayout toggle, int position, ToggleButton tbutton) {
                if(isEdit){
                    if (tbutton.isChecked()) { //如果已经选中
                        //删除 choose
                        tbutton.setChecked(false);

                        for(int i=0;i<picListChoose.size();i++){
                            if((picListChoose.get(i).getUserPicStoreId()== picList.get(position).getUserPicStoreId())){
                                picListChoose.remove(i);
                                break;
                            }
                        }

                        tvChoose.setText("全选");
                    } else { //没有选中
                        //增加
                        tbutton.setChecked(true);
                        picListChoose.add(picList.get(position));
                        if(picListChoose.size() == picList.size()){
                            tvChoose.setText("取消全选");
                        }
                    }
                    updateDownDeleteView();
                }else{
                    downloadfile(picList.get(position).getPicUrl());
                }
            }
        });
    }
    public void downloadfile(final String url) {
        final String path = getUrl(url);

        OkClient.download(3, ParamManager.download(url), null, new IResponseCallback() {
            @Override
            public void onSuccess(int tag, Object object) {
                Log.e("DownLoadImgActivity", "SUCCESS");
                //不是编辑 直接返回 添加到广告位上
                ImgClipResultEvent imgClipResultEvent = new ImgClipResultEvent();
                //imgClipResultEvent.path = IMAGE_FILE_LOCATION;
                //imgClipResultEvent.bitmap = bitmap1;
                imgClipResultEvent.library_url = url;
                imgClipResultEvent.path = Environment.getExternalStorageDirectory()+AppConfig.LOCAL_PRODUCT_DOWNLOAD_PHOTOPATH+ path;
                EventBus.getDefault().post(imgClipResultEvent);
                if (getActivity() != null){
                    getActivity().finish();
                }
            }

            @Override
            public void onError(int tag, OkError error) {
                Log.i("DownLoadImgActivity", "downFailed:异常： " + error);
            }
        }, new ProgressListener() {
            @Override
            public void onProgress(long bytesWritten, long contentLength, long percent) {

            }
        });

    }
    public String getUrl(String url){
        String suffixes="avi|mpeg|3gp|mp3|mp4|wav|jpeg|gif|jpg|png|apk|exe|pdf|rar|zip|docx|doc";
        Pattern pat=Pattern.compile("[\\w]+[\\.]("+suffixes+")");//正则判断
        Matcher mc=pat.matcher(url);//条件匹配
        String substring = "";
        while(mc.find()){
            substring = mc.group();//截取文件名后缀名
            Log.e("==", "substring = "+substring);
        }
        return substring;
    }

    /**
     * 更新下载 删除按钮 是否可点
     */
    private void updateDownDeleteView(){
        if(picListChoose == null || picListChoose.size() == 0){
            //图标是暗的
            tvDowload.setEnabled(false);
            iv_delete.setEnabled(false);
            tvDowload.setImageResource(R.mipmap.download);
            iv_delete.setImageResource(R.mipmap.image_delete);
        }else{
            tvDowload.setEnabled(true);
            iv_delete.setEnabled(true);
            tvDowload.setImageResource(R.mipmap.download_check);
            iv_delete.setImageResource(R.mipmap.image_delete_check);
        }
    }
    private void chooseAll(){
        if(picList == null || picList.size() == 0){
            ToastUtils.showShort("数据为空，请先上传图片到图库！");
            return;
        }
        String str = tvChoose.getText().toString();
        if("全选".equals(str)){
            if(picListChoose!=null && picListChoose.size()>0){
                picListChoose.clear();
            }
            picListChoose.addAll(picList);
            tvChoose.setText("取消全选");
        }else{
            if(picListChoose!=null && picListChoose.size()>0){
                picListChoose.clear();
            }
            tvChoose.setText("全选");
        }
        updateDownDeleteView();
        adapter.notifyDataSetChanged();
    }
    @Subscribe
    public void picListResult(PicListResultEvent picListResultEvent) {
        this.picList = (ArrayList<UserPicStore>) picListResultEvent.picList;
        adapter = new AlbumGridViewAdapter(getContext(),picList,picListChoose);
        pictureList.setAdapter(adapter);
        updateDownDeleteView();
        initListener();
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void uploadPicSuc(UploadPicBean uploadPicBean) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvChoose:
                //全选
                chooseAll();

                break;
            case R.id.tvDowload:
                //下载图片
                if(picListChoose == null || picListChoose.size() == 0){
                    ToastUtils.showShort("请选择下载图片！");
                    return;
                }
                String[] str = new String[picListChoose.size()];
                for(int i=0;i<picListChoose.size();i++){
                    str[i] = picListChoose.get(i).getPicUrl();
                }
                Intent intent = new Intent(getContext(), DownLoadImgActivity.class);
                intent.putExtra("urls",str);
                getContext().startActivity(intent);

                break;
            case R.id.iv_delete:
                //删除
                if(picListChoose == null || picListChoose.size() == 0){
                    ToastUtils.showShort("请选择删除的图片！");
                    return;
                }
                StringBuilder str1 = new StringBuilder();
                for(UserPicStore userPicStore:picListChoose){
                    str1.append(userPicStore.getUserPicStoreId()+",");
                }
                //删除对话框
                showRemindDialog(str1.toString());
                break;
        }

    }


    //删除对话框
    private void showRemindDialog(final String strDelete){
        final ConfirmSubmitDialog dialog = new ConfirmSubmitDialog(getContext(),"确定删除？","删除后将无法找回");
        dialog.setClickListenerInterface(new ConfirmSubmitDialog.ClickListenerInterface() {
            @Override
            public void doCancel() {
                dialog.dismiss();
            }

            @Override
            public void doConfirm() {
                dialog.dismiss();
                mImgPresent.deletePicture(strDelete);
            }
        });
        dialog.show();
    }


    @Subscribe
    public void picDeleteResult(PicDeleteResultEvent picDeleteResultEvent) {
        if(picDeleteResultEvent.errBean.getCode() == 0){
            ToastUtils.showShort("删除成功！");
            //更新数据
            for(int i=0;i<picListChoose.size();i++){
                for(int j=0;j<picList.size();j++){
                    //如果选择的集合和返回的图片列表的id相同
                    if((picListChoose.get(i).getUserPicStoreId()== picList.get(j).getUserPicStoreId())){
                        picList.remove(i);
                        break;
                    }
                }
            }
            picListChoose.clear();
            adapter.notifyDataSetChanged();

        }else{
            ToastUtils.showShort("删除失败:"+picDeleteResultEvent.errBean.getMsg());
        }
    }
    @Subscribe
    public void downloadImg(DownloadImgEvent downloadImgEvent){
        if(downloadImgEvent.isSucDownload){
            //清除所选项
            picListChoose.clear();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClickCancel() {
        //取消 恢复默认状态
        if(isEdit){
            for(int i = 0;i< picList.size();i++){
                picList.get(i).setEdit(false);
            }
            adapter.notifyDataSetChanged();
            isEdit = false;
            ((AddAdvertiseImgActivity)getActivity()).setCancelVisible(View.GONE);
            rl_bottom.setVisibility(View.GONE);
            picListChoose.clear();
        }

    }
}
