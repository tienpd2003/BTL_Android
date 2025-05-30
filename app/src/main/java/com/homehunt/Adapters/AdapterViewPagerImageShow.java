package com.homehunt.Adapters;

import android.content.Context;
//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterViewPagerImageShow extends PagerAdapter {
    Context context;
    ArrayList<String> lstStringLinkImage;

    public AdapterViewPagerImageShow(Context context, ArrayList<String> stringLinkImage) {
        this.context = context;
        this.lstStringLinkImage = stringLinkImage;
    }

    @Override
    public int getCount() {
        if(!lstStringLinkImage.isEmpty())
            return lstStringLinkImage.size();
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        downloadImageForImageControl(imageView, position);
        container.addView(imageView, 0);

        return imageView;
    }

    private void downloadImageForImageControl(final ImageView imageDownload, final int positionDownload) {
        Picasso.get().load(lstStringLinkImage.get(positionDownload)).fit().into(imageDownload);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}