package com.test.demo;

import com.test.demo.service.LogReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Arrays;

import static com.test.demo.helper.MetaFields.EXPECTED_OPTION;

@SpringBootApplication
public class DemoApplication implements ApplicationRunner{

	private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);

	@Autowired
	LogReaderService logReaderService;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		LOGGER.info("Arguments: {}", Arrays.toString(args.getSourceArgs()));
		LOGGER.info("Option Arguments: {}", args.getOptionNames());

		if (args.containsOption(EXPECTED_OPTION) && args.getOptionValues(EXPECTED_OPTION).size() == 1) {
			logReaderService.read(new File(args.getOptionValues(EXPECTED_OPTION).get(0)));
		}
	}
}
