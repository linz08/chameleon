package net.javaguitar.model;

import lombok.Data;

@Data
public class DocCategoryModel {

	public DocCategoryModel() {
		// TODO Auto-generated constructor stub
	}

	private int doc_code;
	private String category_name;
	private int up_doc_code;
	private String memo;
	private String create_date;
	private int importance;
}
