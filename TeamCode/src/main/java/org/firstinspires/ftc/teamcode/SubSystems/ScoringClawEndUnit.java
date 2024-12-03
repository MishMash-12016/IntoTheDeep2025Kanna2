package org.firstinspires.ftc.teamcode.SubSystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleServo;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.utils.Configuration;

public class ScoringClawEndUnit extends SubsystemBase {
    CuttleServo clawScoringServo;

    public static double open = 1;
    public static double close = -1;

    public ScoringClawEndUnit() {
        clawScoringServo = new CuttleServo(MMRobot.getInstance().mmSystems.controlHub, Configuration.SCORING_CLAW_SERVO);
    }

    public Command openScoringClaw() {
        return new InstantCommand(() -> clawScoringServo.setPosition(open), this);
    }

    public Command closeScoringClaw() {
        return new InstantCommand(() -> clawScoringServo.setPosition(close), this);
    }
}
