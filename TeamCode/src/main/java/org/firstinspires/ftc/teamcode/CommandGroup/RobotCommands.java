package org.firstinspires.ftc.teamcode.CommandGroup;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.MMSystems;
import org.firstinspires.ftc.teamcode.SubSystems.Elevator;

import java.util.function.DoubleSupplier;

public class RobotCommands {

    private static final MMSystems mmSystems = MMRobot.getInstance().mmSystems;
    private static final double linearIntakeClosed = 0;
    private static final int timeClawClose = 200;
    public final static double elevatorDown = 0;

    /* intake recieve -
    1. open linear intake
    3. intake down and open claw*/

    public static Command IntakeCommand(DoubleSupplier intakeTrigger) {
        return new ParallelCommandGroup(
                mmSystems.linearIntake.setPositionByJoystick(intakeTrigger),
                mmSystems.intakeArm.intakeDown(),
                mmSystems.intakEndUnit.openIntakeClaw());


    }
    /*speciman intake
    open claw
    elavtor down
    prepere scoring angle
     */

    public static Command SpecimanIntake() {
        return new ParallelCommandGroup(
                mmSystems.scoringEndUnit.openScoringClaw(),
                mmSystems.elevator.moveToPose(Elevator.elevatorWallHeight),
                mmSystems.scoringEndUnit.scoreScoringServo()
        );
    }

    /* intake done  -
    1. close claw
    2. intake arm up  ,
    3. close linear intake */
    public static Command IntakeDoneCommand() {
        return new SequentialCommandGroup(
                mmSystems.intakEndUnit.closeIntakeClaw(),
                new WaitCommand(timeClawClose),
                new ParallelCommandGroup(
                        //move the angle of claw to prepare to transfer
                        mmSystems.intakeArm.intakeUp(),
                        mmSystems.linearIntake.setPosition(linearIntakeClosed),
                        mmSystems.elevator.moveToPose(elevatorDown),
                        mmSystems.scoringEndUnit.scoringArmServo(),
                        mmSystems.scoringEndUnit.openScoringClaw()
                ), mmSystems.scoringEndUnit.closeScoringClaw(),
                mmSystems.intakEndUnit.openIntakeClaw()

        );

    }

    public static Command EjectSampleCommand() {
        return new SequentialCommandGroup(
                mmSystems.scoringEndUnit.scoreScoringServo(),
                new WaitCommand(timeClawClose),
                mmSystems.scoringEndUnit.openScoringClaw()
        );
    }

    /* score sample-
    1. elevator get to desired height
    2. scoring servo turn
    3. scoring claw open
     */
    public static Command PrepareHighSample() {
        return new ParallelCommandGroup(
                mmSystems.elevator.moveToPose(Elevator.HIGH_BASKET),
                mmSystems.scoringEndUnit.scoreScoringServo());
    }

    public static Command PrepareLowSample() {
        return new ParallelCommandGroup(
                mmSystems.elevator.moveToPose(Elevator.LOW_BASKET),
                mmSystems.scoringEndUnit.scoreScoringServo());
    }

    public static Command PrepareSpecimanScore() {
        return new SequentialCommandGroup(
                mmSystems.scoringEndUnit.closeScoringClaw(),
                new WaitCommand(timeClawClose),
                new ParallelCommandGroup(
                        mmSystems.elevator.moveToPose(Elevator.highChamber),
                        mmSystems.scoringEndUnit.scoreScoringServo()
                )
        );
    }

    public static Command ScoreSpeciman() {
        return new SequentialCommandGroup(
                mmSystems.elevator.moveToPose(Elevator.highChamber),
                mmSystems.scoringEndUnit.openScoringClaw(),
                new ParallelCommandGroup(
                        mmSystems.scoringEndUnit.scoringArmServo(),
                        mmSystems.elevator.moveToPose(Elevator.elevatorDown)

                ));
    }

    /* scoring back to hold-
    1. scoring claw close
    2. elevator go back to desired height and scoring servo turn
     */
    public static Command elevatorDowm() {
        return new SequentialCommandGroup(
                MMRobot.getInstance().mmSystems.scoringEndUnit.openScoringClaw(),
                new WaitCommand(timeClawClose),
                new ParallelCommandGroup(
                        mmSystems.scoringEndUnit.scoringArmServo(),
                        mmSystems.elevator.moveToPose(Elevator.elevatorDown)
                )
        );
    }
}


