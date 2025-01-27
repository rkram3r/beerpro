package ch.beerpro.presentation.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.beerpro.data.repositories.*;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Price;
import ch.beerpro.domain.models.Rating;
import ch.beerpro.domain.models.Wish;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class DetailsViewModel extends ViewModel implements CurrentUser {

    private final MutableLiveData<String> beerId = new MutableLiveData<>();
    private final LiveData<Beer> beer;
    private final LiveData<List<Rating>> ratings;
    private final LiveData<List<Rating>> myRatings;
    private final LiveData<Wish> wish;
    private final LiveData<List<Price>> prices;

    private final LikesRepository likesRepository;
    private final WishlistRepository wishlistRepository;

    public DetailsViewModel() {
        // TODO We should really be injecting these!
        BeersRepository beersRepository = new BeersRepository();
        RatingsRepository ratingsRepository = new RatingsRepository();
        PriceRepository priceRepository = new PriceRepository();
        likesRepository = new LikesRepository();
        wishlistRepository = new WishlistRepository();

        MutableLiveData<String> currentUserId = new MutableLiveData<>();
        beer = beersRepository.getBeer(beerId);
        wish = wishlistRepository.getMyWishForBeer(currentUserId, getBeer());
        ratings = ratingsRepository.getRatingsForBeer(beerId);
        myRatings = ratingsRepository.getMyRatingsForBeer(currentUserId, beerId);
        prices = priceRepository.getPricesForBeer(beerId);
        currentUserId.setValue(getCurrentUser().getUid());
    }

    public LiveData<Beer> getBeer() {
        return beer;
    }

    public LiveData<Wish> getWish() {
        return wish;
    }

    public LiveData<List<Rating>> getRatings() {
        return ratings;
    }

    public LiveData<List<Rating>> getMyRatings() {
        return myRatings;
    }

    public LiveData<List<Price>> getPrices() {
        return prices;
    }

    public void setBeerId(String beerId) {
        this.beerId.setValue(beerId);
    }

    public void toggleLike(Rating rating) {
        likesRepository.toggleLike(rating);
    }

    public Task<Void> toggleItemInWishlist(String itemId) {
        return wishlistRepository.toggleUserWishlistItem(getCurrentUser().getUid(), itemId);
    }

    public float calcAvgRating(List<Rating> ratings) {
        float sum = 0;
        if (!ratings.isEmpty()) {
            for (Rating rating : ratings) {
                sum += rating.getRating();
            }
            sum /= ratings.size();
        }
        return sum;
    }

    public float calcAvgPrice(List<Price> prices) {
        float sum = 0;
        if (!prices.isEmpty()) {
            for (Price price : prices) {
                sum += price.getPrice();
            }
            sum /= prices.size();
        }
        return sum;
    }
}