package com.example.appunti.data;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NoteRepository implements NoteDataSource{

    private static NoteRepository INSTANCE = null;

    private final NoteDataSource mNoteRemoteDataSource;

    private static List<Nota> mCachedNotes;

    private NoteRepository(@NotNull NoteDataSource mNoteRemoteDataSource){
        this.mNoteRemoteDataSource = mNoteRemoteDataSource;
    }

    public static NoteRepository getInstance(NoteDataSource noteRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new NoteRepository(noteRemoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getNote(@NonNull LoadNoteCallback callback) {
        if(mCachedNotes !=null) {
            callback.onNoteLoaded(mCachedNotes);
            return;
        }
        getNoteFromRemoteDataSource(callback);
    }

    @Override
    public void getNota(@NonNull String notaId, @NonNull GetNotaCallback callback) {
        //if(mCachedNotes==null){
            mNoteRemoteDataSource.getNota(notaId, new GetNotaCallback() {
                @Override
                public void onNotaLoaded(Nota nota) {
                    callback.onNotaLoaded(nota);

                }
                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        /*} else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                Optional<Nota> res = mCachedNotes.stream().filter(n -> String.valueOf(n.getId()).equals(notaId)).findFirst();
                callback.onNotaLoaded(res.get());
            }
        }*/
    }

    private void getNoteFromRemoteDataSource(@NonNull final LoadNoteCallback callback) {
        mNoteRemoteDataSource.getNote(new LoadNoteCallback() {
            @Override
            public void onNoteLoaded(List<Nota> notes) {
                refreshCache(notes);
                //refreshLocalDataSource(tasks);
                callback.onNoteLoaded(mCachedNotes);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Nota> notes) {
        if (mCachedNotes == null) {
            mCachedNotes = new ArrayList<>();
        }
        mCachedNotes.clear();
        for (Nota n : notes) {
            mCachedNotes.add(n);
        }
        //mCacheIsDirty = false;
    }

    @Override
    public void saveNota(@NonNull Nota nota, @NonNull SaveNotaCallback callback) {
        mNoteRemoteDataSource.saveNota(nota, callback);

    }

    @Override
    public void addNota(@NonNull Nota nota) {
        mNoteRemoteDataSource.addNota(nota);
    }

    @Override
    public void refreshNote() {

    }

    @Override
    public void deleteAllNote() {

    }

    @Override
    public void deleteNota(@NonNull Nota nota) {
        mNoteRemoteDataSource.deleteNota(nota);
    }
}
