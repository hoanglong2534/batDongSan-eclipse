package com.javaweb.repository.entity;

public class RentareaEntity {
	private Long id;
	private String value;
	private Long buildingID;
	private String createddate;
	private String modefieddate;
	private String createdby;
	private String modifiedby;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Long getBuildingID() {
		return buildingID;
	}
	public void setBuildingID(Long buildingID) {
		this.buildingID = buildingID;
	}
	public String getCreateddate() {
		return createddate;
	}
	public void setCreateddate(String createddate) {
		this.createddate = createddate;
	}
	public String getModefieddate() {
		return modefieddate;
	}
	public void setModefieddate(String modefieddate) {
		this.modefieddate = modefieddate;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public String getModifiedby() {
		return modifiedby;
	}
	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}


}
