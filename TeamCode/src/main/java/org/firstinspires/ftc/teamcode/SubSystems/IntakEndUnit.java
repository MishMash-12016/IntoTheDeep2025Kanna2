package org.firstinspires.ftc.teamcode.SubSystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleServo;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.utils.Configuration;

public class IntakEndUnit extends SubsystemBase {

    CuttleServo clawIntakeServo;
    public final double open = 1;
    public final double close = 0;

    // claw close or open
    public IntakEndUnit() {
        clawIntakeServo = new CuttleServo(MMRobot.getInstance().mmSystems.expansionHub, Configuration.CLAW_INTAKE_SERVO);
    }
    public Command openIntakeClaw() {
        return new InstantCommand(() -> clawIntakeServo.setPosition(open), this);
    }

    public Command closeIntakeClaw() {
        return new InstantCommand(() -> clawIntakeServo.setPosition(close), this);
    }
}
