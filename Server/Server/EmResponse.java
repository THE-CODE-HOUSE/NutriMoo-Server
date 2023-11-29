public class EmResponse extends Message{
    private double CMS;

    public EmResponse(double CMS) {
        this.CMS = CMS;
    }

    public double getCMS() {
        return CMS;
    }
}
