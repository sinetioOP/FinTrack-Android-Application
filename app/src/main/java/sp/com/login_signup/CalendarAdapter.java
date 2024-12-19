package sp.com.login_signup;


import static com.android.volley.VolleyLog.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

   private final ArrayList<String> daysOfMonth;
   ImageView ActiveWork;
   TextView Red_text;
   private final OnItemListener onItemListener;

   public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener) {
      this.daysOfMonth = daysOfMonth;

      this.onItemListener = onItemListener;
   }

   @NonNull
   @Override
   public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(parent.getContext());
      View view = inflater.inflate(R.layout.calendar_cell, parent, false);
      ActiveWork= view.findViewById(R.id.Active_work);
      Red_text = view.findViewById(R.id.cellDayText);
      ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
      layoutParams.height = (int) (parent.getHeight() * 0.12);
      return new CalendarViewHolder(view, onItemListener);
   }

   @Override
   public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
      String dayText = daysOfMonth.get(position);
      holder.dayOfMonth.setText(dayText);


      DatabaseReference reference = FirebaseDatabase.getInstance("https://fintrack-cca93-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("ByID");
      FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
      reference.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
         @Override
         public void onComplete(@NonNull Task<DataSnapshot> task) {
            if (task.isSuccessful()) {
               if (task.getResult().exists()) {
                  DataSnapshot dataSnapshot = task.getResult();
                  String calendar_active_month = String.valueOf(dataSnapshot.child("Calendar_active_month").getValue());
                  String[] Calendar_day = calendar_active_month.split("-");
                  String calendar_day = Calendar_day[0]+"-"+Calendar_day[1]+"-"+dayText.toString().trim();
                  System.out.println("calendar_active_month00 "+calendar_day);
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
                                         if(calendar_day.equals(singleUser.get("date"))){
                                            System.out.println("yesyes");
                                            holder.Work_icon.setImageResource(R.drawable.baseline_work_24);
                                            holder.dayOfMonth.setTextColor(Color.RED);
                                         }else{

                                         }


                                      } else {
                                         Log.e(TAG, "Unexpected data type: " + value.getClass().getName());
                                      }
                                   }
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
               }
            }else {

            }

         }
      });



   }

   @Override
   public int getItemCount() {
      return daysOfMonth.size();
   }

   public interface OnItemListener {
      void onItemClick(int position, String dayText);
   }
}
