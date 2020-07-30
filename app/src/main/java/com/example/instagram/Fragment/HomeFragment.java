package com.example.instagram.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.instagram.Adapter.PostAdapter;
import com.example.instagram.Adapter.StoryAdapter;
import com.example.instagram.Model.Post;
import com.example.instagram.Model.Story;
import com.example.instagram.R;
import com.example.instagram.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postLists;

    private RecyclerView recyclerView_story;
    private StoryAdapter storyAdapter;
    private List<Story> storyList;


    private List<String> followingList;

    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView =view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postLists = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(),postLists);
        recyclerView.setAdapter(postAdapter);

        recyclerView_story = view.findViewById(R.id.recycler_view_story);
        recyclerView_story.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView_story.setLayoutManager(linearLayoutManager1);
        storyList=new ArrayList<>();
        storyAdapter = new StoryAdapter(getContext(),storyList);
        recyclerView_story.setAdapter(storyAdapter);

        progressBar = view.findViewById(R.id.progress_circular);

        checkFollowing();

        return view;
    }
    private  void checkFollowing(){
        followingList = new ArrayList<>();

        checkforuserlogin();
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference("Follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                    followingList.add(snapshot.getKey());
                }
                readposts();
                readStory();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void checkforuserlogin() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

            String mUID=user.getUid();

         //   SharedPreferences sp=getSharedPreferences("SP_User",MODE_PRIVATE);
         //   SharedPreferences.Editor editor=sp.edit();
         //   editor.putString("Current_USERID",mUID);
         //   editor.apply();

            //updatetoken
            //noinspection deprecation
          //  updateToken(FirebaseInstanceId.getInstance().getToken());
        }
        else{
            startActivity(new Intent(getContext(), RegisterActivity.class));

        }
    }


    private void readposts(){
         DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts");
         reference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postLists.clear();

                 for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                     for (String id : followingList){

                         assert post != null;
                         if (post.getPublisher().equals(id)){
                          //   Toast.makeText(getContext(), ""+post.getPublisher(), Toast.LENGTH_SHORT).show();
                             postLists.add(post);
                         }
                     }
                 }

                 postAdapter.notifyDataSetChanged();
                 progressBar.setVisibility(View.GONE);
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
     }
     private void readStory(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long timecurrent = System.currentTimeMillis();
                storyList.clear();
                storyList.add(new Story("",  0, 0,"",
                        FirebaseAuth.getInstance().getCurrentUser().getUid()));
                for (String id: followingList){
                    int countstory=0;
                    Story story = null;
                    for (DataSnapshot snapshot: dataSnapshot.child(id).getChildren()){
                        story = snapshot.getValue(Story.class);
                        if(timecurrent > story.getTimestart() && timecurrent < story.getTimeend()){
                            countstory++;
                        }
                    }
                    if (countstory > 0){
                        storyList.add(story);
                    }
                }
                storyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
     }
}
