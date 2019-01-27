package alarma.example.com.alarmaph;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;

    EditText user;
    EditText pass;
    Button login, reg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.user = (EditText) findViewById(R.id.editText3);
        this.pass = (EditText) findViewById(R.id.editText4);
        this.login = (Button) findViewById(R.id.button);
        this.reg = (Button) findViewById(R.id.button2);



        login.setOnClickListener(this);
        reg.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        if(!isNetworkConnected()){
            Toast.makeText(this, "No Internet Connect. Please check your Connection", Toast.LENGTH_SHORT).show();
        }else{
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Intent intent = new Intent(this, browseactivity.class);
                startActivity(intent);
            }
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    public void goTo(){
        Intent intent = new Intent(this, browseactivity.class);
        startActivity(intent);
        Toast.makeText(this, "Welcome!.", Toast.LENGTH_SHORT).show();
    }

    public void loginError(){
        if(!isNetworkConnected()){
            Toast.makeText(this, "No Internet Connection. Please check your Connection", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Invalid Password or email.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.button) {

            String user = this.user.getText().toString();
            String pass = this.pass.getText().toString();
            if (TextUtils.isEmpty(user)) {
                Toast.makeText(getApplicationContext(), "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(user, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                goTo();
                            } else {
                                loginError();
                            }
                        }
                    });
        }
        else{
            Intent intent = new Intent(this,registration.class);
            startActivity(intent);;
        }
    }
}
