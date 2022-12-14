package com.example.appunti.main;

import com.example.appunti.BasePresenter;
import com.example.appunti.BaseView;
import com.example.appunti.data.Nota;

import java.util.List;

public interface MainContract {

    interface View extends BaseView<Presenter>{
        //aggiorno la lista delle note nella schermata iniziale
        void updateNoteOnView(List<Nota> notes);
        //chiamata se ottengo errore dal model
        void onDataNotAvailable();
    }

    interface Presenter extends BasePresenter{

    }
}
