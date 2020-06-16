package kr.ssu.ai_fitness.poseestimation;

public class CurExerciseState {
    int curNum;
    int curSet;
    int curExr;
    int state;

    public CurExerciseState(int curNum, int curSet, int curExr, int state){
        this.curNum = curNum;
        this.curSet = curSet;
        this.curExr = curExr;
        this.state = state;
    }

    public int getCurNum() {
        return curNum;
    }

    public void setCurNum(int curNum) {
        this.curNum = curNum;
    }

    public int getCurSet() {
        return curSet;
    }

    public void setCurSet(int curSet) {
        this.curSet = curSet;
    }

    public int getCurExr() {
        return curExr;
    }

    public void setCurExr(int curExr) {
        this.curExr = curExr;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
