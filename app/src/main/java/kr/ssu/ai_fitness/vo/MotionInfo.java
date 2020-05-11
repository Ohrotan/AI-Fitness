package kr.ssu.ai_fitness.vo;

public class MotionInfo {
    private String title;
    private int count;
    private int set;
    private int perfectCount;
    private int goodCount;
    private int badCount;

    public MotionInfo(String title, int count, int set, int perfectCount, int goodCount, int badCount) {
        this.title = title;
        this.count = count;
        this.set = set;
        this.perfectCount = perfectCount;
        this.goodCount = goodCount;
        this.badCount = badCount;
    }

    public int getPerfectCount() {
        return perfectCount;
    }

    public void setPerfectCount(int perfectCount) {
        this.perfectCount = perfectCount;
    }

    public int getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(int goodCount) {
        this.goodCount = goodCount;
    }

    public int getBadCount() {
        return badCount;
    }

    public void setBadCount(int badCount) {
        this.badCount = badCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }
}
