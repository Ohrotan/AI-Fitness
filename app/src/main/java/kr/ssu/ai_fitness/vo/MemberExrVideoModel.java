package kr.ssu.ai_fitness.vo;

public class MemberExrVideoModel {
    int id; //member_exr_history의 id
    int counts;
    int sets;
    String thumb_img;
    String video;
    String title;//비디오의 동작 명칭.
    String feedback;
    String time;
    String date;

    public MemberExrVideoModel(int id, int counts, int sets, String thumb_img, String video, String title, String feedback, String time, String date) {
        this.id = id;
        this.counts = counts;
        this.sets = sets;
        this.thumb_img = thumb_img;
        this.video = video;
        this.title = title;
        this.feedback = feedback;
        this.time = time;
        this.date = date;
    }

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

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
