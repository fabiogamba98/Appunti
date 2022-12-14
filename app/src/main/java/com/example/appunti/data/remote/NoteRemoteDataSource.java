package com.example.appunti.data.remote;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.appunti.data.Nota;
import com.example.appunti.data.NoteDataSource;
import com.example.appunti.data.NoteRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NoteRemoteDataSource implements NoteDataSource {

    private static NoteRemoteDataSource INSTANCE = null;

    private static DatabaseReference myRef;

    private static List<Nota> notes = new ArrayList<>();

    private NoteRemoteDataSource(){
        myRef = FirebaseDatabase.getInstance("https://appunti-4bd4c-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        handleListener();
    }

    public static NoteRemoteDataSource getInstance(){
        if(INSTANCE == null){
            INSTANCE = new NoteRemoteDataSource();
        }
        return INSTANCE;
    }

    private void handleListener() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        notes.clear();
                        populateList(snapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void populateList(DataSnapshot snapshot){
        for(DataSnapshot obj : snapshot.getChildren()){
            if(obj.getValue(Nota.class) != null)
                notes.add(obj.getValue(Nota.class));
            Log.d("sto caricando le note", String.valueOf(obj.getValue(Nota.class)));
        }
    }

    @Override
    public void getNote(@NonNull LoadNoteCallback callback) {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        notes.clear();
                        populateList(snapshot);
                        Log.d("Remote data", "Finito di caricare l'array");
                        callback.onNoteLoaded(notes);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public void getNota(@NonNull String notaId, @NonNull GetNotaCallback callback) {
        myRef.child(notaId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Nota nota = task.getResult().getValue(Nota.class);
                Log.d("Remote", nota.getTitle());
                callback.onNotaLoaded(nota);
            }
        });
    }

    @Override
    public void saveNota(@NonNull Nota nota, @NonNull SaveNotaCallback callback) {
        int idTimeMillis = (int) (System.currentTimeMillis() / 1000);
        myRef.child(String.valueOf(nota.getId())).removeValue();
        //nota.setId(idTimeMillis);
        myRef.child(String.valueOf(nota.getId())).setValue(nota).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onNotaSaved(nota);
            }
        });

    }

    @Override
    public void addNota(@NonNull Nota nota) {
        myRef.child(String.valueOf(nota.getId())).setValue(nota).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    @Override
    public void refreshNote() {

    }

    @Override
    public void deleteAllNote() {

    }

    @Override
    public void deleteNota(@NonNull Nota nota) {
        myRef.child(String.valueOf(nota.getId())).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // MainActivity.eliminateSnackOk();
                    }
        });
    }
}
