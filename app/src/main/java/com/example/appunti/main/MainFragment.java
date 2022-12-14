package com.example.appunti.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appunti.R;
import com.example.appunti.data.Nota;
import com.example.appunti.details.DetailsActivity;
import com.example.appunti.listener.IonClickRecyclerListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainFragment extends Fragment implements MainContract.View{
    private static RecyclerView recyclerView;
    private static RecyclerViewAdapter adapter;

    private MainContract.Presenter mPresenter;

    private ActivityResultLauncher<Intent> detailsActivityLauncher;

    public MainFragment() {
        // Required empty public constructor
    }


    public static MainFragment newInstance(Object o, Object o1) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Override
    public void onResume() {
        Log.d("mainFragment", "sono onResume");
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.content_main, container, false);
        recyclerView = v.findViewById(R.id.recyclerView);

        //setting the launcher for receive the ok on delete
        detailsActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Activity is OK, bisogna segnalare all'activity precedente che ho finito -> eliminateSnackOk();
                        Snackbar.make(getActivity().findViewById(R.id.viewLayout), "Nota eliminata", Snackbar.LENGTH_SHORT).show();
                    }
                });

        return v;
    }



    private void setRecyclerView(List<Nota> notes) {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        adapter = new RecyclerViewAdapter(notes, getActivity());
        //Log.d("sss", String.valueOf(notes.get(0)));
        adapter.setOnClickRecyclerListener(new IonClickRecyclerListener() {
            @Override
            public void onClick(Nota nota) {
                detailsActivityLauncher.launch(DetailsActivity.getDetailsIntent(getActivity(), nota));
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void updateNoteOnView(List<Nota> notes) {
        setRecyclerView(notes);
        adapter.setNewNotes(notes);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDataNotAvailable() {
        Snackbar.make(getActivity().findViewById(R.id.viewLayout), "Errore database", Snackbar.LENGTH_SHORT).show();
    }
}
