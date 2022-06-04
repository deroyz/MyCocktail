package com.example.mycocktail.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mycocktail.R;
import com.example.mycocktail.data.FavoriteEntry;
import com.example.mycocktail.network.datamodel.Drink;

import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> {

    private static final String LOG_TAG = DrinkAdapter.class.getSimpleName();

    private List<Drink> mDrinks;
    private List<FavoriteEntry> mFavoriteEntries;
    private DrinkAdapterListener mDrinkAdapterListener;
    private Context mContext;
    private Activity mActivity;

    public DrinkAdapter(List<Drink> drinks, List<FavoriteEntry> favoriteEntries, DrinkAdapterListener drinkAdapterListener, Context context, FragmentActivity activity) {

        this.mFavoriteEntries = favoriteEntries;
        this.mDrinks = drinks;
        this.mDrinkAdapterListener = drinkAdapterListener;
        this.mContext = context;
        this.mActivity = activity;

    }

    public DrinkAdapter(List<FavoriteEntry> favoriteEntries, DrinkAdapterListener drinkAdapterListener) {

        this.mFavoriteEntries = favoriteEntries;
        this.mDrinkAdapterListener = drinkAdapterListener;

    }

    public void setDrinks(List<Drink> drinks) {

        this.mDrinks = drinks;
        notifyDataSetChanged();

    }

    public void setFavoriteEntries(List<FavoriteEntry> favoriteEntries) {

        this.mFavoriteEntries = favoriteEntries;
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

        if (mDrinks != null) {

            final Drink drink = mDrinks.get(position);

            for(int i= 0; )
            final FavoriteEntry favoriteEntry = mFavoriteEntries.get(position);



            String cocktailName = drink.getStrDrink();
            String imageUrl = drink.getStrDrinkThumb();
            String cocktailId = drink.getIdDrink();

            holder.cocktailName.setText(cocktailName);

            Glide.with(holder.itemView)
                    .load(imageUrl)
                    .into(holder.cocktailImage);



        } else if (mFavoriteEntries != null) {

            final FavoriteEntry favoriteEntry = mFavoriteEntries.get(position);

            String cocktailName = favoriteEntry.getStrDrink();
            String imageUrl = favoriteEntry.getStrDrinkThumb();

            holder.cocktailName.setText(cocktailName);

            Glide.with(holder.itemView)
                    .load(imageUrl)
                    .into(holder.cocktailImage);

        }

    }

    @Override
    public int getItemCount() {

        int itemCount = 0;

        if (mDrinks == null && mFavoriteEntries == null) {

            return 0;

        } else if (mDrinks != null) {

            itemCount = mDrinks.size();

        } else if (mFavoriteEntries != null) {

            itemCount = mFavoriteEntries.size();

        }

        return itemCount;

    }

    public class DrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView cocktailName;
        private ImageView cocktailImage;
        private Button recipeButton;
        private Button addLogButton;
        private CheckBox favoriteButton;


        public DrinkViewHolder(@NonNull View itemView) {
            super(itemView);

            cocktailName = itemView.findViewById(R.id.tv_drink_cocktailName);
            cocktailImage = itemView.findViewById(R.id.iv_drink_cocktail);
            recipeButton = itemView.findViewById(R.id.btn_get_recipe);
            addLogButton = itemView.findViewById(R.id.btn_add_to_log);
            favoriteButton = itemView.findViewById(R.id.btn_favorite);

            addLogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrinkAdapterListener.addLogOnClick(view, getBindingAdapterPosition());
                    Log.e(LOG_TAG, "onAddLogClick");
                }
            });

            recipeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrinkAdapterListener.recipeOnclick(view, getBindingAdapterPosition());
                    Log.e(LOG_TAG, "onRecipeClick");
                }
            });

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrinkAdapterListener.favoriteOnClick(view, getBindingAdapterPosition());
                    Log.e(LOG_TAG, "onFavoriteClick");
                }
            });


            // itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int elementId = Integer.parseInt(mDrinks.get(getBindingAdapterPosition()).getIdDrink());
            mDrinkAdapterListener.onItemClickListener(elementId);


        }

    }

    public interface DrinkAdapterListener {

        void onItemClickListener(int itemId);

        void recipeOnclick(View v, int position);

        void addLogOnClick(View v, int position);

        void favoriteOnClick(View v, int position);


    }

}
