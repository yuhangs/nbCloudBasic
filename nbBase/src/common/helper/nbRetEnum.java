package common.helper;

public class nbRetEnum {
	public static class ReturnCode{
		
		public static final Long SUCCESS = 0x0001l;
		public static final Long TOKEN_EXPIRED = 0x0002l;
		public static final Long TOKEN_NOT_EXIST = 0x0003l;
		public static final Long USERNAME_PASSWORD_ERROR = 0x0004l;
		public static final Long CREATE_TOKEN_ERROR = 0x0005l;
		public static final Long MISSING_APPID = 0x0006l;
		public static final Long USERNAME_ALREADY_EXIST = 0x0007l;
		
	}
	
	public static class ReturnString{
		
		public static final String SUCCESS = "Success";
		public static final String TOKEN_EXPIRED = "Token has expired!";
		public static final String TOKEN_NOT_EXIST = "Token or appID does not exist!";
		public static final String USERNAME_PASSWORD_ERROR = "Username or Password Error!";
		public static final String CREATE_TOKEN_ERROR = "Create token error!";
		public static final String MISSING_APPID = "APP ID is missing!";
		public static final String USERNAME_ALREADY_EXIST = "Username already exist!";
		
	}
}
