package org.firstinspires.ftc.teamcode.Commands.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.ArmPosition;
import org.firstinspires.ftc.teamcode.Commands.armCommands.cartridge.CartridgeSetState;
import org.firstinspires.ftc.teamcode.Commands.armCommands.multiSystem.ArmGetToPosition;
import org.firstinspires.ftc.teamcode.Commands.armCommands.turret.RotateTurretByPID;
import org.firstinspires.ftc.teamcode.Commands.auto.trajectoryUtils.TrajectoryFollowerCommand;
import org.firstinspires.ftc.teamcode.Commands.auto.trajectoryUtils.TrajectoryPoses;
import org.firstinspires.ftc.teamcode.Commands.intakeRoller.IntakeRotate;
import org.firstinspires.ftc.teamcode.Commands.intakeRoller.ResetPixelCount;
import org.firstinspires.ftc.teamcode.RoadRunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.RobotControl;
import org.firstinspires.ftc.teamcode.SubSystems.Cartridge;
import org.firstinspires.ftc.teamcode.Utils.AllianceColor;
import org.firstinspires.ftc.teamcode.Utils.DetectionSide;

public class ScoringCommand extends SequentialCommandGroup {

    static RobotControl robot;

    public ScoringCommand(Command scoringCommand, Command secondScoringCommand, RobotControl robot, Double numOfCycle) {
        ScoringCommand.robot = robot;
        addCommands(
                new ParallelCommandGroup(
                        getTrajectoryCommand(robot),
                        new IntakeRotate(robot.intake.roller, robot.intake.roller.EJECT_POWER).withTimeout(1500),
                        new WaitCommand(1700).andThen(
                                new ConditionalCommand(
                                        new ArmGetToPosition(robot, ArmPosition.SCORING, robot.allianceColor == AllianceColor.RED),
                                        new ArmGetToPosition(robot, ArmPosition.SAFE_PLACE, true),
                                        () -> robot.teamPropDetector.getTeamPropSide() != DetectionSide.CLOSE
                                ),
                                new InstantCommand(() -> RotateTurretByPID.DEADLINE_FOR_TURRET = 700),//todo maybe will work with less time
                                scoringCommand
                        )
                ),
                new ConditionalCommand(
                        new SequentialCommandGroup( //todo ask if we still need this ('cause it's another 200ms wasted)
                                new CartridgeSetState(robot.cartridge, Cartridge.State.SEMI_OPEN),
                                new WaitCommand(200),
                                new CartridgeSetState(robot.cartridge, Cartridge.State.OPEN)
                        ),
                        new CartridgeSetState(robot.cartridge, Cartridge.State.OPEN),
                        () -> numOfCycle == 1
                ),
                new ResetPixelCount(robot),
                new InstantCommand(() -> RotateTurretByPID.DEADLINE_FOR_TURRET = 2000),
                new WaitCommand(300)
// <- todo remove this when we need elevator               secondScoringCommand
        );
    }


    private Command getTrajectoryCommand(RobotControl robot) {
        return new ConditionalCommand(
                new ConditionalCommand(
                        new TrajectoryFollowerCommand(TrajectoriesRed.FRONT.trajectory, robot.autoDriveTrain).andThen(resetPoseEstimate(robot)),
                        new TrajectoryFollowerCommand(TrajectoriesRed.NORMAL.trajectory, robot.autoDriveTrain).andThen(resetPoseEstimate(robot)),
                        () -> robot.teamPropDetector.getTeamPropSide() == DetectionSide.CLOSE
                ),
                new ConditionalCommand(
                        new TrajectoryFollowerCommand(TrajectoriesBlue.FRONT.trajectory, robot.autoDriveTrain).andThen(resetPoseEstimate(robot)),
                        new TrajectoryFollowerCommand(TrajectoriesBlue.NORMAL.trajectory, robot.autoDriveTrain).andThen(resetPoseEstimate(robot)),
                        () -> robot.teamPropDetector.getTeamPropSide() == DetectionSide.CLOSE
                ),
                () -> robot.allianceColor == AllianceColor.RED
        );
    }

