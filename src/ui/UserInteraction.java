package ui;

import helpers.Input;
import models.User;
import repository.ProductRepository;

public abstract class UserInteraction {
    protected ProductRepository productRepository;

    protected UserInteraction(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public abstract void onLogin();

    public abstract void afterLogin();

    public abstract void onLogout();

    protected boolean editUsername(User user) {
        System.out.println();
        String username = Input.getUsername("Enter new username >> ");
        System.out.println();
        System.out.println("Old username >> " + user.getUsername());
        System.out.println("New username >> " + username);
        System.out.println();
        if (Input.getYesOrNoAsBoolean("Do you really want to change old username to new username [y/n] >> ")) {
            user.setUsername(username);
            return true;
        }
        return false;
    }

    protected boolean editPassword(User user) {
        String p = Input.getPassword("Enter old password [or enter 'exit' to goback] >> ",
                (pwd) -> pwd.equals("exit") || pwd.equals(user.getPassword()), "Last password doesn't match!!");
        if (p.equals("exit"))
            return false;
        String newPassword = Input.getPassword("Enter new password >> ");
        String confirmPassword = Input.getPassword("Confirm new password >> ", newPassword::equals,
                "confirm password does not match with new password!");
        if (Input.getYesOrNoAsBoolean("Do you really want to change old password to new password [y/n] >> ")) {
            user.setPassword(confirmPassword);
            return true;
        }
        return false;
    }

    protected void clearScreen() {
        System.out.println("\033[H\033[2J");
    }

}
