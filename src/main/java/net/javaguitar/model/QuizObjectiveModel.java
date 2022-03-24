package net.javaguitar.model;

import lombok.Data;

@Data
public class QuizObjectiveModel {

	public QuizObjectiveModel() {
		// TODO Auto-generated constructor stub
	}

	private int doc_code;
	private int quiz_number;
	private int quiz_object_num;
	private String quiz_object_name;
	private String create_date;
}
