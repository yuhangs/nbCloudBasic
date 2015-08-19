package service.basicFunctions;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import common.helper.nbReturn;
import common.helper.nbReturnMessageEnum;
import service.common.ScheduledService;
import database.basicFunctions.dao.TokenPublishDao;
import database.basicFunctions.dao.UserInfoDao;
import database.models.NbTokenPublisher;
import database.models.NbUser;

@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService{

	@Autowired
	private UserInfoDao userInfoDao;
	@Autowired
	private ScheduledService scheduledService;
	@Autowired
	private TokenPublishDao tokenPublishDao;
	
	@Override
	public NbUser getFirstUserInfo() {
		
		NbUser userInfo = userInfoDao.getFirstUser();
		
		return userInfo;
	}

	@Override
	public nbReturn verifyUser(String username, 
			                   String password, 
			                   String appID, 
			                   String clientUuid, 
			                   Long lifecycleSec, 
			                   Boolean needToken) throws Exception {
		
		nbReturn nbRet = new nbReturn();
		
		NbUser nbUser = userInfoDao.verifyUser(username, password);
		NbTokenPublisher nbTokenPublish;
		
		if( nbUser == null ){ //用户认证失败
			
			nbRet.setRetCode(nbReturnMessageEnum.ReturnCode.USERNAME_PASSWORD_ERROR);
			nbRet.setRetString(nbReturnMessageEnum.ReturnString.USERNAME_PASSWORD_ERROR);
			
		}else{ //用户认证成功
			
			nbRet.setObject(nbUser);
			
			if( needToken ){//需要返回token
				
				nbReturn nbTmpRet = tokenPublishDao.createNewToken(appID, nbUser, clientUuid, lifecycleSec);
				
				if( nbTmpRet.getRetCode() == nbReturnMessageEnum.ReturnCode.SUCCESS ){//创建成功
					
					nbTokenPublish = (NbTokenPublisher)nbTmpRet.getObject();
					nbRet.setObject(nbTokenPublish);
					
				}
				else{//创建失败了
					
					nbRet.setRetCode(nbReturnMessageEnum.ReturnCode.CREATE_TOKEN_ERROR);
					nbRet.setRetString(nbReturnMessageEnum.ReturnString.CREATE_TOKEN_ERROR);
					
				}
			}

		}
		
		return nbRet;
	}

	@Override
	public nbReturn checkToken(String tokenString, String appID, Boolean ifRefresh) {
		
		nbReturn nbRet = new nbReturn();
		
		NbTokenPublisher nbTokenPublish =  tokenPublishDao.checkToken(tokenString, appID);
		
		//错误处理开始
		if( nbTokenPublish == null ){//没有这个token
			nbRet.setRetCode(nbReturnMessageEnum.ReturnCode.TOKEN_NOT_EXIST);
			nbRet.setRetString(nbReturnMessageEnum.ReturnString.TOKEN_NOT_EXIST);
			
		}else{
			
			Date toBeExpired = nbTokenPublish.getTokenFreshed();
			Date currentDate = Calendar.getInstance().getTime();
			Long milSec = currentDate.getTime() - toBeExpired.getTime();
			
			if( milSec > nbTokenPublish.getTokenLifecycleSec() ){ // expired
				
				nbRet.setRetCode(nbReturnMessageEnum.ReturnCode.TOKEN_EXPIRED);
				nbRet.setRetString(nbReturnMessageEnum.ReturnString.TOKEN_EXPIRED);
				
			}else{ // 有这个token，也没有过期
				
				if( ifRefresh ){ //需要刷新一下
					
					nbTokenPublish.setTokenFreshed(currentDate);
					nbTokenPublish = tokenPublishDao.update(nbTokenPublish);
				}
				
			}
			
			nbRet.setObject(nbTokenPublish);
		}

		return nbRet;
	}


//	@Override
//	public boolean execute() {
//		System.out.println("userinfo is doing his scheduled job");
//		return true;
//	}
//
//	@Override
//	public boolean registerTask() {
//		scheduledService.putNewScheduleTask(this, ScheduledTaskTime.RUN_0001_SEC);
//		return true;
//	}
//
//	@Override
//	public boolean unregisterTask() {
//		scheduledService.removeScheduleTask(this);
//		return true;
//	}
	
	

}
