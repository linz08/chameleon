package net.javaguitar.controller;

import net.javaguitar.model.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
public class DocController {

    @Autowired
    SqlSession ss;

    // 문서 기본 페이지
    @RequestMapping(value = "/doc/index", method = RequestMethod.GET)
    public ModelAndView docList(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        //List<DocModel> docList = ss.selectList("net.javaguitar.mapper.DocMapper.selectDocIndex");
        StatModel statModel = ss.selectOne("net.javaguitar.mapper.StatMapper.selectQuizCount");
        StatModel statModel_doc = ss.selectOne("net.javaguitar.mapper.StatMapper.selectDocCount");
        StatModel statModel_answer = ss.selectOne("net.javaguitar.mapper.StatMapper.selectQuizAnswerCount");
        StatModel statModel_ratio = ss.selectOne("net.javaguitar.mapper.StatMapper.selectQuizAnswerRatio");

        // List<DocModel> docTodayList = ss.selectList("net.javaguitar.mapper.DocMapper.selectDocToDay"); 밑으로
        LocalDate now = LocalDate.now();

        // 이번달 체크
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        String month_val = now.format(formatter);

        mav.addObject("statModel", statModel);
        mav.addObject("statModel_doc", statModel_doc);
        mav.addObject("statModel_answer", statModel_answer);
        mav.addObject("statModel_ratio", statModel_ratio);
        mav.addObject("month_val", month_val);
        //mav.addObject("docTodayList", docTodayList);
        mav.setViewName("content/doc/index");
        return mav;
    }

    //월별 문서 정리 -> 나중엔 캘린더로 변경 고려
    @RequestMapping(value = "/doc/calendar", method = RequestMethod.GET)
    public ModelAndView docCalendar(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        // 이번달 체크
            LocalDate now = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        String month_val = now.format(formatter);
        DocModel docModel = new DocModel();
        docModel.setDoc_month(month_val);
        List<DocModel> quizTodayList;
        quizTodayList = ss
                .selectList("net.javaguitar.mapper.DocMapper.selectDocToDay", docModel);
        mav.addObject("month_val", month_val);
        mav.addObject("quizTodayList", quizTodayList);
        mav.setViewName("content/doc/calendar");
        return mav;
    }
    @RequestMapping(value = "/doc/calendar/{yyyymm}", method = RequestMethod.GET)
    public ModelAndView docCalendarYYYYmm(@PathVariable String yyyymm) {
        ModelAndView mav = new ModelAndView();
        DocModel docModel = new DocModel();
        docModel.setDoc_month(yyyymm);
        List<DocModel> quizTodayList;
        quizTodayList = ss
                .selectList("net.javaguitar.mapper.DocMapper.selectDocToDay", docModel);
        mav.addObject("month_val", yyyymm);
        mav.addObject("quizTodayList", quizTodayList);
        mav.setViewName("content/doc/calendar");
        return mav;
    }

    @RequestMapping(value = {"/quiz/doc/monthlist"}, method = RequestMethod.POST)
    public @ResponseBody
    List<DocModel> quizDocTodayList(@ModelAttribute("quizDocumentModel") DocModel docModel) {
        List<DocModel> quizTodayList;
        quizTodayList = ss
                .selectList("net.javaguitar.mapper.DocMapper.selectDocToDay", docModel);

        return quizTodayList;

    }

