package net.javaguitar.model;

import lombok.Data;

@Data
public class StatModel {

    public StatModel() {
        // TODO Auto-generated constructor stub
    }

    private String current_count;
    private String total_count;
    private String fault_count;
    private String doc_count;
    private String no_answer_count; // 한번도 안 푼 문제
}
