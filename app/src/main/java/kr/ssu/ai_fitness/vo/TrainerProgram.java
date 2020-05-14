package kr.ssu.ai_fitness.vo;

public class TrainerProgram {
    private String title;
    private int period;
    //private int gender;
    private String gender;
    private double level;
    private int curNum;
    private int max;
    private int totalNum;
    private double rating;
    private String equip;

    public TrainerProgram(String title, int period, int curNum, int max, int totalNum, double rating, double level, String equip, String gender){
        this.title = title;
        this.period = period;
        this.curNum = curNum;
        this.max = max;
        this.totalNum = totalNum;
        this.rating = rating;
        this.level = level;
        this.equip = equip;
        this.gender = gender;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    public int getCurNum() {
        return curNum;
    }

    public void setCurNum(int curNum) {
        this.curNum = curNum;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getEquip() {
        return equip;
    }

    public void setEquip(String equip) {
        this.equip = equip;
    }
}
