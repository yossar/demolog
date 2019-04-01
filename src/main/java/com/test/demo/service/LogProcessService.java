package com.test.demo.service;

import com.test.demo.domain.EventLog;
import com.test.demo.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.test.demo.helper.MetaFields.THRESHOLD;

@Service
public class LogProcessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogProcessService.class);

    @Autowired
    LogRepository logRepository;

    public EventLog processEventLog(EventLog eventLog) {
        eventLog.setDbId(eventLog.getId(), eventLog.getTimestamp());
        EventLog existingLogEvent = logRepository.getById(eventLog.getId());

        if (existingLogEvent != null &&
                Math.abs(existingLogEvent.getTimestamp() - eventLog.getTimestamp()) > THRESHOLD) {
            updateEventLogIfNeeded(eventLog, existingLogEvent, logRepository);
            LOGGER.warn("Alert on: {}", eventLog.getId());
        }
        return eventLog;
    }

    private void updateEventLogIfNeeded(EventLog eventLog, EventLog existingEventLog, LogRepository logRepository) {
        if (existingEventLog.getTimestamp() > eventLog.getTimestamp()) {
            existingEventLog.setAlert(true);
            logRepository.save(existingEventLog);
        } else {
            eventLog.setAlert(true);
        }
    }
}
