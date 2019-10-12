package ch.beerpro.presentation.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;

import java.util.List;

import ch.beerpro.data.repositories.BeersRepository;
import ch.beerpro.data.repositories.CurrentUser;
import ch.beerpro.data.repositories.LikesRepository;
import ch.beerpro.data.repositories.RatingsRepository;
import ch.beerpro.data.repositories.WishlistRepository;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Rating;
import ch.beerpro.domain.models.Wish;

public class DetailsViewModel extends ViewModel implements CurrentUser {

    private final MutableLiveData<String> beerId = new MutableLiveData<>();
    private final LiveData<Beer> beer;
    private final LiveData<List<Rating>> ratings;
    private final LiveData<List<Rating>> myRatings;
    private final LiveData<Wish> wish;

    private final LikesRepository likesRepository;
    private final WishlistRepository wishlistRepository;

    public DetailsViewModel() {
        // TODO We should really be injecting these!
        BeersRepository beersRepository = new BeersRepository();
        RatingsRepository ratingsRepository = new RatingsRepository();
        likesRepository = new LikesRepository();
        wishlistRepository = new WishlistRepository();

        MutableLiveData<String> currentUserId = new MutableLiveData<>();
        beer = beersRepository.getBeer(beerId);
        wish = wishlistRepository.getMyWishForBeer(currentUserId, getBeer());
        ratings = ratingsRepository.getRatingsForBeer(beerId);
        myRatings = ratingsRepository.getMyRatingsForBeer(currentUserId, beerId);
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
}