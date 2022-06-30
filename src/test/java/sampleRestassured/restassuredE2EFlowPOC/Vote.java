package sampleRestassured.restassuredE2EFlowPOC;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Vote {

	private String user_id;
	private String image_id;
	private String sub_id;
	private Date created_at;
	private int value;
	private int id;
	private String country_code;

	public Vote(int id, String image_id, String user_id, String sub_id, Date created_at, int value,
			String country_code) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.image_id = image_id;
		this.sub_id = sub_id;
		this.created_at = created_at;
		this.value = value;
		this.country_code = country_code;
	}

	public Vote() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getImage_id() {
		return image_id;
	}

	public void setImage_id(String image_id) {
		this.image_id = image_id;
	}

	public String getSub_id() {
		return sub_id;
	}

	public void setSub_id(String sub_id) {
		this.sub_id = sub_id;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getConutryCode() {
		return country_code;
	}

	public void setConutryCode(String country_code) {
		this.country_code = country_code;
	}
}