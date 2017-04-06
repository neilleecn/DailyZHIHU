package com.neil.dailyzhihu.ui.topic;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.gson.Gson;
import com.neil.dailyzhihu.adapter.TopicStoryListAdapter;
import com.neil.dailyzhihu.mvp.model.http.api.API;
import com.neil.dailyzhihu.listener.OnContentLoadedListener;
import com.neil.dailyzhihu.R;
import com.neil.dailyzhihu.mvp.model.bean.orignal.TopicStoryListBean;
import com.neil.dailyzhihu.ui.story.StoryDetailActivity;
import com.neil.dailyzhihu.ui.widget.BaseActivity;
import com.neil.dailyzhihu.mvp.model.http.api.AtyExtraKeyConstant;
import com.neil.dailyzhihu.utils.Formater;
import com.neil.dailyzhihu.utils.GsonDecoder;
import com.neil.dailyzhihu.utils.load.LoaderFactory;
import com.nineoldandroids.view.ViewHelper;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 作者：Neil on 2016/3/23 11:05.
 * 邮箱：cn.neillee@gmail.com
 */
public class TopicDetailActivity extends BaseActivity implements ObservableScrollViewCallbacks,
        AdapterView.OnItemClickListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbarView;
    // mListBackgroundView makes ListView's background except header view.
    @Bind(R.id.list_background)
    View mListBackgroundView;
    @Bind(R.id.lv_theme_story_list)
    ObservableListView mListView;
    @Bind(R.id.image)
    ImageView mImageView;
    @Bind(R.id.tv_summary)
    TextView tvIntro;
    private int mParallaxImageHeight;
    private LinearLayout mLLEditors;
    private List<TopicStoryListBean.EditorsBean> mEditors;

    // 当前Theme的Id
    private int mThemeId = -1;
    private String mDefaultImgUrl;
    private String mTopicName;

    private View.OnClickListener upBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TopicDetailActivity.this.finish();
        }
    };

    private View.OnClickListener editorsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Gson gson = new Gson();
            String editorsInfo = gson.toJson(mEditors);
            Intent intent = new Intent(TopicDetailActivity.this, TopicEditorsActivity.class);
            intent.putExtra(AtyExtraKeyConstant.EDITORS_LIST, editorsInfo);
            intent.putExtra(AtyExtraKeyConstant.TOPIC_NAME, mTopicName);
            startActivity(intent);
        }
    };

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_topic_detail);
        ButterKnife.bind(this);

        // 对ActionBar进行设置
        setSupportActionBar(mToolbarView);
        mToolbarView.setNavigationIcon(R.drawable.ic_action_back);
        mToolbarView.setNavigationOnClickListener(upBtnListener);

        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);

        mListView.setScrollViewCallbacks(this);
        // Set padding view for ListView. This is the flexible space.
        View paddingView = new View(this);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                mParallaxImageHeight);
        paddingView.setLayoutParams(lp);

        // This is required to disable header's list selector effect
        paddingView.setClickable(true);

        mListView.addHeaderView(paddingView);
        setDummyData(mListView);

        // 设置HeaderView
        View introHeaderView = getLayoutInflater().inflate(R.layout.lv_header_theme_acty, null);
        mLLEditors = (LinearLayout) introHeaderView.findViewById(R.id.ll_editors);
        mListView.addHeaderView(introHeaderView, null, false);
        setDummyData(mListView);

        introHeaderView.setOnClickListener(editorsListener);
//
//        // 设置FooterView
//        View editorFooter = getLayoutInflater().inflate(R.layout.lv_footer_theme_acty, null);
//        lvEditor = (ListView) editorFooter.findViewById(R.id.lv_editor);
//        llEditorLVWrapper = (LinearLayout) editorFooter.findViewById(R.id.ll_editorLVWrapper);
//        tvLoadingEditor = (TextView) editorFooter.findViewById(R.id.tv_loading_editor);
//        tvLoadingEditor.setOnClickListener(this);
//        mlvStoryList.addFooterView(editorFooter, null, false);
//        llEditorLVWrapper.setVisibility(View.GONE);

        // 获取ThemeId，填充内容
        getExtras();
        if (mThemeId > 0)
            fillContent();

        // 设置StoryList的Item点击事件
        mListView.setOnItemClickListener(this);
    }

    /*获取themeId*/
    private int getExtras() {
        if (getIntent().getExtras() != null) {
            mThemeId = getIntent().getIntExtra(AtyExtraKeyConstant.THEME_ID, -2);
            mDefaultImgUrl = getIntent().getStringExtra(AtyExtraKeyConstant.DEFAULT_IMG_URL);
        }
        return mThemeId;
    }

    /*填充内容*/
    private void fillContent() {
        LoaderFactory.getContentLoader().loadContent(Formater.formatUrl(API.THEME_PREFIX, mThemeId),
                new OnContentLoadedListener() {
                    @Override
                    public void onSuccess(String content, String url) {
                        Logger.json(content);
                        TopicStoryListBean topicStoryListBean = GsonDecoder.getDecoder().decoding(content, TopicStoryListBean.class);
                        TopicStoryListAdapter adapter = new TopicStoryListAdapter(TopicDetailActivity.this, topicStoryListBean);
                        adapter.setDefaultImgUrl(mDefaultImgUrl);
                        mListView.setAdapter(adapter);

                        String bgUrl = topicStoryListBean.getBackground();
                        LoaderFactory.getImageLoader().displayImage(mImageView, bgUrl, null);
                        String introDes = topicStoryListBean.getDescription();
                        mTopicName = topicStoryListBean.getName();
                        tvIntro.setText(introDes);
                        setActionBarText(mTopicName);
//                LoaderFactory.getImageLoader().displayImage(mImageView, themeBGImgUrl, null);
                        mEditors = topicStoryListBean.getEditors();
                        for (int i = 0; i < mEditors.size(); i++) {
                            TopicStoryListBean.EditorsBean bean = mEditors.get(i);
                            CircleImageView imageView = new CircleImageView(TopicDetailActivity.this);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            mLLEditors.addView(imageView, params);
                            LoaderFactory.getImageLoader().displayImage(imageView, bean.getAvatar(), null);
                        }
//                        lvEditor.setAdapter(new TopicEditorListAdapter(TopicDetailActivity.this, editorsBeanList));
                    }
                });
    }

    /*设置标题*/
    private void setActionBarText(String themeName) {
        ActionBar ab = getSupportActionBar();
        if (ab == null || mThemeId < 0) return;
        ab.setTitle(getResources().getString(R.string.activity_topic) + " · " + themeName);
    }

    /*某一主题日报中的某个新闻项被点击*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TopicStoryListBean.TopicStory bean = (TopicStoryListBean.TopicStory) parent.getAdapter().getItem(position);
        int storyId = bean.getStoryId();
        mDefaultImgUrl = (bean.getImage() == null) ? mDefaultImgUrl : bean.getImage();
        Intent intent = new Intent(this, StoryDetailActivity.class);
        intent.putExtra(AtyExtraKeyConstant.STORY_ID, storyId);
        intent.putExtra(AtyExtraKeyConstant.DEFAULT_IMG_URL, mDefaultImgUrl);
        startActivity(intent);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.ZHIHUBlue);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        ViewHelper.setTranslationY(mImageView, -scrollY / 2);

        // Translate list background
        ViewHelper.setTranslationY(mListBackgroundView, Math.max(0, -scrollY + mParallaxImageHeight));
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
