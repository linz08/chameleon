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
	private String read_date;
	private String main_show;
	private int doc_level;
}
