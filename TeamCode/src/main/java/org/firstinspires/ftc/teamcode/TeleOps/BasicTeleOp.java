package org.firstinspires.ftc.teamcode.TeleOps;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Commands.ShootByPID;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.Utils.OpModeType;

@TeleOp(name = "Teleop")
public class BasicTeleOp extends CommandOpMode {

    MMRobot mmRobot = MMRobot.getInstance();

    @Override
    public void initialize() {
        mmRobot.init(OpModeType.TELEOP, hardwareMap, gamepad1, gamepad2, telemetry);

        mmRobot.mmSystems.gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whenPressed(
                new ShootByPID(0)
        );

        mmRobot.mmSystems.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
                new ShootByPID(30)
        );

    }

    @Override
    public void run() {
        super.run();
        telemetry.update();
    }
}