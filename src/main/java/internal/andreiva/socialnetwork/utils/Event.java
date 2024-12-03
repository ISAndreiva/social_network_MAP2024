package internal.andreiva.socialnetwork.utils;

public class Event
{
    private EventType type;

    public Event(EventType type)
    {
        this.type = type;
    }

    public EventType getType()
    {
        return type;
    }
}
