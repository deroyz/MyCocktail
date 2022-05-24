package com.example.mycocktail.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mycocktail.R;
import com.example.mycocktail.network.drinkmodel.Drink;

import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> {

    private static final String LOG_TAG = LogAdapter.class.getSimpleName();

    private List<Drink> mDrinks;
    private DrinkItemClickListener mDrinkItemClickListener;
    private Context mContext;
    private Activity mActivity;

    public DrinkAdapter(List<Drink> drinks, DrinkItemClickListener drinkItemClickListener, Context context, FragmentActivity activity) {

        mDrinks = drinks;
        mDrinkItemClickListener = drinkItemClickListener;
        mContext = context;
        mActivity = activity;

    }

    public void setDrinks(List<Drink> mDrinks) {
        this.mDrinks = mDrinks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_drink, parent, false);

        return new DrinkViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, int position) {

        final Drink drink = mDrinks.get(position);

        String cocktailName = drink.getStrDrink();
        String imageUrl = drink.getStrDrinkThumb();

        holder.cocktailName.setText(cocktailName);

                Glide.with(holder.itemView)
                .load(imageUrl)
                .into(holder.cocktailImage);

    }

    @Override
    public int getItemCount() {

        if (mDrinks == null) {
            return 0;
        }

        return mDrinks.size();

    }

    public class DrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView cocktailName;
        private ImageView cocktailImage;

        public DrinkViewHolder(@NonNull View itemView) {
            super(itemView);

            cocktailName = itemView.findViewById(R.id.tv_drink_cocktailName);
            cocktailImage = itemView.findViewById(R.id.iv_drink_cocktail);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int elementId = Integer.parseInt(mDrinks.get(getAdapterPosition()).getIdDrink());
            mDrinkItemClickListener.onItemClickListener(elementId);

        }

    }

    public interface DrinkItemClickListener {

        void onItemClickListener(int itemId);

    }

}
