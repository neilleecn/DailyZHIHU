package com.neil.dailyzhihu.ui.aty;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.neil.dailyzhihu.Constant;
import com.neil.dailyzhihu.api.API;
import com.neil.dailyzhihu.listener.OnContentLoadedListener;
import com.neil.dailyzhihu.R;
import com.neil.dailyzhihu.bean.orignal.StartImgBean;
import com.neil.dailyzhihu.ui.main.MainActivity;
import com.neil.dailyzhihu.utils.load.LoaderFactory;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：Neil on 2016/3/22 19:08.
 * 邮箱：cn.neillee@gmail.com
 */
public class StartActivity extends AppCompatActivity {
    @Bind(R.id.iv_start)
    ImageView ivStart;
    private String startImgSize = API.START_IMG_SIZE_LARGE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        LoaderFactory.getContentLoader().loadContent(startImgSize, new OnContentLoadedListener() {
            @Override
            public void onSuccess(String content, String url) {
                Gson gson = new Gson();
                StartImgBean startImgBean = gson.fromJson(content, StartImgBean.class);
                String imgUrl = startImgBean.getImg();
                String imgSign = startImgBean.getText();
                loadImg(imgUrl, imgSign);
            }
        });

    }

    private void loadImg(String imgUrl, String imgSign) {
        LoaderFactory.getImageLoader().displayImage(ivStart, imgUrl, null, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                doDelaytoStartMainAty();
            }
        });
    }

    private void doDelaytoStartMainAty() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(Constant.START_ACTY_LAST_MILLIES);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                StartActivity.this.startActivity(intent);
            }
        }.start();
    }
}
