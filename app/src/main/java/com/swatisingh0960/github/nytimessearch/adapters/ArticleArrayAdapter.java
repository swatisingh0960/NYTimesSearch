package com.swatisingh0960.github.nytimessearch.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.swatisingh0960.github.nytimessearch.R;
import com.swatisingh0960.github.nytimessearch.models.Article;

import java.util.List;

/**
 * Created by Swati on 10/19/2016.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, List<Article> articles){
        super(context,android.R.layout.simple_list_item_1,articles);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get the data item for current position
        Article article = this.getItem(position);

        // check to see if existing view being reused
        //if not using recycled view, then inflate the layout
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_article_result,parent,false); // Set it to false, so that it does not attaches right away
            }
        //find the image view inside it
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);
        //clear out the recycled image from convert view from last time
        imageView.setImageResource(0);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);

        tvTitle.setText(article.getHeadline());

        //populate the thumbnail image
        //remotely download image in the background
        String thumbnail = article.getThumbNail();

        if(!TextUtils.isEmpty(thumbnail)){
            Picasso.with(getContext()).load(thumbnail).into(imageView);
        }
        return convertView;
    }
}
