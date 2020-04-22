package kr.ssu.ai_fitness.dto;

public class TrainerVideo {
    //db 컬럼명과 일치 시키기 (나중에 수정 필요)
    //public -> private 바꾸기
    public String id;
    public String thumbImg;
    public String video;
    public String title;
    public String trainerId;
    public TrainerVideo(String title){
        this.title = title;
    }
}
