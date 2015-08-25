package common.helper;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class CommonHelper {

	/**
	 * obj里边包含jpa对象会报错，要过滤一次。
	 * 
	 * @return
	 */
	public static String getStringOfObj(Object obj) {
		//SimplePropertyFilter spf = new SimplePropertyFilter();
		//暂时不考虑持JPA对象报错的问题。以后可能需要加上
		
		SerializeWriter sw = new SerializeWriter();
		JSONSerializer serializer = new JSONSerializer(sw);
		//serializer.getPropertyFilters().add(spf);
		serializer.write(obj);
		return sw.toString();
	}
	
}
