package ui;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import enums.MenuOption;
import helpers.Constants;
import helpers.Input;
import models.Client;
import models.Feedback;
import models.Product;
import models.Ticket;
import models.Update;
import repository.ClientRepository;
import repository.ProductRepository;

public final class ClientInteraction extends UserInteraction {
    private Client client;
    private ClientRepository clientRepository;

    public ClientInteraction(Client client, ClientRepository clientRepository, ProductRepository productRepository) {
        super(productRepository);
        this.client = client;
        this.clientRepository = clientRepository;
    }

    @Override
    public void onLogin() {
        System.out.println(Constants.clearScreen);
        System.out.println("Welcome " + client.getUsername());
        System.out.println("We are very glad to have you back so soon!");
        System.out.println();
        List<Update> updates = new LinkedList<>();
        for (Product product : client.getSubscribedProducts()) {
            updates.addAll(productRepository.getUpdates(product).stream()
                    .filter((update) -> update.getPostedDate().after(client.getLastLoginDate()))
                    .collect(Collectors.toList()));
        }
        if (!updates.isEmpty()) {
            System.out.println("while you were away there has been new releases for some of your subscribed products!");
            System.out.println();
            if (Input.getYesOrNoAsBoolean("Do you want to see those update? [y/N] >> ")) {
                ListView<Update> listViewHelper = new ListView<>(updates);
                listViewHelper.listItems();
                Input.waitUntilReturn();
            }
        }
    }

    @Override
    public void afterLogin() {
        DialogMenu<MenuOption> dialog = new DialogMenu<>(new MenuOption[] {
                MenuOption.VIEW_PROFILE,
                MenuOption.EDIT_PROFILE,
                MenuOption.SUBSCRIBE_NEW_PRODUCT,
                MenuOption.GIVE_FEEDBACK_TO_A_PRODUCT,
                MenuOption.VIEW_RAISED_HELPDESK_TICKETS,
                MenuOption.DELETE_ACCOUNT,
                MenuOption.LOGOUT }), editDialog = new DialogMenu<>(
                        new MenuOption[] {
                                MenuOption.CHANGE_USERNAME,
                                MenuOption.CHANGE_PASSWORD,
                                MenuOption.UNSUBSCRIBE_A_PRODUCT,
                                MenuOption.GO_BACK });
        loop: do {
            System.out.println(Constants.clearScreen);
            System.out.println(dialog);
            MenuOption option = dialog.getOption();
            System.out.println(Constants.clearScreen);
            switch (option) {
                case VIEW_PROFILE:
                    viewProfile();
                    break;
                case EDIT_PROFILE:
                    System.out.println(editDialog);
                    switch (editDialog.getOption()) {
                        case CHANGE_USERNAME:
                            if (editUsername(client) && clientRepository.updateClient(client))
                                System.out.println("Username changed successfully!");
                            break;
                        case CHANGE_PASSWORD:
                            if (editPassword(client) && clientRepository.updateClient(client))
                                System.out.println("Password changed successfully!");
                            break;
                        case UNSUBSCRIBE_A_PRODUCT:
                            unsubscribeProduct();
                            break;
                        case GO_BACK:
                        default:
                            break;
                    }
                    break;
                case SUBSCRIBE_NEW_PRODUCT:
                    subscribeNewProduct();
                    break;
                case GIVE_FEEDBACK_TO_A_PRODUCT:
                    giveFeedback();
                    break;
                case VIEW_RAISED_HELPDESK_TICKETS:
                    viewRaisedTickets();
                    break;
                case DELETE_ACCOUNT:
                    if (deleteAccount()) {
                        client = null;
                        break loop;
                    }
                    break;
                case LOGOUT:
                default:
                    break loop;
            }
            Input.waitUntilReturn();
        } while (true);
    }

    @Override
    public void onLogout() {
        if (client != null) {
            client.setLastLoginDate(new Date());
            clientRepository.updateClient(client);
        }
    }

    public void viewProfile() {
        System.out.println("Your profile:-");
        System.out.println();
        System.out.println();
        System.out.println("Name          : " + client.getUsername());
        System.out.println();
        System.out.println("Email         : " + client.getEmail());
        System.out.println();
        System.out.println("Last Login    : " + client.getLastLoginDate());
        System.out.println();
        ListView<Product> listViewHelper = new ListView<>(client.getSubscribedProducts());
        System.out.println("List of products Subscribed to:-");
        System.out.println();
        listViewHelper.listItems();
    }

