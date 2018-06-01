package com.orientdata.lookforcustomers.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.bean.AddressCollectInfo;
import com.orientdata.lookforcustomers.bean.WrResponse;
import com.orientdata.lookforcustomers.network.api.ApiManager;
import com.orientdata.lookforcustomers.network.api.BaseObserver;
import com.orientdata.lookforcustomers.network.api.ParamsUtil;
import com.orientdata.lookforcustomers.network.util.RxUtil;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends AppCompatActivity {


    @BindView(R.id.seekbar5)
    RangeSeekBar seekbar5;
    @BindView(R.id.tv_no_limit)
    TextView tvNoLimit;
    @BindView(R.id.age_from)
    TextView ageFrom;
    @BindView(R.id.age_to)
    TextView ageTo;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.follow)
    Button follow;
    @BindView(R.id.list)
    ListView list;
    @BindView(R.id.dragView)
    LinearLayout dragView;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout mLayout;


    @OnClick(R.id.tv_no_limit)
    public void onViewClicked() {
        seekbar5.setValue(15, 70);
        seekbar5.invalidate();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        ButterKnife.bind(this);

        mLayout.setAnchorPoint(0.7f);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);


        List<String> your_array_list = Arrays.asList(
                "This",
                "Is",
                "An",
                "Example",
                "ListView",
                "That",
                "You",
                "Can",
                "Scroll",
                ".",
                "It",
                "Shows",
                "How",
                "Any",
                "Scrollable",
                "View",
                "Can",
                "Be",
                "Included",
                "As",
                "A",
                "Child",
                "Of",
                "SlidingUpPanelLayout"
        );

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list);

        list.setAdapter(arrayAdapter);


        seekbar5.setValue(15, 70);
        seekbar5.setOnRangeChangedListener(new OnRangeChangedListener() {

            private float rightValue;
            private float leftValue;

            @Override
            public void onRangeChanged(RangeSeekBar view, float min, float max, boolean isFromUser) {
                //min is left seekbar value, max is right seekbar value
                leftValue = min;
                rightValue = max;
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
                //start tracking touch
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

                Logger.d("左边显示的值：" + leftValue);
                Logger.d("右边显示的值：" + rightValue);

                String leftStr = leftValue + "";
                String lrightValueStr = rightValue + "";

                ageFrom.setText(leftStr.substring(0, 2));
                ageTo.setText(lrightValueStr.substring(0, 2));

                if (leftStr.substring(0, 2).equals("15")) {
                    ageFrom.setText("18及以下");
                }
                if (lrightValueStr.substring(0, 2).equals("70")) {
                    ageFrom.setText("65及以下");
                }
            }
        });


        HashMap<String, Object> map = ParamsUtil.getMap();
        ApiManager.getInstence().getApiService().appAddressGet(ParamsUtil.getParams(map))
                .compose(RxUtil.<WrResponse<List<AddressCollectInfo>>>rxSchedulerHelper())
                .subscribe(new BaseObserver<List<AddressCollectInfo>>() {
                    @Override
                    protected void onSuccees(WrResponse<List<AddressCollectInfo>> t) {
                        List<AddressCollectInfo> result = t.getResult();

                    }

                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {

                    }
                });

    }


}
