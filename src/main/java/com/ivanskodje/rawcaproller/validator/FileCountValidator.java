package com.ivanskodje.rawcaproller.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.io.File;

public class FileCountValidator implements IParameterValidator {
	public void validate(String name, String value) throws ParameterException {
		int n = Integer.parseInt(value);
		if (n < 1) {
			throw new ParameterException("Parameter " + name + " must be an integer value of 1 or greater. Found '" + value + "'");
		}
	}
}
