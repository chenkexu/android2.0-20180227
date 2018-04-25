package com.orientdata.lookforcustomers.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.util.DateTool;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wy on 2017/11/21.
 * Date选择的dialog
 */

public class DatePickerDialog extends Dialog {
    private Context mContext;
    private TextView tvConfirm;
    private TextView tvCancel;
    private DatePicker datePick1;
    private TextView tvDate;

    private int mYear;
    private int mMonthOfYear;
    private int mDayOfMonth;
    private static final String TAG = "DatePickerDialog";

    private String mNowDateTextInner;
    private String mStartDateTextInner;
    private String mEndDateTextInner;
    private String defaultText;

    public DatePickerDialog(Context context,String nowDateTextInner, String startDateTextInner, String endDateTextInner,String defaultText) {
        super(context, R.style.RemindDialog);
        mContext = context;
        mNowDateTextInner=nowDateTextInner;
        mStartDateTextInner=startDateTextInner;
        mEndDateTextInner=endDateTextInner;
        this.defaultText = defaultText;
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_dialog_datepick, null);
        setContentView(view);

        datePick1= (DatePicker) view.findViewById(R.id.datePick1);
        tvDate= (TextView) view.findViewById(R.id.tvDate);

        initDatePicker();

        tvConfirm= (TextView) view.findViewById(R.id.tvConfirm);
        tvCancel= (TextView) view.findViewById(R.id.tvCancel);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDateSelectListener.onDateSelect(mYear,mMonthOfYear,mDayOfMonth);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDateSelectListener.onDateCancel();
            }
        });


        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.8
        dialogWindow.setAttributes(lp);
    }

    private void initDatePicker() {
        Calendar calendar;
        if(mNowDateTextInner!=null&&!mNowDateTextInner.equals("")&&!mNowDateTextInner.equals("null")
                &&!mNowDateTextInner.contains(defaultText)&&!mNowDateTextInner.equals(defaultText)
                ){
            //显示上一次选择数据
            Date date= DateTool.parseStr2Date(mNowDateTextInner, DateTool.FORMAT_DATE);
            calendar=DateTool.parseDate2Calendar(date);
        }else{
            calendar= Calendar.getInstance();//初始化时间
        }
        int year=calendar.get(Calendar.YEAR);
        int monthOfYear=calendar.get(Calendar.MONTH);
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
        tvDate.setText(year+"年"+(monthOfYear+1)+"月"+dayOfMonth);

        DatePicker.OnDateChangedListener dcl=new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear=year;
                mMonthOfYear=monthOfYear;
                mDayOfMonth=dayOfMonth;
                tvDate.setText(mYear+"年"+(mMonthOfYear+1)+"月"+mDayOfMonth);
            }
        };
        //
        datePick1.init(year,monthOfYear,dayOfMonth,dcl);
        //
        Log.i(TAG, "initDatePicker: mNowDateTextInner:"+mNowDateTextInner);
        Log.i(TAG, "initDatePicker: mStartDateTextInner:"+mStartDateTextInner);
        if (mStartDateTextInner!=null){
            Calendar calendar_s=DateTool.parseStr2Calendar(mStartDateTextInner,DateTool.FORMAT_DATE);
            long  time_s=calendar_s.getTimeInMillis();
            Log.i(TAG, "initDatePicker: time_s:"+time_s);
            datePick1.setMinDate(time_s);
        }
        Log.i(TAG, "initDatePicker: mEndDateTextInner:"+mEndDateTextInner);
        if (mEndDateTextInner!=null){
            Calendar calendar_e=DateTool.parseStr2Calendar(mEndDateTextInner,DateTool.FORMAT_DATE);
            long  time_e=calendar_e.getTimeInMillis();
            Log.i(TAG, "initDatePicker: calendar_e:"+calendar_e);
            datePick1.setMaxDate(time_e);
        }
        setDatePickerDividerColor(datePick1);
    }
    /**
     *
     * 设置时间选择器的分割线颜色
     * @param datePicker
     */
    private void setDatePickerDividerColor(DatePicker datePicker){
        // Divider changing:

        // 获取 mSpinners
        LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);

        // 获取 NumberPicker
        LinearLayout mSpinners      = (LinearLayout) llFirst.getChildAt(0);
        mSpinners.setEnabled(false);
        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);

            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field field : pickerFields) {
                if (field.getName().equals("mYearPicker")
                        || field.getName().equals("mYearSpinner")) {
                    field.setAccessible(true);
                    Object yearPicker = new Object();
                    try {
                        yearPicker = field.get(datePicker);
                        ((View) yearPicker).setVisibility(View.GONE);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            for (Field pf : pickerFields) {
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    try {
                        pf.set(picker, new ColorDrawable(mContext.getResources().getColor(R.color.dialog_color)));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
    public interface OnDateSelectListener {
        void onDateSelect(int year, int monthOfYear, int dayOfMonth);
        void onDateCancel();
    }

    public void setOnDateSelectListener(OnDateSelectListener onDateSelectListener) {
        mOnDateSelectListener = onDateSelectListener;
    }

    private OnDateSelectListener mOnDateSelectListener;

}
