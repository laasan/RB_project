public class person {
    String   NoteId      //идентификатор записи
            ;
    String ClientType  //тип клиента 1-5
            ;
    String Fio         //фамили имя потчество одной строкой
            ;
    String Inn         //ИНН
            ;
    String PolnNaim    //ПолнНаимИНБОЮЛ и НаимЮЛ
            ;
    String Razdel      //раздел: 11,12,2 соответственно для Раздел1.1,Раздел1.2,Раздел2
            ;
    String UchStatus   //статус участника для Раздел2
            ;
    String DocumentNum // Серия+Номер паспорта (для начала)
            ;
    String SourceFileName //файл из которого его достали
            ;
    String CodeOtkaz   //код отказа
            ;
    String DateOtkaz   //дата отказа
            ;
    String Birthday    //день рождения физ.лица
            ;

    public String getNoteId() {
        if(this.NoteId != null) return NoteId;
        else return "";
    }

    public void setNoteId(String noteId) {
        NoteId = noteId;
    }

    public String getClientType() {
        if(this.ClientType != null) return ClientType;
        else return "";
    }

    public void setClientType(String clientType) {
        ClientType = clientType;
    }

    public String getFio() {
        if(this.Fio != null) return Fio.replace(";"," ");
        else return "";
    }

    public void setFio(String fio) {
        Fio = fio;
    }

    public String getInn() {
        if(this.Inn != null) return Inn;
        else return "";
    }

    public void setInn(String inn) {
        Inn = inn;
    }

    public String getPolnNaim() {
        if(this.PolnNaim != null) return PolnNaim.replace(";"," ");
        else return "";
    }

    public void setPolnNaim(String polnNaim) {
        PolnNaim = polnNaim;
    }

    public String getRazdel() {
        if(this.Razdel != null) return Razdel;
        else return "";
    }

    public void setRazdel(String razdel) {
        Razdel = razdel;
    }

    public String getUchStatus() {
        if(this.UchStatus != null) return UchStatus;
        else return "";
    }

    public void setUchStatus(String uchStatus) {
        UchStatus = uchStatus;
    }

    public String getDocumentNum() {
        if(this.DocumentNum != null) return DocumentNum.replace(";"," ");
        else return "";
    }

    public void setDocumentNum(String documentNum) {
        DocumentNum = documentNum;
    }

    public String getSourceFileName() {
        if(this.SourceFileName != null) return SourceFileName;
        else return "";
    }

    public void setSourceFileName(String sourceFileName) {
        SourceFileName = sourceFileName;
    }

    public String getCodeOtkaz() {
        if(this.CodeOtkaz != null) return CodeOtkaz;
        else return "";
    }

    public void setCodeOtkaz(String codeOtkaz) { CodeOtkaz = codeOtkaz; }

    public String getDateOtkaz() {
        if(this.DateOtkaz != null) return DateOtkaz;
        else return "";
    }

    public void setDateOtkaz(String dateOtkaz) { DateOtkaz = dateOtkaz; }

    public String getBirthday() {
        if(this.Birthday != null) return Birthday;
        else return "";
    }

    public void setBirthday(String birthday) { Birthday = birthday; }
}
