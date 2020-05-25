package kr.ssu.ai_fitness.vo;

public class DayProgramVideoModel {
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
}
