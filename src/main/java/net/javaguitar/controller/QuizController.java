package net.javaguitar.controller;

import net.javaguitar.model.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class QuizController {

    @Autowired
    SqlSession ss;


    @RequestMapping(value = {"/", "/quiz", "/index"}, method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();

        QuizModel quizModel = ss.selectOne("net.javaguitar.mapper.QuizMapper.selectQuiz");

        if (quizModel.getQuiz_code() == 2) { // 객관식인 경우
            List<QuizObjectiveModel> ObjectList = ss
                    .selectList("net.javaguitar.mapper.QuizObjectiveMapper.selectQuizObjective", quizModel);
            mav.addObject("objectList", ObjectList);
        }
        // 문제패턴
        List<CodeModel> ptnCodeList = ss.selectList("net.javaguitar.mapper.CodeMapper.selectCode", 5);
        // 난이도
        List<CodeModel> lvlCodeList = ss.selectList("net.javaguitar.mapper.CodeMapper.selectCode", 9);

        QuizDocumentModel quizDocumentModel = new QuizDocumentModel();
        quizDocumentModel.setDoc_code(quizModel.getDoc_code());
        quizDocumentModel.setQuiz_number(quizModel.getQuiz_number());
        // 관련문서
        List<QuizDocumentModel> quizDocumentModelList = ss
                .selectList("net.javaguitar.mapper.QuizDocumentMapper.selectQuizDocumentListByQuiz", quizDocumentModel);

        // 퀴즈통계
        QuizStatModel quizStatModel = new QuizStatModel();
        quizStatModel.setDoc_code(quizModel.getDoc_code());
        quizStatModel.setQuiz_number(quizModel.getQuiz_number());
        quizStatModel = ss.selectOne("net.javaguitar.mapper.QuizStatMapper.selectQuizSuccessRate", quizStatModel);

        mav.addObject("ptnCodeList", ptnCodeList);
        mav.addObject("lvlCodeList", lvlCodeList);
        mav.addObject("quizModel", quizModel);
        mav.addObject("quizStatModel", quizStatModel);
        mav.addObject("quizDocumentModelList", quizDocumentModelList);

        mav.setViewName("content/quiz/quiz");
        return mav;
    }

    @RequestMapping(value = {"/index/{doc_code}/{quiz_number}"}, method = RequestMethod.GET)
    public ModelAndView quizIndex(@PathVariable int doc_code, @PathVariable int quiz_number, ModelMap model)
            throws Exception {
        ModelAndView mav = new ModelAndView();

        QuizModel quizModel = new QuizModel();

        quizModel.setDoc_code(doc_code);
        quizModel.setQuiz_number(quiz_number);
        quizModel = ss.selectOne("net.javaguitar.mapper.QuizMapper.selectQuizEdit", quizModel);

        if (quizModel.getQuiz_code() == 2) {
            List<QuizObjectiveModel> ObjectList = ss
                    .selectList("net.javaguitar.mapper.QuizObjectiveMapper.selectQuizObjective", quizModel);
            mav.addObject("objectList", ObjectList);
        }
        // 문제패턴
        List<CodeModel> ptnCodeList = ss.selectList("net.javaguitar.mapper.CodeMapper.selectCode", 5);
        // 난이도
        List<CodeModel> lvlCodeList = ss.selectList("net.javaguitar.mapper.CodeMapper.selectCode", 9);

        QuizDocumentModel quizDocumentModel = new QuizDocumentModel();
        quizDocumentModel.setDoc_code(doc_code);
        quizDocumentModel.setQuiz_number(quiz_number);
        // 관련문서
        List<QuizDocumentModel> quizDocumentModelList = ss
                .selectList("net.javaguitar.mapper.QuizDocumentMapper.selectQuizDocumentListByQuiz", quizDocumentModel);

        // 퀴즈통계
        QuizStatModel quizStatModel = new QuizStatModel();
        quizStatModel.setDoc_code(quizModel.getDoc_code());
        quizStatModel.setQuiz_number(quizModel.getQuiz_number());
        quizStatModel = ss.selectOne("net.javaguitar.mapper.QuizStatMapper.selectQuizSuccessRate", quizStatModel);

        mav.addObject("ptnCodeList", ptnCodeList);
        mav.addObject("lvlCodeList", lvlCodeList);
        mav.addObject("quizModel", quizModel);
        mav.addObject("quizStatModel", quizStatModel);
        mav.addObject("quizDocumentModelList", quizDocumentModelList);

        mav.setViewName("content/quiz/quiz");
        return mav;
    }

    @RequestMapping(value = {"/quiz/desc/{doc_code}/{quiz_number}"}, method = RequestMethod.GET)
    public ModelAndView quiz_desc(@ModelAttribute("quizModel") QuizModel quizModel,
                                  @PathVariable int doc_code, @PathVariable int quiz_number) {
        ModelAndView mav = new ModelAndView();

        quizModel.setDoc_code(doc_code);
        quizModel.setQuiz_number(quiz_number);
        quizModel = ss.selectOne("net.javaguitar.mapper.QuizMapper.selectQuizEdit", quizModel);

        mav.addObject("quizModel", quizModel);

        mav.setViewName("content/quiz/descript");
        return mav;
    }

    @RequestMapping(value = {"/quiz/desc/edit/{doc_code}/{quiz_number}"}, method = RequestMethod.GET)
    public ModelAndView quiz_desc_edit(@ModelAttribute("quizModel") QuizModel quizModel,
                                       @PathVariable int doc_code, @PathVariable int quiz_number) {
        ModelAndView mav = new ModelAndView();

        quizModel.setDoc_code(doc_code);
        quizModel.setQuiz_number(quiz_number);
        quizModel = ss.selectOne("net.javaguitar.mapper.QuizMapper.selectQuizEdit", quizModel);

        mav.addObject("quizModel", quizModel);

        mav.setViewName("content/quiz/descript_edit");
        return mav;
    }

    @RequestMapping(value = {"/quiz/desc/update"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String quizDescUpdate(@ModelAttribute("quizModel") QuizModel quizModel
    ) throws Exception {
        String[] quiz_object_name = null;
        ss.update("net.javaguitar.mapper.QuizMapper.updateQuizDesc", quizModel);

        return "redirect:/quiz/desc/" + quizModel.getDoc_code() + "/" + quizModel.getQuiz_number();
    }

    @RequestMapping(value = {"/quiz/list"}, method = RequestMethod.GET)
    public ModelAndView quizList(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();

        // 출처
        List<CodeModel> srcCodeList = ss.selectList("net.javaguitar.mapper.CodeMapper.selectCode", 13);

        mav.addObject("srcCodeList", srcCodeList);

        mav.setViewName("content/quiz/list");
        return mav;
    }

    @RequestMapping(value = {"/quiz/search/{quiz_title}"}, method = RequestMethod.GET)
    public ModelAndView docName(@PathVariable String quiz_title, HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView();


        List<QuizModel> quizList = ss.selectList("net.javaguitar.mapper.QuizMapper.selectQuizSearch", quiz_title);

        mav.addObject("quizList", quizList);

        mav.setViewName("content/quiz/search");
        return mav;
    }

    @RequestMapping(value = {"/quiz/date/{quiz_date}"}, method = RequestMethod.GET)
    public ModelAndView quizDate(@PathVariable String quiz_date, HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView();


        List<QuizModel> quizList = ss.selectList("net.javaguitar.mapper.QuizMapper.selectQuizDayList", quiz_date);

        mav.addObject("quizList", quizList);

        mav.setViewName("content/quiz/quiz_stat");
        return mav;
    }

    @RequestMapping(value = {"/quiz/doc/{doc_name}"}, method = RequestMethod.GET)
    public ModelAndView quizDocList(@PathVariable String doc_name) {
        ModelAndView mav = new ModelAndView();
        List<QuizModel> quizList = ss.selectList("net.javaguitar.mapper.QuizDocumentMapper.selectQuizDocumentListByDoc", doc_name);
        mav.addObject("doc_name", doc_name);
        mav.addObject("quizList", quizList);
        mav.setViewName("content/quiz/quizdoc_list");
        return mav;
    }

    @RequestMapping(value = {"/quiz/list/{quiz_source}"}, method = RequestMethod.GET)
    public ModelAndView quizListDetail(@PathVariable int quiz_source, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();

        //세션 만들기
        HttpSession session = request.getSession();
        session.setAttribute("quiz_source", quiz_source);

        // 출처
        List<CodeModel> srcCodeList = ss.selectList("net.javaguitar.mapper.CodeMapper.selectCode", 13);

        List<QuizModel> quizList = ss.selectList("net.javaguitar.mapper.QuizMapper.selectQuizList", quiz_source);

        mav.addObject("srcCodeList", srcCodeList);
        mav.addObject("quizList", quizList);
        mav.addObject("quiz_source", quiz_source);

        mav.setViewName("content/quiz/list");
        return mav;
    }

    @RequestMapping(value = {"/quiz/all_list"}, method = RequestMethod.GET)
    public ModelAndView quizAllList() {
        ModelAndView mav = new ModelAndView();

        // 출처
        List<CodeModel> srcCodeList = ss.selectList("net.javaguitar.mapper.CodeMapper.selectCode", 13);
        mav.addObject("srcCodeList", srcCodeList);

        mav.setViewName("content/quiz/all_list");
        return mav;
    }

    @RequestMapping(value = {"/quiz/write"}, method = RequestMethod.GET)
    public ModelAndView quizWrite(@ModelAttribute("quizModel") QuizModel quizModel, ModelMap mode) throws Exception {
        ModelAndView mav = new ModelAndView();
        int quizNumber;
        // 문서분류
        List<DocCategoryModel> docCatehogryList = ss
                .selectList("net.javaguitar.mapper.DocCategoryMapper.selectDocCategoryList");
        // 출처
        List<CodeModel> srcCodeList = ss.selectList("net.javaguitar.mapper.CodeMapper.selectCode", 13);
        // 문항방식
        List<CodeModel> quizCodeList = ss.selectList("net.javaguitar.mapper.CodeMapper.selectCode", 1);
        // 문제패턴
        List<CodeModel> ptnCodeList = ss.selectList("net.javaguitar.mapper.CodeMapper.selectCode", 5);
        // 난이도
        List<CodeModel> lvlCodeList = ss.selectList("net.javaguitar.mapper.CodeMapper.selectCode", 9);

        quizNumber = ss.selectOne("net.javaguitar.mapper.QuizMapper.selectMaxQuizNumber");

        mav.addObject("srcCodeList", srcCodeList);
        mav.addObject("docCatehogryList", docCatehogryList);
        mav.addObject("quizCodeList", quizCodeList);
        mav.addObject("ptnCodeList", ptnCodeList);
        mav.addObject("", lvlCodeList);
        mav.addObject("quizNumber", quizNumber);

        mav.setViewName("content/quiz/write");
        return mav;

    }

    /* 정답 미리 가져오기 */
    @RequestMapping(value = {"/pre_answer"}, method = RequestMethod.POST)
    public @ResponseBody
    String pre_answer(@ModelAttribute("quizModel") QuizModel quizModel) {

        return ss.selectOne("net.javaguitar.mapper.QuizMapper.selectPreAnswer",quizModel);

    }

    @RequestMapping(value = {"/quiz/edit/{doc_code}/{quiz_number}"}, method = RequestMethod.GET)
    public ModelAndView quizEdit(@PathVariable int doc_code, @PathVariable int quiz_number,
                                 @ModelAttribute("quizModel") QuizModel quizModel, ModelMap model) throws Exception {
        ModelAndView mav = new ModelAndView();
        quizModel.setDoc_code(doc_code);
        quizModel.setQuiz_number(quiz_number);
        quizModel = ss.selectOne("net.javaguitar.mapper.QuizMapper.selectQuizEdit", quizModel);

        // 출처
        List<CodeModel> srcCodeList = ss.selectList("net.javaguitar.mapper.CodeMapper.selectCode", 13);
        // 문항방식
        List<CodeModel> quizCodeList = ss.selectList("net.javaguitar.mapper.CodeMapper.selectCode", 1);
        // 문제패턴
        List<CodeModel> ptnCodeList = ss.selectList("net.javaguitar.mapper.CodeMapper.selectCode", 5);
        // 난이도
        List<CodeModel> lvlCodeList = ss.selectList("net.javaguitar.mapper.CodeMapper.selectCode", 9);

        // 문항
        List<QuizObjectiveModel> quizObjectiveList = ss
                .selectList("net.javaguitar.mapper.QuizObjectiveMapper.selectQuizObjective", quizModel);
        // 문서분류
        List<DocCategoryModel> docCatehogryList = ss
                .selectList("net.javaguitar.mapper.DocCategoryMapper.selectDocCategoryList");

        QuizDocumentModel quizDocumentModel = new QuizDocumentModel();
        quizDocumentModel.setDoc_code(doc_code);
        quizDocumentModel.setQuiz_number(quiz_number);
        // 관련문서
        List<QuizDocumentModel> quizDocumentModelList = ss
                .selectList("net.javaguitar.mapper.QuizDocumentMapper.selectQuizDocumentListByQuiz", quizDocumentModel);

        mav.addObject("srcCodeList", srcCodeList);
        mav.addObject("quizCodeList", quizCodeList);
        mav.addObject("ptnCodeList", ptnCodeList);
        mav.addObject("lvlCodeList", lvlCodeList);
        mav.addObject("docCatehogryList", docCatehogryList);
        mav.addObject("quizModel", quizModel);
        mav.addObject("quizObjectiveList", quizObjectiveList);

        mav.addObject("quizDocumentModelList", quizDocumentModelList);
        mav.setViewName("content/quiz/edit");
        return mav;

    }


    @RequestMapping(value = {"/quiz/insert"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String quizInsert(@ModelAttribute("quizModel") QuizModel quizModel, ModelMap model,
                             HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
        int quizNumber;
        String[] quiz_object_name = null;
        quizNumber = ss.selectOne("net.javaguitar.mapper.QuizMapper.selectMaxQuizNumber");
        quizModel.setQuiz_number(quizNumber);
        //quizModel.setQuiz_title(quizModel.getQuiz_title().replaceAll("<table>", "<table style='width:100%';>"));
        //quizModel.setQuiz_title(quizModel.getQuiz_title().replaceAll("<td>", "<td style='border:1px solid hsl(0, 0%, 0%);text-align:center;'>"));
        //quizModel.setQuiz_subtitle(quizModel.getQuiz_subtitle().replaceAll("<table>", "<table style='width:100%';>"));
        //quizModel.setQuiz_subtitle(quizModel.getQuiz_subtitle().replaceAll("<td>", "<td style='border:1px solid hsl(0, 0%, 0%);text-align:center;'>"));
        ss.insert("net.javaguitar.mapper.QuizMapper.insertQuiz", quizModel);

        if (quizModel.getQuiz_code() == 2) { // 객관식인 경우

            quiz_object_name = request.getParameterValues("quiz_object_name");

            for (int i = 0; i < quiz_object_name.length; i++) {
                if (!quiz_object_name[i].equals("")) {
                    //quizModel.setQuiz_object_name(quiz_object_name[i].replaceAll("<table>", "<table style='width:100%';>"));
                    quizModel.setQuiz_object_name(quiz_object_name[i].replaceAll("<td>", "<td style='border:1px solid hsl(0, 0%, 0%);text-align:center;'>"));
                    quizModel.setQuiz_object_num(i + 1);
                    ss.insert("net.javaguitar.mapper.QuizObjectiveMapper.insertQuizObjective", quizModel);
                }
            }

        }
        //세션 만들기
        HttpSession session = request.getSession();
        session.setAttribute("quiz_org_number", Integer.parseInt(quizModel.getQuiz_org_number()) + 1);
        session.setAttribute("doc_code", quizModel.getDoc_code());
        session.setAttribute("quiz_source", quizModel.getQuiz_source());
        return "redirect:/quiz/write";
    }


    @RequestMapping(value = {"/quiz/update"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String quizUpdate(@ModelAttribute("quizModel") QuizModel quizModel,
                             HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
        String[] quiz_object_name = null;
        //quizModel.setQuiz_title(quizModel.getQuiz_title().replaceAll("<table>", "<table style='width:100%';>"));
        //quizModel.setQuiz_subtitle(quizModel.getQuiz_subtitle().replaceAll("<table>", "<table style='width:100%';>"));
        ss.update("net.javaguitar.mapper.QuizMapper.updateQuiz", quizModel);

        if (quizModel.getQuiz_code() == 2) { // 객관식인 경우

            quiz_object_name = request.getParameterValues("quiz_object_name");

            for (int i = 0; i < quiz_object_name.length; i++) {
                //quizModel.setQuiz_object_name(quiz_object_name[i].replaceAll("<table>", "<table style='width:100%';>"));
                quizModel.setQuiz_object_num(i + 1);
                quizModel.setQuiz_object_name(quiz_object_name[i]);
                ss.update("net.javaguitar.mapper.QuizObjectiveMapper.updateQuizObjective", quizModel);
            }

        }

        //세션 만들기
        HttpSession session = request.getSession();
        session.setAttribute("quiz_source", quizModel.getQuiz_source());

        return "redirect:/index/" + quizModel.getDoc_code() + "/" + quizModel.getQuiz_number();
    }


    @RequestMapping(value = {"/quiz_bookmark_insert"}, method = {RequestMethod.POST})
    public @ResponseBody
    void quizBookMarkInsert(HttpServletRequest request, @ModelAttribute("quizBMModel") QuizBMModel quizBMModel) throws Exception {
        int quizBMCnt = 0;

        quizBMCnt = ss.selectOne("net.javaguitar.mapper.QuizBMMapper.selectQuizBMCnt", quizBMModel);
        if (quizBMCnt == 0) {
            ss.insert("net.javaguitar.mapper.QuizBMMapper.insertQuizBM", quizBMModel);
        }
    }

    @RequestMapping(value = {"/quiz_etc_update"}, method = {RequestMethod.POST})
    public @ResponseBody
    void quizEtcUpdate(HttpServletRequest request, @ModelAttribute("quizModel") QuizModel quizModel) throws Exception {
        ss.update("net.javaguitar.mapper.QuizMapper.updateQuizEtc", quizModel);
    }
}
