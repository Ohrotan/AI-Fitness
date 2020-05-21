package kr.ssu.ai_fitness.dto;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class DayProgram implements Parcelable {
    private int id;
    private int exr_id;
    private String title;
    private int seq;
    private String intro;

    public DayProgram() {
    }

    public DayProgram(int exr_id, String title, int seq) {
        this.exr_id = exr_id;
        this.title = title;
        this.seq = seq;
    }

    public DayProgram(int exr_id, String title, int seq, String intro) {
        this.exr_id = exr_id;
        this.title = title;
        this.seq = seq;
        this.intro = intro;
    }

    public DayProgram(int id, int exr_id, String title, int seq, String intro) {
        this.id = id;
        this.exr_id = exr_id;
        this.title = title;
        this.seq = seq;
        this.intro = intro;
    }

    public DayProgram(JSONObject obj) {
        try {
            this.id = obj.getInt("id");
            this.exr_id = obj.getInt("exr_id");
            this.title = obj.getString("title");
            this.seq = obj.getInt("seq");
            this.intro = obj.getString("intro");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected DayProgram(Parcel in) {
        id = in.readInt();
        exr_id = in.readInt();
        title = in.readString();
        seq = in.readInt();
        intro = in.readString();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExr_id() {
        return exr_id;
    }

    public void setExr_id(int exr_id) {
        this.exr_id = exr_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @Override
    public String toString() {
        return "DayProgram{" +
                "id=" + id +
                ", exr_id=" + exr_id +
                ", title='" + title + '\'' +
                ", seq=" + seq +
                ", intro='" + intro + '\'' +
                '}';
    }

    public static final Creator<DayProgram> CREATOR = new Creator<DayProgram>() {
        @Override
        public DayProgram createFromParcel(Parcel in) {
            return new DayProgram(in);
        }

        @Override
        public DayProgram[] newArray(int size) {
            return new DayProgram[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(exr_id);
        dest.writeString(title);
        dest.writeInt(seq);
        dest.writeString(intro);

    }
}
