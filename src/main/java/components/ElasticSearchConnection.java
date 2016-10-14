package components;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;


@Component
public class ElasticSearchConnection {
    @Value("${elasticsearch.cluster}")
    private String clusterExportStaging;
    @Value("${elasticsearch.address}")
    private String clusterAddress;
    @Value("${elasticsearch.tcp.port}")
    private int clusterPort;

    private static Client client = null;

    ElasticSearchConnection() {
    }

    private void createClient() {
        try {
            Settings settings = Settings.settingsBuilder().put("cluster.name", clusterExportStaging).build();
            System.out.println("Trying to Connect to ElasticSearch");
            client = TransportClient.builder().settings(settings).build().addTransportAddress
                    (new InetSocketTransportAddress(InetAddress.getByName(clusterAddress), clusterPort));
            System.out.println("Connected to ElasticSearch");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void closeClient() {
        client.close();
    }

    public Client getClient() {
        if (client == null) {
            createClient();
        }
        return client;
    }
}
