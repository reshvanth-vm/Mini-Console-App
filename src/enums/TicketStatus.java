package enums;

public enum TicketStatus {
    OPEN,
    ON_HOLD,
    ESCALATED,
    CLOSED,
    IN_PROGRESS;

    @Override
    public String toString() {
        return name()
                .charAt(0)
                + name().substring(1)
                        .toLowerCase()
                        .replace("_", " ");
    }
}