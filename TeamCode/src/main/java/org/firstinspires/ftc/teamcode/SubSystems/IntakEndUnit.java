package org.firstinspires.ftc.teamcode.SubSystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleServo;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.utils.Configuration;

public class IntakEndUnit extends SubsystemBase {

    CuttleServo clawIntakeServo;
    public final double open = 0.5;
    public final double close = 22;

    // claw close or open
    public IntakEndUnit() {
        clawIntakeServo = new CuttleServo(MMRobot.getInstance().mmSystems.controlHub, Configuration.clawIntakeServo);
    }
    public Command openClaw() {
        return new InstantCommand(() -> clawIntakeServo.setPosition(open), this);
    }

    public Command closeClaw() {
        return new InstantCommand(() -> clawIntakeServo.setPosition(close), this);
    }
}
