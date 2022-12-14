package com.example.appunti.details;

import com.example.appunti.BasePresenter;
import com.example.appunti.data.Nota;

public interface DetailsActivityContract {
    interface View{
        void showTitle(String titolo);

        void startingEditActivity(Nota nota);

        void createSnackOk();

        void onDataNotAvailable();
    }

    interface Presenter extends BasePresenter {
        void setNotaId(int notaID);

        void onEditFabClick();
    }
}
