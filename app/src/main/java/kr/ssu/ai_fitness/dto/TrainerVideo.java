package kr.ssu.ai_fitness.dto;

public class TrainerVideo {

    private String id;
    private String trainer_id;
    private String thumb_img;
    private String video;
    private String title;
    private String analysis;

    public TrainerVideo() {
    }

    public TrainerVideo(String title){
        this.title = title;
    }

    public TrainerVideo(String id, String trainer_id, String thumb_img, String video, String title, String analysis) {
        this.id = id;
        this.trainer_id = trainer_id;
        this.thumb_img = thumb_img;
        this.video = video;
        this.title = title;
        this.analysis = analysis;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrainer_id() {
        return trainer_id;
    }

    public void setTrainer_id(String trainer_id) {
        this.trainer_id = trainer_id;
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

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }
}
