package com.example.appunti.details;

import androidx.annotation.NonNull;

import com.example.appunti.data.Nota;
import com.example.appunti.data.NoteDataSource;
import com.example.appunti.data.NoteRepository;

public class DetailsFragmentPresenter implements DetailsFragmentContract.Presenter{
    private final NoteRepository mNoteRepository;
    private final DetailsFragmentContract.View mNoteView;

    private Nota notaLocal = null;

    public DetailsFragmentPresenter(NoteRepository noteRepository, DetailsFragmentContract.View noteView) {
        mNoteRepository = noteRepository;
        mNoteView = noteView;
        mNoteView.setPresenter(this);
    }

    @Override
    public void start() {
        getNotaById(notaLocal.getId());
    }

    private void getNotaById(int id) {
        mNoteRepository.getNota(String.valueOf(id), new NoteDataSource.GetNotaCallback() {
            @Override
            public void onNotaLoaded(Nota nota) {
                notaLocal = nota;
                mNoteView.showNota(notaLocal);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void setNotaId(String notaId) {
        notaLocal = new Nota(null,null,Integer.parseInt(notaId));
    }

    @Override
    public void deleteSelectedNotes(Nota nota) {
        mNoteRepository.deleteNota(nota);
    }
}
