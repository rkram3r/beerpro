package ch.beerpro.data.repositories;

import androidx.lifecycle.LiveData;
import ch.beerpro.domain.models.Price;
import ch.beerpro.domain.utils.FirestoreQueryLiveDataArray;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import static androidx.lifecycle.Transformations.switchMap;

public class PriceRepository {
    private final FirestoreQueryLiveDataArray<Price> allPrices = new FirestoreQueryLiveDataArray<>(
            FirebaseFirestore.getInstance().collection(Price.COLLECTION)
                    .orderBy(Price.FIELD_CREATION_DATE, Query.Direction.DESCENDING), Price.class);

    public static LiveData<List<Price>> getPricesByBeer(String beerId) {
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance().collection(Price.COLLECTION)
                .orderBy(Price.FIELD_CREATION_DATE, Query.Direction.DESCENDING)
                .whereEqualTo(Price.FIELD_BEER_ID, beerId), Price.class);
    }

    public LiveData<List<Price>> getAllPrices() {
        return allPrices;
    }

    public LiveData<List<Price>> getPricesForBeer(LiveData<String> beerId) {
        return switchMap(beerId, PriceRepository::getPricesByBeer);
    }
}
