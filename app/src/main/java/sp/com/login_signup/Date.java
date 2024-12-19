package sp.com.login_signup;

class Date {
   private String workplace,date,starting_time,ending_time,break_time,rate;
   private String totalWorkTime , estAmount;
   public Date(){
   }

   public Date(String workplace, String date, String starting_time, String ending_time, String break_time, String rate, String totalWorkTime, String estAmount) {
      this.workplace = workplace;
      this.date = date;
      this.starting_time = starting_time;
      this.ending_time = ending_time;
      this.break_time = break_time;
      this.totalWorkTime = totalWorkTime;
      this.estAmount = estAmount;
      this.rate = rate;
   }

   public void setEstAmount(String estAmount) {
      this.estAmount = estAmount;
   }

   public String getEstAmount() {
      return estAmount;
   }

   public void setTotalWorkTime(String totalWorkTime) {
      this.totalWorkTime = totalWorkTime;
   }

   public String getTotalWorkTime() {
      return totalWorkTime;
   }

   public String getWorkplace() {
      return workplace;
   }

   public String getDate() {
      return date;
   }

   public String getStarting_time() {
      return starting_time;
   }

   public String getEnding_time() {
      return ending_time;
   }

   public String getBreak_time() {
      return break_time;
   }

   public String getRate() {
      return rate;
   }

   public void setWorkplace(String workplace) {
      this.workplace = workplace;
   }

   public void setDate(String date) {
      this.date = date;
   }

   public void setStarting_time(String starting_time) {
      this.starting_time = starting_time;
   }

   public void setEnding_time(String ending_time) {
      this.ending_time = ending_time;
   }

   public void setBreak_time(String break_time) {
      this.break_time = break_time;
   }

   public void setRate(String rate) {
      this.rate = rate;
   }
}

