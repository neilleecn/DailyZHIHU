package com.neil.dailyzhihu.mvp.presenter;

import com.neil.dailyzhihu.base.RxPresenter;
import com.neil.dailyzhihu.listener.OnContentLoadListener;
import com.neil.dailyzhihu.mvp.presenter.constract.BlockGridContract;
import com.neil.dailyzhihu.utils.load.LoaderFactory;

import javax.inject.Inject;

/**
 * 作者：Neil on 2017/4/6 22:29.
 * 邮箱：cn.neillee@gmail.com
 */

public class BlockGridPresenter extends RxPresenter<BlockGridContract.View> implements BlockGridContract.Presenter {

    @Inject
    public BlockGridPresenter() {
    }

    @Override
    public void getBlockData(String url) {
        LoaderFactory.getContentLoader().loadContent(url, new OnContentLoadListener() {
            @Override
            public void onSuccess(String content, String url) {
                mView.showContent(content);
            }

            @Override
            public void onFailure(String errMsg) {
                mView.showError(errMsg);
            }
        });
    }
}