package ch.beerpro.domain.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class MyBeerFromPrice implements MyBeer {
    private Price price;
    private Beer beer;

    public MyBeerFromPrice(Price price, Beer beer) {
        this.price = price;
        this.beer = beer;
    }

    @Override
    public String getBeerId() {
        return price.getBeerId();
    }

    @Override
    public Date getDate() {
        return price.getCreationDate();
    }

    public Price getPrice() {
        return this.price;
    }

    public Beer getBeer() {
        return this.beer;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof MyBeerFromPrice)) return false;
        final MyBeerFromPrice other = (MyBeerFromPrice) o;
        if (!other.canEqual(this)) return false;
        final Object this$price = this.getPrice();
        final Object other$price = other.getPrice();
        if (this$price == null ? other$price != null : !this$price.equals(other$price))
            return false;
        final Object this$beer = this.getBeer();
        final Object other$beer = other.getBeer();
        return this$beer == null ? other$beer == null : this$beer.equals(other$beer);
    }

    private boolean canEqual(final Object other) {
        return other instanceof MyBeerFromPrice;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $price = this.getPrice();
        result = result * PRIME + ($price == null ? 43 : $price.hashCode());
        final Object $beer = this.getBeer();
        result = result * PRIME + ($beer == null ? 43 : $beer.hashCode());
        return result;
    }

    @NonNull
    public String toString() {
        return "MyBeerFromPrice(price=" + this.getPrice() + ", beer=" + this.getBeer() + ")";
    }
}
