package kr.ssu.ai_fitness.dto;

public class ExrProgram {
    private int id;
    private int trainer_id;
    private String title;
    private int period;
    private String equip;
    private char gender;
    private int level;
    private int max;
    private String intro;

    public ExrProgram() {

    }

    public ExrProgram(String title) {
        this.title = title;
    }

    public ExrProgram(String title, int period, String equip, char gender, int level, int max) {
        this.title = title;
        this.period = period;
        this.equip = equip;
        this.gender = gender;
        this.level = level;
        this.max = max;
    }

    public ExrProgram(String title, int period, String equip, char gender, int level, int max, String intro) {
        this.title = title;
        this.period = period;
        this.equip = equip;
        this.gender = gender;
        this.level = level;
        this.max = max;
        this.intro = intro;
    }

    public ExrProgram(int trainer_id, String title, int period, String equip, char gender, int level, int max, String intro) {
        this.trainer_id = trainer_id;
        this.title = title;
        this.period = period;
        this.equip = equip;
        this.gender = gender;
        this.level = level;
        this.max = max;
        this.intro = intro;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrainer_id() {
        return trainer_id;
    }

    public void setTrainer_id(int trainer_id) {
        this.trainer_id = trainer_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getEquip() {
        return equip;
    }

    public void setEquip(String equip) {
        this.equip = equip;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @Override
    public String toString() {
        return "ExrProgram{" +
                "id='" + id + '\'' +
                ", trainer_id='" + trainer_id + '\'' +
                ", title='" + title + '\'' +
                ", period=" + period +
                ", equip='" + equip + '\'' +
                ", gender=" + gender +
                ", level=" + level +
                ", max=" + max +
                ", intro='" + intro + '\'' +
                '}';
    }
}
