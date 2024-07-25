package com.example.coffeeorderapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class LoginActivity extends AppCompatActivity {
    EditText emailEditText, passwordEditText;
    Button loginButton;
    Button redirectToRegisterButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText = findViewById(R.id.login_emailEditText);
        passwordEditText = findViewById(R.id.login_passwordEditText);

        loginButton = findViewById(R.id.login_loginButton);
        redirectToRegisterButton = findViewById(R.id.login_redirectToRegisterButton);
        mAuth = FirebaseAuth.getInstance();

        redirectToRegisterButton.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });

        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(LoginActivity.this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Giriş başarılı", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class)); // MainActivity'e yönlendirme
                    finish();
                } else {
                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                    handleSignInError(errorCode);
                }
            });
        });
    }

    private void handleSignInError(String errorCode) {
        switch (errorCode) {
            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(LoginActivity.this, "Geçersiz özel token.", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(LoginActivity.this, "Token ile Firebase UID eşleşmiyor.", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(LoginActivity.this, "Geçersiz kimlik bilgisi.", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_INVALID_EMAIL":
                emailEditText.setError("Geçersiz e-posta formatı.");
                emailEditText.requestFocus();
                break;
            case "ERROR_WRONG_PASSWORD":
                passwordEditText.setError("Yanlış şifre.");
                passwordEditText.requestFocus();
                passwordEditText.setText("");
                break;
            case "ERROR_USER_MISMATCH":
                Toast.makeText(LoginActivity.this, "E-posta, kimlik doğrulaması yapmış kullanıcıyla eşleşmiyor.", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(LoginActivity.this, "Güvenlik nedenleriyle yakın zamanda tekrar giriş yapmalısınız.", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(LoginActivity.this, "Bu e-posta ile başka bir kimlik doğrulama sağlayıcısı zaten mevcut.", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_EMAIL_ALREADY_IN_USE":
                emailEditText.setError("E-posta zaten başka bir hesap tarafından kullanılıyor.");
                emailEditText.requestFocus();
                break;
            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(LoginActivity.this, "Bu kimlik bilgileri başka bir kullanıcı tarafından zaten kullanılıyor.", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_USER_DISABLED":
                Toast.makeText(LoginActivity.this, "Bu kullanıcı hesabı devre dışı bırakıldı.", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(LoginActivity.this, "Kullanıcı token süresi dolmuş.", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_USER_NOT_FOUND":
                emailEditText.setError("Bu e-posta ile kayıtlı kullanıcı bulunamadı.");
                emailEditText.requestFocus();
                break;
            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(LoginActivity.this, "Geçersiz kullanıcı token.", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(LoginActivity.this, "Bu işlemi yapmak için izin yok.", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(LoginActivity.this, "Giriş başarısız: " + errorCode, Toast.LENGTH_LONG).show();
                break;
        }
    }
}