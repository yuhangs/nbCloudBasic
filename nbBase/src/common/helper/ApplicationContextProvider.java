package common.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextProvider implements ApplicationContextAware {

	private static ApplicationContext ctx;
	
	@Override
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		this.setAppContext(ctx);
	}
	
	public void setAppContext(ApplicationContext ac){
		ctx = ac;
	}
	
	public static ApplicationContext getApplicationContext() {
		return ctx;
	}

}

