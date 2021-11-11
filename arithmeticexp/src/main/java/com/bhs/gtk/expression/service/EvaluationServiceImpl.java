package com.bhs.gtk.expression.service;

import java.util.Optional;
import java.util.zip.CRC32C;

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
	
	@Override
	public EvaluationResponse evaluate(String evalRequest) {
		
        //verify whether evalRequest required
		// if not send available result.
		//if yes:
		   // convert request to get evaluable
		   // evaluate evaluable
		  // save result in DB
		  // send evaluated result.
		long checksum = getChecksum(evalRequest);
		Optional<ExpressionResultEntity> expressionResultContainer = expressionResultRepository.findById(checksum);
		if(expressionResultContainer.isPresent()) {
			ExpressionResultEntity expressionResult = expressionResultContainer.get();
			return getEvaluationResponse(expressionResult);
		}
		
		Expression expression = evaluationRequestConverter.convert(evalRequest);
		//Evaluation logic yet to be written.
		ExpressionResultEntity resultEntity =  getExpressionResultEntity(checksum,evalRequest,100.2,"COMPLETED");
		
		ExpressionResultEntity expressionResult = expressionResultRepository.save(resultEntity);
		
		return getEvaluationResponse(expressionResult);
	}

	private ExpressionResultEntity getExpressionResultEntity(long checksum, String evalRequest, double result, String status) {
		ExpressionResultEntity resultEntity = new ExpressionResultEntity(checksum, evalRequest, result, status);
		return resultEntity;
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
