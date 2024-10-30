package internal.andreiva.socialnetwork.repository.file;

import internal.andreiva.socialnetwork.domain.Entity;

import java.io.*;
import java.util.Map;
import java.util.UUID;

/**
 * Abstract class for a repository that saves data in a file
 * @param <E> - type of entities saved in repository
 */
public abstract class AbstractFileRepository<E extends Entity>
{
    protected String filename;

    /**
     * Constructor
     * @param fileName - the name of the file to load data from
     */
    public AbstractFileRepository(String fileName)
    {
        this.filename = fileName;
    }

    /**
     * Create an entity from a line
     * @param line - the line to parse
     * @return the entity created or null if an error occurred
     */
    public abstract E createEntity(String line);

    /**
     * Save an entity to a string
     * @param entity - the entity to save
     * @return the string representation of the entity
     */
    public abstract String saveEntity(E entity);

    /**
     * Load data from the file
     * @return a map of entities loaded from the file
     */
    public Map<UUID, E> loadData()
    {
        Map<UUID, E> entities = new java.util.HashMap<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = reader.readLine()) != null){
                E entity = createEntity(line);
                entities.put(entity.getId(), entity);
            }
            return entities;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Save data to the file
     * @param entities - the entities to save
     */
    public void saveData(Map<UUID, E> entities)
    {
        try  ( BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            entities.values().forEach(entity -> {
                String ent = saveEntity(entity);
                try
                {
                    writer.write(ent);
                    writer.newLine();
                } catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
                });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
