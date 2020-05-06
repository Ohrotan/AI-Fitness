package kr.ssu.ai_fitness.dto;

public class DayProgramVideoList {
    private String id;
    private String day_id;
    private String video_id;
    private String name;
    private int count;  //운동 횟수
    private int set;    //운동 세트
    private int order;  //운동 순서

    //어댑터 테스트용
    public DayProgramVideoList(String name){
        this.name = name;
    }

    public DayProgramVideoList(String id, String day_id, String video_id, String name, int count, int set, int order) {
        this.id = id;
        this.day_id = day_id;
        this.video_id = video_id;
        this.name = name;
        this.count = count;
        this.set = set;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDay_id() {
        return day_id;
    }

    public String getVideo_id() {
        return video_id;
    }

    public int getCount() {
        return count;
    }

    public int getSet() {
        return set;
    }

    public int getOrder() {
        return order;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDay_id(String day_id) {
        this.day_id = day_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
