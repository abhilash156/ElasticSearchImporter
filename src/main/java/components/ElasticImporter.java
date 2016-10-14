package components;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

@Component
public class ElasticImporter {
    @Autowired
    ElasticSearchConnection elasticSearchConnection;

    private Client client;

    ElasticImporter() {
    }


    private Client getClient() {
        if (client == null) {
            client = elasticSearchConnection.getClient();
        }
        return client;
    }

    public boolean addDocumentsFromJSONFile(String index, String type, String filePath) {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = null;
        try {
            Reader in = new FileReader(filePath);
            jsonArray = (JSONArray) jsonParser.parse(in);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        if (jsonArray != null) {
            IndexRequest indexRequest;
            BulkRequestBuilder bulkRequestBuilder = getClient().prepareBulk();
            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;
                indexRequest = new IndexRequest(index, type);
                indexRequest.source(jsonObject.toJSONString());
                bulkRequestBuilder.add(indexRequest);
            }
            BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
            if (!bulkResponse.hasFailures()) {
                return true;
            } else {
                System.out.println(bulkResponse.buildFailureMessage());
                BulkItemResponse bulkItemResponse[] = bulkResponse.getItems();
                for (BulkItemResponse bulkItemResponseTemp : bulkItemResponse) {
                    System.out.println(bulkItemResponseTemp.getFailureMessage());
                }
                return false;
            }
        }
        return false;
    }
}
