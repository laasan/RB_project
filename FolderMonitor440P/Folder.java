import java.util.Comparator;

public class Folder implements Comparable<Folder>{

    private String name;
    private String dateStart;
    private String dateChange;
    private String state;
    private String filesSymb;


    public Folder(){
        this.name = "";
        this.dateStart = "19000101";
        this.dateChange = "";
        this.state = "";
        this.filesSymb = "";
    }

    public Folder(String name, String dateStart, String dateChange, String state, String filesSymb){
        this.name = name;
        this.dateStart = dateStart;
        this.dateChange = dateChange;
        this.state = state;
        this.filesSymb = filesSymb;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateChange() {
        return dateChange;
    }

    public void setDateChange(String dateChange) {
        this.dateChange = dateChange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFilesSymb() {
        return filesSymb;
    }

    public void setFilesSymb(String filesSymb) {
        this.filesSymb = filesSymb;
    }

    static class Comparators {//через лямбду
        static Comparator<Folder> STATE = (f1, f2) -> f1.getState().compareTo(f2.getState());
    }

    @Override
    public int compareTo(Folder f) {
        return Comparators.STATE.compare(this, f);
    }

    /*
        static class Comparators { //через Comparator.comparing
        static Comparator<Folder> STATE = new Comparator<Folder>() {
            @Override
            public int compare(Folder f1, Folder f2) {
                return f1.getState().compareTo(f2.getState());
            }
        };
    }
    */
}