    private void giveFeedback() {
        ListView<Product> subscribedProducts = new ListView<>(client.getSubscribedProducts());
        subscribedProducts.listItems();
        Product product = subscribedProducts.selectItemByUser(
                "Enter product index no. to give feedback to that product or enter 0 to get back >> ");
        if (product != null) {
            boolean giveFeedback = true;
            clearScreen();
            System.out.println(product + System.lineSeparator());
            List<Feedback> feeds = productRepository.getFeedbacks(product);
            if (!feeds.isEmpty() && Input.getYesOrNoAsBoolean(
                    "Do you want to view all other clients feedbacks of this product? [Y/n] >> ")) {
                System.out.println(Constants.clearScreen);
                ListView<Feedback> feedbacks = new ListView<>(feeds);
                feedbacks.listItems();
                if (Input.getYesOrNoAsBoolean("Do you want to upvote any clients feedback [y/n] >> ")) {
                    Feedback feedback = feedbacks.selectItemByUser(
                            "Enter the feedback no. to upvote it or 0 to go back and give a feedback of you own >> ");
                    if (feedback != null) {
                        feedback.upvote();
                        if (productRepository.updateFeedback(feedback)) {
                            System.out.println();
                            System.out.println("Feedback upvoted successfully!");
                            System.out.println(Constants.clearScreen);
                        }
                        giveFeedback = Input.getYesOrNoAsBoolean(
                                "Do you still want to give your own feedback for this product? [y/n] >> ");
                    }
                }
            }
            if (giveFeedback) {
                System.out.println(product + System.lineSeparator());
                if (productRepository.addFeedback(new Feedback(
                        Input.getString("Enter feedback message >> "), product, client)) != null) {
                    System.out.println();
                    System.out.println("Feedback added successfully!");
                }
            }
        }
    }

    private void subscribeNewProduct() {
        List<Product> unsubscribedProducts = productRepository.getProducts();
        for (Product pr : client.getSubscribedProducts())
            unsubscribedProducts.remove(pr);
        ListView<Product> listViewHelper = new ListView<>(unsubscribedProducts);
        listViewHelper.listItems();
        Product product = listViewHelper.selectItemByUser("Enter product no. to subscribe it or 0 to go back >> ");
        if (product != null) {
            client.subscribeProduct(product);
            clientRepository.updateClient(client);
            System.out.println("Product is successfully subscribed!");
        }
    }

    private void viewRaisedTickets() {
        ListView<Ticket> listView = new ListView<>(clientRepository.getRaisedTickets(client));
        listView.listItems();
        Input.waitUntilReturn();
    }

    private void unsubscribeProduct() {
        if (client.getSubscribedProducts().size() > 1) {
            ListView<Product> listViewHelper = new ListView<>(client.getSubscribedProducts());
            listViewHelper.listItems();
            Product product = listViewHelper
                    .selectItemByUser("Enter product index no. to unsubscribe it or enter 0 to get back >> ");
            if (product != null) {
                System.out.println("Unsubscribed product " + product.getName());
                client.unsubscribeProduct(product);
                clientRepository.updateClient(client);
            }
        } else {
            System.out.println("*Oops, you have subscribed to only one product*");
            System.out.println("you need to be a subscriber of atleast one product inorder to be a client!");
        }
    }

    private boolean deleteAccount() {
        if (Input.getYesOrNoAsBoolean(
                "Are you sure to delete your precious account and leave us for now? [Y/n] >> ")) {
            do {
                if (Input.getPassword("Enter your password one last time >> ").equals(client.getPassword())) {
                    clientRepository.removeClient(client);
                    System.out.println("We are going to miss you for sure and we hope you come back soon!");
                    System.out.println("Bye...    :(");
                    return true;
                } else
                    System.out.println("Wrong password!");
            } while (Input.getYesOrNoAsBoolean("Do you still want to delete account? [Y/n} >> "));
        } // TODO reason for leaving
        return false;
    }
}
