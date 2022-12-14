package com.example.appunti.edit;

import com.example.appunti.BasePresenter;
import com.example.appunti.BaseView;
import com.example.appunti.data.Nota;

public interface EditContract {
    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{
        void saveNote(Nota nota);

        void newNote(Nota nota);
    }
}
