package ui;

import helpers.Constants;
import helpers.Input;
import models.Client;
import models.Product;
import repository.ClientRepository;
import repository.ProductRepository;

public class ClientSignUp implements SignUp {
    private Client client;
    private Product subscribedProduct;
    private ClientRepository clientRepository;
    private ProductRepository productRepository;

    public ClientSignUp(ClientRepository clientRepository, ProductRepository productRepository) {
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void greet() {
        System.out.println("We at " + Constants.corpName + " corp are very grateful to WELCOME You");
        System.out.println();
        // TODO: ask about how they know this company
    }

    @Override
    public boolean isReadyToQualify() {
        System.out.println("In order to join as a Client in this corp");
        System.out.println("You need to be a user of atleast one of our products!");
        System.out.println();
        return (Input.getYesOrNoAsBoolean("Do you want to look into products [Y/n] >> "));
    }

    @Override
    public boolean isQualifiedToJoin() {
        System.out.println(Constants.clearScreen);
        System.out.println("The below listed are the products of the company!");
        System.out.println("Please choose any one to proceed further and qualify you as a client!");
        System.out.println();
        ListView<Product> listView = new ListView<Product>(productRepository.getProducts());
        listView.listItems();
        subscribedProduct = listView
                .selectItemByUser("Enter product no. to subscribe that product or 0 to go back >> ");
        if (subscribedProduct != null) {
            System.out.println();
            System.out.println("That's great, you have selected the product '" + subscribedProduct.getName() + "'");
            System.out.println();
        }
        return subscribedProduct != null;
    }

    @Override
    public boolean gatherDetails() {
        System.out.println("To proceed further, please enter your details below:");
        System.out.println();
        String username = Input.getUsername("ENTER YOUR USERNAME >> ");
        System.out.println();
        String email = null;
        do {
            if (email != null) {
                System.out.println("Oops, provided email is already present, give a new email!");
                System.out.println();
            }
            email = Input.getEmail("ENTER YOUR EMAIL >> ");
        } while (clientRepository.isEmailAlreadyPresent(email));
        System.out.println();
        String password = Input.getPasswordAfterConformPassword();
        if (Input.getYesOrNoAsBoolean("Do you really wish to join this company? [Y/n] >> ")) {
            client = new Client(username, email, password, subscribedProduct);
            return true;
        }
        return false;
    }

    @Override
    public void join() {
        Client clientJoined = clientRepository.addClient(client);
        if (clientJoined == null) {
            System.out.println("Sorry, there is some technical issue on our side!");
            return;
        }
        System.out.println("Hooyah! you became our beloved client to our company!");
        System.out.println();
        System.out.println("Here are your details!");
        System.out.println();
        System.out.println(clientJoined);
        System.out.println("Login with your credentials next time!");
        Input.waitUntilReturn();
    }

}
