package vcsc.teamcode.cmp.claw;

public enum ClawPose {
    OPEN(1.0),
    MOSTLY_CLOSED(0.108),
    CLOSED(0.0);

    public static final double MIN = 0.45;
    public static final double MAX = 0.9;


    final Double pos;

    ClawPose(Double position) {
        pos = position;
    }

    public Double getPos() {
        return pos;
    }
}