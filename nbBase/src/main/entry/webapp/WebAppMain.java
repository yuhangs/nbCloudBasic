package main.entry.webapp;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import common.helper.nbStringUtil;
import service.basicFunctions.UserInfoService;
import database.models.NbUser;



@Controller
public class WebAppMain {

	 @Autowired  
	 private UserInfoService userInfoService;
	 
	@RequestMapping(value = "/index") 
    public ModelAndView index(){  
        //创建模型跟视图，用于渲染页面。并且指定要返回的页面为home页面  
    	Map<String,Object> data = new HashMap<String,Object>();  
        data.put("helloWorld","helloWorld");
        
        NbUser userInfo = userInfoService.getFirstUserInfo();
        data.put("userInfoMap", userInfo.modelToMap() );
        
        NbUser verifyResult = userInfoService.verifyUser("firstUser", "firstPassword");
        data.put("verifyRet", verifyResult.modelToMap() );

        ModelAndView mav = new ModelAndView("home",data);
        return mav;  
    }
	
	@RequestMapping(value = "/index2") 
    public ModelAndView index2() throws Exception{  
		String text = "a2345678901234567890123456789012a2345678901234567890123456789012a2345678901234567890123456789012";
		String ret = nbStringUtil.encryptMD5(text);
		Map<String,Object> data = new HashMap<String,Object>();  
        data.put("helloWorld", ret );
        ModelAndView mav = new ModelAndView("home",data);
        return mav;  
    }
	
}
