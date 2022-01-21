package com.example.app_dota;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_dota.interfaces.UserApi;
import com.example.app_dota.model.Users;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    //Dialog Variables
    AlertDialog.Builder builderDialog;
    AlertDialog alertDialog;

    TextView bienvenidoLabel,continuarLabel,nuevoUsuario;
    ImageView loginImageView;
    TextInputLayout usuarioTextField, contrasenaTextField;
    MaterialButton inicioSesion;

    //EDIT
    EditText usuarioEditText,contrasenaEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginImageView = findViewById(R.id.loginImageView);
        bienvenidoLabel = findViewById(R.id.bienvenidoLabel);
        continuarLabel = findViewById(R.id.continuarLabel);
        usuarioTextField = findViewById(R.id.usuarioTextField);
        contrasenaTextField = findViewById(R.id.contrasenaTextField);
        inicioSesion = findViewById(R.id.inicioSesion);
        nuevoUsuario = findViewById(R.id.nuevoUsuario);

        //EDIT
        usuarioEditText = findViewById(R.id.usuarioEditText);
        contrasenaEditText = findViewById(R.id.contrasenaEditText);

        inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Validacion();
                //showAlertDialog(R.layout.my_error_dialog_login);
            }
        });

        nuevoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);

                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(loginImageView, "logoImageTrans");
                pairs[1] = new Pair<View, String>(bienvenidoLabel, "textTrans");
                pairs[2] = new Pair<View, String>(continuarLabel, "iniciaSesionTextTrans");
                pairs[3] = new Pair<View, String>(usuarioTextField, "emailInputTextTrans");
                pairs[4] = new Pair<View, String>(contrasenaTextField, "passwordInputTextTrans");
                pairs[5] = new Pair<View, String>(inicioSesion, "buttonSignInTrans");
                pairs[6] = new Pair<View, String>(nuevoUsuario, "newUserTrans");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
                    startActivity(intent, options.toBundle());
                }else {
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    private void Validacion(){
        String email = usuarioEditText.getText().toString();
        String password = contrasenaEditText.getText().toString();
        if(email.isEmpty()) {
            usuarioEditText.setError("No se ingreso el email");
        }
        if(password.isEmpty()) {
            contrasenaEditText.setError("No se ingreso la contraseña");
        }
        if(!email.isEmpty() & !password.isEmpty()){
            LoginUser();
        }
    }

    private void LoginUser(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://61e8cd1e7ced4a00172ff6a0.mockapi.io/App_Dota/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);

        String email = usuarioEditText.getText().toString();
        String password = contrasenaEditText.getText().toString();
        Call<List<Users>> call = service.getUser(email);
        call.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                if(!response.isSuccessful()){
                    showAlertDialog(R.layout.my_error_dialog_login);
                    return;
                }
                List<Users> user = response.body();

                if(user.size()==1){
                    for (Users Objuser : user) {
                        if(Objuser.getEmail().equals(email) & Objuser.getPassword().equals(password)){

                            Intent intent = new Intent(LoginActivity.this, StoreMenuActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(LoginActivity.this,"Bienvenido",Toast.LENGTH_SHORT).show();

                        }
                        else {
                            showAlertDialog(R.layout.my_error_dialog_login);
                        }
                    }
                }else {
                    showAlertDialog(R.layout.my_error_dialog_login);
                }

            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                showAlertDialog(R.layout.my_error_dialog_login);
            }
        });
    }

    private void showAlertDialog(int myLayout){
        builderDialog = new AlertDialog.Builder(this);
        View layoView = getLayoutInflater().inflate(myLayout, null);

        builderDialog.setView(layoView);
        alertDialog = builderDialog.create();
        alertDialog.show();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               alertDialog.dismiss();

            }
        }, 3000);


    }
}