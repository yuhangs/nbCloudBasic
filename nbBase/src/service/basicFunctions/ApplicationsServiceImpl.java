package service.basicFunctions;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import common.helper.nbReturn;
import common.helper.nbStringUtil;
import database.basicFunctions.dao.ApplicationsDao;

@Service("applicationsService")
public class ApplicationsServiceImpl implements ApplicationsService {
	
	@Autowired
	ApplicationsDao applicationsDao;

	@Override
	public nbReturn checkSignature(String appID, String timeStamp,
			String Signature) throws Exception {
		nbReturn nbRet = new nbReturn();
		
		//检查需要验证的签名是否5秒之内的新鲜有效的签名
		Date parameterDate = nbStringUtil.String2DateTime(timeStamp);
		Calendar cal = Calendar.getInstance();
		if( ( cal.getTime().getTime() - parameterDate.getTime() ) > 5000  ){
			// 如果传进来的timestamp是5秒之前的话，系统将视此次signature为无效的
			nbRet.setError(nbReturn.ReturnCode.SIGNATURE_WRONG);
			return nbRet;
		}
		
		nbRet = applicationsDao.generateSignature(appID, timeStamp);
		
		if( !nbRet.isSuccess() )
			return nbRet;
		
		String correctSignature = (String)nbRet.getObject();
		
		if( correctSignature.equals(Signature) ){
			nbRet.setObject(Boolean.valueOf(true));
		}else{
			nbRet.setError(nbReturn.ReturnCode.SIGNATURE_WRONG);
			nbRet.setObject(Boolean.valueOf(false));
		}
		
		return nbRet;
	}

}
