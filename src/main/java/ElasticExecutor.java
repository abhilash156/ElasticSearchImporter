import java.util.Date;

public class ElasticExecutor {
    public static void main(String[] args) {
        ElasticSearchImporter importer = new ElasticSearchImporter();
        System.out.println(new Date());
        Long startTime = new Date().getTime();
        for(int i = 1; i <= 10; i++) {
            String filePath = "C:\\Users\\CS62\\Downloads\\AccountData\\Account25_" + i + ".json";
            importer.addDocumentsFromJSONFile("bank", "account", filePath);
        }
        System.out.println(new Date());
        Long endTime = new Date().getTime();
        System.out.println("TotalTime: " + (endTime - startTime) / 1000 + " seconds");
    }
}
