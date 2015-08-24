package service.basicFunctions;

import common.helper.nbReturn;
import database.models.NbUser;


public interface UserInfoService {//extends ScheduledTaskInterface{

	public NbUser getFirstUserInfo();
	public nbReturn checkToken(String tokenString, String appID, Boolean ifRefresh);
	public nbReturn verifyUser(String username, String password, String appID, String clinetUuid, Long lifecycleSec, Boolean needToken) throws Exception;
	public nbReturn RegisterUser(String username, String password, String mobile, String email, String AppID)  throws Exception;
	
}
