package com.example.admin.inventory;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class scanItemsActivity extends AppCompatActivity {
    public static EditText resultsearcheview;
    private FirebaseAuth firebaseAuth;
    ImageButton scantosearch;
    Button searchbtn;
    Adapter adapter;
    RecyclerView mrecyclerview;
    DatabaseReference mdatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_items);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser=users.getEmail();
        String resultemail = finaluser.replace(".","");
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Items");
        resultsearcheview = findViewById(R.id.searchfield);
        scantosearch = findViewById(R.id.imageButtonsearch);
        searchbtn = findViewById(R.id.searchbtnn);

        mrecyclerview = findViewById(R.id.recyclerViews);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mrecyclerview.setLayoutManager(manager);
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchtext = resultsearcheview.getText().toString();
                firebasesearch(searchtext);
            }
        });

        scantosearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScanCodeActivitysearch.class));
            }
        });


    }
    public void firebasesearch(String searchtext) {
        Query firebaseSearchQuery = mdatabaseReference.orderByChild("itemname").startAt(searchtext).endAt(searchtext + "\uf8ff");
        Query firebaseSearchQueryNumber = mdatabaseReference.orderByChild("itembarcode").startAt(searchtext).endAt(searchtext + "\uf8ff");

        firebaseSearchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    setupFirebaseRecyclerAdapter(firebaseSearchQuery);
                } else {
                    firebaseSearchQueryNumber.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                setupFirebaseRecyclerAdapter(firebaseSearchQueryNumber);
                            } else {
                                Toast.makeText(getApplicationContext(), "Item not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupFirebaseRecyclerAdapter(Query query) {
        FirebaseRecyclerAdapter<Items, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Items, UsersViewHolder>(
                Items.class,
                R.layout.list_layout,
                UsersViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Items model, int position) {
                viewHolder.setDetails(getApplicationContext(), model.getItembarcode(), model.getItemcategory(), model.getItemname(), model.getItemlocation(), model.getItemprice());
            }
        };

        mrecyclerview.setAdapter(firebaseRecyclerAdapter);
    }
    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public UsersViewHolder(View itemView){
            super(itemView);
            mView =itemView;
        }

        public void setDetails(Context ctx, String itembarcode, String itemcategory, String itemname, String itemlocation,String itemprice){
            TextView item_barcode = (TextView) mView.findViewById(R.id.viewitembarcode);
            TextView item_name = (TextView) mView.findViewById(R.id.viewitemname);
            TextView item_category = (TextView) mView.findViewById(R.id.viewitemcategory);
            TextView item_price = (TextView) mView.findViewById(R.id.viewitemprice);
            TextView item_location = (TextView) mView.findViewById(R.id.viewitemlocation);
            item_barcode.setText(itembarcode);
            item_category.setText(itemcategory);
            item_name.setText(itemname);
            item_location.setText(itemlocation);
            item_price.setText(itemprice);

        }

    }}