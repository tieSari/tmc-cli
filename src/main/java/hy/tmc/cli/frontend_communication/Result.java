package hy.tmc.cli.frontend_communication;

public enum Result {

    SUCCESS,
    ERROR,
    RESULT_DATA;

    private String data;

    /**
     *
     * @return String of data
     */
    public String getData() {
        return this.data;
    }

    /**
     *
     */
    public void setData() {

    }
}
