package com.example.filmspecsv2;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.filmspecsv2.Model.MovieItem;
import com.example.filmspecsv2.ViewHolder.MovieViewHolder;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProfileLibraryActivity extends AppCompatActivity {
    Dialog removeFav;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public FirebaseRecyclerPagingAdapter<MovieItem, MovieViewHolder> adapter;
    GridLayoutManager gridLayoutManager;
    int position;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_library);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);

        removeFav = new Dialog(this);

        final RequestOptions Roptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.movie_poster_stock)
                .error(R.drawable.movie_poster_stock);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(){
                adapter.refresh();
            }
        });


        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);//initialize recycler view (start of recycler code)
        recyclerView.setHasFixedSize(true);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("USERS")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Favorites");
        }

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(3)
                .build();

        final DatabasePagingOptions<MovieItem> options = new DatabasePagingOptions.Builder<MovieItem>()
                //.setLifecycleOwner(this)
                .setQuery(databaseReference, config, MovieItem.class)
                .build();

        adapter = new FirebaseRecyclerPagingAdapter<MovieItem, MovieViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MovieViewHolder holder, int position, @NonNull final MovieItem model) {

                final String poster = model.getPoster();//save title
                final String title = model.getTitle();//save poster
                final String barcode = model.getBarcode();//save barcode

                Glide.with(getApplicationContext()).load(poster).override(600, 800).apply(Roptions).into(holder.i1);
                holder.t1.setText(title);//display movie title in recycler view


                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(ProfileLibraryActivity.this, MovieDisplayActivity.class);
                        i.putExtra("movie_key", barcode);
                        i.putExtra("poster_key", poster);
                        startActivity(i);
                    }
                });

                holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        showPopup(view, barcode, title, poster);
                        return true;
                    }
                });

            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch(state){
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        mSwipeRefreshLayout.setRefreshing(true);
                        break;
                    case LOADED:
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;
                    case FINISHED:
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;
                    case ERROR:
                        break;
                }
            }

            @Override
            protected void onError(@NonNull DatabaseError databaseError){
                mSwipeRefreshLayout.setRefreshing(false);
                databaseError.toException().printStackTrace();
                // Handle Error
            }


            @NonNull
            @Override
            public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_layout, parent, false);
                return new MovieViewHolder(view);
            }
        };

        adapter.retry();
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter); //set adapter for recycler view (end of recycler view code)
        position = gridLayoutManager.findFirstVisibleItemPosition();
        if(savedInstanceState != null){
            recyclerView.scrollToPosition(savedInstanceState.getInt("position"));
        }


        //initialize bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //assign icon option to this activity
        bottomNavigationView.setSelectedItemId(R.id.profile_library);

        //listen to item selected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile_library:
                        return true;
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


        //check if user is signed in (deny access if not by sending to log in page)
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            Intent intent = new Intent(this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            Toast.makeText(this, "You must be signed in to access the library", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }

    }//end of onCreate

    @Override
    protected void onSaveInstanceState(Bundle state) {
        state.putInt("position", position);
        super.onSaveInstanceState(state);

    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        if(state != null){
            recyclerView.scrollToPosition(state.getInt("position"));
        }
    }


    @Override
    protected void onStart() {//start adapter for recycler view code
        super.onStart();
        if(adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {//stop recycler view code
        super.onStop();
        if(adapter == null)
            adapter.stopListening();

    }

    @Override
    protected void onResume() {//resume recycler view code
        super.onResume();
    }

    //top library menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.library_menu, menu);
        return true;
    }
    //what actions to take when user clicks top menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //longpress popup
    public void showPopup(View v, final String barcode, final String title, String poster){
        TextView movieName;
        Button removeMovie;
        Button keepMovie;
        ImageView posterShow;

        removeFav.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //layout for popup
        removeFav.setContentView(R.layout.movie_delete_popup);

        //set movie title
        movieName = (TextView) removeFav.findViewById(R.id.movie_unfav_title);
        movieName.setText(title);

        //buttons
        removeMovie = (Button) removeFav.findViewById(R.id.remove_movie);
        keepMovie = (Button) removeFav.findViewById(R.id.keep_movie);

        //poster Display Options
        final RequestOptions Roptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.movie_poster_stock)
                .error(R.drawable.movie_poster_stock);

        //display poster
        posterShow = (ImageView) removeFav.findViewById(R.id.movie_unfav_poster);
        Glide.with(getApplicationContext()).load(poster).override(600, 800).apply(Roptions).into(posterShow);

        //remove movie
        removeMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(barcode).setValue(null);
                Toast.makeText(getApplicationContext(), title + " Was Removed from your Library", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfileLibraryActivity.this, ProfileLibraryActivity.class);
                removeFav.dismiss();
                startActivity(intent); // start same activity
                finish(); // destroy older activity
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

        });

        //keep movie exit popup
        keepMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFav.dismiss();

            }
        });

        removeFav.show();//show popup

    }//end of showPopup


    //go back to home on back press
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ProfileLibraryActivity.this,HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
    }


}
