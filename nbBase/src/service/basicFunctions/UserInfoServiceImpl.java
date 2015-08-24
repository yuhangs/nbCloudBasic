package service.basicFunctions;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import common.helper.nbReturn;
import common.helper.nbRetEnum;
import common.helper.nbStringUtil;
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

	/**
	 * 用于验证用户名及密码
	 * 
	 * @param username 用户名
	 * 		  password 密码
	 *        appID    商家ID
	 *        clientUuid 终端的UUID，如果是web调用可填写IP地址，可为空，但是请谨慎使用
	 *        lifecycleSec token的生命周期，不可小于60秒，不可大于14400秒
	 *        needToken 是否需要生成token，如果是web页面生成可以不用生成token
	 * @return 返回nbReturn类型的返回值。object为NbTokenPublish（如果needToken为true）
	 * 
	 */
	@Override
	public nbReturn verifyUser(String username, 
			                   String password, 
			                   String appID, 
			                   String clientUuid, 
			                   Long lifecycleSec, 
			                   Boolean needToken) throws Exception {
		
		nbReturn nbRet = new nbReturn();
		if( lifecycleSec == null ) lifecycleSec = 7200l;
		if( lifecycleSec < 60l) lifecycleSec = 60l;
		if( lifecycleSec > 14400l ) lifecycleSec = 14400l;
		if( appID == null ) {
			nbRet.setRetCode(nbRetEnum.ReturnCode.MISSING_APPID);
			nbRet.setRetString(nbRetEnum.ReturnString.MISSING_APPID);
			return nbRet;
		}
		if( clientUuid == null ) clientUuid = "none";
		
		
		NbUser nbUser = userInfoDao.verifyUser(username, nbStringUtil.encryptMD5(password), appID);
		NbTokenPublisher nbTokenPublish;
		
		if( nbUser == null ){ //用户认证失败
			
			nbRet.setRetCode(nbRetEnum.ReturnCode.USERNAME_PASSWORD_ERROR);
			nbRet.setRetString(nbRetEnum.ReturnString.USERNAME_PASSWORD_ERROR);
			
		}else{ //用户认证成功
			
			nbRet.setObject(nbUser);
			
			if( needToken ){//需要返回token
				
				nbReturn nbTmpRet = tokenPublishDao.createNewToken(appID, nbUser, clientUuid, lifecycleSec);
				
				if( nbTmpRet.getRetCode() == nbRetEnum.ReturnCode.SUCCESS ){//创建成功
					
					nbTokenPublish = (NbTokenPublisher)nbTmpRet.getObject();
					nbRet.setObject(nbTokenPublish);
					
				}
				else{//创建失败了
					
					nbRet.setRetCode(nbRetEnum.ReturnCode.CREATE_TOKEN_ERROR);
					nbRet.setRetString(nbRetEnum.ReturnString.CREATE_TOKEN_ERROR);
					
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
			nbRet.setRetCode(nbRetEnum.ReturnCode.TOKEN_NOT_EXIST);
			nbRet.setRetString(nbRetEnum.ReturnString.TOKEN_NOT_EXIST);
			
		}else{
			
			Date toBeExpired = nbTokenPublish.getTokenFreshed();
			Date currentDate = Calendar.getInstance().getTime();
			Long milSec = currentDate.getTime() - toBeExpired.getTime();
			
			if( milSec > (nbTokenPublish.getTokenLifecycleSec()*1000) ){ // expired
				
				nbRet.setRetCode(nbRetEnum.ReturnCode.TOKEN_EXPIRED);
				nbRet.setRetString(nbRetEnum.ReturnString.TOKEN_EXPIRED);
				
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

	@Override
	public nbReturn RegisterUser(String username, String password,
			String mobile, String email, String appID) throws Exception {
		
		NbUser checkUser = userInfoDao.findByUsernameAndAppid(username, appID);
		if( checkUser != null ){ //用户名已经存在
			nbReturn nbRet = new nbReturn();
			nbRet.setRetCode(nbRetEnum.ReturnCode.USERNAME_ALREADY_EXIST);
			nbRet.setRetString(nbRetEnum.ReturnString.USERNAME_ALREADY_EXIST);
			return nbRet;
		}
		//用户名可以用，开始注册用户
		NbUser nbUser = new NbUser();
		nbUser.setEmail(email);
		nbUser.setMobilePhone(mobile);
		nbUser.setPassword(nbStringUtil.encryptMD5(password));
		nbUser.setUsername(username);
		nbUser.setApplicationId(appID);
		nbUser = userInfoDao.save(nbUser);
		
		nbReturn nbRet = new nbReturn();
		nbRet.setObject(nbUser);
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
