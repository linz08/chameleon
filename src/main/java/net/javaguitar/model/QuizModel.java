package net.javaguitar.model;

import lombok.Data;

@Data
public class QuizModel {

	public QuizModel() {
		// TODO Auto-generated constructor stub
	}

	private int doc_code;
	private String doc_code_name;
	private int quiz_number;
	private int quiz_code;
	private String category_name;
	private String quiz_title;
	private String quiz_subtitle;
	private int pattern_code;
	private String pattern_code_name;
	private int quiz_level;
	private String quiz_level_name;
	private int quiz_source;
	private String quiz_source_name;
	private String quiz_memo;
	private int quiz_object_num;
	private String quiz_object_name;
	private String create_date;
	private String quiz_org_number;
	
	private String quiz_answer; //퀴즈 정답
	private String quiz_desc; //퀴즈 정답 설명
	private int success_y;  // 성공퀴즈 수
	private int success_n; // 실패퀴즈 수
	private int today_yn; // 오늘 푼 문제
}
