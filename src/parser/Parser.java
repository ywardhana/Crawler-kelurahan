package parser;

import com.esotericsoftware.yamlbeans.YamlConfig;
import com.esotericsoftware.yamlbeans.YamlWriter;
import corpus.Kelurahan;
import java.io.FileWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.IOException;
import java.text.Normalizer;
import java.util.HashSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Yulistiyan Wardhana
 */
public class Parser {

    public static void main(String args[]) throws IOException, InterruptedException {
        Kelurahan daftarKelurahan = new Kelurahan();

        String[] kata_kunci = {"a", "i", "u", "e", "o"};
        String item = "a";
        String ex = "aiueo";
        for (int i = 0; i < 26; i++) {
            while (true) {
                System.out.println(item);
                if (!ex.contains(item)) {
                    try {
                        Document doc = Jsoup.connect("http://mfdonline.bps.go.id/index.php?link=hasil_pencarian")
                                .data("pilihcari", "desa")
                                .data("kata_kunci", item).maxBodySize(0)
                                // and other hidden fields which are being passed in post request.
                                .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36").timeout(900000000)
                                .post();

                        Element isi = doc.selectFirst("div.entry");
                        Elements datas = isi.select("tr.table_content");

                        for (Element data : datas) {
                            Elements elements = data.select("td[width=100]");
                            if (!elements.isEmpty()) {
                                daftarKelurahan.put(normalize(elements.get(0).text()),
                                        normalize(elements.get(1).text()),
                                        normalize(elements.get(2).text()),
                                        normalize(elements.get(3).text()));
                            } else {
                                System.out.println(data);
                            }
                        }
                        TimeUnit.SECONDS.sleep(30);
                    } catch (org.jsoup.UncheckedIOException e) {
                        continue;
                    } catch (Exception e) {
                        continue;
                    }
                }
                break;
            }
            item = Character.toString((char) (item.toCharArray()[0] + 1));
        }

        YamlWriter writer = new YamlWriter(new FileWriter("output.yml"));
        writer.getConfig().writeConfig.setWriteClassname(YamlConfig.WriteClassName.NEVER);
        writer.write(daftarKelurahan.getData());
        writer.close();

    }

    private static String normalize(String str) {
        String subjectString = str.replace(" </td>", "");
        subjectString = Normalizer.normalize(subjectString, Normalizer.Form.NFD);
        String resultString = subjectString.replaceAll("[^\\x00-\\x7F]", "");
        return resultString;
    }

    private static String getLabel(int i) {
        switch (i) {
            case 0:
                return "Provinsi";
            case 1:
                return "Kota";
            case 2:
                return "Kecamatan";
            default:
                return "Kelurahan";
        }
    }
}
