package cn.neillee.dailyzhijiu.presenter.constract;

import cn.neillee.dailyzhijiu.base.BasePresenter;
import cn.neillee.dailyzhijiu.base.BaseView;
import cn.neillee.dailyzhijiu.model.bean.orignal.OriginalStory;

/**
 * 作者：Neil on 2017/4/6 22:26.
 * 邮箱：cn.neillee@gmail.com
 */

public interface BlockGridContract {
    public final static int COLUMN = 0;
    public final static int TOPIC = 1;

    interface View extends BaseView {
        void showContent(OriginalStory content);

        void showError(String errMsg);
    }

    interface Presenter extends BasePresenter<View> {
        void getBlockData(int type);
    }
}
