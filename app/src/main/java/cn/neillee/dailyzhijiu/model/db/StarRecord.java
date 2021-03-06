package cn.neillee.dailyzhijiu.model.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * 作者：Neil on 2016/4/27 15:11.
 * 邮箱：cn.neillee@gmail.com
 */
@Entity(nameInDb = "star_record", generateGettersSetters = false)
public class StarRecord {
    @Id(autoincrement = true)
    private Long _id;
    @Property(nameInDb = "story_id")
    private int storyId;
    @Property(nameInDb = "title")
    private String title;
    @Property(nameInDb = "image")
    private String image;
    @Property(nameInDb = "timestamp")
    private long timestamp;

    public StarRecord(int storyId, long timestamp, String image, String title) {
        this.image = image;
        this.title = title;
        this.storyId = storyId;
        this.timestamp = timestamp;
    }

    @Generated(hash = 2041012289)
    public StarRecord(Long _id, int storyId, String title, String image,
            long timestamp) {
        this._id = _id;
        this.storyId = storyId;
        this.title = title;
        this.image = image;
        this.timestamp = timestamp;
    }

    @Generated(hash = 1872581448)
    public StarRecord() {
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
