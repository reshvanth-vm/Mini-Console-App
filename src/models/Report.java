package models;

import java.util.Date;

public final class Report {
    private final int fromEmployeeId, toEmployeeId;
    private final String title, body;
    private final Date reportedDate;

    public Report(int fromEmployeeId, int toEmployeeId, String title, String body, Date reportedDate) {
        this.fromEmployeeId = fromEmployeeId;
        this.toEmployeeId = toEmployeeId;
        this.title = title;
        this.body = body;
        this.reportedDate = reportedDate;
    }

    public Report(int fromEmployeeId, int toEmployeeId, String title, String body) {
        this(fromEmployeeId, toEmployeeId, title, body, new Date());
    }

    public int getFromEmployeeId() {
        return fromEmployeeId;
    }

    public int getToEmployeeId() {
        return toEmployeeId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Date getReportedDate() {
        return reportedDate;
    }

}
