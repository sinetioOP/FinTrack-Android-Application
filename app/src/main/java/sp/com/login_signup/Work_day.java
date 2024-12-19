package sp.com.login_signup;

class Work_day {
   String month, year;

   public Work_day() {

   }
   public Work_day(String month,String year) {
      this.month = month;
      this.year = year;
   }

   public String getMonth() {
      return month;
   }

   public String getYear() {
      return year;
   }

   public void setMonth(String month) {
      this.month = month;
   }

   public void setYear(String year) {
      this.year = year;
   }
}
