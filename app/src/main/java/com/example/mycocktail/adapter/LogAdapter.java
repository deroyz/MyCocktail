package com.example.mycocktail.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycocktail.MainActivity;
import com.example.mycocktail.R;
import com.example.mycocktail.data.LogDatabase;
import com.example.mycocktail.data.LogEntry;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {

    private static final String LOG_TAG = LogAdapter.class.getSimpleName();

    private static final String DATE_FORMAT = "dd/MM/yyy";

    final private ItemClickListener mItemClickListener;

    private List<LogEntry> mLogEntries;
    private Context mContext;
    private Activity mActivity;

    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());


    public LogAdapter(LogDatabase mLogDatabase, ItemClickListener mItemClickListener, Context mContext, Activity mActivity) {

        this.mItemClickListener = mItemClickListener;
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mLogEntries = mLogDatabase.logDao().loadALLLogs().getValue();

    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_log, parent, false);

        return new LogViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {

       final LogEntry logEntry = mLogEntries.get(position);

        String cocktailName = logEntry.getName();
        holder.cocktailNameView.setText(cocktailName);

        String comment = logEntry.getComment();
        holder.commentView.setText(comment);

        String updatedAt = dateFormat.format(logEntry.getUpdatedAt());
        holder.updatedAtView.setText(updatedAt);

        String price = NumberFormat.getCurrencyInstance(new Locale("US", "en"))
                .format(logEntry.getPrice());
        holder.cocktailPriceView.setText(price);

        String rating = "" + logEntry.getRating();
        holder.ratingView.setText(rating);

    }

    public List<LogEntry> getLogs() {
        return mLogEntries;
    }

    public void setLogs(List<LogEntry> mLogEntries) {
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

        private View view;

        private TextView cocktailNameView;
        private TextView updatedAtView;
        private TextView commentView;
        private TextView cocktailPriceView;
        private TextView ratingView;


        public LogViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView.findViewById(R.id.list_item);

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
