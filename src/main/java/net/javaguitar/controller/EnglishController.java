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


@Controller
public class EnglishController {

	@Autowired
	SqlSession ss;

	/**
	 * 
	 * @Method Name: english
	 * @Method 설명 : 영어 풀이(랜덤)
	 * @author : javaguitar
	 * @version : 0.1
	 * @since : 1.0
	 * @param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/english" }, method = RequestMethod.GET)
	public ModelAndView english_index(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();

		EnglishModel englishModel = ss.selectOne("net.javaguitar.mapper.EnglishMapper.selectEnglish");

		mav.addObject("englishModel", englishModel);

		mav.setViewName("content/english/english");
		return mav;
	}

	/**
	 * 
	 * @Method Name: quizWrite
	 * @Method 설명 : 퀴즈 등록폼
	 * @author : javaguitar
	 * @version : 0.1
	 * @since : 1.0
	 * @param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/english/write" }, method = RequestMethod.GET)
	public ModelAndView quizWrite(@ModelAttribute("englishModel") QuizModel englishModel, ModelMap mode) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("content/quiz/write");
		return mav;

	}

	/**
	 * 
	 * @Method Name: quizInsert
	 * @Method 설명 : 문제 등록
	 * @author : javaguitar
	 * @version : 0.1
	 * @since : 1.0
	 * @param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/english/insert" }, method = { RequestMethod.POST, RequestMethod.GET })
	public String quizInsert(@ModelAttribute("englishModel") QuizModel englishModel, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
		int quizNumber;
		String[] quiz_object_name = null;
		quizNumber = ss.selectOne("net.javaguitar.mapper.QuizMapper.selectMaxQuizNumber");
		englishModel.setQuiz_number(quizNumber);
		ss.insert("net.javaguitar.mapper.QuizMapper.insertQuiz", englishModel);

		if (englishModel.getQuiz_code() == 2) { // 객관식인 경우

			quiz_object_name = request.getParameterValues("quiz_object_name");

			for (int i = 0; i < quiz_object_name.length; i++) {
				englishModel.setQuiz_object_name(quiz_object_name[i]);
				englishModel.setQuiz_object_num(i + 1);
				ss.insert("net.javaguitar.mapper.QuizObjectiveMapper.insertQuizObjective", englishModel);
			}

		}
		return "redirect:/quiz/list/" + englishModel.getQuiz_source();
	}

	/**
	 * 
	 * @Method Name: quizUpdate
	 * @Method 설명 : 문제 수정
	 * @author : javaguitar
	 * @version : 0.1
	 * @since : 1.0
	 * @param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/english/update" }, method = { RequestMethod.POST, RequestMethod.GET })
	public String quizUpdate(@ModelAttribute("englishModel") QuizModel englishModel, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
		String[] quiz_object_name = null;
		ss.update("net.javaguitar.mapper.QuizMapper.updateQuiz", englishModel);

		if (englishModel.getQuiz_code() == 2) { // 객관식인 경우

			quiz_object_name = request.getParameterValues("quiz_object_name");

			for (int i = 0; i < quiz_object_name.length; i++) {
				englishModel.setQuiz_object_name(quiz_object_name[i]);
				englishModel.setQuiz_object_num(i + 1);
				ss.update("net.javaguitar.mapper.QuizObjectiveMapper.updateQuizObjective", englishModel);
			}

		}
		return "redirect:/index/" + englishModel.getDoc_code() + "/" + englishModel.getQuiz_number();
	}



}
