package com.example.campusconnect;

public class SavedEvent {

        private String userId;
        private String name;
        private String location;
        private String startTime;
        private String date;
        private String org;
        private String desc;


    SavedEvent() {
            userId = "UserIdNotProvided";
            name = "EventNameNotProvided";
            location = "NoEventLocationProvided";
            startTime = "11:59";
            date = "01012019";
            org = "EventOrgNotEntered";
            desc = "N/A";
        }

    SavedEvent(String dateSelected){
            this.date = dateSelected;
        }

        public SavedEvent(String userId, String name, String location, String startTime, String date, String org, String desc) {
            this.userId = userId;
            this.location = location;
            this.startTime = startTime;
            this.date = date;
            this.org = org;
            this.desc = desc;
        }

        public String getUserId() { return userId; }

        public String getName() { return name; }

        public String getLocation() { return location; }

        public String getStartTime() { return startTime; }

        public String getDate() { return date; }

        public String getOrg() { return org; }

        public String getDesc() { return desc; }

        public void setUserId(String userId) { this.userId = userId; }

        public void setName(String name) { this.name = name; }

        public void setLocation(String location) { this.location = location; }

        public void setStartTime(String startTime) { this.startTime = startTime; }

        public void setDate(String date) { this.date = date; }

        public void setOrg(String org) { this.org = org; }

        public void setDesc(String desc) { this.desc = desc; }

        public String toString()
        {
            return userId + "|" + name + "|" + location + "|" + startTime + "|" + date + "|" + org + "|" + desc;
        }
        // UPDATE: to date/time storage to java.util.Calendar

        public String getStartTime_Formatted(){
            switch(this.startTime.length())
            {
                case 3:
                    // _H:MM
                    return String.format(" %c:%c%c",
                            startTime.charAt(0),
                            startTime.charAt(1),
                            startTime.charAt(2));
                case 4:
                    // HH:MM
                    return String.format("%c%c:%c%c",
                            startTime.charAt(0),
                            startTime.charAt(1),
                            startTime.charAt(2),
                            startTime.charAt(3));
                default:
                    // STORED-TIME FORMAT ERROR
                    return "00:00";
            }
        }

        // OUTPUT FORMAT: MM/DD/YYYY
        public String getDate_Standard(){
            return String.format("%s/%s/%s", getMonth_Integer(), getDay(), getYear());
        }

        private String getDay() {
            return new String(new char[]{date.charAt(2), date.charAt(3)});
        }

        private String getMonth_Integer() {
            return new String(new char[]{date.charAt(0), date.charAt(1)});
        }

        // OUTPUT FORMAT: Month (D)th, YYYY
        public String getDate_Full(){
            return String.format("%s %s, %s", getMonth_Name(), getDay_wContraction(), getYear());
        }

        private String getDay_wContraction() {
            String dayBuilder = new String(new char[]{date.charAt(2), date.charAt(3)});

            return dayBuilder + dayModifier();
        }

        // Character at index String[3] determines the contraction
        // e.g. 02052020: String Index[3] == 5 -> Contraction: 'rd' -> Feb 3[rd] 2020
        private String dayModifier()
        {
            char dayDigit = this.date.charAt(3);

            if(dayDigit == '1')
                return "st";
            else if (dayDigit == '2')
                return "nd";
            else if (dayDigit == '3')
                return "rd";
            else
                return "th";
        }

        private String getYear() {
            return new String(new char[]{date.charAt(4), date.charAt(5),
                    date.charAt(6), date.charAt(7),});
        }

        // Int Parser: Java Textbook Chapter 10.10.6
        private String getMonth_Name() {
            int month;
            String monthName;

            month = Integer.parseInt(new String(new char[]{date.charAt(0), date.charAt(1)}));
            //month++;

            switch(month){
                case 1:
                    monthName = "January";
                    break;
                case 2:
                    monthName = "February";
                    break;
                case 3:
                    monthName = "March";
                    break;
                case 4:
                    monthName = "April";
                    break;
                case 5:
                    monthName = "May";
                    break;
                case 6:
                    monthName = "June";
                    break;
                case 7:
                    monthName = "July";
                    break;
                case 8:
                    monthName = "August";
                    break;
                case 9:
                    monthName = "September";
                    break;
                case 10:
                    monthName = "October";
                    break;
                case 11:
                    monthName = "November";
                    break;
                case 12:
                    monthName = "December";
                    break;
                default:
                    monthName = "Error";
                    break;
            }

            return monthName;
        }


}
