package net.javaguitar.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.javaguitar.model.DocModel;

@Controller
public class DocController {

	@Autowired
	SqlSession ss;

	@RequestMapping(value = "/doc/index", method = RequestMethod.GET)
	public ModelAndView docList(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();

		List<DocModel> docList = ss.selectList("net.javaguitar.mapper.DocMapper.selectDocIndex");
		mav.addObject("docList", docList);
		mav.setViewName("content/doc/list");
		return mav;
	}

	@RequestMapping(value = "/doc/{doc_name}", method = RequestMethod.GET)
	public ModelAndView docName(@PathVariable String doc_name) {
		ModelAndView mav = new ModelAndView();

		DocModel docContent = ss.selectOne("net.javaguitar.mapper.DocMapper.selectDoc", doc_name);
		mav.addObject("docContent", docContent);
		mav.setViewName("content/doc/view");
		return mav;
	}
	
	@RequestMapping(value = "/doc/edit/{doc_name}", method = RequestMethod.GET)
	public ModelAndView docEdit(@PathVariable String doc_name) {
		ModelAndView mav = new ModelAndView();

		DocModel docContent = ss.selectOne("net.javaguitar.mapper.DocMapper.selectDoc", doc_name);
		mav.addObject("docContent", docContent);
		mav.setViewName("content/doc/edit");
		return mav;
	}

	/**
	 * 
	 * @Method Name: boardWrite
	 * @Method 설명 : 게시글 등록폼
	 * @작성자 : 최용진
	 * @작성일 : 2016. 6. 23.
	 * @param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/doc/write" }, method = RequestMethod.GET)
	public String docWrite(@ModelAttribute("docVO") DocModel docVO, ModelMap model) throws Exception {

		return "content/doc/write";

	}

	@RequestMapping(value = { "/doc/insert" }, method = { RequestMethod.POST, RequestMethod.GET })
	public String docInsert(@ModelAttribute("docVO") DocModel docVO, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws Exception {

		return "redirect:/doc/write";

	}

}
