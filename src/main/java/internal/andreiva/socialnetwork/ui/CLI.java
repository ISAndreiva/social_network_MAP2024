package internal.andreiva.socialnetwork.ui;

import internal.andreiva.socialnetwork.service.Service;

import java.util.Scanner;

public class CLI
{
    Service service;
    Scanner scanner = new Scanner(System.in);
    public CLI(Service service)
    {
        this.service = service;
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
                case "add_friendship":
                    add_friendship();
                    break;
                case "delete_friendship":
                    delete_friendship();
                    break;
                case "show_friends":
                    show_friends();
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
        System.out.println("add_friendship - Add a friendship");
        System.out.println("delete_friendship - Delete a friendship");
        System.out.println("show_friends - Show all friends of a user");
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
            service.addUser(firstName, lastName, username, email);
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
            service.deleteUser(username);
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
            service.updateUser(firstName, lastName, username, email);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void showUsers()
    {
        for (String user : service.getUsers())
        {
            System.out.println(user);
        }
    }

    private void add_friendship()
    {
        System.out.print("Username 1:");
        String username1 = scanner.nextLine();
        System.out.print("Username 2:");
        String username2 = scanner.nextLine();
        try
        {
            service.addFriendship(username1, username2);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void delete_friendship()
    {
        System.out.print("Username 1:");
        String username1 = scanner.nextLine();
        System.out.print("Username 2:");
        String username2 = scanner.nextLine();
        try
        {
            service.deleteFriendship(username1, username2);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void show_friends()
    {
        System.out.print("Username:");
        String username = scanner.nextLine();
        for (String friend : service.getFriends(username))
        {
            System.out.println(friend);
        }
    }



}
