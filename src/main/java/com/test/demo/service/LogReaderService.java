package com.test.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.demo.domain.EventLog;
import com.test.demo.helper.LineSyntaxHelper;
import com.test.demo.repository.LogRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;


@Slf4j
@Service
public class LogReaderService {
    private final static Logger LOGGER = LoggerFactory.getLogger(LogReaderService.class);

    @Autowired
    LogRepository logRepository;

    @Autowired
    LogProcessService logProcessService;

    public void read(File file) throws IOException {
        LineIterator it = FileUtils.lineIterator(file, "UTF-8");
        EventLog log;
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                while (!LineSyntaxHelper.verify(line)) {
                    LOGGER.warn("Line skipped![{}]", line);
                    line = it.nextLine();
                }
                log = new ObjectMapper().readValue(line, EventLog.class);
                logRepository.save(logProcessService.processEventLog(log));
            }
        } finally {
            it.close();
        }
    }


}
