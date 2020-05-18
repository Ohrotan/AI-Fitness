package kr.ssu.ai_fitness.vo;

public class AllTrainer {
    private int id;
    private String name;
    private String image;
    private double rating;
    private double heightValue;
    private double weightValue;
    private double muscleValue;
    private double fatValue;
    private int gender;
    private String birthValue;
    private String introValue;
    private int regMemberNum;

    public AllTrainer(int id, String name, String image, double rating, double heightValue, double weightValue, double muscleValue, double fatValue, int gender, String birthValue, String introValue, int regMemberNum) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.rating = rating;
        this.heightValue = heightValue;
        this.weightValue = weightValue;
        this.muscleValue = muscleValue;
        this.fatValue = fatValue;
        this.gender = gender;
        this.birthValue = birthValue;
        this.introValue = introValue;
        this.regMemberNum = regMemberNum;
    }

    public AllTrainer(int id, String name, double rating, String image) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.image = image;
    }

    public AllTrainer(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public AllTrainer(int id, String name, String image, double rating) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.rating = rating;
    }

    public double getHeightValue() {
        return heightValue;
    }

    public void setHeightValue(double heightValue) {
        this.heightValue = heightValue;
    }

    public double getWeightValue() {
        return weightValue;
    }

    public void setWeightValue(double weightValue) {
        this.weightValue = weightValue;
    }

    public double getMuscleValue() {
        return muscleValue;
    }

    public void setMuscleValue(double muscleValue) {
        this.muscleValue = muscleValue;
    }

    public double getFatValue() {
        return fatValue;
    }

    public void setFatValue(double fatValue) {
        this.fatValue = fatValue;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthValue() {
        return birthValue;
    }

    public void setBirthValue(String birthValue) {
        this.birthValue = birthValue;
    }

    public String getIntroValue() {
        return introValue;
    }

    public void setIntroValue(String introValue) {
        this.introValue = introValue;
    }


    public int getRegMemberNum() {
        return regMemberNum;
    }

    public void setRegMemberNum(int regMemberNum) {
        this.regMemberNum = regMemberNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getRating(){
        return rating;
    }

    public void setRating(double rating){
        this.rating = rating;
    }
}
