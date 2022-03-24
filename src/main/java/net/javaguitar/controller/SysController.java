package net.javaguitar.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SysController {

	@Autowired
	SqlSession ss;

	/**
	 * 
	 * @Method Name: quizIndex
	 * @Method 설명 : 퀴즈 풀이(랜덤)
	 * @author : javaguitar
	 * @version : 0.1
	 * @since : 1.0
	 * @param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/sys/index" }, method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();

		mav.setViewName("sys/index");
		return mav;
	}

}
