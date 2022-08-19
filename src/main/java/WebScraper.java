import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class WebScraper {

    public static void main (String[] args) {

        // read a csv file that contains URLs
        try {
            CSVReader csvReader = new CSVReader(new FileReader("src/main/resources/URLs.csv"));
            File output = new File("src/main/resources/output.csv");
            CSVWriter writer = new CSVWriter(new FileWriter(output));
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                // extract data from URLs
                String[] name = nextLine[0].split(" ");
                String[] specs = getData(nextLine[1]);
                String[] outArr = new String[name.length + specs.length];
                System.arraycopy(name, 0, outArr, 0, name.length);
                System.arraycopy(specs, 0, outArr, name.length, specs.length);
                writer.writeNext(outArr);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] getData(String url) {
        String[] arr = new String[5];
        try {
            Document doc = Jsoup.connect(url).get();

            for (Element row : doc.select("div.specs-content")) {
                String line = row.text();
                String[] words = line.split(" ");
                for (int i = 0; i < words.length; i++) {
                    switch (words[i]) {
                        case "Power:":
                            arr[0] = words[i + 1];
                            break;
                        case "Weight:":
                            arr[1] = words[i + 1];
                            break;
                        case "60":
                            arr[2] = words[i + 2];
                            break;
                        case "5–60":
                            arr[3] = words[i + 2];
                            break;
                        case "1/4-Mile:":
                            arr[4] = words[i + 1];
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

}
