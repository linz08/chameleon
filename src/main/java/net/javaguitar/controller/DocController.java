package net.javaguitar.controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.javaguitar.model.QuizModel;
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
    public ModelAndView docName(@PathVariable String doc_name, HttpServletRequest request) throws Exception {

        if (request.getHeader("Referer") != null) {
            String referrer = (request.getHeader("Referer"));
            if (referrer.indexOf("/doc/") > 0) {
                String decodeResult = URLDecoder.decode(referrer.substring(referrer.indexOf("/doc/") + 5), "UTF-8");
                decodeResult = decodeResult.replaceAll("edit/","").replaceAll("index","");
                //세션 만들기
                if (!decodeResult.equals("edit")) {
                    HttpSession session = request.getSession();
                    String doc_path = "";
                    if (session.getAttribute("doc_path") != null) {
                        doc_path = session.getAttribute("doc_path").toString();
                    }
                    if (!doc_path.contains(decodeResult)) {
                        session.setAttribute("doc_path", doc_path + "/" + decodeResult);
                    }
                }
            }
        }
        ModelAndView mav = new ModelAndView();
        int docCheck = ss.selectOne("net.javaguitar.mapper.DocMapper.selectDocCheck", doc_name);
        DocModel docModel = new DocModel();
        if (docCheck == 0) {
            docModel.setDoc_name(doc_name);
            ss.insert("net.javaguitar.mapper.DocMapper.insertDoc", docModel);
        }
        docModel = ss.selectOne("net.javaguitar.mapper.DocMapper.selectDoc", doc_name);
        mav.addObject("docModel", docModel);
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

    /**
     * @param
     * @return
     * @throws Exception
     * @Method Name: boardWrite
     * @Method 설명 : 게시글 등록폼
     * @작성자 : 최용진
     * @작성일 : 2016. 6. 23.
     */
    @RequestMapping(value = {"/doc/write"}, method = RequestMethod.GET)
    public String docWrite(@ModelAttribute("docVO") DocModel docVO, ModelMap model) throws Exception {

        return "content/doc/write";

    }

    /**
     * @param
     * @return
     * @throws Exception
     * @Method Name: quizUpdatejava.nio.charset.StandardCharsets.UTF_8.toString()
     * @Method 설명 : 문제 수정
     * @author : javaguitar
     * @version : 0.1
     * @since : 1.0
     */
    @RequestMapping(value = {"/doc/update"}, method = {RequestMethod.POST})
    public String docUpdate(@ModelAttribute("docModel") DocModel docModel, ModelMap model,
                            HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
        ss.update("net.javaguitar.mapper.DocMapper.updateDoc", docModel);
        String docName = URLEncoder.encode(docModel.getDoc_name(), java.nio.charset.StandardCharsets.UTF_8.toString());
        docName = docName.replaceAll("\\+", "%20");
        return "redirect:/doc/" + docName;
    }

}
