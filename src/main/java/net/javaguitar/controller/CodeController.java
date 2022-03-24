package net.javaguitar.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import net.javaguitar.model.CodeModel;
import net.javaguitar.util.MapUtil;

@Controller
public class CodeController {

	@Autowired
	SqlSession ss;

	@ResponseBody
	@RequestMapping(value = "/code/codeSelect", method = RequestMethod.POST)
	public String upper_code(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String upper_code_id = request.getParameter("upper_code_id");
		Gson gson = new Gson();

		List<CodeModel> upperList = ss.selectList("net.javaguitar.mapper.CodeMapper.selectCodeManage",
				Integer.parseInt(upper_code_id));
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("upperList", upperList);
		String jsonString = gson.toJson(map);

		return jsonString;
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
	@ResponseBody
	@RequestMapping(value = { "/code/update" }, method = {
			RequestMethod.POST }, produces = "application/json; charset=utf8")
	public void codeUpdate(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestBody HashMap<String, Object> paramMap) throws Exception {
		Gson gson = new Gson();
		Map<String, Object> data = new HashMap<String, Object>();
		CodeModel codeModel = new CodeModel();
		JSONObject wrapParamJson = new JSONObject(paramMap);
		JSONArray paramJsonArr = (JSONArray) wrapParamJson.get("changedData");
		String col_status = null;
		for (Object obj : paramJsonArr) {
			JSONObject paramJson = (JSONObject) obj;
			MapUtil.convertMapToXssStringVO(paramJson.toMap(), codeModel);

			col_status = paramJson.get("col_status").toString();

			switch (col_status) {
			case "I":
				System.out.println("insert");
				codeModel.setCode_id(ss.selectOne("net.javaguitar.mapper.CodeMapper.selectMaxCode_id"));
				ss.insert("net.javaguitar.mapper.CodeMapper.insertCode", codeModel);
				break;
			case "U":

				System.out.println("update");
				ss.insert("net.javaguitar.mapper.CodeMapper.updateCode", codeModel);
				break;
			default:
				System.out.println("delete");
				break;
			}

		}
		data.put("result", "success");
		response.getWriter().print(gson.toJson(data));
	}

}
