package main.entry.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import common.helper.HttpWebIOHelper;

@Controller
public class APIMain {

	private HttpWebIOHelper httpWebIOHelper = new HttpWebIOHelper();
    
    @RequestMapping(value = "/nbapi/apisampleJSON") 
    public void indexJSON(HttpServletResponse response,HttpServletRequest request) throws IOException{
    	
		Map<String, Object> jsonMap = httpWebIOHelper.servletInputStream2JsonMap(request);
		if( jsonMap != null){
			String password = (String) jsonMap.get("password");
			System.out.println(password);
		}
		
        //创建模型跟视图，用于渲染页面。并且指定要返回的页面为home页面  
    	Map<String, Object> theData = new HashMap<String, Object>();
    	List<String> subData = new ArrayList<String>();
    	subData.add("data1");
    	subData.add("data2");
    	theData.put("name", "helloWord");
    	theData.put("value", "helloValue");
    	theData.put("subData", subData);
    	httpWebIOHelper.printWebJson(theData, response);
    }

}
