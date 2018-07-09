package com.ivanskodje.rawcaproller.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class NumberValidator implements IParameterValidator {

	public void validate(String name, String value) throws ParameterException {
		int n = Integer.parseInt(value);
		if (n < 0) {
			throw new ParameterException("Parameter " + name + " must be a positive integer. Found '" + value + "'");
		}
	}
}
