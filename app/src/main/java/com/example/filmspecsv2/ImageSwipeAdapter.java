package com.example.filmspecsv2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ImageSwipeAdapter extends PagerAdapter {
    private int [] image_resources = {R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.five};
    private Context ctx;
    private LayoutInflater layoutInflater;

    public ImageSwipeAdapter(Context ctx){
        this.ctx = ctx;
    }

    @Override
    public  int getCount() {
        return image_resources.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (RelativeLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View preview = layoutInflater.inflate(R.layout.swipe_layout,container, false);

        ImageView imageView = (ImageView)preview.findViewById(R.id.swiped_image);
        imageView.setImageResource(image_resources[position]);
        container.addView(preview);
        return preview;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
