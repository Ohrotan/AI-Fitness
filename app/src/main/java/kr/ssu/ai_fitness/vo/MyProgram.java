package kr.ssu.ai_fitness.vo;

public class MyProgram {
    private int id;
    private String trainer_name;
    private String program_title;

    public MyProgram(int id, String trainer_name, String program_title) {
        this.id = id;
        this.trainer_name = trainer_name;
        this.program_title = program_title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrainer_name() {
        return trainer_name;
    }

    public void setTrainer_name(String trainer_name) {
        this.trainer_name = trainer_name;
    }

    public String getProgram_title() {
        return program_title;
    }

    public void setProgram_title(String program_title) {
        this.program_title = program_title;
    }
}
