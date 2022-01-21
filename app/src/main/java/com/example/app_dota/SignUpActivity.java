package com.example.app_dota;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_dota.interfaces.UserApi;
import com.example.app_dota.model.Users;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    TextView nuevoUsuario,bienvenidoLabel,continuarLabel;
    ImageView singUpImageView;
    TextInputLayout usuarioSignUpTextField, contrasenaTextField,nameTextField;
    MaterialButton inicioSesion;

    //EDIT
    EditText nameAllEditText,emailEditText,passwodEditText,passwordConfirmeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        singUpImageView = findViewById(R.id.signUpImageView);
        bienvenidoLabel = findViewById(R.id.bienvenidoLabel);
        continuarLabel = findViewById(R.id.continuarLabel);
        usuarioSignUpTextField = findViewById(R.id.usuarioSignUpTextField);
        contrasenaTextField = findViewById(R.id.contrasenaTextField);
        inicioSesion = findViewById(R.id.inicioSesion);
        nuevoUsuario = findViewById(R.id.nuevoUsuario);

        //EDIT
        nameAllEditText = findViewById(R.id.nameAllEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwodEditText = findViewById(R.id.passwodEditText);
        passwordConfirmeEditText = findViewById(R.id.passwordConfirmeEditText);

        nuevoUsuario.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                transitionBack();
            }
        });
        inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validacion();
            }
        });
    }

    @Override
    public void onBackPressed(){
        transitionBack();
    }


    public void transitionBack(){
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);

        Pair[] pairs = new Pair[7];
        pairs[0] = new Pair<View, String>(singUpImageView, "logoImageTrans");
        pairs[1] = new Pair<View, String>(bienvenidoLabel, "textTrans");
        pairs[2] = new Pair<View, String>(continuarLabel, "iniciaSesionTextTrans");
        pairs[3] = new Pair<View, String>(usuarioSignUpTextField, "emailInputTextTrans");
        pairs[4] = new Pair<View, String>(contrasenaTextField, "passwordInputTextTrans");
        pairs[5] = new Pair<View, String>(inicioSesion, "buttonSignInTrans");
        pairs[6] = new Pair<View, String>(nuevoUsuario, "newUserTrans");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this,pairs);
            startActivity(intent, options.toBundle());

        }else {
            startActivity(intent);
            finish();
        }
    }
//nameAllEditText,emailEditText,passwodEditText,passwordConfirmeEditText;
    private void CreateUser(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://61e8cd1e7ced4a00172ff6a0.mockapi.io/App_Dota/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);

        Users user = new Users();
        user.setName(nameAllEditText.getText().toString());
        user.setEmail(emailEditText.getText().toString());
        if(passwodEditText.getText().toString().equals(passwordConfirmeEditText.getText().toString())) {
            user.setPassword(passwodEditText.getText().toString());
            Call<Users> call = service.createUser(user);
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(Call<Users> call, Response<Users> response) {
                    Intent intent = new Intent(SignUpActivity.this, StoreMenuActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(SignUpActivity.this,"Bienvenido",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Users> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this,"Error",Toast.LENGTH_SHORT).show();
                }
            });

        }
        if(!passwodEditText.getText().toString().equals(passwordConfirmeEditText.getText().toString())) {
            Toast.makeText(SignUpActivity.this,"La contraseña no coincide",Toast.LENGTH_SHORT).show();
        }



    }

    private void Validacion(){
        String email = emailEditText.getText().toString();
        String name = nameAllEditText.getText().toString();
        String password = passwodEditText.getText().toString();
        String passwordConfirm = passwordConfirmeEditText.getText().toString();

        if(email.isEmpty()) {
            emailEditText.setError("No se ingreso el email");
        }
        if(password.isEmpty()) {
            passwodEditText.setError("No se ingreso la contraseña");
        }
        if(passwordConfirm.isEmpty()) {
            passwordConfirmeEditText.setError("No se ingreso la contraseña");
        }
        if(name.isEmpty()) {
            nameAllEditText.setError("No se ingreso el nombre");
        }
        if(!email.isEmpty() & !password.isEmpty() & !name.isEmpty() & !passwordConfirm.isEmpty() ){
            CreateUser();
        }
    }

}