package org.firstinspires.ftc.teamcode.SubSystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleServo;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.utils.Configuration;

public class ScoringArm extends SubsystemBase {
    private final CuttleServo servoLeft;
    private final CuttleServo servoRight;

    public final double up = 1;
    public final double down = -1;

    public ScoringArm() {
        servoLeft = new CuttleServo(MMRobot.getInstance().mmSystems.controlHub, Configuration.SERVO_LEFT_SCORING_ARM);
        servoRight = new CuttleServo(MMRobot.getInstance().mmSystems.controlHub, Configuration.SERVO_RIGHT_SCORING_ARM);
    }

    //Tell arm to get to position
    public Command setPosition(double newPos) {
        return new InstantCommand(()-> {
            servoLeft.setPosition(newPos);
            servoRight.setPosition(newPos);} ,
                this);
    }
}