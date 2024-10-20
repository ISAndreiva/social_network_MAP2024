package internal.andreiva.socialnetwork.repository.file;

import internal.andreiva.socialnetwork.domain.Entity;

import java.io.*;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractFileRepository<E extends Entity>
{
    protected String filename;

    public AbstractFileRepository(String fileName)
    {
        this.filename = fileName;
    }

    public abstract E createEntity(String line);
    public abstract String saveEntity(E entity);

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

    public void saveData(Map<UUID, E> entities)
    {
        try  ( BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            for (E entity: entities.values()) {
                String ent = saveEntity(entity);
                writer.write(ent);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
