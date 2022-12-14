package com.example.appunti.edit;

import android.util.Log;

import com.example.appunti.data.Nota;
import com.example.appunti.data.NoteDataSource;
import com.example.appunti.data.NoteRepository;
import com.example.appunti.details.DetailsActivity;

public class EditFragmentPresenter implements EditContract.Presenter{
    private NoteRepository mNoteRepository;
    private final EditContract.View mView;

    public EditFragmentPresenter(NoteRepository noteRepository, EditContract.View view){
        mNoteRepository = noteRepository;
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start(){

    }


    @Override
    public void saveNote(Nota nota) {
        mNoteRepository.saveNota(nota, new NoteDataSource.SaveNotaCallback() {
            @Override
            public void onNotaSaved(Nota nota) {
                Log.d("nuovo id forse", String.valueOf(nota.getId()));
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

    }

    @Override
    public void newNote(Nota nota) {
        mNoteRepository.addNota(nota);
    }
}
