package com.example.mycocktail.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

    private List<LogEntry> mLogEntries;
    final private LogItemClickListener mLogItemClickListener;
    private Context mContext;
    private Activity mActivity;

    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    public LogAdapter(LogDatabase mLogDatabase, LogItemClickListener mLogItemClickListener, Context mContext, Activity mActivity) {

        this.mLogItemClickListener = mLogItemClickListener;
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
        holder.textViewCocktailName.setText(cocktailName);

        String comment = logEntry.getComment();
        holder.textViewComment.setText(comment);

        String updatedAt = dateFormat.format(logEntry.getUpdatedAt());
        holder.textViewUpdatedAt.setText(updatedAt);

        String price = NumberFormat.getCurrencyInstance(new Locale("US", "en"))
                .format(logEntry.getPrice());
        holder.textViewPrice.setText(price);

        String rating = "" + logEntry.getRating();
        holder.textViewRating.setText(rating);

        String photoPath = logEntry.getPhotoPath();
                Glide.with(mContext)
                .load(photoPath)
                .into(holder.imageViewLogCocktail);

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

        private TextView textViewCocktailName;
        private TextView textViewUpdatedAt;
        private TextView textViewComment;
        private TextView textViewPrice;
        private TextView textViewRating;
        private ImageView imageViewLogCocktail;


        public LogViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView.findViewById(R.id.list_item);

            textViewCocktailName = itemView.findViewById(R.id.tv_log_cocktailName);
            textViewUpdatedAt = itemView.findViewById(R.id.tv_logUpdatedAt);
            textViewComment = itemView.findViewById(R.id.tv_comment);
            textViewPrice = itemView.findViewById(R.id.tv_cocktailPrice);
            textViewRating = itemView.findViewById(R.id.tv_rating);
            imageViewLogCocktail = itemView.findViewById(R.id.iv_log_cocktail);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int elementId = mLogEntries.get(getAdapterPosition()).getId();
            mLogItemClickListener.onItemClickListener(elementId);
        }
    }

    public interface LogItemClickListener {

        void onItemClickListener(int itemId);

    }
}
