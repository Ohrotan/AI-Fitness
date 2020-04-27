package kr.ssu.ai_fitness.dto;

public class DayProgram {
    private String id;
    private String exr_id;
    private String title;
    private int order;
    private String intro;

    public DayProgram() {
    }

    public DayProgram(String title) {
        this.title = title;
    }

    public DayProgram( String exr_id, String title, int order, String intro) {
        this.exr_id = exr_id;
        this.title = title;
        this.order = order;
        this.intro = intro;
    }

    public DayProgram(String id, String exr_id, String title, int order, String intro) {
        this.id = id;
        this.exr_id = exr_id;
        this.title = title;
        this.order = order;
        this.intro = intro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExr_id() {
        return exr_id;
    }

    public void setExr_id(String exr_id) {
        this.exr_id = exr_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
