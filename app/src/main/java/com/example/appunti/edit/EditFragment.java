package com.example.appunti.edit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.appunti.R;
import com.example.appunti.data.Nota;
import com.example.appunti.listener.MyOnEditNoteListener;
import com.example.appunti.listener.MyOnNewNoteListener;

public class EditFragment extends Fragment implements EditContract.View, MyOnNewNoteListener, MyOnEditNoteListener {
    private static final String TAG = EditActivity.class.getName();
    private static final String ACTION_KEY = TAG + ".action.key";
    private static final String NOTA_KEY = TAG + ".nota.key";

    private EditText editText;

    private EditContract.Presenter mPresenter;

    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public void setPresenter(EditContract.Presenter presenter){
        mPresenter = presenter;
    }

    public static EditFragment newInstance(String param1, String param2){
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        //questo sarebbe da mettere nell'interfaccia del presenter
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.content_edit, container, false);
        editText = v.findViewById(R.id.textNote);

        //recupero la nota dalla DetailsActivity
        Nota nota = (Nota) getActivity().getIntent().getSerializableExtra(NOTA_KEY);

        //imposto in base alla modalità passta
        char action=getActivity().getIntent().getCharExtra(ACTION_KEY,'n');
        if(action == 'e'){
            editText.setText(nota.getText());
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onNewNote(Nota nota) {
        nota.setText(editText.getText().toString());
        mPresenter.newNote(nota);
    }

    @Override
    public void onEditNote(Nota nota) {
        nota.setText(editText.getText().toString());
        Log.d("Nota dopo il fragment", String.valueOf(nota.getId()));
        mPresenter.saveNote(nota);
    }
}
