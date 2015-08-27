package database.models;

import java.io.Serializable;

import javax.persistence.*;

import database.common.nbBaseModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The persistent class for the nb_user_extra_attributes_config database table.
 * 
 */
@Entity
@Table(name="nb_user_extra_attributes_config")
@NamedQuery(name="NbUserExtraAttributesConfig.findAll", query="SELECT n FROM NbUserExtraAttributesConfig n")
public class NbUserExtraAttributesConfig implements Serializable, nbBaseModel  {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Long id;

	@Column(nullable=false, length=64)
	private String applicationId;

	@Column(nullable=false, length=16)
	private String attributeCode;

	@Lob
	@Column(nullable=false)
	private String attributeDes;

	@Column(nullable=false, length=32)
	private String attributeName;

	//bi-directional many-to-one association to NbUserExtraAttibute
	@OneToMany(mappedBy="nbUserExtraAttributesConfig")
	private List<NbUserExtraAttibute> nbUserExtraAttibutes;

	public NbUserExtraAttributesConfig() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApplicationId() {
		return this.applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getAttributeCode() {
		return this.attributeCode;
	}

	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}

	public String getAttributeDes() {
		return this.attributeDes;
	}

	public void setAttributeDes(String attributeDes) {
		this.attributeDes = attributeDes;
	}

	public String getAttributeName() {
		return this.attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public List<NbUserExtraAttibute> getNbUserExtraAttibutes() {
		return this.nbUserExtraAttibutes;
	}

	public void setNbUserExtraAttibutes(List<NbUserExtraAttibute> nbUserExtraAttibutes) {
		this.nbUserExtraAttibutes = nbUserExtraAttibutes;
	}

	public NbUserExtraAttibute addNbUserExtraAttibute(NbUserExtraAttibute nbUserExtraAttibute) {
		getNbUserExtraAttibutes().add(nbUserExtraAttibute);
		nbUserExtraAttibute.setNbUserExtraAttributesConfig(this);

		return nbUserExtraAttibute;
	}

	public NbUserExtraAttibute removeNbUserExtraAttibute(NbUserExtraAttibute nbUserExtraAttibute) {
		getNbUserExtraAttibutes().remove(nbUserExtraAttibute);
		nbUserExtraAttibute.setNbUserExtraAttributesConfig(null);

		return nbUserExtraAttibute;
	}

	@Override
	public Map<String, Object> modelToMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", id);
		data.put("attributeCode", attributeCode);
		data.put("attributeDes", attributeDes);
		data.put("attributeName", attributeName);
		return data;
	}

}