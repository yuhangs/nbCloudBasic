package database.basicFunctions.dao;

import common.helper.nbReturn;
import database.common.BaseDao;
import database.models.NbApplications;

public interface ApplicationsDao extends BaseDao<NbApplications>{

	public nbReturn generateSignature(String appID, String timeStamp) throws Exception;
}
