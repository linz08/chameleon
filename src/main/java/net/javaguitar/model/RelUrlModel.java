package net.javaguitar.model;

import lombok.Data;

@Data
public class RelUrlModel {

	public RelUrlModel() {
		// TODO Auto-generated constructor stub
	}

	private int doc_code;
	private int link_seq;
	private String link_memo;
	private String inout_yn;
}
