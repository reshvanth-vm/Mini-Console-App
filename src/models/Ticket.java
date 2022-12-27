package models;

import java.util.Date;

import enums.TicketStatus;

public final class Ticket extends Feedback {
    private TicketStatus status;

    public Ticket(String message, Product product, Client client) {
        super(message, product, client);
        this.status = TicketStatus.OPEN;
    }

    public Ticket(
            String message,
            Product product,
            Client client,
            TicketStatus status,
            int upvotes,
            Date postedDate) {
        super(message, product, client, upvotes, postedDate);
        this.status = status;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return super.toString() + System.lineSeparator() + "status: " + status;
    }

}
