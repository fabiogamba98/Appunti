package com.example.appunti.details;

import android.util.Log;

import com.example.appunti.data.Nota;
import com.example.appunti.data.NoteDataSource;
import com.example.appunti.data.NoteRepository;
import com.example.appunti.edit.EditActivity;

public class DetailsActivityPresenter implements DetailsActivityContract.Presenter{
    private final NoteRepository mNoteRepository;
    private final DetailsActivityContract.View mNoteView;

    private Nota notaLocal = null;
    public DetailsActivityPresenter(NoteRepository mNoteRepository, DetailsActivityContract.View mNoteView) {
        this.mNoteRepository = mNoteRepository;
        this.mNoteView = mNoteView;
    }

    @Override
    public void start() {
        mNoteRepository.getNota(String.valueOf(notaLocal.getId()), new NoteDataSource.GetNotaCallback() {
            @Override
            public void onNotaLoaded(Nota nota) {
                //Log.d("presenter", notaLocal.getTitle());
                notaLocal = nota;
                mNoteView.showTitle(notaLocal.getTitle());
            }

            @Override
            public void onDataNotAvailable() {
                mNoteView.onDataNotAvailable();
            }
        });
    }

    @Override
    public void setNotaId(int notaID) {
        Log.d("presenter id della nota", String.valueOf(notaID));
        notaLocal = new Nota(null, null, notaID);
    }

    @Override
    public void onEditFabClick() {
        Log.d("presenter importante", String.valueOf(notaLocal.getId()));
        mNoteView.startingEditActivity(notaLocal);
    }
}
