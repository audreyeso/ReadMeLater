package com.test.readmelater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.readmelater.models.Book;

import java.util.ArrayList;

/**
 * Created by audreyeso on 9/8/16.
 */
public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.SampleViewHolder> {
    private static final String TAG = "Adapter";
    private ArrayList<Book> data;

    public static class SampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewThumbnail;
        public TextView textViewAuthor;
        public TextView textViewTitle;

        public SampleViewHolder(final View itemView) {
            super(itemView);

            textViewAuthor = (TextView) itemView.findViewById(R.id.recycler_view_author_text_view);
            textViewTitle = (TextView) itemView.findViewById(R.id.recycler_view_title_text_view);
            imageViewThumbnail = (ImageView) itemView.findViewById(R.id.recycler_view_image_view);


            /**
             * We set a click listener on the item view itself. The whole item not the image or textView.
             *
             * Therefore whole item is clickable now
             */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Clicked on item: " + getLayoutPosition());
                }
            });
        }
    }

    public CustomRecyclerViewAdapter(ArrayList<Book> inComingData){
        this.data = inComingData;
    }

    @Override
    public SampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get context from parent ViewGroup
        Context context = parent.getContext();

        // Get layoutInflater, which will inflate our custom list item layout for us
        LayoutInflater inflater = LayoutInflater.from(context);

        /**
         * Inflate the custom list item layout. The view returned back is our top level
         * view. If you look at step 0, you'll see our top level layout is LinearLayout.
         *
         * We pass this LinearLayout view to our SampleViewHolder so we can pull our
         * ImageView and TextView out of it via their id's
         */
        View listItemLayout = inflater.inflate(R.layout.recycler_view_book_list, parent, false);

        // Return a new SampleViewHolder instance
        SampleViewHolder viewHolder = new SampleViewHolder(listItemLayout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SampleViewHolder holder, int position) {
        // Get our data item for the current position from the data list
        Book dataItem = data.get(position);
        /**
         * Pull out the inflated TextView/ImageView references out of our SampleViewHolder
         * instance.
         *
         * Look at the constructor of SampleViewHolder() and note that variable fields
         * 'imageView' and 'textView' are both public ( which is why we don't need a getter ).
         */
        TextView textViewTitle = holder.textViewTitle;
        TextView textViewAuthor = holder.textViewAuthor;
        ImageView imageViewThumbnail = holder.imageViewThumbnail;

        // put our dataItem string as text into the text view
        textViewTitle.setText(dataItem.getTitle());
        textViewAuthor.setText(dataItem.getAuthor());

        Log.d("Response",dataItem.getTitle());
        Log.d("Response",dataItem.getAuthor());



        // set the launcher icon as our image resource
        imageViewThumbnail.setImageResource(R.mipmap.ic_launcher);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}