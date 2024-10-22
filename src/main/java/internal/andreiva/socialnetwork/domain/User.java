package internal.andreiva.socialnetwork.domain;

import java.util.Objects;

/**
 * Object that defines a user
 */
public class User extends Entity
{
    private String firstName;
    private String lastName;
    private String username;
    private String email;

    /**
     * Constructor
     * @param firstName - the first name
     * @param lastName - the last name
     * @param username - the username
     * @param email - the email address
     */
    public User(String firstName, String lastName, String username, String email)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
    }

    /**
     * Get the first name
     * @return the first name
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Set the first name
     * @param firstName - the first name
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * Get the last name
     * @return the last name
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Set the last name
     * @param lastName - the last name
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * Get the username
     * @return the username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Set the username
     * @param username - the username
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Get the email address
     * @return the email address
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * Set the email address
     * @param email - the email address
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return getFirstName().equals(user.getFirstName()) &&
                getLastName().equals(user.getLastName()) &&
                getUsername().equals(user.getUsername()) &&
                getEmail().equals(user.getEmail());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getFirstName(), getLastName(), getUsername(), getEmail());
    }
}
