import java.util.Comparator;

public class Folder implements Comparable<Folder>{

    private String name;
    private String dateStart;
    private String state;
    private String filesSymb;


    public Folder(){
        this.name = "";
        this.dateStart = "19000101";
        this.state = "";
        this.filesSymb = "";
    }

    public Folder(String name, String dateStart, String state, String filesSymb){
        this.name = name;
        this.dateStart = dateStart;
        this.state = state;
        this.filesSymb = filesSymb;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
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

    @Override
    public int compareTo(Folder f) {
        return Comparators.STATE.compare(this, f);
    }

    public static class Comparators {
        public static Comparator<Folder> STATE = new Comparator<Folder>() {
            @Override
            public int compare(Folder f1, Folder f2) {
                return f1.getState().compareTo(f2.getState());
            }
        };
    }
}
