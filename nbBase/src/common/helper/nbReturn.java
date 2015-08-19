package common.helper;

public class nbReturn {
	
	private String 	retString;
	private Long 	retCode;
	private Object  object;
	
	public nbReturn(){
		retString = nbReturnMessageEnum.ReturnString.SUCCESS;
		retCode = nbReturnMessageEnum.ReturnCode.SUCCESS;
		object = null;
	}
	
	public String getRetString() {
		return retString;
	}
	
	public void setRetString(String retString) {
		this.retString = retString;
	}
	
	public Long getRetCode() {
		return retCode;
	}
	
	public void setRetCode(Long retCode) {
		this.retCode = retCode;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
