package com.example.hiennguyen.firebaseexample;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final String TAG = MainActivityFragment.class.getSimpleName();
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.btn_add_data)
    FloatingActionButton mBtnAdd;

    private List<String> mData;
    private List<FoodDetail> mFoodDetails;

    private Unbinder mUnbind;
    private DatabaseReference mDatabase;
    private FirebaseAdapter mAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mUnbind = ButterKnife.bind(this, view);
        mFoodDetails = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        updateDataChange();

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Add Data!");
                alertDialog.setMessage("Add an item");

                final EditText input = new EditText(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(layoutParams);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String item = input.getText().toString();
                        if (item.equals(""))
                            return;

                        FoodDetail foodDetail = new FoodDetail("test@test.com", false, item);
                        mDatabase.child("groceryItems").child(item).setValue(foodDetail);
                    }
                });

                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

        return view;
    }

    public void updateDataChange() {
        mDatabase.child("groceryItems").addValueEventListener(new ValueEventListener() {
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
