package com.ivanskodje.rawcaproller;

import com.beust.jcommander.ParameterException;

public class RawCapRollerApplication {

	public static void main(String[] args) {
		try {
			new RawCapRoller().run(args);
		} catch (ParameterException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
