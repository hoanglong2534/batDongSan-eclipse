package com.javaweb.repository.entity;

public class BuildingRenttypeEntity {
	private Long id;
	private Long buildingID;
	private Integer renttypeID;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getBuildingID() {
		return buildingID;
	}
	public void setBuildingID(Long buildingID) {
		this.buildingID = buildingID;
	}
	public Integer getRenttypeID() {
		return renttypeID;
	}
	public void setRenttypeID(Integer renttypeID) {
		this.renttypeID = renttypeID;
	}
	
	
}
