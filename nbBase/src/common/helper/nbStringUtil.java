package common.helper;

import java.security.MessageDigest;

import org.apache.tomcat.util.codec.binary.Base64;

public class nbStringUtil {

	private static final String KEY_MD5 = "MD5";
	
	/**
	 * MD5加密
	 *  
	 * @param data
	 * @return
	 * @throws Exception
	 */

	public static byte[] encryptMD5(byte[] data) throws Exception {
		
		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
		md5.update(data);
		
		return md5.digest();
		
	}
	
	/**
	 * MD5加密
	 *  
	 * @param data
	 * @return
	 * @throws Exception
	 */

	public static String encryptMD5(String text) throws Exception {
		
		byte[] data = text.getBytes();
		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
		md5.update(data);
		byte[] cryptedData = md5.digest();
		String crypted = Base64.encodeBase64String(cryptedData);
		
		return crypted;
	}
	
	private static String bytes2Hex(byte[] bts) {  
        StringBuffer des = new StringBuffer();  
        String tmp = null;  
        for (int i = 0; i < bts.length; i++) {  
            tmp = (Integer.toHexString(bts[i] & 0xFF));  
            if (tmp.length() == 1) {  
                des.append("0");  
            }  
            des.append(tmp);  
        }  
        return des.toString();  
    }  
	
	/**
	* BASE64解密
	* 
	* @param key
	* @return
	* @throws Exception
	*/
	public static byte[] decryptBASE64(String key) throws Exception {
		return Base64.decodeBase64(key);
	}

	/**
	* BASE64 加密
	* 
	* @param key
	* @return
	* @throws Exception
	*/
	public static String encryptBASE64(byte[] key) throws Exception {
		return Base64.encodeBase64String(key);
	}
	
}
