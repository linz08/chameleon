package net.javaguitar.controller;

import com.google.gson.Gson;
import net.javaguitar.model.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class QuizStatController {

    @Autowired
    SqlSession ss;


    @RequestMapping(value = {"/quiz_stat_insert"}, method = {RequestMethod.POST})
    public @ResponseBody
    void quizStatInsert(HttpServletRequest request, @ModelAttribute("quizStatModel") QuizStatModel quizStatModel) throws Exception {
        int quizSeq = 0;
        int quizStatCnt = 0;
        // 현재 날짜 구하기
        LocalDate now = LocalDate.now();
        // 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 포맷 적용
        String formatedNow = now.format(formatter);

        quizStatModel.setQuiz_date(formatedNow);

        quizSeq = ss.selectOne("net.javaguitar.mapper.QuizStatMapper.selectMaxQuizStatSeq", quizStatModel);

        quizStatModel.setQuiz_seq(quizSeq);
        quizStatCnt = ss.selectOne("net.javaguitar.mapper.QuizStatMapper.selectQuizStatCnt", quizStatModel);
        if(quizStatModel.getSuccess_yn().equals("X")) { // X인 경우만 Update 처리
            if (quizStatCnt == 0) {
                ss.insert("net.javaguitar.mapper.QuizStatMapper.insertQuizStat", quizStatModel);
            }
            else {
                ss.update("net.javaguitar.mapper.QuizStatMapper.updateQuizStat", quizStatModel);
            }
        }
        else {
            if (quizStatCnt == 0) {
                ss.insert("net.javaguitar.mapper.QuizStatMapper.insertQuizStat", quizStatModel);
            }
        }
    }
    @RequestMapping(value = {"/stat/calendar/{yyyymm}"}, method = RequestMethod.GET)
    public ModelAndView statCalendar(@PathVariable String yyyymm) {
        ModelAndView mav = new ModelAndView();
        CalendarModel calendarModel = new CalendarModel();
        int reqYear = 2023; //Integer.parseInt(yyyymm.substring(0,4));
        int reqMonth = 4; //Integer.parseInt(yyyymm.substring(4,2));

        if(yyyymm != null) {
            reqYear = Integer.parseInt(yyyymm.substring(0,4));
            reqMonth = Integer.parseInt(yyyymm.substring(4,6));
        }

        //Calendar cal = Calendar.getInstance();
        LocalDate toDay = LocalDate.of(reqYear,reqMonth,1); //현재날짜 LocalDate firstDay = LocalDate.of(toDay.getYear(),toDay.getMonthValue(),1);LocalDate.now()

        calendarModel.setIntYear(toDay.getYear());
        calendarModel.setIntMonth(toDay.getMonthValue());
        calendarModel.setStrMonth(toDay.getMonth().toString());
        calendarModel.setIntLastDate(toDay.lengthOfMonth());  //월의 마지막 날짜

        DayOfWeek dayofWeek = toDay.getDayOfWeek();
        calendarModel.setIntFirstWeekDay(dayofWeek.getValue());


        mav.addObject("calendarModel", calendarModel);


        List<CalendarModel> calendarList = ss.selectList("net.javaguitar.mapper.QuizStatMapper.selectCalendarList");
        mav.addObject("calendarList", calendarList);

        mav.setViewName("content/stat/calendar");
        return mav;
    }
}
