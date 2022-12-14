package com.example.appunti.details;

import com.example.appunti.BasePresenter;
import com.example.appunti.BaseView;
import com.example.appunti.data.Nota;

public interface DetailsFragmentContract {
    interface View extends BaseView<Presenter>{
        void showNota(Nota nota); //refactor/pull members up

        void showAlert(String title, String message, Nota nota);

    }
    interface Presenter extends BasePresenter {

        void setNotaId(String notaId);

        void deleteSelectedNotes(Nota nota);
    }
}
