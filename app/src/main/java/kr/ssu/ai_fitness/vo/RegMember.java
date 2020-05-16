package kr.ssu.ai_fitness.vo;

public class RegMember {
    private int id;
    private String name;
    private String title;
    private int exrId;

    public RegMember(int id, String name, String title, int exrId){
        this.id = id;
        this.name = name;
        this.title = title;
        this.exrId = exrId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getExrId(){
        return exrId;
    }

    public void setExrId(int exrId){
        this.exrId = exrId;
    }
}
