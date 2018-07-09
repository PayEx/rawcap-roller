package com.ivanskodje.rawcaproller;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RawCapRoller {
	List<String> files = new ArrayList<>();

	public void run(String... args) {
		Args arguments = new Args();

		JCommander client = buildClientWithArguments(arguments);
		client.parse(args);
		validateArgumentsAgainstEachOther(arguments);

		// Setup before looping
		int numberOfFilesToCreate = arguments.getFileCount();
		int counter = 1;
		do {
			try {
				String[] rawCapCommand = buildCommand(arguments);
				ProcessBuilder builder = new ProcessBuilder(rawCapCommand);
				builder.redirectErrorStream(true);
				final Process process = builder.start();
				Runtime.getRuntime().addShutdownHook(new Thread(() -> {
					process.destroy();
				}));

				BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line;
				int rawCounter = 0;
				while ((line = input.readLine()) != null) {
					if (rawCounter < 2) {
						System.out.println(line);
						rawCounter++;
					} else {
						System.out.print(line + "\r");
					}
				}
				System.out.println();
				input.close();

				if (arguments.isRollFiles()) {
					System.out.println("Completed:\t" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
				} else {
					System.out.println("-------------------------------------------");
					System.out.println("Completed (" + counter + "/" + arguments.getFileCount() + "): " + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
				}
				System.out.println("-------------------------------------------");
			} catch (Exception ex) {
				throw new ParameterException(ex.getMessage());
			}

			// Handle counter
			if (counter % numberOfFilesToCreate == 0) {
				if (arguments.isRollFiles()) {
					File file = new File(files.get(0));
					file.delete();
					files.remove(0);
					continue;
				} else {
					break;
				}
			}
			counter++;
		}
		while (counter <= numberOfFilesToCreate || arguments.isRollFiles()); // TODO: Delete oldest file when exceeding rollFiles!

	}

	/**
	 * We want to make sure we don't set both packet count AND seconds, since they are mutually exclusive.
	 *
	 * @param arguments
	 */
	private void validateArgumentsAgainstEachOther(Args arguments) {
		if (arguments.getPacketCount() != 0 && arguments.getSeconds() != 0) {
			throw new ParameterException("You cannot specify packet packetCount (-c) and seconds (-s) in the same command! Remove one or both. ");
		}

		if (arguments.getFileCount() > 1 && arguments.getSeconds() == 0 && arguments.getPacketCount() == 0) {
			throw new ParameterException("You cannot have more than 1 -fc (--filecount) without setting -s (--seconds) or -c (--count). ");
		}
	}

	/**
	 * We build the client in order to turn off case sensitive arguments. When the client is parsed, the Args object will be populated with data, given they pass validation.
	 *
	 * @param arguments
	 */
	private JCommander buildClientWithArguments(Args arguments) {
		JCommander client = JCommander.newBuilder()
				.addObject(arguments)
				.build();
		client.setCaseSensitiveOptions(false);
		return client;
	}

	private String[] buildCommand(Args arguments) {
		String resultFileName = getFormattedFileName(arguments);
		files.add(resultFileName);
		List<String> cmdArguments = new ArrayList<String>();
		cmdArguments.add(arguments.getRawCapFilePath());

		if (arguments.getPacketCount() != 0) {
			cmdArguments.add("-c"); // -c stands for packet counts in RawCap.exe
			cmdArguments.add(arguments.getPacketCount() + "");
		}

		if (arguments.getSeconds() != 0) {
			cmdArguments.add("-s"); // -s stands for time in seconds to run in RawCap.exe
			cmdArguments.add(+arguments.getSeconds() + ""); // -s stands for time in seconds to run in RawCap.exe
		}
		cmdArguments.add("-f"); // RawCap.exe command -f to dump files
		cmdArguments.add(arguments.getInterfaceNumber() + "");
		cmdArguments.add(resultFileName);

		return cmdArguments.toArray(new String[cmdArguments.size()]);
	}

	private String getFormattedFileName(Args arguments) {
		return (arguments.getFileCount() > 1 || arguments.isRollFiles() ? getRollingFileName(arguments) : arguments.getOutputFilePath());
	}

	private String getRollingFileName(Args arguments) {
		String formattedDateTime = getFormattedDateTime();

		String outputPath = arguments.getOutputFilePath();
		List<String> outputPathInArray = new ArrayList<>(Arrays.asList(outputPath.split("\\.")));
		outputPathInArray.add(outputPathInArray.size() - 1, formattedDateTime);

		return getCombinedFilePathFromArray(outputPathInArray);

	}

	private String getCombinedFilePathFromArray(List<String> outputPathInArray) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < outputPathInArray.size(); i++) {
			stringBuilder.append(outputPathInArray.get(i));
			if (i != outputPathInArray.size() - 1) {
				stringBuilder.append(".");
			}
		}
		return stringBuilder.toString();
	}

	private String getFormattedDateTime() {
		LocalDateTime dateTime = LocalDateTime.now();
		String timeFormat = "yyyyMMddHHmmssSS";
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(timeFormat);
		return dateTime.format(dateTimeFormatter);
	}
}
