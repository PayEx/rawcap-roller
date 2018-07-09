package com.ivanskodje.rawcaproller.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class TextValidator implements IParameterValidator {

	public void validate(String name, String value) throws ParameterException {
		if (value == null || value.length() == 0) {
			throw new ParameterException("Parameter " + name + " cannot be empty. ");
		}
	}
}
