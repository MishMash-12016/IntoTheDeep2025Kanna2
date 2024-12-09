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

    static double currentPose=0;

    public LinearIntakeArmTeleOp() {
        super(OpModeType.NonCompetition.EXPERIMENTING);
    }

    @Override
    public void onInit() {

        robotInstance.mmSystems.initRobotSystems();

        Trigger dothishit = new Trigger(
                () -> robotInstance.mmSystems.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.05
        );

        Trigger finish = new Trigger(
                () -> robotInstance.mmSystems.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.05
        );

        dothishit.whileActiveOnce(RobotCommands.IntakeCommand(()->gamepad1.left_trigger));

        finish.whenInactive(RobotCommands.IntakeDoneCommand());

//        Trigger addToPoseCondition = new Trigger(
//                () -> robotInstance.mmSystems.gamepadEx1.getButton(GamepadKeys.Button.A)
//        );
//        Trigger subToPoseCondition = new Trigger(
//                () -> robotInstance.mmSystems.gamepadEx1.getButton(GamepadKeys.Button.B)
//        );
//        Trigger littleAddToPoseCondition = new Trigger(
//                () -> robotInstance.mmSystems.gamepadEx1.getButton(GamepadKeys.Button.X)
//        );
//        Trigger littleSubToPoseCondition = new Trigger(
//                () -> robotInstance.mmSystems.gamepadEx1.getButton(GamepadKeys.Button.Y)
//        );
//        addToPoseCondition.whenActive(() ->currentPose+=0.05);
//        subToPoseCondition.whenActive(() ->currentPose-=0.05);
//        littleAddToPoseCondition.whenActive(() ->currentPose+=0.01);
//        littleSubToPoseCondition.whenActive(() ->currentPose-=0.01);

//        robotInstance.mmSystems.intakeArm.setDefaultCommand(robotInstance.mmSystems.intakeArm.setPosition(()->currentPose));

//        rightTriggerCondition.whenActive(
//                robotInstance.mmSystems.intakeArm.setPosition(currentPose));
    }

    @Override
    public void run() {
        super.run();

        telemetry.addData("Joystick Position: ", robotInstance.mmSystems.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER));
        telemetry.addData("current pose: ", currentPose);
        telemetry.update();
    }

}