package service.basicFunctions;

import common.helper.nbReturn;

public interface ApplicationsService {

	public nbReturn checkSignature(String appID, String timeStamp, String Signature) throws Exception;
}
