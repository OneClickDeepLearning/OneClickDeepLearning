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

    private List<String> templatesList;
    private Map<String, String> templates;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> collection;


    public DefaultDatabaseService(){
        templatesList = new ArrayList<>();
        templates = new HashMap<>();


        mongoClient = new MongoClient( "localhost" , 27017 );
        mongoDatabase = mongoClient.getDatabase("Oneclick");
        collection = mongoDatabase.getCollection("templates");

    }

    @Override
    public List<String> getTemplatesList() {

        synchronized (this) {
            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            while (mongoCursor.hasNext()) {
                templatesList.add(mongoCursor.next().get("name").toString());
            }
        }
        return templatesList;
    }


    @Override
    public Map<String, String> getTemplates(List<String> ids) {
        return null;
    }
}
