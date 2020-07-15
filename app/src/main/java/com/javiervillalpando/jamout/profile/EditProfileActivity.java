package com.javiervillalpando.jamout.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.javiervillalpando.jamout.MainActivity;
import com.javiervillalpando.jamout.R;

public class EditProfileActivity extends AppCompatActivity {
    private Button saveChangesButton;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        saveChangesButton = findViewById(R.id.saveChangesButton);
        logoutButton = findViewById(R.id.logoutButton);

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMainActivity();
            }
        });
    }
    public void goToMainActivity(){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }
}