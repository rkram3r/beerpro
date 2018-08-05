package ch.beerpro.rating;

import android.util.Log;
import ch.beerpro.helpers.EntityClassSnapshotParser;
import ch.beerpro.models.Rating;
import android.net.Uri;
import androidx.lifecycle.ViewModel;
import ch.beerpro.models.Beer;
import com.google.android.gms.tasks.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

public class RatingViewModel extends ViewModel {

    private static final String TAG = "RatingViewModel";

    private EntityClassSnapshotParser<Rating> parser = new EntityClassSnapshotParser<>(Rating.class);
    private Beer item;
    private Uri photo;

    public Beer getItem() {
        return item;
    }

    public void setItem(Beer item) {
        this.item = item;
    }

    public Uri getPhoto() {
        return photo;
    }

    public void setPhoto(Uri photo) {
        this.photo = photo;
    }

    public Task<Rating> saveRating(Beer item, float rating, String comment, Uri localPhotoUri) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;

        return uploadFileToFirebaseStorage(localPhotoUri).continueWithTask(task -> {
            String photoUrl = null;

            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                photoUrl = downloadUri.toString();
            } else if (task.isCanceled()) {
                // the user did not take a photo, that's ok!
            } else {
                throw task.getException();
            }

            Rating newRating = new Rating(item.getId(), item.getName(), user.getUid(), user.getDisplayName(),
                    user.getPhotoUrl().toString(), photoUrl, rating, comment, new Date());
            Log.i(TAG, "Adding new rating: " + newRating.toString());
            return FirebaseFirestore.getInstance().collection("ratings").add(newRating);

        }).continueWithTask(task -> {
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

    private Task<Uri> uploadFileToFirebaseStorage(Uri localPhotoUri) {

        if (localPhotoUri == null) {
            return Tasks.forCanceled();
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference imageRef = storageRef.child("images/" + localPhotoUri.getLastPathSegment());
        Log.i(TAG, "Uploading image " + localPhotoUri);

        return imageRef.putFile(localPhotoUri).addOnFailureListener(exception -> {
            Log.e(TAG, "Uploading image failed", exception);
        }).addOnSuccessListener(taskSnapshot -> {
            Log.i(TAG, "Uploading image successful: " + taskSnapshot.getMetadata().getName());
        }).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return imageRef.getDownloadUrl();
        });
    }
}