package ch.beerpro.data.repositories;

import androidx.lifecycle.LiveData;
import ch.beerpro.domain.models.*;
import ch.beerpro.domain.utils.Quadruple;

import java.util.*;

import static androidx.lifecycle.Transformations.map;
import static ch.beerpro.domain.utils.LiveDataExtensions.combineLatest;

public class MyBeersRepository {

    private static List<MyBeer> getMyBeers(Quadruple<List<Wish>, List<Rating>, List<Price>, HashMap<String, Beer>> input) {
        List<Wish> wishlist = input.getFirst();
        List<Rating> ratings = input.getSecond();
        List<Price> prices = input.getThird();
        HashMap<String, Beer> beers = input.getFourth();

        ArrayList<MyBeer> result = new ArrayList<>();
        Set<String> beersAlreadyOnTheList = new HashSet<>();
        for (Wish wish : wishlist) {
            String beerId = wish.getBeerId();
            result.add(new MyBeerFromWishlist(wish, beers.get(beerId)));
            beersAlreadyOnTheList.add(beerId);
        }

        for (Rating rating : ratings) {
            String beerId = rating.getBeerId();
            if (beersAlreadyOnTheList.contains(beerId)) {
                // if the beer is already on the wish list, don't add it again
            } else {
                result.add(new MyBeerFromRating(rating, beers.get(beerId)));
                // we also don't want to see a rated beer twice
                beersAlreadyOnTheList.add(beerId);
            }
        }

        for (Price price : prices) {
            String beerId = price.getBeerId();
            if (beersAlreadyOnTheList.contains(beerId)) {
                // if the beer is already on the wish list, don't add it again
            } else {
                result.add(new MyBeerFromPrice(price, beers.get(beerId)));
                // we also don't want to see a rated beer twice
                beersAlreadyOnTheList.add(beerId);
            }
        }
        Collections.sort(result, (r1, r2) -> r2.getDate().compareTo(r1.getDate()));
        return result;
    }


    public LiveData<List<MyBeer>> getMyBeers(LiveData<List<Beer>> allBeers, LiveData<List<Wish>> myWishlist,
                                             LiveData<List<Rating>> myRatings, LiveData<List<Price>> myPrices) {
        return map(combineLatest(myWishlist, myRatings, myPrices, map(allBeers, Entity::entitiesById)),
                MyBeersRepository::getMyBeers);
    }

}
