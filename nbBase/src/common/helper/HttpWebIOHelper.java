package common.helper;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

public class HttpWebIOHelper {
	
	private CommonHelper commonHelper = new CommonHelper();

	public void printWebJson(Object theData, HttpServletResponse response) throws IOException{
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(commonHelper.getStringOfObj(theData));
		out.flush();
		out.close();
	}
	
	public String servletInputStream2String(HttpServletRequest request) throws IOException{
		request.setCharacterEncoding("utf-8");
		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		while( (line = reader.readLine()) != null){
			stringBuilder.append(line);
		}
		return stringBuilder.toString();
	}
	
	public Map<String, Object> servletInputStream2JsonMap(HttpServletRequest request) throws IOException{
		String jsonString = servletInputStream2String(request);
		return (Map<String, Object>)JSONObject.parseObject(jsonString);
	}
}
