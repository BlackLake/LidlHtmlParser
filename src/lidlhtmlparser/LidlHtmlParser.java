/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lidlhtmlparser;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author blacklake
 */
public class LidlHtmlParser {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, SQLException, Exception {
        ArrayList<Product> products = GetProducts();
        GetImages(products);

        int counter = 0;
        for (Product product : products) {
            System.out.println(product.name + "\t" + product.price + "\t" + product.getImageURL());
            counter++;
        }
        System.out.println(counter + " Products");

        String dbURL = "jdbc:mysql://localhost:3306/Price_Tracker?characterEncoding=utf8";
        String username = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(dbURL, username, password)) {
            if (conn != null) {
                System.out.println("Connected");

                String sql = "TRUNCATE Lidl_Temp";

                PreparedStatement statement = conn.prepareStatement(sql);

                try {
                    statement.executeUpdate();
                    System.out.println("All products was deleted successfully!");
                } catch (Exception e) {
                    System.out.println("Could not Truncate!");
                }

                for (Product product : products) {

                    sql = "INSERT INTO Lidl_Temp (name, price, imgURL) VALUES (?, ?, ?)";

                    statement = conn.prepareStatement(sql);
                    statement.setString(1, product.getName());
                    statement.setString(2, product.getPrice());
                    statement.setString(3, product.getImageURL());

                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("New product was inserted successfully!");
                    }

                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    private static ArrayList<Product> GetProducts() throws IOException {
        Document doc = (Document) Jsoup.connect("https://www.lidl.lt/lt/Pasiulymai-35.htm?id=32&week=1").get();

        ArrayList<Product> products = new ArrayList<>();

        Elements newsHeadlines = doc.select("li.productgrid__item");

        for (Element headline : newsHeadlines) {
            String name = headline.attr("data-name");
            if ("".equals(headline.attr("data-price"))) {
                continue;
            }
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.GERMANY);
            float price = Float.parseFloat(headline.attr("data-price"));
            String formattedPrice = formatter.format(price / 100);

            Product product = new Product(name, formattedPrice);
            products.add(product);
        }

        return products;
    }

    private static void GetImages(ArrayList<Product> products) throws IOException {
        Document doc = (Document) Jsoup.connect("https://www.lidl.lt/lt/Pasiulymai-35.htm?id=32&week=1").get();

        Elements newsHeadlines = doc.select("div.picture img");
        for (Element headline : newsHeadlines) {
            String name = headline.attr("alt");
            for (Product p : products) {
                if (p.name.equals(name)) {
                    String srcSet = headline.attr("data-srcset");
                    String[] parts = srcSet.split(",");
                    String src = srcSet.split(",")[parts.length - 1];
                    String Url = src.substring(0, src.length() - 5).trim();

                    p.setImageURL(Url);

                }
            }
        }
    }

}
