package internal.andreiva.socialnetwork;

import internal.andreiva.socialnetwork.service.Service;
import internal.andreiva.socialnetwork.ui.CLI;

public class app
{
    public void run()
    {
        Service service = new Service("./data/users.txt", "./data/friendships.txt");
        CLI cli = new CLI(service);
        cli.run();
    }
}
