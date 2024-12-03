package org.firstinspires.ftc.teamcode.TeleOp;

import com.arcrobotics.ftclib.command.button.Button;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.ButtonReader;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.CommandGroup.RobotCommands;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.utils.OpModeType;

@TeleOp
public class LinearIntakeArmTeleOp extends MMOpMode {
    MMRobot robotInstance = MMRobot.getInstance();

    public LinearIntakeArmTeleOp() {
        super(OpModeType.NonCompetition.EXPERIMENTING);
    }

    @Override
    public void onInit() {

        robotInstance.mmSystems.initRobotSystems();

        Trigger rightTriggerCondition = new Trigger(
                () -> robotInstance.mmSystems.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.05
        );

        rightTriggerCondition.whenActive(
                robotInstance.mmSystems.intakeArm.setPosition(1).andThen(robotInstance.mmSystems.intakeArm.setPosition(0)));
    }

    @Override
    public void run() {
        super.run();
        telemetry.addData("Joystick Position: ", robotInstance.mmSystems.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER));
        telemetry.update();
    }

}