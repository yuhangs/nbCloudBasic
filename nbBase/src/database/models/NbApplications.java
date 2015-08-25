package database.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

import database.common.nbBaseModel;


/**
 * The persistent class for the nb_applications database table.
 * 
 */
@Entity
@Table(name="nb_applications")
@NamedQuery(name="NbApplication.findAll", query="SELECT n FROM NbApplications n")
public class NbApplications implements Serializable, nbBaseModel {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false, length=64)
	private String applicationID;

	@Lob
	@Column(nullable=false)
	private String applicationDes;

	@Column(nullable=false, length=64)
	private String secretKey;

	public NbApplications() {
	}

	public String getApplicationID() {
		return this.applicationID;
	}

	public void setApplicationID(String applicationID) {
		this.applicationID = applicationID;
	}

	public String getApplicationDes() {
		return this.applicationDes;
	}

	public void setApplicationDes(String applicationDes) {
		this.applicationDes = applicationDes;
	}

	public String getSecretKey() {
		return this.secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	@Override
	public Map<String, Object> modelToMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("applicationID", applicationID);
		data.put("secretKey", secretKey);
		data.put("applicationDes", applicationDes);
		return data;
	}

}