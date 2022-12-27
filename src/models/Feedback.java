package models;

import java.util.Date;

public class Feedback {
    protected int upvotes;
    protected final String message;
    protected final Product product;
    protected final Client client;
    protected final Date postedDate; // published date

    public Feedback(String message, Product product, Client client) {
        this(message, product, client, 0, new Date());
    }

    public Feedback(String message, Product product, Client client, int upvotes, Date postedDate) {
        this.message = message;
        this.product = product;
        this.client = client;
        this.upvotes = upvotes;
        this.postedDate = postedDate;
    }

    public String getMessage() {
        return message;
    }

    public Product getProduct() {
        return product;
    }

    public Client getClient() {
        return client;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void upvote() {
        this.upvotes++;
    }

    public Date getPublishedDate() {
        return postedDate;
    }

    @Override
    public String toString() {
        String nl = System.lineSeparator();
        return "For product: " + product.getName() +
                nl + message +
                nl + "upvotes: " + upvotes + " posted by: " + client.getUsername() + " posted on: " + postedDate;
    }

}
