package com.example.pjaidmobile;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // uruchomienie TicketDetailActivity
        Intent intent = new Intent(MainActivity.this, TicketDetailActivity.class);
        intent.putExtra("TICKET_ID", "TK001");
        startActivity(intent);

        finish();
    }
}
