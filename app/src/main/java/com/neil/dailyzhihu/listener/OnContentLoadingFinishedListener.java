package com.neil.dailyzhihu.listener;

/**
 * 作者：Neil on 2016/4/15 18:53.
 * 邮箱：cn.neillee@gmail.com
 */
public interface OnContentLoadingFinishedListener {
    /**
     * 内容加载完成
     *
     * @param content 加载完成的内容
     */
    public void onFinish(String content,String url);
}
