package sp.com.login_signup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;


public class add extends AppCompatActivity {
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1234 ;
    EditText Workplace,Date,Starting_time,Ending_time,Break_time,Rate;
    Button AddBtn;
    TextView CancelBtn;
    ImageView EditBtn;
    private DatabaseReference mDatabase;
    TimePickerDialog picker;

    int PlaceRequestCode = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Workplace = findViewById(R.id.workplace);
        Date = findViewById(R.id.date);
        Starting_time = findViewById(R.id.starting_time);
        Ending_time = findViewById(R.id.ending_time);
        Break_time = findViewById(R.id.break_time);
        Rate = findViewById(R.id.rate);
        AddBtn = findViewById(R.id.addBtn);
        CancelBtn=findViewById(R.id.cancelBtn);
        EditBtn = findViewById(R.id.edit_location);

        Workplace.setOnEditorActionListener((textView, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
               Workplace.clearFocus();
               hideKeyboard(Workplace);
                return true;
            }
            return false;
        });
        Rate.setOnEditorActionListener((textView, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                Rate.clearFocus();
                hideKeyboard(Rate);

                return true;
            }
            return false;
        });

        AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String workplace = Workplace.getText().toString().trim();
                String date = Date.getText().toString().trim();
                String rate = Rate.getText().toString().trim();
                String starting_time = Starting_time.getText().toString().trim();
                String ending_time = Ending_time.getText().toString().trim();
                String break_time = Break_time.getText().toString().trim();
                if (workplace.equals("")&&date.equals("")&&rate.equals("")&&starting_time.equals("")&&ending_time.equals("")&&break_time.equals("")){
                    Workplace.setError("Workplace is empty");
                    Workplace.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                    Date.setError("");
                    Date.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                    Rate.setError("Rate is empty");
                    Rate.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                    Starting_time.setError("");
                    Starting_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                    Ending_time.setError("");
                    Ending_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                    Break_time.setError("");
                    Break_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                }else if(workplace.equals("")){
                    Workplace.setError("");
                    Workplace.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                } else if(date.equals("")){
                    Date.setError("");
                    Date.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                } else if(starting_time.equals("")){
                    Starting_time.setError("");
                    Starting_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                }else if(ending_time.equals("")){
                    Ending_time.setError("");
                    Ending_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                }else if(break_time.equals("")){
                    Break_time.setError("");
                    Break_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                }else if(rate.equals("")){
                    Rate.setError("Rate is empty");
                    Rate.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                }
                else {
                    String[] parts = date.split("-");
                    date = parts[2]+"-"+parts[1]+"-"+parts[0];
                    double TotalHours = calculateTotalHours(starting_time, ending_time, break_time);
                    String totalPay = String.valueOf(TotalHours * Integer.parseInt(rate));
                    System.out.println(Integer.parseInt(rate));
                    String totalHours = String.valueOf(TotalHours);
                    System.out.println("TOTAL PAY "+totalPay);
                    Date data = new Date(workplace,date,starting_time,ending_time,break_time,rate, totalHours, totalPay);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    mDatabase = FirebaseDatabase.getInstance("https://fintrack-cca93-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("ByID");
                    mDatabase.child(user.getUid()).child("Work").child(date).setValue(data);
                    mDatabase.child(user.getUid()).child("Work").child("Date").setValue(date);

                    Intent intent =new Intent(add.this, MainActivity.class);
//                    intent.putExtra("fragment", "work");
                    startActivity(intent);
                    finish();
                }
            }
        });


        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date.setError(null);
                Date.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                ShowCalendar(Date);
            }
        });

        Starting_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Starting_time.setError(null);
                Starting_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                showTimePickerDialog(Starting_time);
            }
        });
        Ending_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ending_time.setError(null);
                Ending_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                showTimePickerDialog(Ending_time);

            }
        });
        Break_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Break_time.setError(null);
                Break_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                showTimePickerDialog(Break_time);
            }
        });
        Workplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Workplace.setError(null);
                Workplace.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));

            }
        });

        Rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rate.setError(null);
                Rate.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
            }
        });

        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(add.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private double calculateTotalHours(String startTime, String endTime, String breakTime) {
        String[] startParts = startTime.split(":");
        String[] endParts = endTime.split(":");
        String[] breakParts = breakTime.split(":");
        System.out.println("Breaktime "+breakParts[0]+" "+breakParts[1]);

        int startHour = Integer.parseInt(startParts[0]);
        int startMinute = Integer.parseInt(startParts[1]);
        int endHour = Integer.parseInt(endParts[0]);
        int endMinute = Integer.parseInt(endParts[1]);
        int breakHour = Integer.parseInt(breakParts[0]);
        int breakMinute = Integer.parseInt(breakParts[1]);
        double startInHours = startHour + startMinute / 60.0;
        double endInHours = endHour + endMinute / 60.0;
        double breakInHours = breakHour + breakMinute / 60.0;
        System.out.println("breaktime "+breakInHours);
        double totalHours = (endInHours - startInHours) - (breakInHours);
        if (totalHours<0){
            totalHours=24.0+totalHours;
        }
        System.out.println("TOtal_hours "+totalHours);
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        return Double.parseDouble(numberFormat.format(totalHours));
    }

    public void showTimePickerDialog(TextView time) {
        final Dialog dialog = new Dialog(add.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.time_picker);
        TimePicker timePicker = dialog.findViewById(R.id.time_picker);
        ImageView ConfirmBtn = dialog.findViewById(R.id.confirmBtn);
        timePicker.setIs24HourView(true);
        dialog.show();
        if (time == Break_time) {
            timePicker.setHour(0);
            timePicker.setMinute(30);
        }
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(2);
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.setText(String.format("%02d", timePicker.getHour())+":"+String.format("%02d", timePicker.getMinute()));
                dialog.cancel();
            }
        });


    }

    public void ShowCalendar(EditText to_update){
        final Dialog dialog = new Dialog(add.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.show();
        dialog.setContentView(R.layout.calendar);
        CalendarView Calendar = dialog.findViewById(R.id.calendar);
        Calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                to_update.setText(String.format("%02d", dayOfMonth)+"-"+String.format("%02d", month+1)+"-"+String.format("%02d", year));
                dialog.cancel();
            }
        });
    }

    public void startAutoCompleteActivity(View view){
        Intent intent=new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY,
                Arrays.asList(Place.Field.ID,Place.Field.NAME)).
                setCountries(Arrays.asList("SG", "MY", "ID"))
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(),"AIzaSyDgXrryer0iePsZ4h4-EW0qftPb-VBTUK4");
            PlacesClient placesClient = Places.createClient(this);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                String placeName = place.getName();
                Log.i("Autocomplete", "Place: " + placeName);

                // Update your EditText or any other view with the selected place name
                EditText to_update = findViewById(R.id.workplace); // replace with your EditText ID
                to_update.setText(placeName);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("Autocomplete", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i("Autocomplete", "User canceled the autocomplete activity.");
            }
        }
    }

    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
        public boolean onSupportNavigateUp () {
            super.onBackPressed();
            return super.onNavigateUp();
        }

    }