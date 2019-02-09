package acceler.ocdl.service;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultDatabaseService implements DatabaseService {

    private final MongoDatabase mongoDatabase;
    private final MongoCollection<Document> collection;


    public DefaultDatabaseService() {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        mongoDatabase = mongoClient.getDatabase("Oneclick");
        collection = mongoDatabase.getCollection("templates");
    }

    @Override
    public List<String> getTemplatesList() {

        List<String> templatesList = new ArrayList<>();
        
        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        while (mongoCursor.hasNext()) {
            templatesList.add(mongoCursor.next().get("name").toString());
        }

        return templatesList;
    }


    @Override
    public Map<String, String> getTemplates(List<String> ids) {

        Map<String, String> templates = new HashMap<>();

        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        while (mongoCursor.hasNext()) {

            Document next = mongoCursor.next();

            if (ids.contains(next.get("ID").toString())) {
                templates.put(next.get("code").toString(), next.get("descrp").toString());
            }
        }

        return templates;
    }
}
