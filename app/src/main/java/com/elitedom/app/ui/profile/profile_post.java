package com.elitedom.app.ui.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.elitedom.app.R;
import com.elitedom.app.ui.messaging.ProfileMessaging;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile_post extends AppCompatActivity {

    private String profileImageUri;
    private CardView mCard;
    private TextView mUsername, mPostTitle, mPostText;
    private ImageView mLiked, mDisliked;
    private int like_status, dislike_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_post);

        CircleImageView mProfileImage = findViewById(R.id.profile_image);
        RelativeLayout relativeLayout = findViewById(R.id.user_feed_container);
        ImageView mPostImage = findViewById(R.id.postImage);
        TextView mAuthor = findViewById(R.id.author);
        mPostText = findViewById(R.id.post_text);
        mCard = findViewById(R.id.action_cards);
        mUsername = findViewById(R.id.username);
        mPostTitle = findViewById(R.id.title);
        mLiked = findViewById(R.id.liked);
        mDisliked = findViewById(R.id.disliked);
        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Objects.requireNonNull(getSupportActionBar()).hide();

        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        Intent intent = getIntent();
        mPostTitle.setText(intent.getStringExtra("title"));
        mPostTitle.setContentDescription(intent.getStringExtra("uid"));
        mPostText.setText(intent.getStringExtra("subtext"));
        mPostText.setContentDescription(intent.getStringExtra("dorm"));
        profileImageUri = intent.getStringExtra("profileImage");
        Glide.with(this)
                .load(profileImageUri)
                .into(mProfileImage);
        mAuthor.setText(intent.getStringExtra("author"));
        Glide.with(this)
                .load(intent.getStringExtra("image"))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mPostImage);

        mDatabase.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            assert document != null;
                            mUsername.setText(Objects.requireNonNull(document.get("firstName")).toString() + " " + Objects.requireNonNull(document.get("lastName")).toString() + "'s Profile");
                        }
                    }
                });

        mPostImage.setClipToOutline(true);
        mPostText.setClipToOutline(true);
    }

    public void backAction(View view) {
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
    }

    public void messageActivity(View view) {
        Intent intent = new Intent(this, ProfileMessaging.class);
        intent.putExtra("username", mUsername.getText().toString());
        intent.putExtra("uid", mPostTitle.getContentDescription().toString());
        intent.putExtra("dorm", mPostText.getContentDescription().toString());
        intent.putExtra("profileImage", profileImageUri);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(), mCard, "messaging_transition");
        ActivityCompat.startActivity(this, intent, options.toBundle());
        setResult(Activity.RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }

    public void likePost(View view) {
        if (dislike_status == 1) {
            AnimatedVectorDrawable animatedVectorDrawable =
                    (AnimatedVectorDrawable) getDrawable(R.drawable.ic_thumb_down_liked_24dp);
            mDisliked.setImageDrawable(animatedVectorDrawable);
            assert animatedVectorDrawable != null;
            animatedVectorDrawable.start();
            dislike_status = 0;
        }
        if (like_status == 0) {
            AnimatedVectorDrawable animatedVectorDrawable =
                    (AnimatedVectorDrawable) getDrawable(R.drawable.ic_thumb_up_unliked_24dp);
            mLiked.setImageDrawable(animatedVectorDrawable);
            assert animatedVectorDrawable != null;
            animatedVectorDrawable.start();
            like_status = 1;
        }
        else {
            AnimatedVectorDrawable animatedVectorDrawable =
                    (AnimatedVectorDrawable) getDrawable(R.drawable.ic_thumb_up_liked_24dp);
            mLiked.setImageDrawable(animatedVectorDrawable);
            assert animatedVectorDrawable != null;
            animatedVectorDrawable.start();
            like_status = 0;
        }
    }

    public void dislikePost(View view) {
        if (like_status == 1) {
            AnimatedVectorDrawable animatedVectorDrawable =
                    (AnimatedVectorDrawable) getDrawable(R.drawable.ic_thumb_up_liked_24dp);
            mLiked.setImageDrawable(animatedVectorDrawable);
            assert animatedVectorDrawable != null;
            animatedVectorDrawable.start();
            like_status = 0;
        }
        if (dislike_status == 0) {
            AnimatedVectorDrawable animatedVectorDrawable =
                    (AnimatedVectorDrawable) getDrawable(R.drawable.ic_thumb_down_unliked_24dp);
            mDisliked.setImageDrawable(animatedVectorDrawable);
            assert animatedVectorDrawable != null;
            animatedVectorDrawable.start();
            dislike_status = 1;
        }
        else {
            AnimatedVectorDrawable animatedVectorDrawable =
                    (AnimatedVectorDrawable) getDrawable(R.drawable.ic_thumb_down_liked_24dp);
            mDisliked.setImageDrawable(animatedVectorDrawable);
            assert animatedVectorDrawable != null;
            animatedVectorDrawable.start();
            dislike_status = 0;
        }
    }
}