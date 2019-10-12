package ch.beerpro.presentation.details.addprice;

import androidx.lifecycle.ViewModel;

import ch.beerpro.domain.models.Beer;

public class AddPriceViewModel extends ViewModel {
    private static final String TAG = "AddPriceViewModel";
    private Beer item;

    public Beer getItem() {
        return item;
    }

    public void setItem(Beer item) {
        this.item = item;
    }
}
