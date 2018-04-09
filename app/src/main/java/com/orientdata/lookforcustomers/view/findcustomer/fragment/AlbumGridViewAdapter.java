package com.orientdata.lookforcustomers.view.findcustomer.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v7.view.menu.ShowableListMenu;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.bean.UserPicStore;
import com.orientdata.lookforcustomers.util.GlideUtil;
import com.orientdata.lookforcustomers.util.ToastUtils;

import java.util.ArrayList;


/**
 * 这个是显示一个文件夹里面的所有图片时用的适配器
 *
 * @author wy
 */
public class AlbumGridViewAdapter extends BaseAdapter {
    final String TAG = getClass().getSimpleName();
    private Context mContext;
    private ArrayList<UserPicStore> dataList;//所有图片
    private ArrayList<UserPicStore> selectedDataList;//已经选择的图片的集合
    private DisplayMetrics dm;
    //	BitmapCache cache;
    public AlbumGridViewAdapter(Context c, ArrayList<UserPicStore> dataList, ArrayList<UserPicStore> selectedDataList) {
        mContext = c;
//		cache = new BitmapCache();
        this.dataList = dataList;
        this.selectedDataList = selectedDataList;
        dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public Object getItem(int position) {
        if (dataList != null) {
            return dataList.get(position);
        } else {
            return null;
        }
    }

    public long getItemId(int position) {
        return 0;
    }


//	BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
//		@Override
//		public void imageLoad(ImageView imageView, Bitmap bitmap,
//                              Object... params) {
//			if (imageView != null && bitmap != null) {
//				String url = (String) params[0];
//				if (url != null && url.equals((String) imageView.getTag())) {
//					((ImageView) imageView).setImageBitmap(bitmap);
//				}
//			}
//		}
//	};

    /**
     * 存放列表项控件句柄
     */
    private class ViewHolder {
        public ImageView imageView;
        public ToggleButton toggleButton;
        public RelativeLayout rl_toggle;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.plugin_camera_select_imageview, parent, false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            viewHolder.toggleButton = (ToggleButton) convertView.findViewById(R.id.toggle_button);
            viewHolder.toggleButton.setVisibility(View.GONE);
            viewHolder.rl_toggle = (RelativeLayout) convertView.findViewById(R.id.rl_toggle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String path = "'";
        if(dataList!=null && dataList.size()>0){
            boolean isEdit = dataList.get(0).isEdit();
            if(!isEdit){
                viewHolder.toggleButton.setVisibility(View.GONE);
            }else{
                viewHolder.toggleButton.setVisibility(View.VISIBLE);
            }
        }
        if (dataList != null && dataList.size() > position)
            path = dataList.get(position).getPicUrl();

//        Glide.with(mContext).load(path).into(viewHolder.imageView);

        GlideUtil.getInstance().loadImage(mContext,viewHolder.imageView,path,R.mipmap.image_ggid_error,true);

        viewHolder.rl_toggle.setTag(position);
        viewHolder.rl_toggle.setOnClickListener(new ToggleClickListener(viewHolder.toggleButton));
        viewHolder.rl_toggle.setOnLongClickListener(new ToggleLongClickListener1(viewHolder.toggleButton));

        if (null != selectedDataList && selectedDataList.size() > 0) {
            for (int i = 0; i < selectedDataList.size(); i++) {
                if ((selectedDataList.get(i).getUserPicStoreId() == dataList.get(position).getUserPicStoreId())) {
                    viewHolder.toggleButton.setChecked(true);
                    break;
                } else {
                    viewHolder.toggleButton.setChecked(false);
                }
            }
        } else {
            viewHolder.toggleButton.setChecked(false);
        }

        return convertView;
    }

    public int dipToPx(int dip) {
        return (int) (dip * dm.density + 0.5f);
    }

    private class ToggleLongClickListener implements View.OnLongClickListener {


        @Override
        public boolean onLongClick(View v) {
            if (mOnLongItemClickListener != null) {
                mOnLongItemClickListener.onLongItemClick1(v);
            }
            return false;
        }
    }

    private class ToggleClickListener implements OnClickListener {

        ToggleButton tbutton;

        public ToggleClickListener(ToggleButton toggleButton) {
            tbutton = toggleButton;
        }

        @Override
        public void onClick(View view) {
            if (view instanceof ToggleButton) {
                ToggleButton toggleButton = (ToggleButton) view;
                int position = (Integer) toggleButton.getTag();
                if (dataList != null && mOnItemClickListener != null && position < dataList.size()) {
//					mOnItemClickListener.onItemClick(toggleButton, position, toggleButton.isChecked());
                }
            } else if (view instanceof RelativeLayout) {
                RelativeLayout toggle = (RelativeLayout) view;
                int position = (Integer) toggle.getTag();
                if (dataList != null && mOnItemClickListener != null && position < dataList.size()) {
                    mOnItemClickListener.onItemClick(toggle, position, tbutton);
                }
            }
        }
    }
    private class ToggleLongClickListener1 implements View.OnLongClickListener {

        ToggleButton tbutton;

        public ToggleLongClickListener1(ToggleButton toggleButton) {
            tbutton = toggleButton;
        }

        @Override
        public boolean onLongClick(View v) {
            if (mOnLongItemClickListener != null) {
                mOnLongItemClickListener.onLongItemClick1(v);
            }
            //显示框 和 取消按钮
            return true;
        }
    }



    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    private OnLongItemClickListener mOnLongItemClickListener;

    public void setOnLongItemClickListener(OnLongItemClickListener l) {
        mOnLongItemClickListener = l;
    }

    public interface OnItemClickListener {
        public void onItemClick(RelativeLayout view, int position, ToggleButton tb);
    }

    public interface OnLongItemClickListener {
        public void onLongItemClick1(View view);
    }

}
