package internal.andreiva.socialnetwork.domain.validator;

public interface Validator<T> {
    /**
     * Validates an entity
     * @param entity - the entity to validate
     * @throws ValidationException if the entity is not valid
     */
    void validate(T entity) throws ValidationException;
}