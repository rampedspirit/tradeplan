package com.bhs.gtk.condition.util;

import org.springframework.stereotype.Component;

@Component
public class UpdateHelper {
	
//	public ConditionEntity getUpdatedConditionEntity(PatchModel patchModel, ConditionEntity conditionTobeUpdated) {
//		// input validation.
//		ConditionEntity updatedConditionEntity = conditionTobeUpdated;
//		for (PatchData patchData : patchModel.getPatchData()) {
//			updatedConditionEntity = getUpdatedFilterEntity(patchData, updatedConditionEntity);
//		}		
//		return updatedConditionEntity;
//	}
//
//	private ConditionEntity getUpdatedFilterEntity(PatchData patchData, ConditionEntity conditionEntity) {
//		// input validation.
//		ConditionEntity updatedConditionEntity = conditionEntity;
//		if (!StringUtils.equals(patchData.getOperation().name(), OperationEnum.REPLACE.name())) {
//			// throw unsupported operation exception.
//			return null;
//		}
//		switch (patchData.getProperty()) {
//		case NAME:
//			updatedConditionEntity.setName(patchData.getValue());
//			break;
//		case DESCRIPTION:
//			updatedConditionEntity.setDescription(patchData.getValue());
//			break;
//		case CODE:
//			updatedConditionEntity.setCode(patchData.getValue());
//			break;
//		case PARSE_TREE:
//			updatedConditionEntity.setParseTree(patchData.getValue());
//			break;
//		default:
//			// throw unsupported property.
//		}
//		return updatedConditionEntity;
//	}

}
