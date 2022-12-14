package com.example.appunti.data;

import androidx.annotation.NonNull;

import java.util.List;

public interface NoteDataSource {
    interface LoadNoteCallback {

        void onNoteLoaded(List<Nota> note);

        void onDataNotAvailable();
    }

    interface GetNotaCallback {

        void onNotaLoaded(Nota nota);

        void onDataNotAvailable();
    }

    interface SaveNotaCallback {

        void onNotaSaved(Nota nota);

        void onDataNotAvailable();
    }

    void getNote(@NonNull LoadNoteCallback callback);

    void getNota(@NonNull String notaId, @NonNull GetNotaCallback callback);

    void saveNota(@NonNull Nota nota, @NonNull SaveNotaCallback callback);

    void addNota(@NonNull Nota nota);


    void refreshNote();

    void deleteAllNote();

    void deleteNota(@NonNull Nota nota);
}
