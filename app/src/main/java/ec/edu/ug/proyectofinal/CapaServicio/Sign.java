package ec.edu.ug.proyectofinal.CapaServicio;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import ec.edu.ug.proyectofinal.CapaPresentacion.CursosActivity;

public class Sign {
    private String serverClientId;
    public void signInWithGoogle(Context context) {
        serverClientId = "877166692771-262bajal84j4p0h351jhvl9njb6futeu.apps.googleusercontent.com";

        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder().setFilterByAuthorizedAccounts(false).setServerClientId(serverClientId).setAutoSelectEnabled(false).build();

        GetCredentialRequest request = new GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build();

        CredentialManager credentialManager = CredentialManager.create(context);

        credentialManager.getCredentialAsync(context, request, null, ContextCompat.getMainExecutor(context),
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse response) {
                        // Credencial obtenida de forma exitosa
                        Credential credential = response.getCredential();

                        if (credential instanceof CustomCredential &&
                                credential.getType().equals(GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
                            try {
                                GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.getData());
                                String idToken = googleIdTokenCredential.getIdToken();

                                // Envía este idToken a Firebase o a tu servidor backend
                                Log.d("GoogleLogin", "Token obtenido: " + idToken);
                                AuthCredential credentials = GoogleAuthProvider.getCredential(idToken, null);

                                FirebaseAuth.getInstance().signInWithCredential(credentials).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        // El usuario inició sesión correctamente en Firebase
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        Log.d("FirebaseLogin", "Bienvenido: " + user.getDisplayName());

                                        // Redirigir a la pantalla principal de tu app
                                        Intent intent = new Intent(context, CursosActivity.class);
                                        context.startActivity(intent);
                                    } else {
                                        // Error al autenticar con Firebase
                                        Log.e("FirebaseLogin", "Fallo en Firebase: ", task.getException());
                                    }
                                });


                            } catch (Exception e) {
                                Log.e("GoogleLogin", "Error al procesar el token de Google: " + e.getMessage());
                            }
                        } else {
                            Log.w("GoogleLogin", "Tipo de credencial no esperado.");
                        }
                    }

                    @Override
                    public void onError(GetCredentialException e) {
                        // Manejo de errores globales del flujo
                        Log.e("GoogleLogin", "Error en el inicio de sesión: " + e.getMessage());
                    }
                }
        );
    }
}
