package com.example.owner.fictapp;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class EmailForm extends AppCompatActivity {

    private static String email = " ";
    private static String subject = " ";
    private static String body = " ";

    TextInputLayout s;
    TextInputLayout b;
    AppCompatButton send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_form);

        email = getIntent().getExtras().getString("email");

        email = "jamesborg2012@gmail.com";

        s = (TextInputLayout) findViewById(R.id.subject);
        b = (TextInputLayout) findViewById(R.id.body);
        send = (AppCompatButton) findViewById(R.id.btn_send);

        subject = s.getEditText().getText().toString();
        body = b.getEditText().getText().toString();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    private void sendEmail()
    {
        Log.e("Send Email", "");
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setData(Uri.parse("mailto:"));
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(i, "Send Email with..."));
        finish();
        Log.e("Finished sending Email", "");

    }
}
