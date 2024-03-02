package org.firstinspires.ftc.teamcode.Commands.armCommands.multiSystem;

import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.ArmPosition;
import org.firstinspires.ftc.teamcode.Commands.armCommands.antiTurret.AntiTurretGetToPosition;
import org.firstinspires.ftc.teamcode.Commands.armCommands.cartridge.ScoringFirstPixel;
import org.firstinspires.ftc.teamcode.Commands.armCommands.elbow.ElbowGetToPosition;
import org.firstinspires.ftc.teamcode.Commands.armCommands.elevator.ElevatorGetToHeightPID;
import org.firstinspires.ftc.teamcode.Commands.armCommands.extender.ExtenderSetPosition;
import org.firstinspires.ftc.teamcode.Commands.armCommands.turret.RotateTurretByPID;
import org.firstinspires.ftc.teamcode.RobotControl;
import org.firstinspires.ftc.teamcode.SubSystems.AntiTurret;
import org.firstinspires.ftc.teamcode.SubSystems.Elbow;
import org.firstinspires.ftc.teamcode.SubSystems.Elevator;
import org.firstinspires.ftc.teamcode.SubSystems.Extender;
import org.firstinspires.ftc.teamcode.SubSystems.Turret;

public class UnsafeMoveArm extends ConditionalCommand {
    public static final long EXTENDER_WAIT_TIME = 50;
    private final RobotControl robot;
    private final ArmPosition position;

    public UnsafeMoveArm(RobotControl robot, ArmPosition position, boolean isLeftOfBoard) {
        super(
                new UnsafeMoveArmUp(robot, position, isLeftOfBoard),
                new UnsafeMoveArmDown(robot, position, isLeftOfBoard),
                () -> (ArmGetToPosition.lastPosition.getElevatorHeight() < position.getElevatorHeight())
        );
        this.robot = robot;
        this.position = position;
    }

    @Override
    public void initialize() {
        super.initialize();
        if(position == ArmPosition.SAFE_PLACE || position == ArmPosition.AUTO_INTAKE || position == ArmPosition.INTAKE) {
            robot.elbow.updateSafePlace = true;
        }
    }
}