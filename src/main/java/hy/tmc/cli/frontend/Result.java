package hy.tmc.cli.frontend;

public enum Result {

    SUCCESS,
    ERROR,
    RESULT_DATA;

    private String data;

    /*
     *Data with result
     * @return String of data
     */
    public String getData() {
        return this.data;
    }

    /*
     *Set resultdata
     */
    public void setData() {

    }
}
