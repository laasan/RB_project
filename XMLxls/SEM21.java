public class SEM21 extends SEM03{
    String BoardId,BoardName,TradeDate,ShortName,SecurityId,Type_,RegNumber,FaceValue,Volume,Value_,CurrencyId,OpenPeriod,Open_,Low_,High_,Close_,LowOffer,HighBid,WAPrice,ClosePeriod,TrendClose,TrendWAP,Bid,Offer,Prev,YieldAtWAP,YieldClose,AccInt,MarketPrice,NumTrades,IssueSize,TrendClsPR,TrendWapPR,MatDate,MarketPrice2,AdmittedQuote,ListName,PrevLegalClosePrice,LegalOpenPrice,LegalClosePrice,OpenVal,CloseVal,EngBrdName,EngName,EngType,BoardType,Duration,MPValTrd,MP2ValTrd,AdmittedValue;

    public String getBoardId() {
        if(this.BoardId instanceof String) return BoardId;
        else return "";
    }

    public void setBoardId(String boardId) {
        BoardId = boardId;
    }

    public String getBoardName() {
        if(this.BoardName instanceof String) return BoardName;
        else return "";
    }

    public void setBoardName(String boardName) {
        BoardName = boardName;
    }

    public String getTradeDate() {
        if(this.TradeDate instanceof String) return TradeDate;
        else return "";
    }

    public void setTradeDate(String tradeDate) {
        TradeDate = tradeDate;
    }

    public String getShortName() {
        if(this.ShortName instanceof String) return ShortName;
        else return "";
    }

    public void setShortName(String shortName) {
        ShortName = shortName;
    }

    public String getSecurityId() {
        if(this.SecurityId instanceof String) return SecurityId;
        else return "";
    }

    public void setSecurityId(String securityId) {
        SecurityId = securityId;
    }

    public String getType_() {
        if(this.Type_ instanceof String) return Type_;
        else return "";
    }

    public void setType_(String type_) {
        Type_ = type_;
    }

    public String getRegNumber() {
        if(this.RegNumber instanceof String) return RegNumber;
        else return "0";
    }

    public void setRegNumber(String regNumber) {
        RegNumber = regNumber;
    }

    public String getFaceValue() {
        if(this.FaceValue instanceof String) return FaceValue;
        else return "";
    }

    public void setFaceValue(String faceValue) {
        FaceValue = faceValue;
    }

    public String getVolume() {
        if(this.Volume instanceof String) return Volume;
        else return "0";
    }

    public void setVolume(String volume) {
        Volume = volume;
    }

    public String getValue_() {
        if(this.Value_ instanceof String) return Value_;
        else return "0";
    }

    public void setValue_(String value_) {
        Value_ = value_;
    }

    public String getCurrencyId() {
        if(this.CurrencyId instanceof String) return CurrencyId;
        else return "";
    }

    public void setCurrencyId(String currencyId) {
        this.CurrencyId = currencyId;
    }

    public String getOpenPeriod() {
        if(this.OpenPeriod instanceof String) return OpenPeriod;
        else return "0";
    }

    public void setOpenPeriod(String openPeriod) {
        OpenPeriod = openPeriod;
    }

    public String getOpen_() {
        if(this.Open_ instanceof String) return Open_;
        else return "0";
    }

    public void setOpen_(String open_) {
        Open_ = open_;
    }

    public String getLow_() {
        if(this.Low_ instanceof String) return Low_;
        else return "0";
    }

    public void setLow_(String low_) {
        Low_ = low_;
    }

    public String getHigh_() {
        if(this.High_ instanceof String) return High_;
        else return "0";
    }

    public void setHigh_(String high_) {
        this.High_ = high_;
    }

    public String getClose_() {
        if(this.Close_ instanceof String) return Close_;
        else return "0";
    }

    public void setClose_(String close_) {
        Close_ = close_;
    }

    public String getLowOffer() {
        if(this.LowOffer instanceof String) return LowOffer;
        else return "0";
    }

    public void setLowOffer(String lowOffer) {
        LowOffer = lowOffer;
    }

    public String getHighBid() {
        if(this.HighBid instanceof String) return HighBid;
        else return "0";
    }

    public void setHighBid(String highBid) {
        HighBid = highBid;
    }

    public String getWAPrice() {
        if(this.WAPrice instanceof String) return WAPrice;
        else return "0";
    }

    public void setWAPrice(String wAPrice) {
        WAPrice = wAPrice;
    }

    public String getClosePeriod() {
        if(this.ClosePeriod instanceof String) return ClosePeriod;
        else return "0";
    }

    public void setClosePeriod(String closePeriod) {
        ClosePeriod = closePeriod;
    }

    public String getTrendClose() {
        if(this.TrendClose instanceof String) return TrendClose;
        else return "0";
    }

    public void setTrendClose(String trendClose) {
        TrendClose = trendClose;
    }

    public String getTrendWAP() {
        if(this.TrendWAP instanceof String) return TrendWAP;
        else return "0";
    }

    public void setTrendWAP(String trendWAP) {
        TrendWAP = trendWAP;
    }

    public String getBid() {
        if(this.Bid instanceof String) return Bid;
        else return "0";
    }

    public void setBid(String bid) {
        Bid = bid;
    }

    public String getOffer() {
        if(this.Offer instanceof String) return Offer;
        else return "0";
    }

    public void setOffer(String offer) {
        Offer = offer;
    }

    public String getPrev() {
        if(this.Prev instanceof String) return Prev;
        else return "0";
    }

    public void setPrev(String prev) {
        Prev = prev;
    }

    public String getYieldAtWAP() {
        if(this.YieldAtWAP instanceof String) return YieldAtWAP;
        else return "0";
    }

    public void setYieldAtWAP(String yieldAtWAP) {
        YieldAtWAP = yieldAtWAP;
    }

    public String getYieldClose() {
        if(this.YieldClose instanceof String) return YieldClose;
        else return "0";
    }

    public void setYieldClose(String yieldClose) {
        YieldClose = yieldClose;
    }

    public String getAccInt() {
        if(this.AccInt instanceof String) return AccInt;
        else return "0";
    }

    public void setAccInt(String accInt) {
        AccInt = accInt;
    }

    public String getMarketPrice() {
        if(this.MarketPrice instanceof String) return MarketPrice;
        else return "0";
    }

    public void setMarketPrice(String marketPrice) {
        MarketPrice = marketPrice;
    }

    public String getNumTrades() {
        if(this.NumTrades instanceof String) return NumTrades;
        else return "0";
    }

    public void setNumTrades(String numTrades) {
        NumTrades = numTrades;
    }

    public String getIssueSize() {
        if(this.IssueSize instanceof String) return IssueSize;
        else return "0";
    }

    public void setIssueSize(String issueSize) {
        IssueSize = issueSize;
    }

    public String getTrendClsPR() {
        if(this.TrendClsPR instanceof String) return TrendClsPR;
        else return "0";
    }

    public void setTrendClsPR(String trendClsPR) {
        TrendClsPR = trendClsPR;
    }

    public String getTrendWapPR() {
        if(this.TrendWapPR instanceof String) return TrendWapPR;
        else return "0";
    }

    public void setTrendWapPR(String trendWapPR) {
        TrendWapPR = trendWapPR;
    }

    public String getMatDate() {
        if(this.MatDate instanceof String) return MatDate;
        else return "";
    }

    public void setMatDate(String matDate) {
        MatDate = matDate;
    }

    public String getMarketPrice2() {
        if(this.MarketPrice2 instanceof String) return MarketPrice2;
        else return "0";
    }

    public void setMarketPrice2(String marketPrice2) {
        MarketPrice2 = marketPrice2;
    }

    public String getAdmittedQuote() {
        if(this.AdmittedQuote instanceof String) return AdmittedQuote;
        else return "0";
    }

    public void setAdmittedQuote(String admittedQuote) {
        AdmittedQuote = admittedQuote;
    }

    public String getListName() {
        if(this.ListName instanceof String) return ListName;
        else return "0";
    }

    public void setListName(String listName) {
        ListName = listName;
    }

    public String getPrevLegalClosePrice() {
        if(this.PrevLegalClosePrice instanceof String) return PrevLegalClosePrice;
        else return "0";
    }

    public void setPrevLegalClosePrice(String prevLegalClosePrice) {
        PrevLegalClosePrice = prevLegalClosePrice;
    }

    public String getLegalOpenPrice() {
        if(this.LegalOpenPrice instanceof String) return LegalOpenPrice;
        else return "0";
    }

    public void setLegalOpenPrice(String legalOpenPrice) {
        LegalOpenPrice = legalOpenPrice;
    }

    public String getLegalClosePrice() {
        if(this.LegalClosePrice instanceof String) return LegalClosePrice;
        else return "0";
    }

    public void setLegalClosePrice(String legalClosePrice) {
        LegalClosePrice = legalClosePrice;
    }

    public String getOpenVal() {
        if(this.OpenVal instanceof String) return OpenVal;
        else return "0";
    }

    public void setOpenVal(String openVal) {
        OpenVal = openVal;
    }

    public String getCloseVal() {
        if(this.CloseVal instanceof String) return CloseVal;
        else return "0";
    }

    public void setCloseVal(String closeVal) {
        CloseVal = closeVal;
    }

    public String getEngBrdName() {
        if(this.EngBrdName instanceof String) return EngBrdName;
        else return "";
    }

    public void setEngBrdName(String engBrdName) {
        EngBrdName = engBrdName;
    }

    public String getEngName() {
        if(this.EngName instanceof String) return EngName;
        else return "";
    }

    public void setEngName(String engName) {
        EngName = engName;
    }

    public String getEngType() {
        if(this.EngType instanceof String) return EngType;
        else return "";
    }

    public void setEngType(String engType) {
        EngType = EngType;
    }

    public String getBoardType() {
        if(this.BoardType instanceof String) return BoardType;
        else return "";
    }

    public void setBoardType(String boardType) {
        BoardType = boardType;
    }

    public String getDuration() {
        if(this.Duration instanceof String) return Duration;
        else return "0";
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getMPValTrd() {
        if(this.MPValTrd instanceof String) return MPValTrd;
        else return "0";
    }

    public void setMPValTrd(String mPValTrd) {
        MPValTrd = mPValTrd;
    }

    public String getMP2ValTrd() {
        if(this.MP2ValTrd instanceof String) return MP2ValTrd;
        else return "0";
    }

    public void setMP2ValTrd(String mP2ValTrd) {
        MP2ValTrd = mP2ValTrd;
    }

    public String getAdmittedValue() {
        if(this.AdmittedValue instanceof String) return AdmittedValue;
        else return "0";
    }

    public void setAdmittedValue(String admittedValue) {
        AdmittedValue = admittedValue;
    }
}
