package com.example.appunti.details;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

//import com.example.appunti.Database.AppDatabase;
import com.example.appunti.R;
import com.example.appunti.data.Nota;
import com.example.appunti.data.NoteRepository;
import com.example.appunti.data.remote.NoteRemoteDataSource;
import com.example.appunti.details.DetailsActivityContract;
import com.example.appunti.edit.EditActivity;
import com.example.appunti.listener.MyOnClickDetailsListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailsActivity extends AppCompatActivity implements DetailsActivityContract.View {

    private static final String TAG = DetailsActivity.class.getName();
    private static final String KEY = TAG + ".key";

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView textNote;

    private MyOnClickDetailsListener myOnClickDetailsListener;

    private DetailsActivityContract.Presenter mPresenter;

    public void setMyOnClickDetailsListener(MyOnClickDetailsListener myOnClickDetailsListener) {
        this.myOnClickDetailsListener = myOnClickDetailsListener;
    }

    private Nota nota;

    private ActivityResultLauncher<Intent> editActivityLauncher;

    private void createFragment() {
        DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentAnchor);

        //necessario per il ripristino alla rotazione
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(detailsFragment == null) {
            detailsFragment = DetailsFragment.newInstance(null, null);
            fragmentTransaction.add(R.id.fragmentAnchor, detailsFragment);
            fragmentTransaction.commit();
        }

        //il presenter qui lo salvo perche gli imposto l'id della nota da andare a recuperare
        mPresenter = new DetailsActivityPresenter(NoteRepository.getInstance(NoteRemoteDataSource.getInstance()),this);
        new DetailsFragmentPresenter(NoteRepository.getInstance(NoteRemoteDataSource.getInstance()),detailsFragment);

        //settaggio del presenter sulla base di cosa ho ricevuto dall'intent, getting the nota by the id passed in the intent
        nota = (Nota) getIntent().getSerializableExtra(KEY);        //Nota nota = (Nota) getIntent().getExtras().getSerializable(KEY); che è uguale
        assert nota !=null;  //lancia un'eccezione
        mPresenter.setNotaId(nota.getId());

        //sembrerebbe sia l'azione di elimina per ricondursi all'alertView sul fragment
        DetailsFragment finalDetailsFragment = detailsFragment;
        setMyOnClickDetailsListener(new MyOnClickDetailsListener() {
            @Override
            public void onClick(Nota nota) {
                finalDetailsFragment.onClick(nota);
            }
        });
        /*setMyOnClickDetailsListener(nota -> {
            finalDetailsFragment.onClick(nota);
        });*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        createFragment();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);

        editActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Activity is OK, bisogna segnalare all'activity precedente che ho finito
                        createSnackOk();
                    }
                });

        FloatingActionButton editFab = findViewById(R.id.editFab);
        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onEditFabClick();
            }
        });

        FloatingActionButton deleteFab = findViewById(R.id.deleteFab);
        deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myOnClickDetailsListener.onClick(nota);
            }
        });
    }

    //lifeCycle utilizzato sia in apertura subito dopo l'onCreate, sia al recupero da rotazione e cambio di focus
    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }


    @Override
    public void showTitle(String titolo) {
        collapsingToolbarLayout.setTitle(titolo);
    }

    @Override
    public void startingEditActivity(Nota nota) {
        editActivityLauncher.launch(EditActivity.getIntentEdit(getApplicationContext(), 'e', nota));
    }

    @Override
    public void createSnackOk() {
        Snackbar.make(findViewById(R.id.viewLayout), "Nota salvata", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDataNotAvailable() {
        Snackbar.make(findViewById(R.id.viewLayout), "Errore caricamento nota", Snackbar.LENGTH_SHORT).show();
    }

    public static Intent getDetailsIntent(Context context, Nota nota) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(KEY, nota);
        return intent;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}