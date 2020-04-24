package kr.ssu.ai_fitness.dto;

public class ChatData {

    private String massage;
    private String name;

    public ChatData(String massage, String name) {
        this.massage = massage;
        this.name = name;
    }

    public String getMassage() {
        return massage;
    }

    public String getName() {
        return name;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public void setName(String name) {
        this.name = name;
    }
}
