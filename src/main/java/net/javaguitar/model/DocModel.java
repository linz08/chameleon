package net.javaguitar.model;

import lombok.Data;

@Data
public class DocModel {

	public DocModel() {
		// TODO Auto-generated constructor stub
	}

	private String doc_name;
	private String new_doc_name;
	private String upper_doc_name;
	private int sort_num;
	private String doc_content;
	private int read_num;
	private int quiz_cnt; // 퀴즈 갯수
	private String read_date;
	private String main_show;
	private String doc_month; // 이번달
	private int doc_level;
	private String memo; //암기법
	private String manager; //담당자
	private String modify_date; //수정일
	private int rankings; // 토픽 순위
	private String memory_comment;
	private String cdate;
}
