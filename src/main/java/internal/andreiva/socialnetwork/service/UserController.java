package internal.andreiva.socialnetwork.service;


import internal.andreiva.socialnetwork.domain.User;
import internal.andreiva.socialnetwork.domain.validator.UserValidator;
import internal.andreiva.socialnetwork.repository.FileMemoRepo;

import java.util.UUID;

public class UserController
{
    private FileMemoRepo<User> userRepository;
    private UserValidator userValidator;

    public UserController(FileMemoRepo<User> userRepository, UserValidator userValidator)
    {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    public void addUser(String firstName, String lastName, String username, String email)
    {
        if (checkUserExists(username) != null)
        {
            throw new ServiceException("User already exists");
        }
        User u = new User(firstName, lastName, username, email);
        u.setId(UUID.randomUUID());
        userValidator.validate(u);
        if (userRepository.save(u) != null)
        {
            throw new ServiceException("An error occurred adding the user");
        }
    }

    public User deleteUser(String username)
    {
        UUID id = checkUserExists(username);
        if (id == null)
        {
            throw new ServiceException("User does not exist");
        }
        User user = userRepository.delete(id);
        if (user == null)
        {
            throw new ServiceException("An error occurred deleting the user");
        }
        return user;
    }

    public void updateUser(String firstName, String lastName, String username, String email)
    {
        UUID id = checkUserExists(username);
        if (id == null)
        {
            throw new ServiceException("User does not exist");
        }
        User u = new User(firstName, lastName, username, email);
        u.setId(id);
        userValidator.validate(u);
        if (userRepository.update(u) != null)
        {
            throw new ServiceException("An error occurred updating the user");
        }
    }

    public String[] getUsers()
    {
        String[] users = new String[(int)userRepository.findAll().spliterator().getExactSizeIfKnown()];
        int i = 0;
        for (User u : userRepository.findAll())
        {
            users[i] = u.toString();
            i++;
        }
        return users;
    }

    public Iterable<User> getUsersIterable()
    {
        return userRepository.findAll();
    }

    public User getUser(UUID id)
    {
        for (User u : userRepository.findAll())
        {
            if (u.getId().equals(id))
            {
                return u;
            }
        }
        return null;
    }

    /**
     * @param username username to look for
     * @return UUID of user if it exists, null otherwise
     */
    public UUID checkUserExists(String username)
    {
        for (User u : userRepository.findAll())
        {
            if (u.getUsername().equals(username))
            {
                return u.getId();
            }
        }
        return null;
    }

}
