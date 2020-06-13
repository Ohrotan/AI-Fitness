package kr.ssu.ai_fitness.vo;

public class DayProgram {
    private int id;
    private int mem_id;
    private String title;
    private String feedback;

    public DayProgram(int id, int mem_id, String title, String feedback){
        this.id = id;
        this.mem_id = mem_id;
        this.title = title;
        this.feedback = feedback;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getMem_id() {
        return mem_id;
    }

    public void setMem_id(int mem_id) {
        this.mem_id = mem_id;
    }
}
