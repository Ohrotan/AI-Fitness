package kr.ssu.ai_fitness.dto;

public class MemberExrHistory {
    private String id;
    private String mem_id;
    private String exr_id;
    private String day_id;
    private String day_program_video_id;
    private String video;
    private String feedback;
    private String time;
    private String date;

    public MemberExrHistory(String id, String mem_id, String exr_id, String day_id, String day_program_video_id, String video, String feedback, String time, String date) {
        this.id = id;
        this.mem_id = mem_id;
        this.exr_id = exr_id;
        this.day_id = day_id;
        this.day_program_video_id = day_program_video_id;
        this.video = video;
        this.feedback = feedback;
        this.time = time;
        this.date = date;
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

    public String getDate() {
        return date;
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

    public void setDate(String date) {
        this.date = date;
    }
}
