package com.neil.dailyzhihu.ui.main;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.neil.dailyzhihu.Constant;
import com.neil.dailyzhihu.api.API;
import com.neil.dailyzhihu.ui.widget.DownloadedHighLightDecorator;
import com.neil.dailyzhihu.listener.OnContentLoadingFinishedListener;
import com.neil.dailyzhihu.R;
import com.neil.dailyzhihu.adapter.PastStoryListAdapter;
import com.neil.dailyzhihu.bean.orignallayer.BeforeStoryListBean;
import com.neil.dailyzhihu.ui.story.CertainStoryActivity;
import com.neil.dailyzhihu.api.AtyExtraKeyConstant;
import com.neil.dailyzhihu.utils.date.DateInNumbers;
import com.neil.dailyzhihu.utils.date.DateUtil;
import com.neil.dailyzhihu.utils.GsonDecoder;
import com.neil.dailyzhihu.utils.load.LoaderFactory;
import com.orhanobut.logger.Logger;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PastFragment extends Fragment implements AdapterView.OnItemClickListener,
        ObservableScrollViewCallbacks, View.OnClickListener {

    private static final String LOG_TAG = PastFragment.class.getSimpleName();

    @Bind(R.id.lv_before)
    ObservableListView mLvBefore;

    private Context mContext;
    /*选中的日期，格式为：yyyyMMDD，如20161002*/
    private String pickedDate;
    private DownloadedHighLightDecorator downloadedHighLightDecorator;

    private Button mBtnLoadSetting;
    private MaterialCalendarView mMCV;
    private TextView mTvDateDisplay;

    private boolean shouldShownMCV = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化今天
        pickedDate = DateUtil.calendar2yyyyMMDD(Calendar.getInstance());
        loadPickedDateStory();
    }

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_past, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        initViews();
    }

    private void initViews() {
        View header = LayoutInflater.from(mContext).inflate(R.layout.fm_past_header, null);
        Button btnPickDate = (Button) header.findViewById(R.id.btn_pickDate);
        mBtnLoadSetting = (Button) header.findViewById(R.id.btn_loadsetting);
        mMCV = (MaterialCalendarView) header.findViewById(R.id.material_calendarview);
        mTvDateDisplay = (TextView) header.findViewById(R.id.tv_dateDisplay);
        mLvBefore.addHeaderView(header);

        mLvBefore.setOnItemClickListener(this);
        btnPickDate.setOnClickListener(this);
        mBtnLoadSetting.setOnClickListener(this);

        // 初始化MaterialCalendarView
        // init Decorator
        downloadedHighLightDecorator = new DownloadedHighLightDecorator();
        mMCV.addDecorator(downloadedHighLightDecorator);
        mMCV.invalidateDecorators();

        mMCV.setVisibility(View.GONE);
        mMCV.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if (!selected || mContext == null) return;
                if (!downloadedHighLightDecorator.contains(new CalendarDay(date.getCalendar()))) {//未下载
                    downloadedHighLightDecorator.addDatesAndUpdate(DateUtil.calendar2yyyyMMDD(date.getCalendar()));
                    widget.invalidateDecorators();
                } else {//已下载
                    pickedDate = DateUtil.calendar2yyyyMMDD(date.getCalendar());
                    loadPickedDateStory();
                }
            }
        });
    }

    private void loadPickedDateStory() {
        LoaderFactory.getContentLoader().loadContent(API.BEFORE_NEWS_PREFIX + pickedDate,
                new OnContentLoadingFinishedListener() {
                    @Override
                    public void onFinish(String content) {
                        Logger.json(content);
                        BeforeStoryListBean beforeStory = GsonDecoder.getDecoder().decoding(content, BeforeStoryListBean.class);
                        PastStoryListAdapter adapter = new PastStoryListAdapter(mContext, beforeStory);
                        mLvBefore.setAdapter(adapter);
                        updateDateDisplay();
                    }
                }
        );
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BeforeStoryListBean.StoriesBean storiesBean = (BeforeStoryListBean.StoriesBean) parent.getAdapter().getItem(position);
        Intent intent = new Intent(mContext, CertainStoryActivity.class);
        intent.putExtra(AtyExtraKeyConstant.STORY_ID, storiesBean.getStoryId());
        mContext.startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        AppCompatActivity activity = (AppCompatActivity) mContext;
        ActionBar ab = activity.getSupportActionBar();
        if (ab == null) {
            return;
        }
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pickDate:
                pickDate();
                break;
            case R.id.btn_loadsetting:
                shouldShownMCV = !shouldShownMCV;
                if (shouldShownMCV) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("提示").setMessage("即将出现新闻日历表，点击日历项，即可缓存当天新闻列表及新闻内容。")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mMCV.setVisibility(View.VISIBLE);
                            mBtnLoadSetting.setText("隐藏缓存设置");
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
                } else {
                    mMCV.setVisibility(View.GONE);
                    mBtnLoadSetting.setText("缓存设置");
                }
                break;
        }
    }

    private void updateDateDisplay() {
        mTvDateDisplay.setText(pickedDate);
    }

    private void pickDate() {
        DateInNumbers dateInNumbers = DateUtil.yyyyMMDD2DateInNumbers(pickedDate);
        DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                pickedDate = DateUtil.dateInNumbers2yyyyMMDD(year, monthOfYear + 1, dayOfMonth);
                Toast.makeText(mContext, pickedDate, Toast.LENGTH_SHORT).show();
                loadPickedDateStory();
            }
        },dateInNumbers.getYear() , dateInNumbers.getMonthOfYear() - 1, dateInNumbers.getDayOfMonth());
        dialog.show();

        // 设置日期选择器的日期选择区间（最大为今日，最小为2013年5月20日）
        DatePicker datePicker = dialog.getDatePicker();
        Calendar today = Calendar.getInstance();
        datePicker.setMaxDate(today.getTimeInMillis());
        today.set(Constant.MIN_YEAR_OF_PAST_STORY, Constant.MIN_MONTH_OF_YEAR_OF_PAST_STORY - 1, Constant.MIN_DAY_OF_MONTH_OF_PAST_STORY);
        datePicker.setMinDate(today.getTimeInMillis());
    }

    /* 懒加载相关 */

    // 标志位，标志已经初始化完成，因为setUserVisibleHint是在onCreateView之前调用的，
    // 在视图未初始化的时候，在lazyLoad当中就使用的话，就会有空指针的异常
    private boolean isPrepared;

    //标志当前页面是否可见
    private boolean isVisible;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;
        lazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 懒加载
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void lazyLoad() {
        if (!isVisible || !isPrepared) {
            return;
        }
        //getData();//数据请求
        loadPickedDateStory();
    }

    protected void onInvisible() {
    }
}