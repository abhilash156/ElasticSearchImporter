import components.ElasticImporter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ElasticSearchImporter {

    private ElasticImporter importer;
    public ElasticSearchImporter() {

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");
        importer = (ElasticImporter) applicationContext.getBean("elasticImporter");
    }

    public void addDocumentsFromJSONFile(String index, String type, String filePath) {
        getImporter().addDocumentsFromJSONFile(index, type, filePath);
    }

    private ElasticImporter getImporter() {
        return importer;
    }
}