    @RequestMapping(value = "/doc/{doc_name}", method = RequestMethod.GET)
    public ModelAndView docName(@PathVariable String doc_name, HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView();
        int docCheck = ss.selectOne("net.javaguitar.mapper.DocMapper.selectDocCheck", doc_name);
        DocModel docModel = new DocModel();
        if (docCheck == 0) {
            //String referer = request.getHeader("Referer");
            //String upper_doc_name = referer.substring(referer.lastIndexOf("/") + 1);
            //upper_doc_name = URLDecoder.decode(upper_doc_name, java.nio.charset.StandardCharsets.UTF_8.toString());
            docModel.setUpper_doc_name("index");
            docModel.setDoc_name(doc_name);
            ss.insert("net.javaguitar.mapper.DocMapper.insertDoc", docModel);


            DocKeywordModel docKeywordModel = new DocKeywordModel();
            docKeywordModel.setKeyword(docModel.getDoc_name());
            docKeywordModel.setDoc_name(docModel.getDoc_name());
            ss.insert("net.javaguitar.mapper.DocKeywordMapper.insertDocKeyword", docKeywordModel);
        }
        // 문서레벨
        List<CodeModel> docLevelList = ss.selectList("net.javaguitar.mapper.CodeMapper.selectCode", 27);

        docModel = ss.selectOne("net.javaguitar.mapper.DocMapper.selectDoc", doc_name);

        //관련퀴즈 카운트
        int quizCnt = ss.selectOne("net.javaguitar.mapper.QuizDocumentMapper.selectQuizDocumentCount", doc_name);

        //유사 문서명
        DocKeywordModel docKeywordModel = new DocKeywordModel();
        docKeywordModel.setDoc_name(doc_name);
        List<DocKeywordModel> docKeywordModelList = ss
                .selectList("net.javaguitar.mapper.DocKeywordMapper.selectDocKeyword", docKeywordModel);


        mav.addObject("docModel", docModel);
        mav.addObject("docLevelList", docLevelList);
        mav.addObject("docKeywordModelList", docKeywordModelList);
        mav.addObject("quizCnt", quizCnt);
        mav.setViewName("content/doc/view");
        return mav;
    }

    @RequestMapping(value = "/doc/edit/{doc_name}", method = RequestMethod.GET)
    public ModelAndView docEdit(@PathVariable String doc_name) {
        ModelAndView mav = new ModelAndView();

        DocModel docModel = ss.selectOne("net.javaguitar.mapper.DocMapper.selectDoc", doc_name);
        mav.addObject("docModel", docModel);
        mav.setViewName("content/doc/edit");
        return mav;
    }

    @RequestMapping(value = {"/doc/delete/{doc_name}"}, method = {RequestMethod.GET})
    public String docDelete(@ModelAttribute("docModel") DocModel docModel
    ) throws Exception {
        DocKeywordModel docKeywordModel = new DocKeywordModel();
        docKeywordModel.setDoc_name(docModel.getDoc_name());
        ss.update("net.javaguitar.mapper.DocMapper.deleteDoc", docModel);
        ss.update("net.javaguitar.mapper.DocMapper.deleteDocbyQuiz", docModel);
        ss.update("net.javaguitar.mapper.DocKeywordMapper.deleteDocByKeyword", docKeywordModel);
        return "redirect:/doc/index";
    }

