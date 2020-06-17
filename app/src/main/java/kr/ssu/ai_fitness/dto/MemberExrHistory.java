package kr.ssu.ai_fitness.dto;

public class MemberExrHistory {
    private String id;
    private String mem_id;
    private String exr_id;
    private String day_id;
    private String day_program_video_id;
    private String video;
    private String thumb_img;
    private String feedback;
    private String time;
    private String reg_date;

    public MemberExrHistory() {

    }

    public MemberExrHistory(String mem_id, String exr_id, String day_id, String day_program_video_id, String video, String thumb_img, String feedback, String time, String reg_date) {

        this.mem_id = mem_id;
        this.exr_id = exr_id;
        this.day_id = day_id;
        this.day_program_video_id = day_program_video_id;
        this.video = video;
        this.thumb_img = thumb_img;
        this.feedback = feedback;
        this.time = time;
        this.reg_date = reg_date;
    }

    public MemberExrHistory(String id, String mem_id, String exr_id, String day_id, String day_program_video_id, String video, String thumb_img, String feedback, String time, String reg_date) {
        this.id = id;
        this.mem_id = mem_id;
        this.exr_id = exr_id;
        this.day_id = day_id;
        this.day_program_video_id = day_program_video_id;
        this.video = video;
        this.thumb_img = thumb_img;
        this.feedback = feedback;
        this.time = time;
        this.reg_date = reg_date;
    }

    public String getId() {
        return id;
    }

    public String getMem_id() {
        return mem_id;
    }

    public String getExr_id() {
        return exr_id;
    }

    public String getDay_id() {
        return day_id;
    }

    public String getDay_program_video_id() {
        return day_program_video_id;
    }

    public String getVideo() {
        return video;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getTime() {
        return time;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public void setExr_id(String exr_id) {
        this.exr_id = exr_id;
    }

    public void setDay_id(String day_id) {
        this.day_id = day_id;
    }

    public void setDay_program_video_id(String day_program_video_id) {
        this.day_program_video_id = day_program_video_id;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getThumb_img() {
        return thumb_img;
    }

    public void setThumb_img(String thumb_img) {
        this.thumb_img = thumb_img;
    }

    @Override
    public String toString() {
        return "MemberExrHistory{" +
                "id='" + id + '\'' +
                ", mem_id='" + mem_id + '\'' +
                ", exr_id='" + exr_id + '\'' +
                ", day_id='" + day_id + '\'' +
                ", day_program_video_id='" + day_program_video_id + '\'' +
                ", video='" + video + '\'' +
                ", thumb_img='" + thumb_img + '\'' +
                ", feedback='" + feedback + '\'' +
                ", time='" + time + '\'' +
                ", reg_date='" + reg_date + '\'' +
                '}';
    }
}
