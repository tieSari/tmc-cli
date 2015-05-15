package hy.tmc.cli.backendCommunication;

public class HTTPResult {
     
    private String data;
    private int statusCode;
    private boolean success;
    
    /**
     *
     * @param data
     * @param statusCode
     * @param success
     */
    public HTTPResult(String data, int statusCode, boolean success) {
        this.data = data;
        this.statusCode = statusCode;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
