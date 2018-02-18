public class person {
    String   NoteId      //идентификатор записи
            ,ClientType  //тип клиента 1-5
            ,Fio         //фамили имя потчество одной строкой
            ,Inn         //ИНН
            ,PolnNaim    //ПолнНаимИНБОЮЛ и НаимЮЛ
            ,Razdel      //раздел: 11,12,2 соответственно для Раздел1.1,Раздел1.2,Раздел2
            ,UchStatus   //статус участника для Раздел2
            ,DocumentNum // Серия+Номер паспорта (для начала)
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
        if(this.Fio != null) return Fio;
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
        if(this.PolnNaim != null) return PolnNaim;
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
        if(this.DocumentNum != null) return DocumentNum;
        else return "";
    }

    public void setDocumentNum(String documentNum) {
        DocumentNum = documentNum;
    }
}
