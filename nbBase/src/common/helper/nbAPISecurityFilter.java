package common.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import service.basicFunctions.UserInfoService;

public class nbAPISecurityFilter implements Filter{

	private List<String> excludedPages = new ArrayList<String>();
	
	WebApplicationContext springContext;
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		Boolean isExcludedPath = false;
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		Map<String, String[]> getParameter = request.getParameterMap();
		
		
		String tokenKey = null;
		String appID = null;
		if( getParameter.get("token") != null )
			tokenKey = getParameter.get("token")[0];
		if( getParameter.get("appID") != null )
			appID = getParameter.get("appID")[0];
		
		
		String servletPath = httpServletRequest.getServletPath();
		
		for( String excludePath : excludedPages){
			
			//设计上应该是/openAPI/路径下的所有的api都是不需要登录访问的，详细的参看web.xml中的配置
			if( servletPath.startsWith(excludePath) ){
				isExcludedPath = true;
			}
		}
		
		System.out.println("nbAPISecurityFilter: "+servletPath+" isExcludedPath:"+isExcludedPath);
		
		//需要拦截的
		if( !isExcludedPath ){
			nbReturn nbRet = new nbReturn();
			
			if( tokenKey == null || appID == null ){
				//需要验证token, tokenKey appID都不为空
				nbRet.setError(nbReturn.ReturnCode.NEED_TOKEN_APPID_FOR_AUTH);
				HttpWebIOHelper.printReturnJson(nbRet, (HttpServletResponse) response);
				return;
			}
			
			//开始验证token
			UserInfoService userInfoService  = 
					(UserInfoService)ApplicationContextProvider.getBeanByName("userInfoService");
			nbRet = userInfoService.checkToken(tokenKey, appID, true);
			if( !nbRet.isSuccess() ){
				//验证token失败
				HttpWebIOHelper.printReturnJson(nbRet, (HttpServletResponse) response);
				return;
			}
	
		}
		//验证token成功
		chain.doFilter(request, response);
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String excludedPage = filterConfig.getInitParameter("excludedPages");
		String[] excludedPageArray = excludedPage.split(";");
		excludedPages.clear();
		for(int i = 0 ; i < excludedPageArray.length ; i++){
			excludedPages.add(excludedPageArray[i].trim());
		}
		springContext = 
		        WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
	}
}
