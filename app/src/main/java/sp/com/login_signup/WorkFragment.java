package sp.com.login_signup;

import static com.android.volley.VolleyLog.TAG;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkFragment extends Fragment {

    private List<Info> infoList;
    private FloatingActionButton AddBtn;
    private DemoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        infoList = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_work, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DemoAdapter(getContext(), infoList);
        recyclerView.setAdapter(adapter);


        // Get datasnapshot at your "users" root node
        DatabaseReference ref = FirebaseDatabase.getInstance("https://fintrack-cca93-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("ByID").child(FirebaseAuth.getInstance().getUid()).child("Work");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Object value = snapshot.getValue();
                                if (value instanceof Map) {
                                    Map<String, Object> singleUser = (Map<String, Object>) value;
                                    Info info = new Info();

                                    info.setDate(getStringValue(singleUser.get("date")));
                                    info.setTotal_hours(getStringValue(singleUser.get("totalWorkingHours")));
                                    info.setRate(getStringValue(singleUser.get("rate")));
                                    info.setHotel(getStringValue(singleUser.get("workplace")));
                                    info.setStartTime(getStringValue(singleUser.get("starting_time")));
                                    info.setEndTime(getStringValue(singleUser.get("ending_time")));
                                    info.setBreakTime(getStringValue(singleUser.get("break_time")));
                                    info.setEst(getStringValue(singleUser.get("estAmount")));
                                    System.out.println(singleUser.get("estAmount"));
                                    infoList.add(info);
                                } else {
                                    Log.e(TAG, "Unexpected data type: " + value.getClass().getName());
                                }
                            }
                            adapter.notifyDataSetChanged();
                            System.out.println("Data fetched successfully");
                        } else {
                            Log.e(TAG, "No data available");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle database error
                        System.out.println("Database error: " + databaseError.getMessage());
                    }
                });

        AddBtn = view.findViewById(R.id.fab_button);
        AddBtn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

        AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), add.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private String getStringValue(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof String) {
            return (String) value;
        } else if (value instanceof Long) {
            return Long.toString((Long) value);
        } else if (value instanceof Integer) {
            return Integer.toString((Integer) value);
        } else if (value instanceof Double) {
            return Double.toString((Double) value);
        } else {
            return value.toString();
        }
    }
}
