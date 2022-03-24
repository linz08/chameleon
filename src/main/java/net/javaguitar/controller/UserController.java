package net.javaguitar.controller;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import net.javaguitar.model.UsersModel;

@Controller
public class UserController {

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
	@GetMapping("/login")
	public String login(@ModelAttribute("usersModel") UsersModel usersModel) {
		return "content/users/login";
	}

}
