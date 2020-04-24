package kr.ssu.ai_fitness.dto;

public class Person {
    private String name;
    private String message;
    private String timestamp;

    public Person(String name, String message, String timestamp) {
        this.name = name;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}