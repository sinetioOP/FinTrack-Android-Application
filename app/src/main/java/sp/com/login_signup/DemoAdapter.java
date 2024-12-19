package sp.com.login_signup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DemoAdapter extends RecyclerView.Adapter<DemoVH> {
   private Context context;
   List<Info> infoList;

   public DemoAdapter(Context context, List<Info> infoList) {
      this.infoList = infoList;
      this.context = context;
   }

   @NonNull
   @Override
   public DemoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      @SuppressLint("ResourceType") View view = LayoutInflater.from(parent.getContext())
              .inflate(R.layout.item, parent, false);
      return new DemoVH(view).LinkAdpater(this);
   }

   @Override
   public void onBindViewHolder(@NonNull DemoVH holder, int position) {
      Info info = infoList.get(position);
      holder.hotelName.setText(info.getHotel());
      holder.date.setText(info.getDate());
      holder.startTime.setText(info.getStartTime());
      holder.endTime.setText(info.getEndTime());
      holder.rate.setText(info.getRate());
      holder.estimateAmount.setText(info.getEst());

   }

   @Override
   public int getItemCount() {
      return infoList.size();
   }
}

class DemoVH extends RecyclerView.ViewHolder{

   TextView hotelName, date, startTime,endTime, estimateAmount, rate;
   private DemoAdapter adapter;
   public DemoVH(@NonNull View itemView) {
      super(itemView);
      CardView item = itemView.findViewById(R.id.item);


      hotelName = itemView.findViewById(R.id.hotel_name);
      date = itemView.findViewById(R.id.date);
      startTime = itemView.findViewById(R.id.starting_time);
      endTime = itemView.findViewById(R.id.ending_time);
      estimateAmount = itemView.findViewById(R.id.estimate_amount);
      rate = itemView.findViewById(R.id.rate);

      itemView.findViewById(R.id.estimate_amount).setOnClickListener(view -> {
         adapter.infoList.remove(getAdapterPosition());
         adapter.notifyItemRemoved(getAdapterPosition());
      });
   }

   public DemoVH LinkAdpater(DemoAdapter adapter){
      this.adapter = adapter;
      return this;
   }
}