package com.example.owner.fictapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SignIn extends AppCompatActivity {

    DatabaseHelper helper = new DatabaseHelper(this);
    private static final String TAG = "SignupActivity";
    public TextInputLayout editname;
    public TextInputLayout editemail;
    public TextInputLayout editpassword;
    public Spinner dropdown;
    public Spinner dropdown2;
    public AppCompatButton submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        editname= (TextInputLayout)findViewById(R.id.name);
        editemail = (TextInputLayout)findViewById(R.id.email);
        editpassword= (TextInputLayout)findViewById(R.id.password);
        dropdown = (Spinner)findViewById(R.id.spinner1);
        dropdown2 = (Spinner)findViewById(R.id.spinner2);
        submit=(AppCompatButton)findViewById(R.id.btn_signup);
        String[] items = new String[]{"CCE", "CPS", "ICS", "CIS"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        String[] items2 = new String[]{"1st Year", "2nd Year", "3rd Year"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        dropdown2.setAdapter(adapter2);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }
        submit.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(SignIn.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = editname.getEditText().getText().toString();
        String email = editemail.getEditText().getText().toString();
        String password = editpassword.getEditText().getText().toString();
        String course = dropdown.getSelectedItem().toString();
        String year = dropdown2.getSelectedItem().toString();
        int yearInt=0;
        if(year=="1st Year")
        {
           yearInt=1;
        }
        else if(year=="2nd Year")
        {
            yearInt=2;
        }
        else if(year=="3rd Year")
        {
            yearInt=3;
        }

        Users u=new Users();
        u.setname(name);
        u.setemail(email);
        u.setPassword(password);
        u.setCourse(course);
        u.setYear(yearInt);
        helper.insertUsers(u);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onSignupSuccess() {
        submit.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
        Intent i = new Intent(SignIn.this, UserChoice.class);
        startActivity(i);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        submit.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = editname.getEditText().getText().toString();
        String email = editemail.getEditText().getText().toString();
        String password = editpassword.getEditText().getText().toString();

        if (name.isEmpty()) {
            editname.setError("Name cannot be left empty");
            valid = false;
        } else {
            editname.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editemail.setError("enter a valid email address");
            valid = false;
        } else {
            editemail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            editpassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            editpassword.setError(null);
        }

        return valid;
    }





}
