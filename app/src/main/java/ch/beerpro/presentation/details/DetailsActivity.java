package ch.beerpro.presentation.details;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.beerpro.GlideApp;
import ch.beerpro.R;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Price;
import ch.beerpro.domain.models.Rating;
import ch.beerpro.domain.models.Wish;
import ch.beerpro.presentation.details.addprice.AddPriceActivity;
import ch.beerpro.presentation.details.createrating.CreateRatingActivity;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static ch.beerpro.presentation.utils.DrawableHelpers.setDrawableTint;

public class DetailsActivity extends AppCompatActivity implements OnRatingLikedListener {

    public static final String ITEM_ID = "item_id";
    private static final String TAG = "DetailsActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;

    @BindView(R.id.photo)
    ImageView photo;

    @BindView(R.id.avgRating)
    TextView avgRating;

    @BindView(R.id.numRatings)
    TextView numRatings;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.avgPrice)
    TextView avgPrice;

    @BindView(R.id.priceCurrency)
    TextView priceCurrency;

    @BindView(R.id.priceQuantity)
    TextView priceQuantity;

    @BindView(R.id.priceDescription)
    TextView priceDescription;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.wishlist)
    ToggleButton wishlist;

    @BindView(R.id.manufacturer)
    TextView manufacturer;

    @BindView(R.id.category)
    TextView category;

    @BindView(R.id.myRatingBar)
    RatingBar myRatingBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private RatingsRecyclerViewAdapter adapter;

    private DetailsViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        toolbar.setTitleTextColor(Color.alpha(0));

        String beerId = getIntent().getExtras().getString(ITEM_ID);

        model = ViewModelProviders.of(this).get(DetailsViewModel.class);
        model.setBeerId(beerId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RatingsRecyclerViewAdapter(this, model.getCurrentUser());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        model.getBeer().observe(this, this::updateBeer);
        model.getRatings().observe(this, this::updateRatings);
        model.getMyRatings().observe(this, this::updateMyRatings);
        model.getWish().observe(this, this::toggleWishlistView);
        model.getPrices().observe(this, this::updateAvgPrice);

        recyclerView.setAdapter(adapter);
    }

    private void addNewRating(RatingBar ratingBar) {
        Intent intent = new Intent(this, CreateRatingActivity.class);
        intent.putExtra(CreateRatingActivity.ITEM, model.getBeer().getValue());
        intent.putExtra(CreateRatingActivity.RATING, ratingBar.getRating());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, myRatingBar, "rating");
        startActivity(intent, options.toBundle());
    }

    private void addNewPrice() {
        Intent intent = new Intent(this, AddPriceActivity.class);
        intent.putExtra(AddPriceActivity.ITEM, model.getBeer().getValue());
        startActivity(intent);
    }


    @OnClick(R.id.actionsButton)
    public void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.single_bottom_sheet_dialog, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);

        Button ratingButton = dialog.findViewById(R.id.giveRating);
        ratingButton.setOnClickListener(v -> {
            addNewRating(myRatingBar);
            dialog.dismiss();
        });

        Button priceButton = dialog.findViewById(R.id.addPrice);
        priceButton.setOnClickListener(v -> {
            addNewPrice();
            dialog.dismiss();
        });

        dialog.show();
    }

    @OnClick(R.id.button2)
    public void shareBeer() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Hi Beerbro! Look at that beer:"+name.getText().toString();
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void updateBeer(Beer item) {
        name.setText(item.getName());
        manufacturer.setText(item.getManufacturer());
        category.setText(item.getCategory());
        name.setText(item.getName());
        GlideApp.with(this).load(item.getPhoto()).apply(new RequestOptions().override(120, 160).centerInside())
                .into(photo);
        ratingBar.setNumStars(5);
        ratingBar.setRating(item.getAvgRating());
        avgRating.setText(getResources().getString(R.string.fmt_avg_rating, item.getAvgRating()));
        numRatings.setText(getResources().getString(R.string.fmt_ratings, item.getNumRatings()));
        toolbar.setTitle(item.getName());
    }

    private void updateAvgPrice(List<Price> prices) {
        float average = model.calcAvgPrice(prices);
        if (average == 0) {
            avgPrice.setVisibility(View.INVISIBLE);
            priceCurrency.setVisibility(View.INVISIBLE);
            priceQuantity.setVisibility(View.INVISIBLE);
            priceDescription.setText("Noch keine Preisangaben");
        } else {
            avgPrice.setVisibility(View.VISIBLE);
            priceCurrency.setVisibility(View.VISIBLE);
            priceQuantity.setVisibility(View.VISIBLE);

            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            avgPrice.setText(decimalFormat.format(average));
            priceDescription.setText("Durchschnittlicher Preis");
            priceCurrency.setText("Chf");
        }
    }

    private void updateRatings(List<Rating> ratings) {
        adapter.submitList(new ArrayList<>(ratings));
    }

    private void updateMyRatings(List<Rating> ratings) {
        myRatingBar.setRating(model.calcAvgRating(ratings));
    }

    @Override
    public void onRatingLikedListener(Rating rating) {
        model.toggleLike(rating);
    }

    @OnClick(R.id.wishlist)
    public void onWishClickedListener(View view) {
        model.toggleItemInWishlist(model.getBeer().getValue().getId());
        /*
         * We won't get an update from firestore when the wish is removed, so we need to reset the UI state ourselves.
         * */
        if (!wishlist.isChecked()) {
            toggleWishlistView(null);
        }
    }

    private void toggleWishlistView(Wish wish) {
        if (wish != null) {
            int color = getResources().getColor(R.color.colorPrimary);
            setDrawableTint(wishlist, color);
            wishlist.setChecked(true);
        } else {
            int color = getResources().getColor(android.R.color.darker_gray);
            setDrawableTint(wishlist, color);
            wishlist.setChecked(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
