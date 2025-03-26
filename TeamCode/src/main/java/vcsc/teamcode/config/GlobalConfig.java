package vcsc.teamcode.config;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@Config
public class GlobalConfig {
    public static double TPR = 28;
    public static PIDFCoefficients rotationCoeffs = new PIDFCoefficients(0.01, 0, 0, 0);
    public static PIDFCoefficients extensionCoeffs = new PIDFCoefficients(0.01, 0, 0, 0);
}
