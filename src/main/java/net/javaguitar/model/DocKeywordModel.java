package net.javaguitar.model;

import lombok.Data;

@Data
public class DocKeywordModel {

	public DocKeywordModel() {
		// TODO Auto-generated constructor stub
	}

	private String doc_name;
	private String keyword;
	private String unique_yn;
	private String quiz_cnt;
}
