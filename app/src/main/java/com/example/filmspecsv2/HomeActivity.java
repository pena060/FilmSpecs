package com.example.filmspecsv2;


import android.content.Intent;
import android.os.Bundle;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
import com.google.firebase.database.Query;


public class HomeActivity extends AppCompatActivity {
    private Toast mToast = null;
    RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public FirebaseRecyclerPagingAdapter<MovieItem, MovieViewHolder> adapter;
    GridLayoutManager gridLayoutManager;
    String LIST_STATE_KEY = "state_key";
    Parcelable listState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);

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
        databaseReference = FirebaseDatabase.getInstance().getReference().child("BLURAY");
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(14)
                .setPageSize(7)
                .build();

        final DatabasePagingOptions<MovieItem> options = new DatabasePagingOptions.Builder<MovieItem>()
                .setQuery(databaseReference, config, MovieItem.class)
                .build();

        adapter = new FirebaseRecyclerPagingAdapter<MovieItem, MovieViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MovieViewHolder holder, int position, @NonNull final MovieItem model) {

                final String poster = model.getPoster();
                Glide.with(getApplicationContext()).load(poster).override(600, 800).apply(Roptions).into(holder.i1);
                holder.t1.setText(model.getTitle());//display movie title in recycler view


                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(HomeActivity.this, MovieDisplayActivity.class);
                        String resultCode = model.getBarcode();
                        if(resultCode != "Information Needed")
                        {
                            i.putExtra("movie_key", resultCode);
                            i.putExtra("poster_key", poster);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(HomeActivity.this, "Oops. Seems like we ran into an page under construction. Information will be added soon :) ", Toast.LENGTH_SHORT).show();
                        }

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
                        Toast.makeText(HomeActivity.this, "End of Movie Data..", Toast.LENGTH_LONG).show();
                        break;
                    case ERROR:
                        retry();
                        break;
                }
            }

                @Override
            protected void onError(@NonNull DatabaseError databaseError){
                    mSwipeRefreshLayout.setRefreshing(false);
                    databaseError.toException().printStackTrace();
                    // Handle Error
                    retry();
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
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


        //initialize bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //assign icon option to this activity
        bottomNavigationView.setSelectedItemId(R.id.home);

        //listen to item selected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.profile_library:
                        startActivity(new Intent(getApplicationContext(),ProfileLibraryActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }

        }); //end of navigation


    }//end of onCreate

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putParcelable(LIST_STATE_KEY, gridLayoutManager.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        Parcelable listState = state.getParcelable(LIST_STATE_KEY);
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
        if (listState != null) {
            adapter.startListening();
        }
    }

    //top login menu (admit one) and settings menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }
    //what actions to take when user clicks top menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sign_in_up) {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {//if not signed in, send to log in page
                Toast.makeText(HomeActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                return true;
            } else if (FirebaseAuth.getInstance().getCurrentUser() != null) {//if already signed in, tell the user he already signed in
                if (mToast != null) mToast.cancel();//if toast is already showing, don't show a new toast
                mToast = Toast.makeText(this, "You are already logged in, Thank you", Toast.LENGTH_SHORT);
                mToast.show();
                return false;
            }
        }
        return super.onOptionsItemSelected(item);
    }




    public void onBackPressed() {//set what back button does in home page
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Exiting App", Toast.LENGTH_SHORT).show();
        System.exit(0);

    }

}
