public class Folder {

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
}
