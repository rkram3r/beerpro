package ch.beerpro.presentation.details.addprice;

import android.util.Log;
import androidx.lifecycle.ViewModel;

import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Price;
import ch.beerpro.presentation.utils.EntityClassSnapshotParser;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class AddPriceViewModel extends ViewModel {
    private static final String TAG = "AddPriceViewModel";
    private EntityClassSnapshotParser<Price> parser = new EntityClassSnapshotParser<>(Price.class);
    private Beer item;

    public Beer getItem() {
        return item;
    }

    public void setItem(Beer item) {
        this.item = item;
    }

    public Task<Price> savePrice(Beer item, float price, String currency){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;

        Price newPrice = new Price(null, item.getId(), item.getName(), user.getUid(), user.getDisplayName(),price, currency, new Date());
        Log.i(TAG, "Adding new price: " + newPrice.toString());

        return FirebaseFirestore.getInstance().collection("prices").add(newPrice).continueWithTask(task -> {
            if (task.isSuccessful()) {
                return task.getResult().get();
            } else {
                throw task.getException();
            }
        }).continueWithTask(task -> {

            if (task.isSuccessful()) {
                return Tasks.forResult(parser.parseSnapshot(task.getResult()));
            } else {
                throw task.getException();
            }
        });
    }
}
