package com.ivanskodje.rawcaproller;

import com.beust.jcommander.Parameter;
import com.ivanskodje.rawcaproller.validator.FileCountValidator;
import com.ivanskodje.rawcaproller.validator.FileValidator;
import com.ivanskodje.rawcaproller.validator.NumberValidator;
import com.ivanskodje.rawcaproller.validator.TextValidator;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Args {
	@Parameter
	private List parameters = new ArrayList<String>();

	@Parameter(
			names = {"-rc", "--rawcap"},
			description = "Full path to RawCap.exe  By default it looks in the same directory. ",
			validateWith = FileValidator.class)
	private String rawCapFilePath = "RawCap.exe";

	@Parameter(
			names = {"-o", "--output"},
			description = "Full path to the file you want to save. Default: 'dumpfile.pcap'. ",
			validateWith = TextValidator.class)
	private String outputFilePath = "dumpfile.pcap";

	@Parameter(
			names = {"-s", "--seconds", "--sec"},
			description = "Stop sniffing after <sec> seconds (per file). By default runs until manually stopped. ",
			validateWith = NumberValidator.class)
	private long seconds = 0;

	@Parameter(
			names = {"-i", "--interface"},
			description = "The interface number. By default 0. Run RawCap.exe to discover your options. ",
			validateWith = NumberValidator.class)
	private long interfaceNumber = 0;

	@Parameter(
			names = {"-c", "-pc", "--packetCount"},
			description = "Stop sniffing after receiving <packetCount> packets (per file). By default runs until manually stopped. ",
			validateWith = NumberValidator.class)
	private long packetCount = 0;

	@Parameter(
			names = {"-fc", "--filecount"},
			description = "Number of files to build (when given --seconds or --packetCount). Default: 1 ",
			validateWith = FileCountValidator.class)
	private int fileCount = 1;

	@Parameter(
			names = {"-r", "--roll"},
			description = "Whether or not to roll fileCount (runs indefinitely). This will cause the oldest file to be deleted when starting a new file. By default we do not roll the fileCount. ")
	private boolean rollFiles = false;
}
