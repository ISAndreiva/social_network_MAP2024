package internal.andreiva.socialnetwork;

import internal.andreiva.socialnetwork.domain.validator.UserValidator;
import internal.andreiva.socialnetwork.repository.UserRepository;
import internal.andreiva.socialnetwork.service.UserService;
import internal.andreiva.socialnetwork.ui.CLI;

public class app
{
    public void run()
    {
        UserRepository userRepository = new UserRepository("./data/users.txt");
        UserService userService = new UserService(userRepository, new UserValidator());
        CLI cli = new CLI(userService);
        cli.run();
    }
}
