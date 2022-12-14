package com.example.appunti.main;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

//import com.example.appunti.Database.AppDatabase;
import com.example.appunti.edit.EditActivity;
import com.example.appunti.R;
import com.example.appunti.data.NoteRepository;
import com.example.appunti.data.remote.NoteRemoteDataSource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> editActivityLauncher;
    private ActivityResultLauncher<Intent> detailsActivityLauncher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*int reqCode = 1;
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        showNotification(this, "Title", "This is the message to display", intent, reqCode);*/

        Log.d("Main activity", "Sono nel main");

        //setting up the fragment
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        //istanzio il fragment solo se non è già stato creato, recupero da rotazione
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentAnchor);
        if(mainFragment == null) {
            mainFragment = MainFragment.newInstance(null, null);
            fragmentTransaction.add(R.id.fragmentAnchor, mainFragment);
            fragmentTransaction.commit();
        }

        //creating the presenter
        new MainPresenter(NoteRepository.getInstance(NoteRemoteDataSource.getInstance()), mainFragment);

        editActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK)
                    createSnackOk();
            });

        detailsActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK)
                    deleteSnackOk();
            });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editActivityLauncher.launch(EditActivity.getIntentEdit(getApplicationContext(), 'a'));
            }
        });


    }

    private void deleteSnackOk() {
        Snackbar.make(findViewById(R.id.viewLayout), "Nota cancellata", Snackbar.LENGTH_SHORT).show();
    }

    private void createSnackOk() {
        Snackbar.make(findViewById(R.id.viewLayout), "Nota salvata", Snackbar.LENGTH_SHORT).show();
    }

    /*public void showNotification(Context context, String title, String message, Intent intent, int reqCode) {
        //SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(context);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_IMMUTABLE);
        String CHANNEL_ID = "channel_name";// The id of the channel.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_icon_app)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(reqCode, notificationBuilder.build()); // 0 is the request code, it should be unique id

        Log.d("showNotification", "showNotification: " + reqCode);
    }*/
}