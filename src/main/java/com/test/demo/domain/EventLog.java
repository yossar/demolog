package com.test.demo.domain;

import lombok.*;
import javax.persistence.Entity;
import javax.persistence.Id;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventLog {

    @Id
    String dbId;

    @NonNull
    private String id, state;

    @NonNull
    private long timestamp;

    private String type, host;
    private boolean alert;

    public void setDbId(String id, long timestamp) {
        this.dbId = id + String.valueOf(timestamp);
    }

}
