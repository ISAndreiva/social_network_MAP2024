package internal.andreiva.socialnetwork.ui;

import internal.andreiva.socialnetwork.service.UserService;

import java.util.Scanner;

public class CLI
{
    UserService userService;
    Scanner scanner = new Scanner(System.in);
    public CLI(UserService userService)
    {
        this.userService = userService;
    }

    public void run()
    {
        while (true)
        {
            System.out.print(">");
            String command = scanner.nextLine();
            switch (command)
            {
                case "add":
                    add();
                    break;
                case "delete":
                    delete();
                    break;
                case "update":
                    update();
                    break;
                case "exit":
                    return;
                case "help":
                    help();
                    break;
                case "show":
                    showUsers();
                    break;
                default:
                    System.out.println("Invalid command");
            }
        }
    }

    private void help()
    {
        System.out.println("Commands:");
        System.out.println("add - Add a user");
        System.out.println("delete - Delete a user");
        System.out.println("update - Update a user");
        System.out.println("show - Show all users");
        System.out.println("exit - Exit the application");
    }

    private void add()
    {
        System.out.print("First name:");
        String firstName = scanner.nextLine();
        System.out.print("Last name:");
        String lastName = scanner.nextLine();
        System.out.print("Username:");
        String username = scanner.nextLine();
        System.out.print("Email:");
        String email = scanner.nextLine();
        try
        {
            userService.addUser(firstName, lastName, username, email);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void delete()
    {
        System.out.print("Username:");
        String username = scanner.nextLine();
        try
        {
            userService.deleteUser(username);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void update()
    {
        System.out.print("First name:");
        String firstName = scanner.nextLine();
        System.out.print("Last name:");
        String lastName = scanner.nextLine();
        System.out.print("Username:");
        String username = scanner.nextLine();
        System.out.print("Email:");
        String email = scanner.nextLine();
        try
        {
            userService.updateUser(firstName, lastName, username, email);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void showUsers()
    {
        for (String user : userService.getUsers())
        {
            System.out.println(user);
        }
    }

}
