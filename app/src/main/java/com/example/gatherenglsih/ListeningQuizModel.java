package com.example.gatherenglsih;

public class ListeningQuizModel {
    private String questionTitle;
    private String option1;
    private int option1Diagram;
    private String option2;
    private int option2Diagram;
    private String option3;
    private int option3Diagram;
    private String option4;
    private int option4Diagram;
    private int answer;

    public ListeningQuizModel(String questionTitle, String option1, int option1Diagram, String option2, int option2Diagram, String option3, int option3Diagram, String option4, int option4Diagram, int answer) {
        this.questionTitle = questionTitle;
        this.option1 = option1;
        this.option1Diagram = option1Diagram;
        this.option2 = option2;
        this.option2Diagram = option2Diagram;
        this.option3 = option3;
        this.option3Diagram = option3Diagram;
        this.option4 = option4;
        this.option4Diagram = option4Diagram;
        this.answer = answer;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionAudio(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public int getOption1Diagram() {
        return option1Diagram;
    }

    public void setOption1Diagram(int option1Diagram) {
        this.option1Diagram = option1Diagram;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public int getOption2Diagram() {
        return option2Diagram;
    }

    public void setOption2Diagram(int option2Diagram) {
        this.option2Diagram = option2Diagram;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public int getOption3Diagram() {
        return option3Diagram;
    }

    public void setOption3Diagram(int option3Diagram) {
        this.option3Diagram = option3Diagram;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public int getOption4Diagram() {
        return option4Diagram;
    }

    public void setOption4Diagram(int option4Diagram) {
        this.option4Diagram = option4Diagram;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}
