package ch.beerpro.domain.models;

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

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }
}
