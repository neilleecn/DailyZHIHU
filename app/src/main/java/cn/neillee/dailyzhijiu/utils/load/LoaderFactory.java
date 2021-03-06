package cn.neillee.dailyzhijiu.utils.load;

import cn.neillee.dailyzhijiu.utils.cnt.ContentLoaderWrapper;
import cn.neillee.dailyzhijiu.utils.cnt.UniversalContentLoader;
import cn.neillee.dailyzhijiu.utils.img.ImageLoaderWrapper;
import cn.neillee.dailyzhijiu.utils.img.UniversalAndroidImageLoader;

/**
 * ImageLoader工厂类
 * <p/>
 * Created by Clock on 2016/1/18.
 */
public class LoaderFactory {

    private static ImageLoaderWrapper sInstanceImg;
    private static ContentLoaderWrapper sInstanceCnt;
    private static ContentLoaderWrapper sInstanceCntVolley;

    private LoaderFactory() {

    }

    /**
     * 获取图片加载器
     *
     * @return 返回ImageLoaderWrapper(接口)，隐藏实现
     */
    public static ImageLoaderWrapper getImageLoader() {
        if (sInstanceImg == null) {
            synchronized (LoaderFactory.class) {
                if (sInstanceImg == null) {
                    sInstanceImg = new UniversalAndroidImageLoader();//<link>https://github.com/nostra13/Android-Universal-Image-Loader</link>
                }
            }
        }
        return sInstanceImg;
    }

    /**
     * 获取内容加载器
     *
     * @return ContentLoaderWrapper(接口)，隐藏了实现
     */
    public static ContentLoaderWrapper getContentLoader() {
        if (sInstanceCnt == null) {
            synchronized (LoaderFactory.class) {
                if (sInstanceCnt == null) {
                    sInstanceCnt = new UniversalContentLoader();
                }
            }
        }
        return sInstanceCnt;
    }

    /**
     * 获取内容加载器
     *
     * @return ContentLoaderWrapper(接口)，隐藏了实现
     */
    public static ContentLoaderWrapper getContentLoaderVolley() {
        if (sInstanceCntVolley == null) {
            synchronized (LoaderFactory.class) {
                if (sInstanceCntVolley == null) {
                    sInstanceCntVolley = new UniversalContentLoader();
                }
            }
        }
        return sInstanceCntVolley;
    }
}
