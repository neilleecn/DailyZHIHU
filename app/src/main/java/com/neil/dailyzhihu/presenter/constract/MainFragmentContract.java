package com.neil.dailyzhihu.presenter.constract;

import com.neil.dailyzhihu.base.BasePresenter;
import com.neil.dailyzhihu.base.BaseView;
import com.neil.dailyzhihu.model.bean.orignal.OriginalStory;

/**
 * 作者：Neil on 2017/4/6 15:39.
 * 邮箱：cn.neillee@gmail.com
 */

public interface MainFragmentContract {
    public static final int LATEST = 0;
    public static final int HOT = 1;
    public static final int PAST = 2;

    interface View extends BaseView {
        void showContent(OriginalStory content);

        void showError(String errorMsg);

        void refresh(OriginalStory content);
    }

    interface Presenter extends BasePresenter<View> {
        void getNewsListData(int type, String params);

        void startRefresh(int type, String params);
    }
}
