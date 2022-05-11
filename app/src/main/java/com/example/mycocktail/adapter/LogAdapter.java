package com.example.mycocktail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycocktail.R;
import com.example.mycocktail.data.LogEntry;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {

    private static final String DATE_FORMAT = "dd/MM/yyy";

    final private ItemClickListener mItemClickListener;

    private List<LogEntry> mLogEntries;
    private Context mContext;


    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());


    public LogAdapter(ItemClickListener mItemClickListener, Context mContext) {
        this.mItemClickListener = mItemClickListener;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.fragment_mylog, parent, false);

        return new LogViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {

        LogEntry logEntry = mLogEntries.get(position);

        String cocktailName = logEntry.getName();
        String comment = logEntry.getComment();

        String price = NumberFormat.getCurrencyInstance(new Locale("US", "en"))
                .format(logEntry.getPrice());

        String updatedAt = dateFormat.format(logEntry.getUpdatedAt());

        String rating = "" + logEntry.getRating();

        holder.cocktailNameView.setText(cocktailName);
        holder.commentView.setText(comment);
        holder.updatedAtView.setText(updatedAt);
        holder.cocktailPriceView.setText(price);
        holder.ratingView.setText(rating);

    }

    public List<LogEntry> getLogs() {
        return mLogEntries;
    }

    public void setlogs(List<LogEntry> mLogEntries) {
        this.mLogEntries = mLogEntries;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mLogEntries == null) {
            return 0;
        }

        return mLogEntries.size();

    }

    public class LogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView cocktailNameView;
        TextView updatedAtView;
        TextView commentView;
        TextView cocktailPriceView;
        TextView ratingView;


        public LogViewHolder(@NonNull View itemView) {
            super(itemView);

            cocktailNameView = itemView.findViewById(R.id.tv_cocktailName);
            updatedAtView = itemView.findViewById(R.id.tv_logUpdatedAt);
            commentView = itemView.findViewById(R.id.tv_comment);
            cocktailPriceView = itemView.findViewById(R.id.tv_cocktailPrice);
            ratingView = itemView.findViewById(R.id.tv_rating);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int elementId = mLogEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }

    public interface ItemClickListener {

        void onItemClickListener(int itemId);

    }
}
