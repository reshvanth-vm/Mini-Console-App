package databases;

import java.util.List;
import java.util.stream.Collectors;

import models.Feedback;
import models.Product;
import models.Ticket;
import models.Update;
import repository.ModelRepository;
import repository.ProductRepository;

public class ProductDatabase implements ProductRepository {
    private static ProductDatabase productDatabase;
    private ModelRepository modelRepository;

    private ProductDatabase(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public static ProductDatabase getInstance(ModelRepository modelRepository) {
        if (productDatabase == null)
            productDatabase = new ProductDatabase(modelRepository);
        return productDatabase;
    }

    @Override
    public List<Product> getProducts() {
        return modelRepository.getProducts();
    }

    @Override
    public Product addProduct(Product product) {
        return modelRepository.addProduct(product);
    }

    @Override
    public List<Feedback> getFeedbacks(Product product) {
        return modelRepository.getFeedbacks().stream().filter(
                (feedback) -> feedback.getProduct().equals(product)).collect(Collectors.toList());
    }

    @Override
    public List<Update> getUpdates(Product product) {
        return modelRepository.getUpdates().stream().filter(
                (update) -> update.getProduct().equals(product)).collect(Collectors.toList());
    }

    @Override
    public List<Ticket> getTickets(Product product) {
        return modelRepository.getTickets().stream().filter(
            (ticket) -> ticket.getProduct().equals(product)
        ).collect(Collectors.toList());
    }

    @Override
    public boolean updateTicket(Ticket ticket) {
        return modelRepository.updateTicket(ticket);
    }

    @Override
    public Feedback addFeedback(Feedback feedback) {
        return modelRepository.addFeedback(feedback);
    }

    @Override
    public boolean updateFeedback(Feedback feedback) {
        return modelRepository.updateFeedback(feedback);
    }

}
