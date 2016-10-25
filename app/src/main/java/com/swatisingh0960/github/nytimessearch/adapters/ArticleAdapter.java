package com.swatisingh0960.github.nytimessearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.swatisingh0960.github.nytimessearch.models.Article;
import com.swatisingh0960.github.nytimessearch.R;
import java.util.List;

/**
 * Created by Swati on 10/22/2016.
 */

    public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>{

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvTitle;
        public ImageView ivImage;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        }
    }

        private List<Article> mArticles;
        private Context mContext;

        //Pass in the contact array into the constructor
        public ArticleAdapter(Context context, List<Article> articles){
            mArticles = articles;
            mContext = context;
        }

        // Easy access  to the context object in recycler view
        private Context getContext(){
            return mContext;
        }

        //Usually involves inflating a layout  from XML  and returning  the holder
        @Override
        public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            //Inflate the custom layout
            View articleView = inflater.inflate(R.layout.item_article_result,parent,false);

            //Return a new holder instances
            ViewHolder viewHolder = new ViewHolder(articleView);
            return viewHolder;
        }
        // Involves populating data into the item through holder

        @Override
        public void onBindViewHolder(ArticleAdapter.ViewHolder viewHolder, int position) {
            //Get the model based on position
            Article article = mArticles.get(position);

            //Set item views based  on the data model
            TextView textView = viewHolder.tvTitle;
            textView.setText(article.getHeadline());

            ImageView imageView = viewHolder.ivImage;
            imageView.setImageResource(0);

            if (!TextUtils.isEmpty(article.getThumbNail())) {
                Picasso.with(getContext()).load(article.getThumbNail()).into(imageView);
            }
        }
        // Return the total count of items
        @Override
        public int getItemCount() {
            return mArticles.size();
        }

}
