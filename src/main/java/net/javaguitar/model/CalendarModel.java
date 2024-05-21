package net.javaguitar.model;

import lombok.Data;

@Data
public class CalendarModel {

	public CalendarModel() {
		// TODO Auto-generated constructor stub
	}

	private int intYear;  //2024년
	private int intMonth; //4월
	private String strMonth; // January.
	private int intLastDate; //월의 마지막 날짜
	private int intFirstWeekDay; // 1일의 요일 찾기
	private int day; // 날짜
	private int seq; // 일련번호
	private int cnt; // 전체 문제풀이 갯수
	private String title; // 제목
	private String memo; // 내용
}