    private Command resetPoseEstimate(RobotControl robot) {
        return new ConditionalCommand(
                new InstantCommand(() ->
                        robot.autoDriveTrain.setPoseEstimate(new Pose2d(
                                TrajectoryPoses.realBackdropPoseRed.getX(),
                                TrajectoryPoses.realBackdropPoseRed.getY(),
                                robot.autoDriveTrain.getPoseEstimate().getHeading()
                        ))
                ),
                new InstantCommand(() ->
                    robot.autoDriveTrain.setPoseEstimate(new Pose2d(
                            TrajectoryPoses.realBackdropPoseBlue.getX(),
                            TrajectoryPoses.realBackdropPoseBlue.getY(),
                            robot.autoDriveTrain.getPoseEstimate().getHeading()
                    ))
                ),
                () -> robot.allianceColor == AllianceColor.RED
        );
    }

    //Go to backdrop depending on alliance color.

    public enum TrajectoriesRed {

        NORMAL(robot.autoDriveTrain.trajectorySequenceBuilder(TrajectoryPoses.stackPoseRed)
                .setTangent(Math.toRadians(-90))
                .splineToSplineHeading(
                        new Pose2d(TrajectoryPoses.stackPoseRed.getX() + 3, -15, Math.toRadians(90)),
                        Math.toRadians(-90) //Tangent
                )
                .splineToLinearHeading(
                        new Pose2d(-9, -40, Math.toRadians(90)),
                        Math.toRadians(-90) //Tangent
                )
                .splineToLinearHeading(
                        new Pose2d(-15, -61, Math.toRadians(90)),
                        Math.toRadians(-140) //Tangent
                )
                .splineToLinearHeading(
                        new Pose2d(-22, -62, Math.toRadians(90)),
                        Math.toRadians(180), //Tangent
                        robot.trajectories.reduceVelocity(0.6),
                        robot.trajectories.reduceAcceleration(0.6)
                )
                .build()
        ),
        FRONT(robot.autoDriveTrain.trajectorySequenceBuilder(TrajectoryPoses.stackPoseRed)
                .setTangent(Math.toRadians(-90))
                .splineToConstantHeading(
                        new Vector2d(TrajectoryPoses.stackPoseRed.getX() + 1, -15),
                        Math.toRadians(-95) //Tangent
                )
                .splineToConstantHeading(
                        new Vector2d(-30, -20),
                        Math.toRadians(180)
                )
                .splineToConstantHeading(
                        new Vector2d(-40, -36),
                        Math.toRadians(-90)
                )
                .build()
        );

        final TrajectorySequence trajectory;

        TrajectoriesRed(TrajectorySequence trajectory) {
            this.trajectory = trajectory;
        }

    }


    public enum TrajectoriesBlue {

        NORMAL(robot.autoDriveTrain.trajectorySequenceBuilder(TrajectoryPoses.stackPoseBlue)
                .setTangent(Math.toRadians(270))
                .splineToSplineHeading(
                        new Pose2d(TrajectoryPoses.stackPoseBlue.getX() - 2, -15, Math.toRadians(90)),
                        Math.toRadians(270) //Tangent
                )
                .splineToLinearHeading(
                        new Pose2d(9, -40, Math.toRadians(90)),
                        Math.toRadians(270) //Tangent
                )
                .splineToLinearHeading(
                        new Pose2d(15, -58, Math.toRadians(90)),
                        Math.toRadians(320) //Tangent
                )
                .splineToLinearHeading(
                        new Pose2d(28, -63, Math.toRadians(90)),
                        Math.toRadians(-20), //Tangent
                        robot.trajectories.reduceVelocity(0.6),
                        robot.trajectories.reduceAcceleration(0.6)
                )
                .build()
        ),
        FRONT(robot.autoDriveTrain.trajectorySequenceBuilder(TrajectoryPoses.stackPoseBlue)
                .setTangent(Math.toRadians(270))
                .splineToConstantHeading(
                        new Vector2d(TrajectoryPoses.stackPoseBlue.getX() - 1, -15),
                        Math.toRadians(275) //Tangent
                )
                .splineToConstantHeading(
                        new Vector2d(30, -20),
                        Math.toRadians(0)
                )
                .splineToConstantHeading(
                        new Vector2d(40, -36),
                        Math.toRadians(270)
                )
                .build());

        final TrajectorySequence trajectory;

        TrajectoriesBlue(TrajectorySequence trajectory) {
            this.trajectory = trajectory;
        }

    }


}
