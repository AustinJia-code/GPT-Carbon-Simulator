public class Timestamp implements Comparable<Timestamp> {
    private int year, month, day, hour, minute, second;
    public Timestamp(int year, int month, int day, int hour, int minute, int second){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public Timestamp(String timestamp){
        String[] strValues = timestamp.split("-|:|\\+|T");
        int[] intValues = new int[6];

        for(int i = 0; i < 6; i++){
            intValues[i] = Integer.parseInt(strValues[i]);
        }

        this.year = intValues[0];
        this.month = intValues[1];
        this.day = intValues[2];
        this.hour = intValues[3];
        this.minute = intValues[4];
        this.second = intValues[5];
    }

    public boolean equals(Timestamp b){
        if((year == b.getYear())
        && (month == b.getMonth())
        && (day == b.getDay())
        && (hour == b.getHour())
        && (minute == b.getMinute())
        && (second == b.getSecond())){
            return true;
        }
        return false;
    }

    public String toString() {
        return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    }

    public String toCSV(){
        return year + "-" + month + "-" + day + "," + hour + ":" + minute + ":" + second;
    }

    public int getYear(){
        return year;
    }
    public int getMonth(){
        return month;
    }
    public int getDay(){
        return day;
    }
    public int getHour(){
        return hour;
    }
    public int getMinute(){
        return minute;
    }
    public int getSecond(){
        return second;
    }

    public int asNumber(){
        return Integer.parseInt("" + day + hour + minute + second);
    }

    public int compareTo(Timestamp t){
        return asNumber() - t.asNumber();
    }
}
