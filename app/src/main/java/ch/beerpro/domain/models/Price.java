package ch.beerpro.domain.models;

import androidx.annotation.NonNull;
import com.google.firebase.firestore.Exclude;

import java.util.Date;

public class Price implements Entity {
    public static final String COLLECTION = "prices";
    public static final String FIELD_BEER_ID = "beerId";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_CREATION_DATE = "creationDate";

    @Exclude
    private String id;
    private String beerId;
    private String beerName;
    private String userId;
    private String userName;
    private float price;
    private String currency;
    private Date creationDate;

    public Price(String id, String beerId, String beerName, String userId, String userName, float price, String currency, Date creationDate){
        this.id = id;
        this.beerId = beerId;
        this.beerName = beerName;
        this.userId = userId;
        this.userName = userName;
        this.price = price;
        this.currency = currency;
        this.creationDate = creationDate;
    }

    public Price() {

    }

    public String getId(){
        return this.id;
    }

    public String getBeerId(){ return this.beerId;}

    public String getBeerName(){ return this.beerName;}

    public String getUserId(){ return this.userId;}

    public String getUserName(){ return this.userName;}

    public float getPrice() { return this.price;}

    public String getCurrency() {return this.currency;}

    public Date getCreationDate() { return this.creationDate;}

    public void setId(String id){
        this.id = id;
    }

    public void setBeerId(String beerId) {this.beerId = beerId; }

    public void setBeerName(String beerName) {this.beerName = beerName;}

    public void setUserId(String userId) {this.userId = userId;}

    public void setUserName(String userName) {this.userName = userName;}

    public void setPrice(float price) {this.price = price;}

    void setCurrency(String currency) {this.currency = currency;}

    void setCreationDate(Date creationDate) {this.creationDate = creationDate;}

    @NonNull
    public String toString() {
        return "Price(id=" + this.getId() + ", beerId=" + this.getBeerId() + ", beerName=" + this.getBeerName() + ", userId=" + this.getUserId() + ", userName=" + this.getUserName() + ", price=" + this.getPrice() + ", currency=" + this.getCurrency() + ", creationDate=" + this.getCreationDate() + ")";
    }
}
