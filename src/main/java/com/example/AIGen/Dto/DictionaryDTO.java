package com.example.AIGen.Dto;

import java.util.List;

public class DictionaryDTO {
	
    private List<TypeOfDTO> typeOf;
    private List<ActivityAreaDTO> activityArea;
    private List<ExpertiseAreaDTO> expertiseArea;
    private List<StateDTO> opportunity;
	public List<TypeOfDTO> getTypeOf() {
		return typeOf;
	}
	public void setTypeOf(List<TypeOfDTO> typeOf) {
		this.typeOf = typeOf;
	}
	public List<ActivityAreaDTO> getActivityArea() {
		return activityArea;
	}
	public void setActivityArea(List<ActivityAreaDTO> activityArea) {
		this.activityArea = activityArea;
	}
	public List<ExpertiseAreaDTO> getExpertiseArea() {
		return expertiseArea;
	}
	public void setExpertiseArea(List<ExpertiseAreaDTO> expertiseArea) {
		this.expertiseArea = expertiseArea;
	}
	public List<StateDTO> getOpportunity() {
		return opportunity;
	}
	public void setOpportunity(List<StateDTO> opportunity) {
		this.opportunity = opportunity;
	}
    
    
}
