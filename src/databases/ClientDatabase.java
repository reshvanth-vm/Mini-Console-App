package databases;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import models.Client;
import models.Feedback;
import models.Ticket;
import repository.ClientRepository;
import repository.ModelRepository;

public class ClientDatabase implements ClientRepository {
    private static ClientDatabase clientDatabase;
    private ModelRepository modelRepository;

    private Map<String, Client> emailClientMap;

    private ClientDatabase(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
        emailClientMap = new HashMap<>();
        for (Client client : modelRepository.getClients())
            emailClientMap.put(client.getEmail(), client);
    }

    public static ClientDatabase getInstance(ModelRepository modelRepository) {
        if (clientDatabase == null)
            clientDatabase = new ClientDatabase(modelRepository);
        return clientDatabase;
    }

    @Override
    public Client getClient(String email, String password) {
        Client client = emailClientMap.get(email);
        if (client != null && client.getPassword().equals(password))
            return client;
        return null;
    }

    @Override
    public boolean updateClient(Client client) {
        return modelRepository.updateClient(client);
    }

    @Override
    public boolean isEmailAlreadyPresent(String email) {
        return emailClientMap.containsKey(email);
    }

    @Override
    public List<Feedback> getFeedbacks(Client client) {
        return modelRepository.getFeedbacks().stream().filter(
                (feedback) -> feedback.getClient().equals(client)).collect(Collectors.toList());
    }

    @Override
    public List<Ticket> getRaisedTickets(Client client) {
        return modelRepository.getTickets().stream().filter(
                (ticket) -> ticket.getClient().equals(client)).collect(Collectors.toList());
    }

    @Override
    public Client addClient(Client client) {
        return modelRepository.addClient(client);
    }

    @Override
    public Client removeClient(Client client) {
        emailClientMap.remove(client.getEmail());
        for (Feedback feedback : getFeedbacks(client))
            modelRepository.removeFeedback(feedback);
        for (Ticket ticket : getRaisedTickets(client))
            modelRepository.removeTicket(ticket);
        return modelRepository.removeClient(client);
    }

}
