package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @BindView(R.id.image_iv) ImageView mIngredientsIv;
    @BindView(R.id.description_tv) TextView mDescriptionTv;
    @BindView(R.id.origin_tv) TextView mOriginTv;
    @BindView(R.id.also_known_tv) TextView mAlsoKnownAsTv;
    @BindView(R.id.ingredients_tv) TextView mIngredientsTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }
        populateUI(sandwich);


    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        mDescriptionTv.setText(sandwich.getDescription());

        String placeOfOrigin = sandwich.getPlaceOfOrigin();
        Log.d("DetailActivity", String.format("Sandwich %s has origin '%s'",
                sandwich.getMainName(), sandwich.getPlaceOfOrigin()));
        if (placeOfOrigin.isEmpty()) {
            placeOfOrigin = "Unknown";
        }
        mOriginTv.setText(placeOfOrigin);

        ArrayList<String> ingredients = (ArrayList<String>) sandwich.getIngredients();
        mIngredientsTv.setText(joinList(ingredients));

        ArrayList<String> alsoKnownAs = (ArrayList<String>) sandwich.getAlsoKnownAs();
        // If the sandwich has no aliases, use <none>
        if (alsoKnownAs.size() == 0) {
            mAlsoKnownAsTv.setText("None");
        } else {
            mAlsoKnownAsTv.setText(joinList(alsoKnownAs));
        }

        setTitle(sandwich.getMainName());

        Picasso.with(this)
                .load(sandwich.getImage())
                .into(mIngredientsIv);
    }

    private String joinList(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (; i < list.size() - 1; i++) {
            sb.append(list.get(i));
            sb.append(", ");
        }
        sb.append(list.get(i));
        return sb.toString();
    }


}