    @RequestMapping(value = {"/doc/write"}, method = RequestMethod.GET)
    public ModelAndView docWrite() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("content/doc/write");
        return mav;

    }


    @RequestMapping(value = {"/doc/update"}, method = {RequestMethod.POST})
    public String docUpdate(@ModelAttribute("docModel") DocModel docModel
    ) throws Exception {
        if (docModel.getDoc_name().equals("감리") || docModel.getDoc_name().equals("사업관리") ||
                docModel.getDoc_name().equals("소프트웨어공학") || docModel.getDoc_name().equals("데이터베이스") ||
                docModel.getDoc_name().equals("시스템구조") || docModel.getDoc_name().equals("보안") ||
                docModel.getDoc_name().equals("기타")

        ) {
            String strDoc_content = null;
            strDoc_content = urlReplace(docModel.getDoc_content());

            //System.out.println(strDoc_content);
            docModel.setDoc_content(strDoc_content);
        }
        docModel.setDoc_content(docModel.getDoc_content().replaceAll("http://javaguitar.net:8080",""));
        ss.update("net.javaguitar.mapper.DocMapper.updateDoc", docModel);
        String docName = URLEncoder.encode(docModel.getDoc_name(), java.nio.charset.StandardCharsets.UTF_8.toString());
        docName = docName.replaceAll("\\+", "%20");
        return "redirect:/doc/" + docName;
    }

    private String urlReplace(String strDoc_content) {
        int phase_idx = 0;
        int start_idx = 0;
        int idx_end = 0;
        String sub_content = null;
        String strUrl = null;
        String strDocName = null;
        String strReplaceUrl = null;
        String strRepCon = strDoc_content;
        int doc_str_idx = 0;
        int doc_end_idx = 0;
        //System.out.println("strDoc_content="+strDoc_content);
        while (phase_idx != -1) {
            sub_content = strDoc_content.substring(start_idx, strDoc_content.length());
            //System.out.println("sub_content=" + sub_content);

            phase_idx = sub_content.indexOf("<a href=");
            if (phase_idx != -1) {
                idx_end = sub_content.indexOf("</a>");
                strUrl = sub_content.substring(phase_idx, idx_end + 4);
                doc_str_idx = strUrl.indexOf("\">");
                doc_end_idx = strUrl.indexOf("</a>");
                strDocName = (strUrl.substring(doc_str_idx + 2, doc_end_idx));
                //System.out.println("strDocName="+strDocName);
                strReplaceUrl = "<a href=\"/doc/" + strDocName + "\" name=\"" + strDocName + "\">" + strDocName + "</a>";

                strRepCon = strRepCon.replaceAll(strUrl, strReplaceUrl);
                //System.out.println("strRepCon="+strRepCon);

                start_idx = start_idx + idx_end + 4;
            }
        }
        return strRepCon;
    }

    @RequestMapping(value = {"/docSearch"}, method = {RequestMethod.POST})
    public @ResponseBody
    Map<String, Object> docSearch(@RequestParam Map<String, Object> paramMap,
                                  @RequestParam String doc_name) {
        List<Map> resultList = ss.selectList("net.javaguitar.mapper.DocKeywordMapper.selectDocSearch", doc_name);
        paramMap.put("resultList", resultList);
        return paramMap;
    }

    @RequestMapping(value = {"/docAdd"}, method = {RequestMethod.POST})
    public @ResponseBody
    List<QuizDocumentModel> docAdd(@ModelAttribute("quizDocumentModel") QuizDocumentModel quizDocumentModel) {
        ss.insert("net.javaguitar.mapper.QuizDocumentMapper.insertQuizDocument", quizDocumentModel);
        List<QuizDocumentModel> quizDocumentModelList;
        quizDocumentModelList = ss
                .selectList("net.javaguitar.mapper.QuizDocumentMapper.selectQuizDocumentListByQuiz", quizDocumentModel);

        return quizDocumentModelList;
    }

    @RequestMapping(value = {"/docKeywordAdd"}, method = {RequestMethod.POST})
    public @ResponseBody
    List<DocKeywordModel> docKeywordAdd(@ModelAttribute("docKeywordModel") DocKeywordModel docKeywordModel) {
        ss.insert("net.javaguitar.mapper.DocKeywordMapper.insertDocKeyword", docKeywordModel);
        //유사 문서명
         List<DocKeywordModel> docKeywordModelList;
        docKeywordModelList = ss
                .selectList("net.javaguitar.mapper.DocKeywordMapper.selectDocKeyword", docKeywordModel);
        return docKeywordModelList;
    }

    @RequestMapping(value = {"/docDel"}, method = {RequestMethod.POST})
    public @ResponseBody
    void docDel(@ModelAttribute("quizDocumentModel") QuizDocumentModel quizDocumentModel) {
        ss.delete("net.javaguitar.mapper.QuizDocumentMapper.deleteQuizDocument", quizDocumentModel);
    }

    @RequestMapping(value = {"/docKeywordDel"}, method = {RequestMethod.POST})
    public @ResponseBody
    void docKeywordDel(@ModelAttribute("docKeywordModel") DocKeywordModel docKeywordModel) {
        ss.delete("net.javaguitar.mapper.DocKeywordMapper.deleteDocKeyword", docKeywordModel);
    }

    /* 키워드 목록 */
    @RequestMapping(value = {"/docKeyword/list"}, method = RequestMethod.POST)
    public @ResponseBody
    List<DocKeywordModel> docKeywordList(@ModelAttribute("docKeywordModel") DocKeywordModel docKeywordModel) {
        List<DocKeywordModel> docKeywordModelList;
        docKeywordModelList = ss
                .selectList("net.javaguitar.mapper.DocKeywordMapper.selectDocKeyword", docKeywordModel);

        return docKeywordModelList;

    }
    @RequestMapping(value = {"/quiz/doc/list"}, method = RequestMethod.POST)
    public @ResponseBody
    List<QuizDocumentModel> quizDocument(@ModelAttribute("quizDocumentModel") QuizDocumentModel quizDocumentModel) {
        List<QuizDocumentModel> quizDocumentModelList;
        quizDocumentModelList = ss
                .selectList("net.javaguitar.mapper.QuizDocumentMapper.selectQuizDocumentListByQuiz", quizDocumentModel);

        return quizDocumentModelList;

    }

    @RequestMapping(value = {"/upper_doc_name_update"}, method = {RequestMethod.POST})
    public @ResponseBody
    void upper_doc_name_Update(@ModelAttribute("docModel") DocModel docModel) {


        ss.update("net.javaguitar.mapper.DocMapper.updateUpperDocName", docModel);

    }

    @RequestMapping(value = {"/new_doc_name_update"}, method = {RequestMethod.POST})
    public @ResponseBody
    void new_doc_name_Update(@ModelAttribute("docModel") DocModel docModel) {
        DocKeywordModel docKeywordModel = new DocKeywordModel();
        docKeywordModel.setDoc_name(docModel.getDoc_name());
        docKeywordModel.setKeyword(docModel.getNew_doc_name());

        ss.update("net.javaguitar.mapper.DocMapper.updateNewDocName", docModel);
        ss.update("net.javaguitar.mapper.DocMapper.updateNewDocNameQuiz", docModel);
        ss.update("net.javaguitar.mapper.DocKeywordMapper.updateDocKeyword", docKeywordModel);
    }

    @RequestMapping(value = {"/doc_memo_update"}, method = {RequestMethod.POST})
    public String doc_memo_Update(@ModelAttribute("docModel") DocModel docModel
    ) throws Exception {
        ss.update("net.javaguitar.mapper.DocMapper.updateDocMemo", docModel);
        String docName = URLEncoder.encode(docModel.getDoc_name(), java.nio.charset.StandardCharsets.UTF_8.toString());
        docName = docName.replaceAll("\\+", "%20");
        return "redirect:/doc/memo/" + docName;
    }

    @RequestMapping(value = {"/doc/memo/{doc_name}"}, method = RequestMethod.GET)
    public ModelAndView doc_memo_Select(@ModelAttribute("docModel") DocModel docModel,
                                        @PathVariable String doc_name) {
        ModelAndView mav = new ModelAndView();

        docModel.setDoc_name(doc_name);
        docModel = ss.selectOne("net.javaguitar.mapper.DocMapper.selectDoc", doc_name);

        mav.addObject("docModel", docModel);

        mav.setViewName("content/doc/memo");
        return mav;
    }

    @RequestMapping(value = {"/doc/memo/edit/{doc_name}"}, method = RequestMethod.GET)
    public ModelAndView quiz_desc_edit(@ModelAttribute("docModel") DocModel docModel,
                                       @PathVariable String doc_name) {
        ModelAndView mav = new ModelAndView();

        docModel.setDoc_name(doc_name);
        docModel = ss.selectOne("net.javaguitar.mapper.DocMapper.selectDoc", doc_name);

        mav.addObject("docModel", docModel);

        mav.setViewName("content/doc/memo_edit");
        return mav;
    }

    @RequestMapping(value = {"/doc_level_update"}, method = {RequestMethod.POST})
    public @ResponseBody
    void doc_level_Update(@ModelAttribute("docModel") DocModel docModel) {
        ss.update("net.javaguitar.mapper.DocMapper.updateDocLevel", docModel);
    }

}
