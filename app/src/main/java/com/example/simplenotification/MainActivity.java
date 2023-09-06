package com.example.simplenotification;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "NOTIFICATION";
    private static final int NOTIFICATION_ID = 101;
    EditText titleText, descriptionText;
    Button createButton;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findAllView();

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            return;
        }
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    sendNotification();
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
            private void sendNotification() {
                createNotificationChannel();
                String title = titleText.getText().toString();
                String description = descriptionText.getText().toString();

                Intent intent = new Intent(getApplicationContext(),targetActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.noticon)
                        .setContentTitle(title)
                        .setContentText(description)
                        .setContentIntent(pendingIntent) // Set the PendingIntent to open MainActivity
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(false);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                    return;
                }
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            }
        });

    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Step 6: Create the notification channel
            CharSequence name = "Offer";
            String description = "This is my notification channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Step 7: Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void findAllView() {
        titleText = findViewById(R.id.titleET);
        descriptionText = findViewById(R.id.descET);
        createButton = findViewById(R.id.cretaeBTN);
    }


}