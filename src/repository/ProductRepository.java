package repository;

import java.util.List;

import models.Feedback;
import models.Product;
import models.Ticket;
import models.Update;

public interface ProductRepository {

    public List<Product> getProducts();

    public Product addProduct(Product product);

    public List<Feedback> getFeedbacks(Product product);

    public List<Update> getUpdates(Product product);

    public List<Ticket> getTickets(Product product);

    public boolean updateTicket(Ticket ticket);

    public boolean updateFeedback(Feedback feedback);

    public Feedback addFeedback(Feedback feedback);

}
