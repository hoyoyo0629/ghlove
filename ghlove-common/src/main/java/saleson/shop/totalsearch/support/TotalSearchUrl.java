package saleson.shop.totalsearch.support;

public class TotalSearchUrl {
    private String baseUrl;
    private String fields;
    private String from;
    private String charset;

    private String query;
    private String logInfo;
    private String hilightText;
    private String hilightFields;
    private String hilightDefaultState;
    private String orderBy;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(String logInfo) {
        this.logInfo = logInfo;
    }

    public String getHilightText() {
        return hilightText;
    }

    public void setHilightText(String hilightText) {
        this.hilightText = hilightText;
    }

    public String getHilightFields() {
        return hilightFields;
    }

    public void setHilightFields(String hilightFields) {
        this.hilightFields = hilightFields;
    }

    public String getHilightDefaultState() {
        return hilightDefaultState!=null?hilightDefaultState:"off";
    }

    public void setHilightDefaultState(String hilightDefaultState) {
        this.hilightDefaultState = hilightDefaultState;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
