package com.example.instagram.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.instagram.Adapter.PostAdapter;
import com.example.instagram.Model.Post;
import com.example.instagram.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostDetailFragment extends Fragment {

    String postid;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_post_detail, container, false);

        SharedPreferences preferences =getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        //tanish ek no
     //   postid=preferences.getString("postid","tanish");
      postid=getArguments().getString("postid2");
        Log.e("as",""+postid);



        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);


        postList = new ArrayList<>();
        readPost();




        return view;
    }

    private void readPost() {

        Query reference = FirebaseDatabase.getInstance().getReference("posts").orderByChild("postid").equalTo(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Post post1 = snapshot.getValue(Post.class);

                            postList.add(post1);
                        }
                postAdapter = new PostAdapter(getContext(),postList);
                recyclerView.setAdapter(postAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}