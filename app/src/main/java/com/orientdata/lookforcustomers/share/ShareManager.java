package com.orientdata.lookforcustomers.share;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import com.orientdata.lookforcustomers.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * 分享
 */
public class ShareManager {
    public static ShareManager instance;
    private String title = "";
    private String mineContent = "";
    private String otherContent = "";
    private String appContent = "";

    public static String APP_URL = "";
    private Activity activity;

    public static ShareManager getInstance() {
        if (instance == null) {
            synchronized (ShareManager.class) {
                if (instance == null) {
                    instance = new ShareManager();
                }
            }
        }
        return instance;
    }


    /**
     * 分享
     *
     * @param activity 上下文
     * @param entity   分享实体
     */
    public void share(Activity activity, ShareEntity entity) {
        this.activity = activity;
        UMImage image = null;
        if (TextUtils.isEmpty(entity.getPicUrl())) {
            image = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher));
        } else {
            image = new UMImage(activity, entity.getPicUrl());
        }
        String tempTitle;
        String tempContent;
        if (TextUtils.isEmpty(entity.getTitle())) {
            tempTitle = title;
        } else {
            tempTitle = entity.getTitle();
        }
        if (TextUtils.isEmpty(entity.getContent())) {
            tempContent = tempTitle;
        } else {
            tempContent = entity.getContent();
        }
        UMWeb umWeb = new UMWeb(entity.getShareUrl());
        umWeb.setTitle(tempTitle);
        new ShareAction(activity).setPlatform(SHARE_MEDIA.SINA)
                .withText(tempContent)
                .withMedia(umWeb)
                .withMedia(image)
                .setCallback(listener)
                .share();
    }


    public void shareWX(Activity activity, ShareEntity entity) {
        this.activity = activity;
        UMImage image = null;
        if (TextUtils.isEmpty(entity.getPicUrl())) {
            image = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher));
        } else {
            image = new UMImage(activity, entity.getPicUrl());
        }
        String tempTitle;
        String tempContent;
        if (TextUtils.isEmpty(entity.getTitle())) {
            tempTitle = title;
        } else {
            tempTitle = entity.getTitle();
        }
        if (TextUtils.isEmpty(entity.getContent())) {
            tempContent = tempTitle;
        } else {
            tempContent = entity.getContent();
        }
        UMWeb umWeb = new UMWeb(entity.getShareUrl());
        umWeb.setTitle(tempTitle);
        umWeb.setThumb(image);
        umWeb.setDescription(tempContent);
        new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN)
                .withMedia(umWeb)
                .setCallback(listener)
                .share();
    }

    public void shareWXQ(Activity activity, ShareEntity entity) {
        this.activity = activity;
        UMImage image = null;
        if (TextUtils.isEmpty(entity.getPicUrl())) {
            image = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher));
        } else {
            image = new UMImage(activity, entity.getPicUrl());
        }
        String tempTitle;
        String tempContent;
        if (TextUtils.isEmpty(entity.getTitle())) {
            tempTitle = title;
        } else {
            tempTitle = entity.getTitle();
        }
        if (TextUtils.isEmpty(entity.getContent())) {
            tempContent = tempTitle;
        } else {
            tempContent = entity.getContent();
        }
        UMWeb umWeb = new UMWeb(entity.getShareUrl());
        umWeb.setTitle(tempTitle);
        umWeb.setThumb(image);
        umWeb.setDescription(tempContent);
        new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                .withMedia(umWeb)
                .setCallback(listener)
                .share();
    }

    public void shareQQ(Activity activity, ShareEntity entity) {
        this.activity = activity;
        UMImage image = null;
        if (TextUtils.isEmpty(entity.getPicUrl())) {
            image = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher));
        } else {
            image = new UMImage(activity, entity.getPicUrl());
        }
        String tempTitle;
        String tempContent;
        if (TextUtils.isEmpty(entity.getTitle())) {
            tempTitle = title;
        } else {
            tempTitle = entity.getTitle();
        }
        if (TextUtils.isEmpty(entity.getContent())) {
            tempContent = tempTitle;
        } else {
            tempContent = entity.getContent();
        }
        UMWeb umWeb = new UMWeb(entity.getShareUrl());
        umWeb.setTitle(tempTitle);
        umWeb.setThumb(image);
        umWeb.setDescription(tempContent);
        new ShareAction(activity).setPlatform(SHARE_MEDIA.QQ)
                .withMedia(umWeb)
                .setCallback(listener)
                .share();
    }

    public void shareQQZONE(Activity activity, ShareEntity entity) {
        this.activity = activity;
        UMImage image = null;
        if (TextUtils.isEmpty(entity.getPicUrl())) {
            image = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher));
        } else {
            image = new UMImage(activity, entity.getPicUrl());
        }
        String tempTitle;
        String tempContent;
        if (TextUtils.isEmpty(entity.getTitle())) {
            tempTitle = title;
        } else {
            tempTitle = entity.getTitle();
        }
        if (TextUtils.isEmpty(entity.getContent())) {
            tempContent = tempTitle;
        } else {
            tempContent = entity.getContent();
        }
        UMWeb umWeb = new UMWeb(entity.getShareUrl());
        umWeb.setTitle(tempTitle);
        umWeb.setThumb(image);
        umWeb.setDescription(tempContent);
        new ShareAction(activity).setPlatform(SHARE_MEDIA.QZONE)
                .withMedia(umWeb)
                .setCallback(listener)
                .share();
    }

    public void shareWEIBO(Activity activity, ShareEntity entity) {
        this.activity = activity;
        UMShareConfig config = new UMShareConfig();
        config.setSinaAuthType(UMShareConfig.AUTH_TYPE_SSO);
        UMShareAPI.get(activity).setShareConfig(config);
        UMImage image = null;
        if (TextUtils.isEmpty(entity.getPicUrl())) {
            image = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher));
        } else {
            image = new UMImage(activity, entity.getPicUrl());
        }
        String tempTitle;
        String tempContent;
        if (TextUtils.isEmpty(entity.getTitle())) {
            tempTitle = title;
        } else {
            tempTitle = entity.getTitle();
        }
        if (TextUtils.isEmpty(entity.getContent())) {
            tempContent = tempTitle;
        } else {
            tempContent = entity.getContent();
        }
        UMWeb umWeb = new UMWeb(entity.getShareUrl());
        umWeb.setTitle(tempTitle);
        umWeb.setThumb(image);
        umWeb.setDescription(tempContent);
        new ShareAction(activity).setPlatform(SHARE_MEDIA.SINA)
                .withMedia(umWeb)
                .setCallback(listener)
                .share();
    }


    UMShareListener listener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Toast.makeText(activity, "分享失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Toast.makeText(activity, "取消分享", Toast.LENGTH_SHORT).show();
        }
    };


/*===============================================拼接shareEntity================================================*/

    public String getOtherContent() {
        return otherContent;
    }

    public void setOtherContent(String otherContent) {
        this.otherContent = otherContent;
    }

    public String getMineContent() {
        return mineContent;
    }

    public void setMineContent(String mineContent) {
        this.mineContent = mineContent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 分享app
     */
    public ShareEntity getShareEntityFromApp() {
        final ShareEntity entity = new ShareEntity();
        entity.setShareUrl(APP_URL);
        entity.setContent(appContent);
        return entity;
    }

}
