package cn.neillee.dailyzhijiu.base;

/**
 * 作者：Neil on 2017/4/5 14:05.
 * 邮箱：cn.neillee@gmail.com
 */

public interface BasePresenter<T extends BaseView> {
    void attachView(T view);

    void detachView();
}
