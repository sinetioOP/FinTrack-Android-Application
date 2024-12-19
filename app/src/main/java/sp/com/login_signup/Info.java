package sp.com.login_signup;

public class Info {
   private String est;
   private String total_hours;
   private String hotel;
   private String date;
   private String startTime;
   private String endTime;
   private String breakTime;
   private String rate;

   public Info(){

   }

   public Info(String hotel, String date, String startTime,
               String endTime, String breakTime, String rate,String est,String total_hours){
      this.est = est;
      this.total_hours=total_hours;
      this.hotel = hotel;
      this.date = date;
      this.startTime = startTime;
      this.endTime = endTime;
      this.breakTime = breakTime;
      this.rate = rate;
   }

   public String getEst() {
      return est;
   }

   public void setEst(String est) {
      this.est = est;
   }

   public String getTotal_hours() {
      return total_hours;
   }

   public void setTotal_hours(String total_hours) {
      this.total_hours = total_hours;
   }

   public String getHotel() {
      return hotel;
   }

   public void setHotel(String hotel) {
      this.hotel = hotel;
   }

   public String getDate() {
      return date;
   }

   public void setDate(String date) {
      this.date = date;
   }

   public String getStartTime() {
      return startTime;
   }

   public void setStartTime(String startTime) {
      this.startTime = startTime;
   }

   public String getEndTime() {
      return endTime;
   }

   public void setEndTime(String endTime) {
      this.endTime = endTime;
   }

   public String getBreakTime() {
      return breakTime;
   }

   public void setBreakTime(String breakTime) {
      this.breakTime = breakTime;
   }

   public String getRate() {
      return rate;
   }

   public void setRate(String rate) {
      this.rate = rate;
   }
}