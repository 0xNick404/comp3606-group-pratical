package com.example.furnitureorderingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private List<TestUser> testUsers =
            List.of(
                    new TestUser(1, "jon", "password", "Jon Snow"),
                    new TestUser(2, "hannah", "password", "Hannah Montana"),
                    new TestUser(3, "mike", "password", "Mike Wazowski")
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onLoginClicked(View view) {
        final EditText usernameEditText = findViewById(R.id.editTextText);
        final EditText passwordEditText = findViewById(R.id.editTextTextPassword);

        final String userUsername = ((EditText)findViewById(R.id.editTextText)).getText().toString();
        final String userPassword = ((EditText)findViewById(R.id.editTextTextPassword)).getText().toString();

        for(int i = 0; i < testUsers.size(); i++){
            final TestUser user = testUsers.get(i);

            if(user.getUsername().equals(userUsername) && user.getPassword().equals(userPassword)){
                Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
                intent.putExtra("USER_FULL_NAME", user.getFullName());
                startActivity(intent);

                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();

                usernameEditText.setText("");
                passwordEditText.setText("");

                return;
            }
        }

        usernameEditText.setText("");
        passwordEditText.setText("");

        Toast.makeText(this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
    }
}