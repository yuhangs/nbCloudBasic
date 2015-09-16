package service.basicFunctions;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import common.helper.nbReturn;
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
//	@Autowired
//	private UserExtraAttConfigDao userExtraAttConfigDao;
	

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
			nbRet.setError(nbReturn.ReturnCode.MISSING_APPID);
			return nbRet;
		}
		if( clientUuid == null ) clientUuid = "none";
		
		
		NbUser nbUser = userInfoDao.verifyUser(username, nbStringUtil.encryptMD5(password), appID);
		NbTokenPublisher nbTokenPublish;
		
		if( nbUser == null ){ //用户认证失败
			nbRet.setError(nbReturn.ReturnCode.USERNAME_PASSWORD_ERROR);
		}else{ //用户认证成功
			
			nbRet.setObject(nbUser);
			
			if( needToken ){//需要返回token
				
				nbReturn nbTmpRet = tokenPublishDao.createNewToken(appID, nbUser, clientUuid, lifecycleSec);
				
				if( nbTmpRet.isSuccess() ){//创建成功
					
					nbTokenPublish = (NbTokenPublisher)nbTmpRet.getObject();
					nbRet.setObject(nbTokenPublish);
					
				}
				else{//创建失败了
					nbRet.setError(nbReturn.ReturnCode.CREATE_TOKEN_ERROR);
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
			nbRet.setError(nbReturn.ReturnCode.TOKEN_NOT_EXIST);
			
		}else{
			
			Date toBeExpired = nbTokenPublish.getTokenFreshed();
			Date currentDate = Calendar.getInstance().getTime();
			Long milSec = currentDate.getTime() - toBeExpired.getTime();
			
			if( milSec > (nbTokenPublish.getTokenLifecycleSec()*1000) ){ // expired
				nbRet.setError(nbReturn.ReturnCode.TOKEN_EXPIRED);
				
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
			nbRet.setError(nbReturn.ReturnCode.USERNAME_ALREADY_EXIST);
			return nbRet;
		}
		//用户名可以用，开始注册用户
		NbUser nbUser = new NbUser();
		nbUser.setEmail(email);
		nbUser.setMobilePhone(mobile);
		nbUser.setPassword(nbStringUtil.encryptMD5(password));
		nbUser.setUsername(username);
		nbUser.setApplicationId(appID);
		Calendar cal = Calendar.getInstance();
		nbUser.setUserOpenCode(nbStringUtil.encryptMD5(	appID
														+username
														+String.valueOf(cal.getTimeInMillis())
														));
		nbUser = userInfoDao.save(nbUser);
		nbReturn nbRet = new nbReturn();
		nbRet.setObject(nbUser);
		return nbRet;
	}

	@Override
	public nbReturn configUserExtraAttributes(	String appID,
												String attributeCode, 
												String attributeName,
												String attributeDescription,
												OperationFlags operationFlag) {
		nbReturn nbRet = new nbReturn();
		
		if( appID == null || attributeCode == null){
			nbRet.setError(nbReturn.ReturnCode.INSUFFICIENT_PARAMTERS);
			return nbRet;
		}
		
		
//		NbUserExtraAttributesConfig  nbUserExtraUserAttsConfig = userExtraAttConfigDao.findByAppIDAndAttributeCode(appID, attributeCode);
//		
//		//添加
//		if( operationFlag.equals(OperationFlags.USER_EXTRA_ATTRIBUTE_CONFIG_ADD ) ){
//			
//			if( nbUserExtraUserAttsConfig != null ){//数据库里找到了
//			//如果是ADD操作，但是CODE已经存在了
//			nbRet.setError(nbReturn.ReturnCode.ATTIBUTE_CODE_ALREADY_EXIST);
//			}
//			else{
//				//添加一个attribute
//				nbUserExtraUserAttsConfig = new NbUserExtraAttributesConfig();
//				nbUserExtraUserAttsConfig.setApplicationId(appID);
//				nbUserExtraUserAttsConfig.setAttributeCode(attributeCode);
//				nbUserExtraUserAttsConfig.setAttributeDes(attributeDescription);
//				nbUserExtraUserAttsConfig.setAttributeName(attributeName);
//				nbUserExtraUserAttsConfig = userExtraAttConfigDao.save(nbUserExtraUserAttsConfig);
//				nbRet.setObject(nbUserExtraUserAttsConfig);
//			}
//		}
//		
//		//删除
//		if( operationFlag.equals(OperationFlags.USER_EXTRA_ATTRIBUTE_CONFIG_REMOVE ) ){
//			
//			if( nbUserExtraUserAttsConfig == null ){//数据库里没找到了
//				//如果是remove 或者 update操作，但是code却没有找到
//				nbRet.setError(nbReturn.ReturnCode.ATTIBUTE_CODE_NOT_EXIST);
//			}
//			else{
//				//删除一个attribute
//				userExtraAttConfigDao.delete(nbUserExtraUserAttsConfig);
//				nbRet.setObject(null);
//			}
//		}
//		
//		//更新
//		if( operationFlag.equals(OperationFlags.USER_EXTRA_ATTRIBUTE_CONFIG_UPDATE ) ){
//			
//			if( nbUserExtraUserAttsConfig == null ){//数据库里没找到了
//				//如果是remove 或者 update操作，但是code却没有找到
//				nbRet.setError(nbReturn.ReturnCode.ATTIBUTE_CODE_NOT_EXIST);
//			}
//			else{
//				//更新一个attribute
//				nbUserExtraUserAttsConfig.setApplicationId(appID);
//				nbUserExtraUserAttsConfig.setAttributeCode(attributeCode);
//				nbUserExtraUserAttsConfig.setAttributeDes(attributeDescription);
//				nbUserExtraUserAttsConfig.setAttributeName(attributeName);
//				nbUserExtraUserAttsConfig = userExtraAttConfigDao.update(nbUserExtraUserAttsConfig);
//				nbRet.setObject(nbUserExtraUserAttsConfig);
//			}
//		}
		
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
