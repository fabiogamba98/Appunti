package com.example.appunti.edit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

//import com.example.appunti.Database.AppDatabase;
import com.example.appunti.R;
import com.example.appunti.data.Nota;
import com.example.appunti.data.NoteRepository;
import com.example.appunti.data.remote.NoteRemoteDataSource;
import com.example.appunti.listener.MyOnEditNoteListener;
import com.example.appunti.listener.MyOnNewNoteListener;
import com.example.appunti.main.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = EditActivity.class.getName();
    private static final String ACTION_KEY = TAG + ".action.key";
    private static final String NOTA_KEY = TAG + ".nota.key";

    private char action;
    private Nota nota;

    private EditText editTitle;

    private MyOnNewNoteListener myOnNewNoteListener;
    private MyOnEditNoteListener myOnEditNoteListener;

    private void setMyOnNewNoteListener(MyOnNewNoteListener myOnNewNoteListener){
        this.myOnNewNoteListener = myOnNewNoteListener;
    }

    private void setMyOnEditNoteListener(MyOnEditNoteListener myOnEditNoteListener){
        this.myOnEditNoteListener = myOnEditNoteListener;
    }


    private void settingListeners(EditFragment editFragment){
        setMyOnNewNoteListener(new MyOnNewNoteListener() {
            @Override
            public void onNewNote(Nota nota) {
                editFragment.onNewNote(nota);
            }
        });

        setMyOnEditNoteListener(new MyOnEditNoteListener() {
            @Override
            public void onEditNote(Nota nota) {
                editFragment.onEditNote(nota);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTitle = findViewById(R.id.editTitle);

        action = getIntent().getCharExtra(ACTION_KEY, 'n');

        nota = (Nota) getIntent().getSerializableExtra(NOTA_KEY);


        final Nota nota = (Nota) getIntent().getSerializableExtra(NOTA_KEY);


        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        EditFragment editFragment = (EditFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentAnchor);
        if(editFragment == null) {
            editFragment = EditFragment.newInstance(null, null);
            fragmentTransaction.add(R.id.fragmentAnchor, editFragment);
            fragmentTransaction.commit();
        }

        //imposto i pulsanti sulla CollapsingToolbar
        settingListeners(editFragment);

        //presenter del fragment
        new EditFragmentPresenter(NoteRepository.getInstance(NoteRemoteDataSource.getInstance()), editFragment);



        if(action == 'e'){
            editTitle.setText(nota.getTitle());
        }



        /*db = Room.databaseBuilder(this, AppDatabase.class, "Note Database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();*/

        //Log.d("Id nota nell'edit", String.valueOf(nota.getId()));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTitle.getText().toString().trim();

                int idTimeMillis = (int) (System.currentTimeMillis() / 1000);
                if(!title.isEmpty()){
                    if(action == 'a') { //aggiungi al db
                        myOnNewNoteListener.onNewNote(new Nota(editTitle.getText().toString().trim(), null, idTimeMillis));
                        setResult(Activity.RESULT_OK);
                        finish();
                        //myRef.child(String.valueOf(idTimeMillis)).setValue(new Nota(title, noteText, idTimeMillis));
                        //db.noteDao().insertAll(new Nota(title, noteText));
                        //Snackbar.make(view, "Nota salvata", Snackbar.LENGTH_SHORT).show();
                        /*InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                finish();
                            }
                        }, 1500);*/

                    } else if(action == 'e'){
                        int id = nota.getId();
                        myOnEditNoteListener.onEditNote(new Nota(editTitle.getText().toString().trim(), null, nota.getId()));
                        setResult(Activity.RESULT_OK);
                        finish();
                        //myRef.child(String.valueOf(id)).removeValue();
                        //myRef.child(String.valueOf(idTimeMillis)).setValue(new Nota(title, noteText, idTimeMillis));
                        //db.noteDao().deleteNota(nota);
                        //db.noteDao().insertAll(new Nota(title, noteText));
                        //Snackbar.make(view, "Nota salvata", Snackbar.LENGTH_SHORT).show();
                        /*InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                //startActivity(DetailsActivity.getDetailsIntent(EditActivity.this, new Nota(title, noteText)));

                                finish();
                            }
                        }, 1500);*/
                    }
                } else {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Snackbar.make(view, "Inserisci qualcosa", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }


    public static Intent getIntentEdit(Context context, char action){
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(ACTION_KEY, action);

        return intent;
    }

    public static Intent getIntentEdit(Context context, char action, Nota nota){
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(ACTION_KEY, action);
        intent.putExtra(NOTA_KEY, nota);

        return intent;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}