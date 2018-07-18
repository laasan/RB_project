import java.text.SimpleDateFormat;
import java.util.Calendar;

class GetDayTime {
    //текущая дата и время в формате yyyyMMdd HH:mm:ss
    static String now(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd HH:mm:ss.S");
        return format1.format(cal.getTime());
    }
}
