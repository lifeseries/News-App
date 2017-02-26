package com.example.android.sportnewsapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    private Context mContext;

    public NewsAdapter(Context context, List<News> newsItems) {
        super(context, 0, newsItems);
        mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        // Find the news at the given position in the list of news
        News currentNews = getItem(position);

        // Find the TextView with view ID thumbnail
        ImageView thumbnailView = (ImageView) listItemView.findViewById(R.id.thumbnail);

        // Display the thumbnail in Image View
        Picasso.with(mContext)
                .load(currentNews.getThumbnail())
                .placeholder(R.drawable.thumbnail_placeholder)
                .into(thumbnailView);

        // Find the TextView with view ID section
        TextView sectionView = (TextView) listItemView.findViewById(R.id.section);
        sectionView.setText(currentNews.getSection());
        // Find the TextView with the view ID title
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        titleView.setText(currentNews.getTitle());
        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}
