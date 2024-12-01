package org.firstinspires.ftc.teamcode.TeleOp;

import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.utils.OpModeType;


@TeleOp
public class LinearIntakeEndUnitRotatorTeleOp extends MMOpMode {
    MMRobot robotInstance = MMRobot.getInstance();

    public LinearIntakeEndUnitRotatorTeleOp(){
        super(OpModeType.NonCompetition.EXPERIMENTING);
    }

    @Override
    public void onInit() {

        robotInstance.mmSystems.initRobotSystems();
        Trigger leftTriggerCondition = new Trigger(
                () -> robotInstance.mmSystems.gamepadEx2.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.05
        );
        leftTriggerCondition.whileActiveOnce(
                robotInstance.mmSystems.linearIntake.setPositionByJoystick(()-> gamepad2.left_trigger));
    }

    @Override
    public void run() {
        super.run();
        telemetry.addData("Joystick Position: ", robotInstance.mmSystems.linearIntake.getPosition());
        telemetry.update();
    }

}
