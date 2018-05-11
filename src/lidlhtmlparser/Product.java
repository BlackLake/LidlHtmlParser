/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lidlhtmlparser;

/**
 *
 * @author blacklake
 */
public class Product {
    
    String name;
    String price;
    String imageURL;

    public Product() {
        
    }

    public Product(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public Product(String name, String price, String imageURL) {
        this.name = name;
        this.price = price;
        this.imageURL = imageURL;
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    
}
