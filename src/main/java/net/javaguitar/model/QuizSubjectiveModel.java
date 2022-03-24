package net.javaguitar.model;

import lombok.Data;

@Data
public class QuizSubjectiveModel {

	public QuizSubjectiveModel() {
		// TODO Auto-generated constructor stub
	}

	private int doc_code;
	private int quiz_number;
	private String quiz_object_name;
	private String image_text;
	private String create_date;
}
