package kr.ssu.ai_fitness.dto;

public class DayProgramVideo {
    private int id;
    private int day_id;
    private int video_id;
    private String name;
    private int counts;  //운동 횟수
    private int sets;    //운동 세트
    private int seq;  //운동 순서

    //어댑터 테스트용
    public DayProgramVideo(String name) {
        this.name = name;
    }

    public DayProgramVideo(int id, int day_id, int video_id, String name, int counts, int sets, int seq) {
        this.id = id;
        this.day_id = day_id;
        this.video_id = video_id;
        this.name = name;
        this.counts = counts;
        this.sets = sets;
        this.seq = seq;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getDay_id() {
        return day_id;
    }

    public int getVideo_id() {
        return video_id;
    }

    public int getCounts() {
        return counts;
    }

    public int getSets() {
        return sets;
    }

    public int getSeq() {
        return seq;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDay_id(int day_id) {
        this.day_id = day_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "DayProgramVideo{" +
                "id=" + id +
                ", day_id=" + day_id +
                ", video_id=" + video_id +
                ", name='" + name + '\'' +
                ", counts=" + counts +
                ", sets=" + sets +
                ", seq=" + seq +
                '}';
    }
}
