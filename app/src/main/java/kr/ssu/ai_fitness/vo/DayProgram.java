package kr.ssu.ai_fitness.vo;

public class DayProgram {
    private int id;
    private String title;

    public DayProgram(int id, String Title){
        this.id = id;
        this.title = title;
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
}
