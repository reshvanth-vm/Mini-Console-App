import java.io.IOException;

import databases.ClientDatabase;
import databases.EmployeeDatabase;
import databases.ModelDatabase;
import databases.ProductDatabase;
import enums.MenuOption;
import helpers.Constants;
import helpers.FileModelDatabase;
import helpers.Input;
import models.Client;
import models.Employee;
import repository.ClientRepository;
import repository.EmployeeRepository;
import repository.ModelRepository;
import ui.ClientInteraction;
import ui.ClientSignUp;
import ui.DialogMenu;
import ui.EmployeeInteraction;
import ui.EmployeeSignUp;
import ui.SignUp;
import ui.UserInteraction;

public class Main {
    public static void main(String[] args) throws IOException {
        ModelRepository modelRepository = ModelDatabase.getInstance(new FileModelDatabase());

        DialogMenu<MenuOption> dialog = new DialogMenu<>(new MenuOption[] {
                MenuOption.LOGIN,
                MenuOption.SIGN_UP,
                MenuOption.EXIT
        });

        loop: do {
            System.out.println(Constants.clearScreen);
            System.out.println("********************************************************************");
            System.out.println("*                                                                  *");
            System.out.println("*                         Welcome to RV Corp                       *");
            System.out.println("*                 The leading tech corporate in Earth              *");
            System.out.println("*                                                                  *");
            System.out.println("********************************************************************");
            System.out.println();
            System.out.println(dialog);
            switch (dialog.getOption()) {
                case LOGIN:
                    UserInteraction userInteraction = login(modelRepository);
                    if (userInteraction != null) {
                        userInteraction.onLogin();
                        userInteraction.afterLogin();
                        userInteraction.onLogout();
                    }
                    break;
                case SIGN_UP:
                    SignUp signUp = signUp(modelRepository);
                    if (signUp != null) {
                        System.out.println(Constants.clearScreen);
                        signUp.greet();
                        if (signUp.isReadyToQualify() && signUp.isQualifiedToJoin() && signUp.gatherDetails())
                            signUp.join();
                    }
                    break;
                case EXIT:
                default:
                    System.out.println(Constants.clearScreen);
                    break loop;
            }
        } while (true);
    }

    private static UserInteraction login(ModelRepository modelRepository) {
        do {
            System.out.println(Constants.clearScreen);
            System.out.println("Login Page:");
            System.out.println();
            String email = Input.getString("ENTER EMAIL    >> ");
            String password = Input.getPassword("ENTER PASSWORD >> ");
            if (email.contains("@rvcorp.com")) {
                Employee employee = isAdmin(email, password);
                EmployeeRepository employeeRepository = EmployeeDatabase.getInstance(modelRepository);
                if (employee != null || (employee = employeeRepository.getEmployee(email, password)) != null)
                    return new EmployeeInteraction(employee, employeeRepository,
                            ProductDatabase.getInstance(modelRepository));
            } else {
                Client client = null;
                ClientRepository clientRepository = ClientDatabase.getInstance(modelRepository);
                if ((client = clientRepository.getClient(email, password)) != null) {
                    return new ClientInteraction(client, clientRepository,
                            ProductDatabase.getInstance(modelRepository));
                }
            }
            System.out.println("Oops, invalid username or password!");
        } while (Input.getYesOrNoAsBoolean("Do you want to try again [y/n] >> "));
        return null;
    }

    private static SignUp signUp(ModelRepository modelRepository) {
        DialogMenu<MenuOption> signUpDialog = new DialogMenu<>(new MenuOption[] {
                MenuOption.SIGN_UP_AS_CLIENT,
                MenuOption.SIGN_UP_AS_EMPLOYEE,
                MenuOption.GO_BACK });
        System.out.println(Constants.clearScreen);
        System.out.println(signUpDialog);
        switch (signUpDialog.getOption()) {
            case SIGN_UP_AS_CLIENT:
                return new ClientSignUp(ClientDatabase.getInstance(modelRepository),
                        ProductDatabase.getInstance(modelRepository));
            case SIGN_UP_AS_EMPLOYEE:
                return new EmployeeSignUp(EmployeeDatabase.getInstance(modelRepository));
            case GO_BACK:
            default:
                break;
        }
        return null;
    }

    private static Employee isAdmin(String email, String password) {
        Employee admin = Constants.admin;
        if (admin.getEmail().equals(email) && admin.getPassword().equals(password)) {
            return admin;
        }
        return null;
    }

}