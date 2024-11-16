package internal.andreiva.socialnetwork.gui;

import internal.andreiva.socialnetwork.service.Service;

import java.util.Optional;

public class GuiController
{
    protected Service service;
    public void setService(Service service)
    {
        this.service = service;
    }
    public void setSomething(Optional<Object> parameter)  {}
}
