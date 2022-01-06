package com.bhs.gtk.expression.service;

import java.util.Optional;
import java.util.zip.CRC32C;

import org.apache.commons.lang3.RandomUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhs.gtk.expression.model.EvaluationResponse;
import com.bhs.gtk.expression.model.Expression;
import com.bhs.gtk.expression.persistence.ExpressionResultEntity;
import com.bhs.gtk.expression.persistence.ExpressionResultRepository;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
	private ExpressionResultRepository expressionResultRepository;
    
    @Autowired
    private EvaluationRequestConverter evaluationRequestConverter;
    
    @Autowired
    private ExpressionEvaluator expressionEvaluator;
	
	@Override
	public EvaluationResponse evaluate(String evalRequest) {
		long checksum = getChecksum(evalRequest);
		Optional<ExpressionResultEntity> expressionResultContainer = expressionResultRepository.findById(checksum);
		if (expressionResultContainer.isPresent()) {
			ExpressionResultEntity resultEntity = expressionResultContainer.get();
			return getEvaluationResponse(resultEntity);
		}
		//Below code need to be safe for parallel execution (thread-safe) 
		expressionResultRepository.save(new ExpressionResultEntity(checksum, evalRequest,null , "PROCESSING"));
		Expression expression = evaluationRequestConverter.convert(evalRequest);
		double result = expressionEvaluator.evaluate(expression);
		ExpressionResultEntity expressionResultEntity = expressionResultRepository
				.save(new ExpressionResultEntity(checksum, evalRequest, Double.valueOf(result), "COMPLETED"));
		return getEvaluationResponse(expressionResultEntity);
	}


	private EvaluationResponse getEvaluationResponse(ExpressionResultEntity expressionResult) {
		long checkSum = expressionResult.getChecksum();
		String expressionText = expressionResult.getExpression();
		double result = expressionResult.getResult();
		String status = expressionResult.getStatus();
		return new EvaluationResponse(checkSum, expressionText, result, status);
	}

	private long getChecksum(String evalRequest) {
		CRC32C crc = new CRC32C();
		byte[] bytes = evalRequest.getBytes();
		crc.update(bytes, 0, bytes.length);
		return crc.getValue();
	}
	
	

}
