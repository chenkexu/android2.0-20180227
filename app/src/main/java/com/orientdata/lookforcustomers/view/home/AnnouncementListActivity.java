package com.orientdata.lookforcustomers.view.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.bean.AnnouncementBean;
import com.orientdata.lookforcustomers.bean.AnnouncementListsBean;
import com.orientdata.lookforcustomers.network.HttpConstant;
import com.orientdata.lookforcustomers.network.OkHttpClientManager;
import com.orientdata.lookforcustomers.util.DateTool;
import com.orientdata.lookforcustomers.widget.MyTitle;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import vr.md.com.mdlibrary.UserDataManeger;
import vr.md.com.mdlibrary.okhttp.requestMap.MDBasicRequestMap;

/**
 * 公告列表界面
 */
public class AnnouncementListActivity extends WangrunBaseActivity {
    private MyTitle title;
    private SwipeMenuListView listView;
    private List<AnnouncementBean> mAnnouncementList;
    private MyAnnouncementListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_list);
        initView();
        initTitle();
        initListView();
        //getData();


    }

    @Override
    protected void onStart() {
        getData();
        super.onStart();
    }

    private void getData() {
        showDefaultLoading();
        MDBasicRequestMap map = new MDBasicRequestMap();
        map.put("userId", UserDataManeger.getInstance().getUserId());


        OkHttpClientManager.postAsyn(HttpConstant.SELECT_ANNOUNCEMENT_LISTS, new OkHttpClientManager.ResultCallback<AnnouncementListsBean>() {
            @Override
            public void onError(Exception e) {
                //ToastUtils.showShort(e.getMessage());
                hideDefaultLoading();
            }

            @Override
            public void onResponse(AnnouncementListsBean response) {
                hideDefaultLoading();
                if (response.getCode() == 0) {
                    if (response.getResult() == null) {
                        hideDefaultLoading();
                        return;
                    }
                    mAnnouncementList = response.getResult();

                    sortListByTopTag();
                    //写公告列表adapter
                    updateListView();
                }
            }
        }, map);
    }

    private void sortListByTopTag() {
        Collections.sort(mAnnouncementList, new Comparator<AnnouncementBean>() {
            @Override
            public int compare(AnnouncementBean o1, AnnouncementBean o2) {
                return o1.getToptag() - o2.getToptag();
            }
        });
    }

    private void updateListView() {
        if (mAnnouncementList == null) {
            return;
        }
        if (mAdapter == null) {
            mAdapter = new MyAnnouncementListViewAdapter(this);
            listView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

    }

    private void initListView() {
        /**
         * 创建SwipeMenuCreator
         */
/*        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem openItem = new SwipeMenuItem(AnnouncementListActivity.this);
                openItem.setBackground(new ColorDrawable(Color.BLACK));
                openItem.setWidth(dp2px(90));
                openItem.setTitle("置顶");
                openItem.setTitleSize(20);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);
                SwipeMenuItem deleteItem = new SwipeMenuItem(AnnouncementListActivity.this);
                deleteItem.setBackground(new ColorDrawable(Color.RED));
                deleteItem.setWidth(dp2px(90));
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(20);
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);
//                //可以加入图片
//                SwipeMenuItem deleteItem1 = new SwipeMenuItem(context);
//                deleteItem1.setBackground(new ColorDrawable(Color.LTGRAY));
//                deleteItem1.setWidth(dp2px(90));
//                deleteItem1.setIcon(android.R.drawable.ic_delete);
//                menu.addMenuItem(deleteItem1);
            }
        };*/

        /**
         * 实例化
         */

       /* listView.setMenuCreator(creator);
        *//**
         * 监听事件
         *//*
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                //index的值就是在SwipeMenu依次添加SwipeMenuItem顺序值，类似数组的下标。
                //从0开始，依次是：0、1、2、3...
                switch (index) {
                    case 0:
                        Toast.makeText(AnnouncementListActivity.this, "置顶:" + position, Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        Toast.makeText(AnnouncementListActivity.this, "删除:" + position, Toast.LENGTH_SHORT).show();
                        break;
                }

                // false : 当用户触发其他地方的屏幕时候，自动收起菜单。
                // true : 不改变已经打开菜单的样式，保持原样不收起。
                return false;
            }
        });*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AnnouncementListActivity.this, AnnouncementDetailActivity.class);
                intent.putExtra("mTopAnnouncement", mAnnouncementList.get(position));
                startActivity(intent);
            }
        });
    }

    public int dp2px(float dipValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private void initView() {
        title = findViewById(R.id.my_title);
        listView = findViewById(R.id.listView);


    }

    private void initTitle() {
        title.setTitleName("公告列表");
        title.setImageBack(this);
       /* title.setRightText(R.string.test);
        title.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getBaseContext(), TestPhoneSettingActivity.class), 1);
            }
        });*/
    }

    class MyAnnouncementListViewAdapter extends BaseAdapter {

        private Context context = null;

        public MyAnnouncementListViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return mAnnouncementList.size();
        }

        @Override
        public Object getItem(int position) {
            return mAnnouncementList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder;
            if (convertView == null) {
                mHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.fragment_announcement_list_item, null, true);
                mHolder.tv_topic = convertView.findViewById(R.id.tv_topic);
                mHolder.tv_content = convertView.findViewById(R.id.tv_content);
                mHolder.tv_date = convertView.findViewById(R.id.tv_date);
                mHolder.iv_oval = convertView.findViewById(R.id.iv_oval);
                mHolder.rl_announcement = convertView.findViewById(R.id.rl_announcement);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            AnnouncementBean bean = mAnnouncementList.get(position);
            if (bean != null) {
                int lookStatus = bean.getLookStatus();//0 未读 1 已读
                if (lookStatus == 1) {
                    mHolder.iv_oval.setVisibility(View.GONE);
                } else if (lookStatus == 0) {
                    mHolder.iv_oval.setVisibility(View.VISIBLE);
                }
                String title = bean.getTitle();
                if (title != null && title.length() > 12) {
                    title = title.substring(0, 10) + "...";
                }
                mHolder.tv_topic.setText(title);
                String content = bean.getContent();
                if (content != null && content.length() > 18) {
                    content = content.substring(0, 19) + "...";
                }
                mHolder.tv_content.setText(content);
                mHolder.tv_date.setText(DateTool.parseDate2Str(bean.getCreateDate(), "yyyy-MM-dd"));
               /* mHolder.rl_announcement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AnnouncementListActivity.this, AnnouncementDetailActivity.class);
                        intent.putExtra("mTopAnnouncement", mAnnouncementList.get(position));
                        startActivity(intent);
                    }
                });*/
            }

            return convertView;
        }

        class ViewHolder {
            private RelativeLayout rl_announcement;
            private TextView tv_topic;
            private TextView tv_date;
            private TextView tv_content;
            private ImageView iv_oval;
        }
    }

}
