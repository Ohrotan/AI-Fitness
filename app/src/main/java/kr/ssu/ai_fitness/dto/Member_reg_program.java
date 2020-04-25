package kr.ssu.ai_fitness.dto;

public class Member_reg_program {

    String title; //프로그램 이름
    String level; //난이도
    String rating; //평점
    String mem_id_cnt; //회원수
    String mem_id_cnt_sum;//누적 회원수
    String equip; //사용기구
    String gender;//성별
    String trainer_id;//트레이너아이디


    public Member_reg_program(String title, String difficulty, String rating, String mem_id_cnt, String mem_id_cnt_sum, String equip,String gender,String trainer_id) {
        this.title = title;
        this.level = difficulty;
        this.rating = rating;
        this.mem_id_cnt = mem_id_cnt;
        this.mem_id_cnt_sum = mem_id_cnt_sum;
        this.equip = equip;
        this.gender = gender;
        this.trainer_id = trainer_id;
    }
    //프로그램명
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    //난이도
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    //평점
    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getMem_id_cnt() {
        return mem_id_cnt;
    }

    public void setMem_id_cnt(String mem_id_cnt) {
        this.mem_id_cnt = mem_id_cnt;
    }

    public String getMem_id_cnt_sum() {
        return mem_id_cnt_sum;
    }

    public void setMem_id_cnt_sum(String mem_id_cnt_sum) {
        this.mem_id_cnt_sum = mem_id_cnt_sum;
    }

    public String getEquip() {
        return equip;
    }

    public void setEquip(String equip) {
        this.equip = equip;
    }

    public String getGender() {
        return gender;
    }

    public void setTrainer_id(String trainer_id) {
        this.trainer_id = trainer_id;
    }

    public String getTrainer_id() {
        return trainer_id;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "SingerItem{" +
                "title='" + title + '\'' +
                ", level='" + level + '\'' +
                ", rating='" + rating + '\'' +
                ", mem_id_cnt='" + mem_id_cnt + '\'' +
                ", mem_id_cnt_sum='" + mem_id_cnt_sum + '\'' +
                ", equip='" + equip + '\'' +
                ", gender='" + gender + '\'' +
                ", trainer_id='" + trainer_id + '\'' +
                '}';
    }
}
