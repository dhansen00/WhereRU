package com.example.whereruapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.EmptyStackException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A login screen that allows the user to login or signup
 */
public class LoginActivity extends AppCompatActivity {
    public EditText givenUser;
    public EditText givenPassword;
    public TextView txtView;
    public TextView takenUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //create the display
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        //get the views needed for operations
        this.givenUser = (EditText) findViewById(R.id.editUsername);
        this.givenPassword = (EditText) findViewById(R.id.editPassword) ;
        this.txtView = (TextView)findViewById(R.id.invalidLogin);
    }

    /**
     * Logs in the user with the credentials in the text fields
     * @param view The view of the login button
     */
    public void login(View view){
        //Gets the text from the text fields
        Editable text = this.givenUser.getText();
        String username = text.toString();
        Editable text1 = this.givenPassword.getText();
        String password = text1.toString();

        //performs a call to the db to verify the login
        Retrofit instance = RetroClientInstance.getRetrofitInstance();
        GetDataService service = instance.create(GetDataService.class);
        Call<List<RetroLogin>> call = service.getLogin(username,password);
        call.enqueue(new Callback<List<RetroLogin>>() {
            @Override
            public void onResponse(Call<List<RetroLogin>> call, Response<List<RetroLogin>> response) {
                //upon successful login
                List<RetroLogin> x = response.body();

                //verify that the login was correct
                assert x != null;
                if (x.isEmpty()) {
                    LoginActivity.this.givenUser.setText("");
                    LoginActivity.this.givenPassword.setText("");
                    LoginActivity.this.txtView = (TextView) LoginActivity.this.findViewById(R.id.invalidLogin);
                    LoginActivity.this.txtView.setVisibility(View.VISIBLE);
                } else {
                    //if the login was correct change screens to the MapsActivity
                    Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                    intent.putExtra("username", username);
                    LoginActivity.this.startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<List<RetroLogin>> call, Throwable t) {
                throw new EmptyStackException();
            }
        });
    }

    /**
     * Creates an account based off of the credentials in the text fields
     * @param view the view of the create account button
     */
    public void createAccount(View view){
        //get the text from the text views
        Editable text = this.givenUser.getText();
        String username = text.toString();
        Editable text1 = this.givenPassword.getText();
        String password = text1.toString();

        //perform a call to the db to create an account
        GetDataService service = RetroClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<String> call = service.createAccount(username,password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //Upon receipt of a response verify that the account was created
                String body = response.body();
                if(response.isSuccessful() && "Account created successfully".equals(body)){
                    //if created successfully send the user to the MapsActivity
                    Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                    intent.putExtra("username",username);
                    LoginActivity.this.startActivity(intent);
                }
                else{
                    // the account was not created successfully so let the user know with error text
                    LoginActivity.this.takenUser = (TextView) LoginActivity.this.findViewById(R.id.invalidLogin2);
                    LoginActivity.this.takenUser.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                throw new EmptyStackException();
            }
        });
    }


}