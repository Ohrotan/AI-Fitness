package kr.ssu.ai_fitness.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class DayProgramVideoModel implements Parcelable {
    int id;
    int counts;
    int sets;
    String thumb_img;
    String video;
    String title;//비디오의 동작 명칭.

    public DayProgramVideoModel(int id, String thumb_img, String title) {
        this.id = id;
        this.thumb_img = thumb_img;
        this.title = title;
    }

    public DayProgramVideoModel(int id, int counts, int sets, String thumb_img, String video, String title) {
        this.id = id;
        this.counts = counts;
        this.sets = sets;
        this.thumb_img = thumb_img;
        this.video = video;
        this.title = title;
    }

    public DayProgramVideoModel(int counts, int sets, String thumb_img, String video, String title) {
        this.counts = counts;
        this.sets = sets;
        this.thumb_img = thumb_img;
        this.video = video;
        this.title = title;
    }

    protected DayProgramVideoModel(Parcel in) {
        id = in.readInt();
        counts = in.readInt();
        sets = in.readInt();
        thumb_img = in.readString();
        video = in.readString();
        title = in.readString();
    }

    public static final Creator<DayProgramVideoModel> CREATOR = new Creator<DayProgramVideoModel>() {
        @Override
        public DayProgramVideoModel createFromParcel(Parcel in) {
            return new DayProgramVideoModel(in);
        }

        @Override
        public DayProgramVideoModel[] newArray(int size) {
            return new DayProgramVideoModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public String getThumb_img() {
        return thumb_img;
    }

    public void setThumb_img(String thumb_img) {
        this.thumb_img = thumb_img;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(counts);
        parcel.writeInt(sets);
        parcel.writeString(thumb_img);
        parcel.writeString(video);
        parcel.writeString(title);
    }
}
