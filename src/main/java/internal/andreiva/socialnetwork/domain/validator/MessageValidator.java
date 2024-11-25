package internal.andreiva.socialnetwork.domain.validator;

import internal.andreiva.socialnetwork.domain.Message;

public class MessageValidator implements Validator<Message>
{
    @Override
    public void validate(Message entity) throws ValidationException
    {
        if (entity.getId() == null)
        {
            throw new ValidationException("ID must not be null.");
        }
        if (entity.getSender() == null)
        {
            throw new ValidationException("Sender must not be null.");
        }
        if (entity.getDate() == null)
        {
            throw new ValidationException("Date must not be null.");
        }
        if (entity.getText().isEmpty())
        {
            throw new ValidationException("Text must not be empty.");
        }
    }
    private MessageValidator() {}
    private static final MessageValidator instance = new MessageValidator();
    public static MessageValidator getInstance()
    {
        return instance;
    }
}
