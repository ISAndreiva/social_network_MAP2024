package internal.andreiva.socialnetwork.service;


import internal.andreiva.socialnetwork.domain.User;
import internal.andreiva.socialnetwork.domain.validator.UserValidator;
import internal.andreiva.socialnetwork.repository.FileMemoRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

/**
 * Controller for user operations
 */
public class UserController
{
    private FileMemoRepo<User> userRepository;
    private UserValidator userValidator;

    /**
     * Constructor
     * @param userRepository repository for users
     * @param userValidator validator for users
     */
    public UserController(FileMemoRepo<User> userRepository, UserValidator userValidator)
    {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    /**
     * Add a user
     * @param firstName first name
     * @param lastName last name
     * @param username username
     * @param email email
     */
    public void addUser(String firstName, String lastName, String username, String email)
    {
        if (checkUserExists(username) != null)
        {
            throw new ServiceException("User already exists");
        }
        User u = new User(firstName, lastName, username, email);
        u.setId(UUID.randomUUID());
        userValidator.validate(u);
        if (userRepository.save(u).isPresent())
        {
            throw new ServiceException("An error occurred adding the user");
        }
    }

    /**
     * Delete a user
     * @param username username
     * @return the deleted user
     */
    public User deleteUser(String username)
    {
        UUID id = checkUserExists(username);
        if (id == null)
        {
            throw new ServiceException("User does not exist");
        }
        Optional<User> user = userRepository.delete(id);
        if (user.isEmpty())
        {
            throw new ServiceException("An error occurred deleting the user");
        }
        return user.get();
    }

    /**
     * Update a user
     * @param firstName the new first name
     * @param lastName the new last name
     * @param username the existing username
     * @param email the new email
     */
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
        if (userRepository.update(u).isPresent())
        {
            throw new ServiceException("An error occurred updating the user");
        }
    }

    /**
     * Get all users
     * @return array of users
     */
    public List<String> getUsers()
    {
        List<String> users = new ArrayList<>();
        userRepository.findAll().forEach(u -> users.add(u.toString()));
        return users;
    }

    /**
     * Returns an iterable of all users
     */
    public Iterable<User> getUsersIterable()
    {
        return userRepository.findAll();
    }

    /**
     * Get a user by id
     * @param id id of user
     * @return user if the id is valid, null otherwise
     */
    public User getUser(UUID id)
    {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false).filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Check if a user exists
     * @param username username to look for
     * @return UUID of user if it exists, null otherwise
     */
    public UUID checkUserExists(String username)
    {
        //userRepository.findAll(). .filter(p -> {p})
        var user = StreamSupport.stream(userRepository.findAll().spliterator(), false).filter(p -> p.getUsername().equals(username)).findFirst().orElse(null);
        if (user != null)
            return user.getId();
        else
            return null;
    }

}
