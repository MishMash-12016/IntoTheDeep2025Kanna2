package org.firstinspires.ftc.teamcode.TeleOp;

import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.utils.OpModeType;

@TeleOp
public class ClimbingTeleOp extends MMOpMode {

    public ClimbingTeleOp() {
        super(OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION);
    }

    @Override
    public void onInit() {

        MMRobot.getInstance().mmSystems.initRobotSystems();
        Trigger leftTriggerCondition = new Trigger(
                () -> MMRobot.getInstance().mmSystems.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.05
        );
        leftTriggerCondition.whenActive(
                MMRobot.getInstance().mmSystems.elevator.moveToPose(1.0).whenFinished(()-> MMRobot.getInstance().mmSystems.elevator.moveToPose(0.0)).withTimeout(10));
    }


    @Override
    public void run() {
        super.run();
    }
}
