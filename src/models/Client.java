package models;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public final class Client extends User {
    private final List<Product> subscribedProducts;

    public Client(String username, String email, String password, Product product) {
        super(username, email, password);
        this.subscribedProducts = new LinkedList<>();
        subscribedProducts.add(product);
    }

    public Client(String username, String email, String password, Date lastLogin, List<Product> products) {
        super(username, email, password, lastLogin);
        this.subscribedProducts = products;
    }

    public void subscribeProduct(Product product) {
        subscribedProducts.add(product);
    }

    public void unsubscribeProduct(Product product) {
        subscribedProducts.remove(product);
    }

    public List<Product> getSubscribedProducts() {
        return subscribedProducts;
    }

    @Override
    public String toString() {
        String nl = System.lineSeparator();
        String string = "Name  : " + username + nl + "Email : " + email + nl + "Subscribed products : ";
        for (Product product : subscribedProducts)
            string += product.getName() + ",";
        return string;
    }

}
