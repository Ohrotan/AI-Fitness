package kr.ssu.ai_fitness.dto;

public class DayProgram {
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
}
