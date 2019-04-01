package com.test.demo;

import com.test.demo.domain.EventLog;
import com.test.demo.repository.LogRepository;
import com.test.demo.service.LogProcessService;
import com.test.demo.service.LogReaderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTest {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private LogReaderService logReaderService;

    @Autowired
    private LogProcessService logProcessService;

    @Test
    public void whenFindingLogById_thenCorrect() {
        logRepository.save(EventLog.builder()
                .id("abc")
                .state("DONE")
                .timestamp(1L)
                .dbId("abc1")
                .build());

        assertThat(logRepository.getById("abc")).isInstanceOf(EventLog.class);
    }

    @Test
    public void whenFindingAllLogs_thenCorrect() {
        logRepository.save(EventLog.builder()
                .id("aaa")
                .state("STARTED")
                .timestamp(1L)
                .dbId("aaa1")
                .build()
        );
        logRepository.save(EventLog.builder()
                .id("bbb")
                .state("FINISHED")
                .timestamp(2L)
                .dbId("bbb2")
                .build()
        );
        assertThat(logRepository.findAll()).isInstanceOf(List.class);
    }

    @Test
    public void whenFindingExistingLogCreateNewOne_thenCorrect() {
        logRepository.save(EventLog.builder()
                .id("ccc")
                .state("ERROR")
                .timestamp(1L)
                .dbId("ccc1")
                .build());

        EventLog eventLog = EventLog.builder()
                .id("ccc")
                .state("ERROR")
                .timestamp(6L)
                .build();

        assertThat(logProcessService.processEventLog(eventLog).isAlert()).isTrue();
    }

    @Test
    public void whenFindingExistingLogCreateNewOneWoAlert_thenCorrect() {
        logRepository.save(EventLog.builder()
                .id("ccd")
                .state("ERROR")
                .timestamp(1L)
                .dbId("ccc1")
                .build());

        EventLog eventLog = EventLog.builder()
                .id("ccd")
                .state("ERROR")
                .timestamp(5L)
                .dbId("ccc6")
                .build();

        logProcessService.processEventLog(eventLog);

        assertThat(logProcessService.processEventLog(eventLog).isAlert()).isFalse();
    }

    @Test
    public void whenFindingExistingLogAddNewOne_thenCorrect() {
        logRepository.save(EventLog.builder()
                .id("ddd")
                .state("ERROR")
                .timestamp(123L)
                .dbId("ddd123")
                .build());

        EventLog eventLog = EventLog.builder()
                .id("ddd")
                .state("ERROR")
                .timestamp(133L)
                .build();

        logRepository.save(logProcessService.processEventLog(eventLog));
        assertThat(logRepository.findByDbId("ddd133").isAlert()).isTrue();
    }


    @Test
    public void readFile() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("file.log").getFile());
        logReaderService.read(file);

        assertThat(logRepository.findByDbId("scsmbstgra1491377495217").getType()).isEqualTo("APPLICATION_LOG");
    }
}