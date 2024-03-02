package project;

import java.io.Serializable;

public class DateInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int startMonth;
    private int endMonth;
    private int startYear;
    
    public DateInfo(int startMonth, int endMonth, int startYear) {
        this.startMonth = startMonth;
        this.endMonth = endMonth;
        this.startYear = startYear;
    }
    
    public int getStartMonth() {
        return startMonth;
    }
    
    public int getEndMonth() {
        return endMonth;
    }
    
    public int getStartYear() {
        return startYear;
    }
    
    @Override
    public String toString() {
        int endYear;
        
        if (startMonth == 1)
            endYear = startYear;
        else
            endYear = startYear + 1;
        
        return "\n- Date: " + startMonth + "\\" + startYear + " - " + endMonth + "\\" + endYear;
    }   
}
