package ch.beerpro.presentation.details.addprice;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.beerpro.R;
import ch.beerpro.domain.models.Beer;
import pl.tajchert.nammu.Nammu;

public class AddPriceActivity extends AppCompatActivity {
    public static final String ITEM = "item";
    private static final String TAG = "AddPriceActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.priceInput)
    EditText priceInput;

    @BindView(R.id.currencyText)
    TextView currencyText;

    private AddPriceViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);
        ButterKnife.bind(this);
        Nammu.init(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_activity_price));

        Beer item = (Beer) getIntent().getExtras().getSerializable(ITEM);

        model = ViewModelProviders.of(this).get(AddPriceViewModel.class);
        model.setItem(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_price_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                savePrice();
                return true;
            case android.R.id.home:
                if (getParentActivityIntent() == null) {
                    onBackPressed();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void savePrice() {
        if (!allFieldsFilledOutCorrectly()) {
            return;
        }
        float price = Float.parseFloat(priceInput.getText().toString());
        String currency = currencyText.getText().toString();
        // TODO show a spinner!
        // TODO return the new rating to update the new average immediately
        model.savePrice(model.getItem(), price, currency)
                .addOnSuccessListener(task -> onBackPressed())
                .addOnFailureListener(error -> Log.e(TAG, "Could not save price", error));
    }

    private boolean allFieldsFilledOutCorrectly() {
        if (TextUtils.isEmpty(priceInput.getText())) {
            Toast.makeText(getApplicationContext(), "Geben Sie bitte einen Betrag an", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Float.parseFloat(priceInput.getText().toString()) <= 0) {
            Toast.makeText(getApplicationContext(), "Ungültiger Betrag", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
