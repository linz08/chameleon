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
        quizStatModel.setQuiz_time(0);

        quizSeq = ss.selectOne("net.javaguitar.mapper.QuizStatMapper.selectMaxQuizStatSeq", quizStatModel);

        quizStatModel.setQuiz_seq(quizSeq);
        quizStatCnt = ss.selectOne("net.javaguitar.mapper.QuizStatMapper.selectQuizStatCnt", quizStatModel);
        if (quizStatCnt == 0) {
            ss.insert("net.javaguitar.mapper.QuizStatMapper.insertQuizStat", quizStatModel);
        } else {
            ss.update("net.javaguitar.mapper.QuizStatMapper.updateQuizStat", quizStatModel);
        }
    }
}
