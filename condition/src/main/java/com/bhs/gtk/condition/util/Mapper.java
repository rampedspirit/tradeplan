package com.bhs.gtk.condition.util;

import org.springframework.stereotype.Component;

import com.bhs.gtk.condition.model.Condition;
import com.bhs.gtk.condition.persistence.ConditionEntity;

@Component
public class Mapper {

	public ConditionEntity getConditionEntityToPersists(Condition condition) {
		ConditionEntity conditionEntity = new ConditionEntity(condition.getName(), condition.getDescription(),
				condition.getCode(), condition.getParseTree());
		return conditionEntity;
	}
	
	public Condition getCondition(ConditionEntity conditionEntity) {
		Condition condition = new Condition();
		if(conditionEntity == null || conditionEntity.getId() == null) {
			//throw exception
		}
		condition.setId(conditionEntity.getId());
		condition.setName(conditionEntity.getName());
		condition.setDescription(conditionEntity.getDescription());
		condition.setCode(conditionEntity.getCode());
		condition.setParseTree(conditionEntity.getParseTree());
		return condition;
	}
	
}
