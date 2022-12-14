package com.example.appunti.details;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.appunti.R;
import com.example.appunti.data.Nota;
import com.example.appunti.listener.MyOnClickDetailsListener;
import com.google.android.material.snackbar.Snackbar;

public class DetailsFragment extends Fragment implements DetailsFragmentContract.View, MyOnClickDetailsListener {
    //identifier
    private static final String TAG = DetailsActivity.class.getName();
    private static final String KEY = TAG + ".key";

    //layout Object
    private TextView textNote;

    //listeners to connect

    //presenter if implements a view
    private DetailsFragmentContract.Presenter mPresenter;

    @Override
    public void setPresenter(DetailsFragmentContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public static DetailsFragment newInstance(Object o, Object o1) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.content_details, container, false);

        textNote = v.findViewById(R.id.textNote);

        Nota nota = (Nota) getActivity().getIntent().getSerializableExtra(KEY);
        assert nota !=null;  //lancia un'eccezione

        mPresenter.setNotaId(String.valueOf(nota.getId()));  //guarda  il createFragment di questa activity per vedere che è la stessa cosa

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void showNota(Nota nota) {
        textNote.setText(nota.getText());

    }

    public void showAlert(String title, String message, Nota nota) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("Sì", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.deleteSelectedNotes(nota);
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //getActivity().finish();
            }
        });

        builder.show();
    }

    @Override
    public void onClick(Nota nota) {
        showAlert("Conferma cancellazione", "Vuoi cancellare la nota?", nota);
    }
}
