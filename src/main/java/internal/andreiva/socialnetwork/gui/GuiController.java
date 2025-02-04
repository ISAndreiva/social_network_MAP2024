package internal.andreiva.socialnetwork.gui;

import internal.andreiva.socialnetwork.service.Service;


public class GuiController<P>
{
    protected Service service;
    public void setService(Service service)
    {
        this.service = service;
    }
    public void setSomething(P parameter) {}
}
