package io.nuite.modules.system.from;

import java.util.Date;

public class ChatHistoryForm {
    private int id;
    private Date time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
