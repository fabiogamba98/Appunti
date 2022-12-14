package com.example.appunti.main;

import android.util.Log;

import com.example.appunti.data.Nota;
import com.example.appunti.data.NoteDataSource;
import com.example.appunti.data.NoteRepository;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter implements MainContract.Presenter{
    private final NoteRepository mNoteRepository;

    private final MainContract.View mNoteView;

    public MainPresenter(NoteRepository noteRepository, MainContract.View view){
        mNoteRepository = noteRepository;
        mNoteView = view;
        mNoteView.setPresenter(this);
    }

    @Override
    public void start() {
        //il presenter recupera le note dal model, siamo alla prima apertura e quindi è necessario
        Log.d("Presenter", "sono all'inizio di start");
        mNoteRepository.getNote(new NoteDataSource.LoadNoteCallback() {
            @Override
            public void onNoteLoaded(List<Nota> notes) {
                Log.d("presenter", "sei dentro");
                if(notes.size() > 0) {
                    mNoteView.updateNoteOnView(notes);
                }else{
                    mNoteView.updateNoteOnView(new ArrayList<>());  //non è un errore, semplicemente non ci sono note salvate
                }
            }

            @Override
            public void onDataNotAvailable() {
                mNoteView.onDataNotAvailable();
            }
        });
    }
}
