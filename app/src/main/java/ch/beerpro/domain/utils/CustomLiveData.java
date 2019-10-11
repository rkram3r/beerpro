package ch.beerpro.domain.utils;

import android.util.Pair;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

//Source: https://stackoverflow.com/questions/49493772/mediatorlivedata-or-switchmap-transformation-with-multiple-parameters
public class CustomLiveData extends MediatorLiveData<Pair<String, String>> {
    public CustomLiveData(LiveData<String> code, LiveData<String> nbDays) {
        addSource(code, new Observer<String>() {
            public void onChanged(@Nullable String first) {
                setValue(Pair.create(first, nbDays.getValue()));
            }
        });
        addSource(nbDays, new Observer<String>() {
            public void onChanged(@Nullable String second) {
                setValue(Pair.create(code.getValue(), second));
            }
        });
    }
}
