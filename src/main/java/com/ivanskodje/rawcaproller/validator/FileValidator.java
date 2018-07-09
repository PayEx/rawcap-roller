package com.ivanskodje.rawcaproller.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.io.File;

public class FileValidator implements IParameterValidator {

	public void validate(String name, String value) throws ParameterException {
		if (value == null || value.length() == 0) {
			throw new ParameterException("Parameter " + name + " cannot me empty. ");
		}

		File file = new File(value);
		if (file.isDirectory() || !file.exists()) {
			throw new ParameterException("Parameter " + name + " has an invalid file path: '" + value + "'. Please make sure the file exists. ");
		}
	}
}
