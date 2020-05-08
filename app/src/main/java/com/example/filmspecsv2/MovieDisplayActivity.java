package com.example.filmspecsv2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MovieDisplayActivity extends AppCompatActivity{
    private Toast dToast = null;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private boolean doesExist = false;
    DatabaseReference userDatabase;
    DatabaseReference reference;
    ImageView imageView;
    String url;
    Button favebt;
    String upc;
    String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_display);
        //initialize favorites checkbox
        favebt = findViewById(R.id.checkBox);

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userId = user.getUid();
        }

        //initialize movie poster display
        final RequestOptions Roptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.movie_poster_stock)
                .error(R.drawable.movie_poster_stock);

        upc = getIntent().getStringExtra("movie_key");//get code from scan
        url = getIntent().getStringExtra("poster_key");//get code from scan
        reference = FirebaseDatabase.getInstance().getReference("BLURAY").child(upc);//connect to root database
        reference.addValueEventListener(new ValueEventListener() {//connect to child in root
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("studio")) {
                        imageView = findViewById(R.id.movie_poster);

                        title = dataSnapshot.child("title").getValue().toString();//save title
                        String sound = dataSnapshot.child("sound").getValue().toString();//save sound
                        String genre = dataSnapshot.child("genre").getValue().toString();//save Genre
                        String rating = dataSnapshot.child("rating").getValue().toString();//save Rating
                        String year = dataSnapshot.child("year").getValue().toString();//save Year
                        String aspectRatio = dataSnapshot.child("aspectratio").getValue().toString();//save aspect ratio
                        String descAudio = dataSnapshot.child("da").getValue().toString();//save descriptive audio
                        String releaseFormat = dataSnapshot.child("rformat").getValue().toString();//save release format
                        String physicalRelease = dataSnapshot.child("preleasedate").getValue().toString();//save physical release date
                        String studio = dataSnapshot.child("studio").getValue().toString();//save studio
                        String length = dataSnapshot.child("length").getValue().toString();
                        String codec = dataSnapshot.child("codec").getValue().toString();
                        String color = dataSnapshot.child("color").getValue().toString();
                        String cprocess = dataSnapshot.child("cprocess").getValue().toString();
                        String discdigital = dataSnapshot.child("discdigital").getValue().toString();
                        String nformat = dataSnapshot.child("nformat").getValue().toString();
                        String playback = dataSnapshot.child("playback").getValue().toString();
                        String resolution = dataSnapshot.child("resolution").getValue().toString();
                        String subtitles = dataSnapshot.child("subtitles").getValue().toString();
                        if(url == null)
                        {
                            url = dataSnapshot.child("poster").getValue().toString();
                        }

                        //Display Data on screen
                        TextView titleView = (TextView) findViewById(R.id.movie_title_bar);
                        titleView.setText(title);

                        TextView soundView = (TextView) findViewById(R.id.display_movie_sound);
                        soundView.setText(sound);

                        TextView upcView = (TextView) findViewById(R.id.display_barcode);
                        upcView.setText(Html.fromHtml("<b> Barcode: </b>" + upc));

                        TextView lengthView = (TextView) findViewById(R.id.display_movie_length);
                        lengthView.setText(Html.fromHtml(" <b> Runtime: </b>" + length));

                        TextView playbackView = (TextView) findViewById(R.id.display_region_code);
                        playbackView.setText(playback);

                        TextView codecView = (TextView) findViewById(R.id.display_codec);
                        codecView.setText("Codec: " + codec);

                        TextView colorView = (TextView) findViewById(R.id.display_color);
                        colorView.setText("Color: " + color);

                        TextView cprocessView = (TextView) findViewById(R.id.display_cinema_process);
                        cprocessView.setText(cprocess);

                        TextView discdigitalView = (TextView) findViewById(R.id.display_disc_digital);
                        discdigitalView.setText(discdigital);

                        TextView nformatView = (TextView) findViewById(R.id.display_negative_format);
                        nformatView.setText(nformat);

                        TextView resolutionView = (TextView) findViewById(R.id.display_movie_resolution);
                        resolutionView.setText("Resolution: " + resolution);

                        TextView subtitlesView = (TextView) findViewById(R.id.display_subtitles);
                        subtitlesView.setText(subtitles);

                        TextView genreView = (TextView) findViewById(R.id.display_movie_genre);
                        genreView.setText(Html.fromHtml(" <b> Genre: </b>" + genre));

                        TextView ratingView = (TextView) findViewById(R.id.display_movie_rating);
                        ratingView.setText(Html.fromHtml("<b> MPAA Rating: </b>" + rating));

                        TextView aspectView = (TextView) findViewById(R.id.display_aspect_ratio);
                        aspectView.setText("Aspect Ratio: " + aspectRatio);

                        TextView yearView = (TextView) findViewById(R.id.display_movie_year);
                        yearView.setText(Html.fromHtml("<b> Theater Release Year: </b>" + year));

                        TextView descAudioView = (TextView) findViewById(R.id.display_descriptive_audio);
                        descAudioView.setText(descAudio);

                        TextView rFormatView = (TextView) findViewById(R.id.display_movie_physical_release);
                        rFormatView.setText(Html.fromHtml("<b> Release Format: </b>" + releaseFormat));


                        TextView physicalReleaseView = (TextView) findViewById(R.id.display_movie_release);
                        physicalReleaseView.setText(Html.fromHtml("<b> Physical Release: </b>" + physicalRelease));

                        TextView studioView = (TextView) findViewById(R.id.display_movie_studio);
                        studioView.setText(Html.fromHtml(" <b> Studio: </b>" + studio));

                        if (url != "No Poster") {
                            Glide.with(MovieDisplayActivity.this).load(url).override(600, 810).apply(Roptions).into(imageView);

                        }

                    } else {
                        finish();
                        Toast.makeText(MovieDisplayActivity.this, "Error Occurred! Movie does not match database records, sorry", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MovieDisplayActivity.this, SearchActivity.class));
                        overridePendingTransition(0, 0);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        clickSave();//Call function to save movie to Library (only if signed in)
    }//end of onCreate

    //Function is used to save movies into personal library
    public void clickSave(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            //personal library database location
            userDatabase = FirebaseDatabase.getInstance().getReference("USERS")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Favorites").child(upc);

            //check if movie is already inside the library
            userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        // The child doesn't exist
                        doesExist = true;
                    }

                    if (dataSnapshot.getValue() == null) {
                        doesExist = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        favebt.setOnClickListener(new View.OnClickListener() {//what happens when clicked?
            @Override
            public void onClick(View view) {//check if user is signed in
                if(FirebaseAuth.getInstance().getCurrentUser() == null){//prevent user from saving movie to library if they are not signed in
                    if (dToast != null) dToast.cancel();//if toast is already showing, don't show a new toast
                    dToast = Toast.makeText(getApplicationContext(), "Please Log in to use Library Function", Toast.LENGTH_SHORT);
                    dToast.show();
                    return;
                }
                if(doesExist == true){//movie already exists dont save
                    if (dToast != null) dToast.cancel();//if toast is already showing, don't show a new toast
                    dToast = Toast.makeText(getApplicationContext(), "Movie is already in Your Library Cannot add again", Toast.LENGTH_SHORT);
                    dToast.show();

                    return;

                }
                if(doesExist == false && FirebaseAuth.getInstance().getCurrentUser() != null) {//movie can be safely saved in library
                    userDatabase.child("title").setValue(title);
                    userDatabase.child("poster").setValue(url);
                    userDatabase.child("barcode").setValue(upc);
                    if (dToast != null) dToast.cancel();//if toast is already showing, don't show a new toast
                    dToast = Toast.makeText(getApplicationContext(), title + "Added to your Library", Toast.LENGTH_SHORT);
                    dToast.show();
                    doesExist = true;
                    return;
                }
            }
    });




}



}
