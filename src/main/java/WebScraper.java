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
        String[] arr = new String[6];
        try {
            Document doc = Jsoup.connect(url).get();
            String div = "div.specs-content";
            if (doc.select(div).isEmpty()) {
//                System.out.println("DNE");
                div = "div.css-hvcer9 e164kvlk2";
            }

            for (Element row : doc.select(div)) {
                String line = row.text();
                String[] words = line.split(" ");
                for (int i = 0; i < words.length; i++) {
                    switch (words[i]) {
                        case "Power:":
                            arr[0] = words[i + 1];
                            break;
                        case "Power":
                            arr[0] = words[i + 1];
                        case "Torque:":
                            arr[1] = words[i + 1];
                        case "Weight:":
                            arr[2] = words[i + 1];
                            break;
                        case "60":
                            arr[3] = words[i + 2];
                            break;
                        case "5???60":
                            arr[4] = words[i + 2];
                            break;
                        case "1/4-Mile:":
                            arr[5] = words[i + 1];
                            break;
                        case "1/4":
                            arr[5] = words[i + 2];
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

}
