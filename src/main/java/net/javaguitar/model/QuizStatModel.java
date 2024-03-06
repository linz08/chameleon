package net.javaguitar.model;

import lombok.Data;

@Data
public class QuizStatModel {

    public QuizStatModel() {
        // TODO Auto-generated constructor stub
    }

    private String quiz_date;
    private int doc_code;
    private String doc_name;
    private int quiz_number;
    private int quiz_seq;
    private int quiz_num;
    private String success_yn;
    private float quiz_times;
    private String quiz_source;
    private String success_cnt;
    private String success_rate;
    private String fail_cnt;
    private String total_cnt;
}
