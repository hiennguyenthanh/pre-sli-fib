package com.example.hiennguyen.firebaseexample.activity.main;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.hiennguyen.firebaseexample.activity.auth.AuthActivity;
import com.example.hiennguyen.firebaseexample.adapter.FirebaseAdapter;
import com.example.hiennguyen.firebaseexample.model.FoodDetail;
import com.example.hiennguyen.firebaseexample.R;
import com.example.hiennguyen.firebaseexample.utilities.PreferenceUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG = MainActivityFragment.class.getSimpleName();
    private static final String BLANK = "";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.btn_add_data)
    FloatingActionButton mBtnAdd;

    @BindView(R.id.btn_logout)
    FloatingActionButton mBtnLogout;

    private List<String> mData;
    private List<FoodDetail> mFoodDetails;

    private Unbinder mUnbind;
    private DatabaseReference mDatabase;
    private FirebaseAdapter mAdapter;
    private FirebaseAuth auth;
    private ProgressDialog progressBar;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mUnbind = ButterKnife.bind(this, view);
        mFoodDetails = new ArrayList<>();
        progressBar = new ProgressDialog(getContext());
        progressBar.setMessage(getString(R.string.message_progress_dialog));
        progressBar.show();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        updateDataChange();

        return view;
    }

    @OnClick({R.id.btn_logout, R.id.btn_add_data})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_data:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle(getString(R.string.label_add_data));
                alertDialog.setMessage(getString(R.string.label_add_item));

                final EditText input = new EditText(getContext());
                LinearLayout.LayoutParams layoutParams =
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(layoutParams);
                alertDialog.setView(input);

                alertDialog.setPositiveButton(getString(R.string.label_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String item = input.getText().toString();
                        if (BLANK.equals(item))
                            return;

                        FoodDetail foodDetail = new FoodDetail("test@test.com", false, item);
                        mDatabase.child(PreferenceUtil.PREF_GROCERY_ITEMS).child(item).setValue(foodDetail);
                    }
                });

                alertDialog.setNegativeButton(getString(R.string.label_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                alertDialog.show();
                break;
            case R.id.btn_logout:
                auth = FirebaseAuth.getInstance();
                auth.signOut();

                auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user == null) {
                            Intent intent = new Intent(getContext(), AuthActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                });
                break;
        }
    }

    public void updateDataChange() {
        mDatabase.child(PreferenceUtil.PREF_GROCERY_ITEMS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mFoodDetails.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FoodDetail foods = snapshot.getValue(FoodDetail.class);
                    Log.e(TAG, "onDataChange: " + foods.getAddedByUser());
                    mFoodDetails.add(foods);
                }
                mAdapter = new FirebaseAdapter(mFoodDetails);
                mAdapter.notifyDataSetChanged();

                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setAdapter(mAdapter);
                Log.e(TAG, "onCreateView: " + mFoodDetails.size());
                progressBar.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private List<String> getData() {
        mData = new ArrayList<>();
        mData.add("A");
        mData.add("B");
        mData.add("C");
        mData.add("D");
        mData.add("E");

        Log.e(TAG, "getData: " + mData);
        return mData;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbind.unbind();
    }
}