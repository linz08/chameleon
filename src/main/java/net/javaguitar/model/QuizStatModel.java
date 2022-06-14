package net.javaguitar.model;

import lombok.Data;

@Data
public class QuizStatModel {

    public QuizStatModel() {
        // TODO Auto-generated constructor stub
    }

    private String quiz_date;
    private int doc_code;
    private int quiz_number;
    private int quiz_seq;
    private int quiz_num;
    private String success_yn;
    private int quiz_time;
}
