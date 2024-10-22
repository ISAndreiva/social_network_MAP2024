package internal.andreiva.socialnetwork.domain.validator;

import internal.andreiva.socialnetwork.domain.User;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * User validator
 */
public class UserValidator implements Validator<User>
{
    @Override
    public void validate(User entity) throws ValidationException
    {
        if (entity.getId() == null)
        {
            throw new ValidationException("ID must not be null.");
        }
        if (entity.getFirstName().isEmpty() || entity.getFirstName().matches(".*[0-9].*"))
        {
            throw new ValidationException("First name must not be empty and must not contain digits.");
        }
        if (entity.getLastName().isEmpty() || entity.getLastName().matches(".*[0-9].*"))
        {
            throw new ValidationException("Last name must not be empty and must not contain digits.");
        }
        if (entity.getUsername().isEmpty())
        {
            throw new ValidationException("Username must not be empty.");
        }
        //this is overkill but why not
        if (!EmailValidator.getInstance().isValid(entity.getEmail()))
        {
            throw new ValidationException("Email is not valid.");
        }

    }
    private UserValidator() {}
    static UserValidator instance = new UserValidator();
    public static UserValidator getInstance()
    {
        return instance;
    }

}
