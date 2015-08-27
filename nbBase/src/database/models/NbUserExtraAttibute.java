package database.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

import database.common.nbBaseModel;


/**
 * The persistent class for the nb_user_extra_attibutes database table.
 * 
 */
@Entity
@Table(name="nb_user_extra_attibutes")
@NamedQuery(name="NbUserExtraAttibute.findAll", query="SELECT n FROM NbUserExtraAttibute n")
public class NbUserExtraAttibute implements Serializable, nbBaseModel  {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Long id;

	@Lob
	private String attributeValue;

	//bi-directional many-to-one association to NbUserExtraAttributesConfig
	@ManyToOne
	@JoinColumn(name="extraConfigId", nullable=false)
	private NbUserExtraAttributesConfig nbUserExtraAttributesConfig;

	//bi-directional many-to-one association to NbUser
	@ManyToOne
	@JoinColumn(name="userId", referencedColumnName="applicationID", nullable=false)
	private NbUser nbUser;

	public NbUserExtraAttibute() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAttributeValue() {
		return this.attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public NbUserExtraAttributesConfig getNbUserExtraAttributesConfig() {
		return this.nbUserExtraAttributesConfig;
	}

	public void setNbUserExtraAttributesConfig(NbUserExtraAttributesConfig nbUserExtraAttributesConfig) {
		this.nbUserExtraAttributesConfig = nbUserExtraAttributesConfig;
	}

	public NbUser getNbUser() {
		return this.nbUser;
	}

	public void setNbUser(NbUser nbUser) {
		this.nbUser = nbUser;
	}

	@Override
	public Map<String, Object> modelToMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", id);
		data.put("attributeValue", attributeValue);
		data.put("extraConfigId", nbUserExtraAttributesConfig.modelToMap());
		data.put("nbUser", nbUser.modelToMap());
		return data;
	}

}