package common.helper;

import org.hibernate.EmptyInterceptor;


public class nbTransactionInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = -8350144437912899276L;
	
	private String currentAppID;
	
	public nbTransactionInterceptor(){};
	
	private static String[] tablesByAppId = {
			" nb_user ",
	};
	

//	public List<AlternateTables> getOriginalAndNewTableNameList() {
//		return originalAndNewTableNameList;
//	}
//
//
//	public void setOriginalAndNewTableNameList(
//			List<AlternateTables> originalAndNewTableNameList) {
//		this.originalAndNewTableNameList = originalAndNewTableNameList;
//	}

	private String alternateTheTables(String sql){
		
		if( currentAppID != null){
			for(int i = 0 ; i < tablesByAppId.length ; i++){
				String target = tablesByAppId[i];
				String news = " "+tablesByAppId[i].trim()+"_"+currentAppID+" ";
				sql = sql.replace(target, news);
			}
		}
		
		return sql;
	}

	@Override 
	public String onPrepareStatement(String sql) { 
		System.out.println("sql(BE):"+sql); 
		sql = alternateTheTables(sql);
		System.out.println("sql(AF):"+sql); 
		currentAppID = null;
		return sql; 
	}

	public String getCurrentAppID() {
		return currentAppID;
	}

	public void setCurrentAppID(String currentAppID) {
		this.currentAppID = currentAppID;
	}
	

}
