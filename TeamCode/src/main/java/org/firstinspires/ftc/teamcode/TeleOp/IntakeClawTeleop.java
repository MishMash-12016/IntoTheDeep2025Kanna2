package org.firstinspires.ftc.teamcode.TeleOp;

import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.utils.OpModeType;

@TeleOp
public class IntakeClawTeleop extends MMOpMode {
    MMRobot robotInstance = MMRobot.getInstance();

    public IntakeClawTeleop() {
        super(OpModeType.NonCompetition.EXPERIMENTING);
    }


    @Override
    public void onInit() {
        robotInstance.mmSystems.initRobotSystems();

        Trigger rightTriggerCondition = new Trigger(
                () -> robotInstance.mmSystems.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.05
        );
               rightTriggerCondition.whileActiveOnce(
                robotInstance.mmSystems.intakEndUnit.closeIntakeClaw());

        Trigger leftTriggerCondition = new Trigger(
                () -> robotInstance.mmSystems.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.05
        );
        leftTriggerCondition.whileActiveOnce(
                robotInstance.mmSystems.intakEndUnit.openIntakeClaw());

    }
}
