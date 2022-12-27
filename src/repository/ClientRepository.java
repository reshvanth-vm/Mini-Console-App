package repository;

import java.util.List;

import models.Client;
import models.Feedback;
import models.Ticket;

public interface ClientRepository {

    public boolean isEmailAlreadyPresent(String email);

    public List<Feedback> getFeedbacks(Client client);

    public List<Ticket> getRaisedTickets(Client client);

    public Client getClient(String email, String password);

    public Client addClient(Client client);

    public boolean updateClient(Client client);

    public Client removeClient(Client client);

}

