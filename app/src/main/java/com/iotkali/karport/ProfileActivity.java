package com.iotkali.karport;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

public class ProfileActivity extends FragmentActivity {
    private Toolbar toolbar;
    private TextView description_user_info,description_vehicle_info,title_vehicle_info,title_vehicle2_info,description_vehicle2_info;
    private User user;
    private FloatingActionButton fba;
    private CardView extra_card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fba = (FloatingActionButton)findViewById(R.id.fab);
        user = new User(getSharedPreferences("User", Context.MODE_PRIVATE));
        extra_card = (CardView)findViewById(R.id.extra_card);
       if (!user.isOnline()){
            Intent mainIntent = new Intent(ProfileActivity.this, PreLoginActivity.class);
            ProfileActivity.this.startActivity(mainIntent);
            finish();
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }

        fba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extra_card.setAlpha(1.0f);
            }
        });

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        description_user_info = (TextView)findViewById(R.id.description_user_info);
        title_vehicle_info = (TextView)findViewById(R.id.title_vehicle_info);
        description_vehicle_info = (TextView)findViewById(R.id.description_vehicle_info);
        title_vehicle2_info = (TextView)findViewById(R.id.title_vehicle2_info);
        description_vehicle2_info = (TextView)findViewById(R.id.description_vehicle2_info);

        toolbar.setTitle(user.getName());
        description_user_info.setText("Birthdate: " + user.getBirthDate() + "\nEmail: " + user.getEmail() + "\n" +
                "Legal Gender: " + user.getGender() + "\n" +
                "Condition: " + user.getCondition());
        title_vehicle_info.setText("Chevrolet Aveo");
        title_vehicle2_info.setText("Lamborghini Diablo GT-R");
        description_vehicle_info.setText("Plates: UUL7516\nYear: 2015");
        description_vehicle2_info.setText("Plates: EM4297\nYear: 2015");
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }
}
