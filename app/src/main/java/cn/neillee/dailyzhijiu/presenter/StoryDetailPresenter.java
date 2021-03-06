package cn.neillee.dailyzhijiu.presenter;

import cn.neillee.dailyzhijiu.base.RxPresenter;
import cn.neillee.dailyzhijiu.model.bean.orignal.CertainStoryBean;
import cn.neillee.dailyzhijiu.model.bean.orignal.StoryExtraInfoBean;
import cn.neillee.dailyzhijiu.model.db.CachedStory;
import cn.neillee.dailyzhijiu.model.db.GreenDaoHelper;
import cn.neillee.dailyzhijiu.model.db.StarRecord;
import cn.neillee.dailyzhijiu.model.http.RetrofitHelper;
import cn.neillee.dailyzhijiu.presenter.constract.StoryDetailContract;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：Neil on 2017/4/6 17:17.
 * 邮箱：cn.neillee@gmail.com
 */

public class StoryDetailPresenter extends RxPresenter<StoryDetailContract.View> implements StoryDetailContract.Presenter {
    private RetrofitHelper mRetrofitHelper;
    private GreenDaoHelper mGreenDaoHelper;

    @Inject
    StoryDetailPresenter(RetrofitHelper retrofitHelper, GreenDaoHelper greenDaoHelper) {
        this.mRetrofitHelper = retrofitHelper;
        this.mGreenDaoHelper = greenDaoHelper;
    }

    @Override
    public void getStoryData(int storyId) {
        mRetrofitHelper.fetchNewsDetail(storyId).enqueue(new Callback<CertainStoryBean>() {
            @Override
            public void onResponse(Call<CertainStoryBean> call, Response<CertainStoryBean> response) {
                if (response.isSuccessful()) mView.showContent(response.body());
                else Logger.e("Error[%d] in request StoryData", response.code());
            }

            @Override
            public void onFailure(Call<CertainStoryBean> call, Throwable t) {
                mView.showError(t.getMessage());
            }
        });
    }

    @Override
    public void getStoryExtras(int storyId) {
        mRetrofitHelper.fetchNewsExtraInfo(storyId).enqueue(new Callback<StoryExtraInfoBean>() {
            @Override
            public void onResponse(Call<StoryExtraInfoBean> call, Response<StoryExtraInfoBean> response) {
                if (response.isSuccessful()) mView.showExtras(response.body());
                else Logger.e("Error[%d] in request StoryExtras", response.code());
            }

            @Override
            public void onFailure(Call<StoryExtraInfoBean> call, Throwable t) {
                mView.showError(t.getMessage());
            }
        });
    }

    @Override
    public void queryStarRecord(int storyId) {
        StarRecord queryRecord = mGreenDaoHelper.queryStarRecord(storyId);
        mView.showStarRecord(queryRecord, queryRecord != null);
    }

    @Override
    public void queryCachedStory(int storyId) {
        CachedStory story = mGreenDaoHelper.queryCachedStory(storyId);
        if (story == null) getStoryData(storyId);
        else {
            CertainStoryBean storyBean = new CertainStoryBean();
            storyBean.setId(storyId);
            storyBean.setTitle(story.getTitle());
            storyBean.setBody(story.getBody());
            storyBean.setImage(story.getImage());
            storyBean.setImageSource(story.getImageSource());
            mView.showContent(storyBean);
        }
    }

    @Override
    public void cacheCachedStory(CertainStoryBean story) {
        mGreenDaoHelper.cacheCachedStory(story);
    }

    @Override
    public void starStory(int storyId, String title, String image) {
        StarRecord queryRecord = mGreenDaoHelper.queryStarRecord(storyId);
        if (queryRecord == null) {
            long timestamp = System.currentTimeMillis();
            StarRecord record = new StarRecord(storyId, timestamp, image, title);
            mGreenDaoHelper.insertStarRecord(record);
            mView.showStarRecord(record, true);
        } else {
            mGreenDaoHelper.deleteStarRecord(queryRecord);
            mView.showStarRecord(queryRecord, false);
        }
    }
}
