package vr.md.com.mdlibrary.myView.loadlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;

import vr.md.com.mdlibrary.R;

/**
 * Created by Mr.Z on 16/4/23.
 */
public class MDListView extends SwipeMenuListView {
    public static final boolean MODE_LOADMORE = true;
    public static final boolean MODE_LOADMORE_NO = false;
    private View footer;// 底部布局
    private int mtotalItemCount;//全部item的数量
    private int lastVisableItem;//最后一个可见的item
    private boolean isLoading=false;//是否正在加载
    public OnReflashMoreListener mListener2;
    private boolean MODE_HASMORE ;
    private TextView loadingtext;
    private ProgressBar progressbar;
    private LinearLayout linear;

    public MDListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    public MDListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    public MDListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    /**
     * 添加顶部布局文件
     *
     * @param context
     */
    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.footer, null, false);
        linear = (LinearLayout) footer.findViewById(R.id.load_footer);
        loadingtext = (TextView)footer.findViewById(R.id.loadtext);
        progressbar = (ProgressBar)footer.findViewById(R.id.progressbar);

        //先设置底部隐藏
        linear.setVisibility(View.GONE);
        this.addFooterView(footer);
        this.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                //最后一个可见的item是总数量，并且当前滚动状态停止，就加载数据
                if (mtotalItemCount == lastVisableItem && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                    if (!isLoading) {
                        if(MODE_HASMORE){
                            //加载数据
                            isLoading = true;
                            linear.setVisibility(VISIBLE);
                            progressbar.setVisibility(VISIBLE);
                            loadingtext.setText("正在加载");
                            mListener2.onReflashMore();
                        }else{
                            linear.setVisibility(View.GONE);
                            progressbar.setVisibility(GONE);
                            loadingtext.setText("已无更多数据");
                        }

                    }
                }
            }

            /**
             * firstvisebleitem第一个可见的位置
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                lastVisableItem = firstVisibleItem + visibleItemCount;
                mtotalItemCount = totalItemCount;
            }

        });
    }

    /**
     * 底部加载完毕
     */
    public void reflashFooterComplete() {
        isLoading=false;
        linear.setVisibility(View.GONE);
    }

    public void setMode(boolean hasMore){
        MODE_HASMORE = hasMore;
    }

    /**
     * 加载更多接口
     * @author nickming
     *
     */
    public interface OnReflashMoreListener{
        void onReflashMore();
    }

    public void  setOnReflashMoreListener(OnReflashMoreListener listener) {
        mListener2=listener;
    }

}
