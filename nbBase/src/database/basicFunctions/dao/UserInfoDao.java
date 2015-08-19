package database.basicFunctions.dao;

import database.common.BaseDao;
import database.models.NbUser;

public interface UserInfoDao extends BaseDao<NbUser>{
	
	public NbUser getFirstUser();
	public NbUser verifyUser(String username, String password);
	
}