package com.example.whereruapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    Button loginButton;
    EditText givenUser;
    EditText givenPassword;
    TextView txtView;
    TextView takenUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = (Button) findViewById(R.id.button);
        givenUser = (EditText) findViewById(R.id.editUsername);
        givenPassword = (EditText) findViewById(R.id.editPassword) ;
        txtView = (TextView)findViewById(R.id.invalidLogin);
    }
    public void login(View view){
        String username = givenUser.getText().toString();
        String password = givenPassword.getText().toString();

        GetDataService service = RetroClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<RetroLogin>> call = service.getLogin(username,password);
        call.enqueue(new Callback<List<RetroLogin>>() {
            @Override
            public void onResponse(Call<List<RetroLogin>> call, Response<List<RetroLogin>> response) {
                List<RetroLogin> x = response.body();
                System.out.println("\n\n"+x.size());
                if(x.size() != 0) {
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("username",username);
                    startActivity(intent);
                }
                else{
                    givenUser.setText("");
                    givenPassword.setText("");
                    txtView = (TextView)findViewById(R.id.invalidLogin);
                    txtView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<List<RetroLogin>> call, Throwable t) {}
        });
    }
    public void createAccount(View v){
        String username = givenUser.getText().toString();
        String password = givenPassword.getText().toString();
        GetDataService service = RetroClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<String> call = service.createAccount(username,password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful() && response.body().equals("Account created successfully")){
                    System.out.println("\n\'"+response.body()+"\'\n");
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("username",username);
                    startActivity(intent);
                }
                else{
                    takenUser = (TextView)findViewById(R.id.invalidLogin2);
                    takenUser.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}