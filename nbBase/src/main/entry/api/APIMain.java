package main.entry.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import service.basicFunctions.UserInfoService;
import common.helper.HttpWebIOHelper;
import common.helper.nbReturn;


@Controller
public class APIMain {

	//private HttpWebIOHelper httpWebIOHelper = new HttpWebIOHelper();
	
	@Autowired  
	private UserInfoService userInfoService;
    
    @RequestMapping(value = "/nbapi/registerUser") 
    public void registerNewUser(HttpServletResponse response,HttpServletRequest request) throws Exception{
    	
		Map<String, Object> jsonMap = HttpWebIOHelper.servletInputStream2JsonMap(request);
		nbReturn nbRet = new nbReturn();
		
		if( jsonMap != null){
			String username = (String) jsonMap.get("username");
			String password = (String) jsonMap.get("password");
			String mobile = (String) jsonMap.get("mobilePhone");
			String email = (String) jsonMap.get("email");
			String appID = (String) jsonMap.get("appID");
			String appSignature = (String) jsonMap.get("appSignature");
			String applyDateTime = (String) jsonMap.get("applyDateTime");
			
			//TODO:这里需要通过applyDateTime, appSignature 以及 appID 验证APPID的有效性
			
			
			nbRet = userInfoService.RegisterUser(username, 
												 password, 
												 mobile,
												 email,
												 appID);
			
		}else{
			nbRet.setError(nbReturn.ReturnCode.PARAMETER_PHARSE_ERROR);
			
		}

		HttpWebIOHelper.printReturnJson(nbRet, response);
    }

}
