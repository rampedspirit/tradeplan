package com.bhs.gtk.expression.persistence;

import java.io.Serializable;
import java.util.Date;

public class ResultId implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private ExpressionEntity expression;
	private Date evalTime;
	private String scrip;
	
	public ResultId(ExpressionEntity expression,Date evalTime, String scrip) {
		this.expression = expression;
		this.evalTime = evalTime;
		this.scrip = scrip;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((evalTime == null) ? 0 : evalTime.hashCode());
		result = prime * result + ((expression == null) ? 0 : expression.hashCode());
		result = prime * result + ((scrip == null) ? 0 : scrip.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResultId other = (ResultId) obj;
		if (evalTime == null) {
			if (other.evalTime != null)
				return false;
		} else if (!evalTime.equals(other.evalTime))
			return false;
		if (expression == null) {
			if (other.expression != null)
				return false;
		} else if (!expression.equals(other.expression))
			return false;
		if (scrip == null) {
			if (other.scrip != null)
				return false;
		} else if (!scrip.equals(other.scrip))
			return false;
		return true;
	}

	
}
