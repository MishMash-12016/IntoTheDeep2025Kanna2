package org.firstinspires.ftc.teamcode.SubSystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleServo;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.utils.Configuration;

public class IntakeArm extends SubsystemBase {
    CuttleServo servoLeft;
    CuttleServo servoRight;

    public final static double up = 0.5;
    public final static double down = -1.5;

    public IntakeArm() {
        servoLeft = new CuttleServo(MMRobot.getInstance().mmSystems.controlHub, Configuration.INTAKE_ARM_SERVO_LEFT);
        servoRight = new CuttleServo(MMRobot.getInstance().mmSystems.controlHub, Configuration.INTAKE_ARM_SERVO_RIGHT);
    }

    //tell servo intake to get to down position
    public Command setPosition(double newPos) {
        return new InstantCommand(()-> {
            servoLeft.setPosition(newPos);
            servoRight.setPosition(newPos);} ,
                this);
    }
}
