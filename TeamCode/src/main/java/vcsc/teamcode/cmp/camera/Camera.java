package vcsc.teamcode.cmp.camera;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import vcsc.core.util.GlobalTelemetry;
import vcsc.core.abstracts.actuator.Actuator;
import vcsc.core.abstracts.state.State;
import vcsc.teamcode.abstracts.abstracts.Block;

public class Camera extends State<Camera> {
    private final Limelight3A limelight;

    public Camera(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();
    }

    public void takeSnapshot(String name) {
        limelight.captureSnapshot(name);
    }

    public Block getBlock() {
        LLResult result = limelight.getLatestResult();
        double[] block_data = result.getPythonOutput();
        MultipleTelemetry telem = GlobalTelemetry.getInstance();
        telem.addData("Block data", block_data);
        double color = block_data[0];
        double x = block_data[1];
        double y = block_data[2];
        double width = block_data[3];
        double height = block_data[4];
        double angle = block_data[5];
        if (color == 0) {
            return new Block(Block.COLOR.RED, x, y, angle);
        } else if (color == 1) {
            return new Block(Block.COLOR.BLUE, x, y, angle);
        } else if (color == 2) {
            return new Block(Block.COLOR.YELLOW, x, y, angle);
        }
        return null;
    }
}
