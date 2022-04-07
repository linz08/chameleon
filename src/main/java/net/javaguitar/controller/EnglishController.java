package net.javaguitar.controller;

import com.google.gson.Gson;
import net.javaguitar.model.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class EnglishController {

    @Autowired
    SqlSession ss;

    /**
     * @param
     * @return
     * @throws Exception
     * @Method Name: english
     * @Method 설명 : 영어 풀이(랜덤)
     * @author : javaguitar
     * @version : 0.1
     * @since : 1.0
     */
    @RequestMapping(value = {"/english"}, method = RequestMethod.GET)
    public ModelAndView english_index(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();

        EnglishModel englishModel = ss.selectOne("net.javaguitar.mapper.EnglishMapper.selectEnglish");

        mav.addObject("englishModel", englishModel);

        mav.setViewName("content/english/english");
        return mav;
    }

    @RequestMapping(value = {"/english/{oneword}"}, method = RequestMethod.GET)
    public ModelAndView english_select(@PathVariable String oneword,
                                       @ModelAttribute("englishModel") EnglishModel englishModel, ModelMap model) throws Exception {
        ModelAndView mav = new ModelAndView();
        englishModel.setOneword(oneword);

        englishModel = ss.selectOne("net.javaguitar.mapper.EnglishMapper.selectEnglishEdit", englishModel);

        mav.addObject("englishModel", englishModel);
        mav.setViewName("content/english/english");
        return mav;

    }

    /**
     * @param
     * @return
     * @throws Exception
     * @Method Name: quizWrite
     * @Method 설명 : 퀴즈 등록폼
     * @author : javaguitar
     * @version : 0.1
     * @since : 1.0
     */
    @RequestMapping(value = {"/english/write"}, method = RequestMethod.GET)
    public ModelAndView englishWrite(@ModelAttribute("englishModel") EnglishModel englishModel, ModelMap mode) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("content/english/write");
        return mav;

    }

    /**
     * @param
     * @return
     * @throws Exception
     * @Method Name: quizInsert
     * @Method 설명 : 문제 등록
     * @author : javaguitar
     * @version : 0.1
     * @since : 1.0
     */
    @RequestMapping(value = {"/english/insert"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String quizInsert(@ModelAttribute("englishModel") EnglishModel englishModel, ModelMap model,
                             HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
        ss.insert("net.javaguitar.mapper.EnglishMapper.insertEnglish", englishModel);

        return "redirect:/english";
    }

    /**
     * @param
     * @return
     * @throws Exception
     * @Method Name: englishEdit
     * @Method 설명 : 영단어 수정폼
     * @author : javaguitar
     * @version : 0.1
     * @since : 1.0
     */
    @RequestMapping(value = {"/english/edit/{oneword}"}, method = RequestMethod.GET)
    public ModelAndView englishEdit(@PathVariable String oneword,
                                    @ModelAttribute("englishModel") EnglishModel englishModel, ModelMap model) throws Exception {
        ModelAndView mav = new ModelAndView();
        englishModel.setOneword(oneword);

        englishModel = ss.selectOne("net.javaguitar.mapper.EnglishMapper.selectEnglishEdit", englishModel);

        mav.addObject("englishModel", englishModel);
        mav.setViewName("content/english/edit");
        return mav;

    }

    /**
     * @param
     * @return
     * @throws Exception
     * @Method Name: englishUpdate
     * @Method 설명 : 영단어 수정
     * @author : javaguitar
     * @version : 0.1
     * @since : 1.0
     */
    @RequestMapping(value = {"/english/update"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String quizUpdate(@ModelAttribute("englishModel") EnglishModel englishModel, ModelMap model,
                             HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {

        ss.update("net.javaguitar.mapper.EnglishMapper.updateEnglish", englishModel);

        return "redirect:/english/" + englishModel.getOneword();
    }

    /**
     * @param
     * @return
     * @throws Exception
     * @Method Name: english_hide
     * @Method 설명 : 암기가 되어 숨긴 영어 단어
     * @author : javaguitar
     * @version : 0.1
     * @since : 1.0
     */
    @GetMapping("/english/hide")
    @ResponseBody
    public void english_hide(@ModelAttribute("englishModel") EnglishModel englishModel, HttpServletResponse response,
                             @RequestParam String oneword) throws Exception {
        Gson gson = new Gson();
        Map<String, Object> data = new HashMap<String, Object>();

        /* 추후 json을 object로 전환하는 방식으로 변경 필요 (2022.02.22) */
        data.put("oneword", oneword);
        englishModel.setOneword(oneword);
        int retCnt = 0;
        retCnt = ss.update("net.javaguitar.mapper.EnglishMapper.updateViewYn", englishModel);

        if (retCnt > 0) {
            data.put("result", "success");
        } else {
            data.put("result", "fail");
        }

        response.getWriter().print(gson.toJson(data));

    }
}
