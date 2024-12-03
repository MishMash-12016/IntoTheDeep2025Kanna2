package org.firstinspires.ftc.teamcode.TeleOp;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;

import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.utils.OpModeType;

@TeleOp
public class LinearIntakeTeleOp extends MMOpMode {
    MMRobot robotInstance = MMRobot.getInstance();
    private final double maxOpening = 0.23;
    Trigger leftTriggerCondition;

    public LinearIntakeTeleOp(){
        super(OpModeType.NonCompetition.EXPERIMENTING);
    }

    @Override
    public void onInit() {

        robotInstance.mmSystems.initRobotSystems();
        leftTriggerCondition = new Trigger(
                () -> robotInstance.mmSystems.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.05
        );
        leftTriggerCondition.whileActiveOnce(
                robotInstance.mmSystems.linearIntake.setPositionByJoystick(()-> gamepad1.left_trigger*maxOpening));



    }

    @Override
    public void run() {
        super.run();
        telemetry.addData("trigger", leftTriggerCondition.get());
        telemetry.addData("Joystick Position: ", robotInstance.mmSystems.linearIntake.getPosition());
        telemetry.update();
    }

}
