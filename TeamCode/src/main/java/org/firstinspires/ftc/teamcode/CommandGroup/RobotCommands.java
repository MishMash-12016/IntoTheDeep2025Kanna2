package org.firstinspires.ftc.teamcode.CommandGroup;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.MMSystems;
import org.firstinspires.ftc.teamcode.SubSystems.Elevator;
import org.firstinspires.ftc.teamcode.SubSystems.IntakeArm;
import org.firstinspires.ftc.teamcode.SubSystems.LinearIntakeEndUnitRotator;
import org.firstinspires.ftc.teamcode.SubSystems.ScoringArm;

import java.util.function.DoubleSupplier;

public class RobotCommands {
    private static final  MMSystems mmSystems= MMRobot.getInstance().mmSystems;
    private static final double linearIntakeClosed = 0;
    private static final int timeClawClose = 200;
    private static final int timeClawOpen = 100;
    public final static double elevatorDown = 0;
    public final static int timeScoringArm = 150;

    /* intake receive -
    1. open linear intake
    2. intake down
    3. open claw */

    public static Command IntakeCommand(DoubleSupplier intakeTrigger) {
        return new ParallelCommandGroup(
                mmSystems.linearIntake.setPositionByJoystick(intakeTrigger),
                mmSystems.intakeArm.setPosition(IntakeArm.down),
                mmSystems.linearIntakeEndUnitRotator.setPosition(LinearIntakeEndUnitRotator.intakePose),
                mmSystems.intakEndUnit.openIntakeClaw());


    }
    /* specimen intake
    open claw
    elevator down
    prepare scoring angle
     */

    public static Command prepareSpecimenIntake() {
        return new ParallelCommandGroup(
                mmSystems.scoringClawEndUnit.openScoringClaw(),
                mmSystems.elevator.moveToPose(Elevator.elevatorWallHeight),
                mmSystems.scoringArm.setPosition(ScoringArm.scoreSpecimen)
        );
    }

    /* intake done  -
    1. close claw
    2. intake arm up  ,
    3. close linear intake
    4. elevator down
    5. open+close scoring claw
    6. open intake claw
    */
    public static Command IntakeDoneCommand() {
        return new SequentialCommandGroup(
                mmSystems.intakEndUnit.closeIntakeClaw(),
                new WaitCommand(timeClawClose),
                new ParallelCommandGroup(
                        //move the angle of claw to prepare to transfer
                        mmSystems.intakeArm.setPosition(IntakeArm.up),
                        mmSystems.linearIntakeEndUnitRotator.setPosition(LinearIntakeEndUnitRotator.holdpose),
                        mmSystems.linearIntake.setPosition(linearIntakeClosed),
                        mmSystems.elevator.moveToPose(elevatorDown),
                        mmSystems.scoringArm.setPosition(ScoringArm.scoringArmHold),
                        mmSystems.scoringClawEndUnit.openScoringClaw()
                ), mmSystems.scoringClawEndUnit.closeScoringClaw(),
                mmSystems.intakEndUnit.openIntakeClaw()

        );

    }

    public static Command EjectSampleCommand() {
        return new SequentialCommandGroup(
                mmSystems.scoringArm.setPosition(ScoringArm.scoreSpecimen),
                new WaitCommand(timeScoringArm),
                mmSystems.scoringClawEndUnit.openScoringClaw(),
                new WaitCommand(timeClawOpen),
                mmSystems.scoringArm.setPosition(ScoringArm.scoringArmHold)

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
                mmSystems.scoringArm.setPosition(ScoringArm.scoreSample));
    }

    public static Command PrepareLowSample() {
        return new ParallelCommandGroup(
                mmSystems.elevator.moveToPose(Elevator.LOW_BASKET),
                mmSystems.scoringArm.setPosition(ScoringArm.scoreSample));
    }

    public static Command prepareSpecimenScore() {
        return new SequentialCommandGroup(
                mmSystems.scoringClawEndUnit.closeScoringClaw(),
                new WaitCommand(timeClawClose),
                new ParallelCommandGroup(
                        mmSystems.elevator.moveToPose(Elevator.highChamber),
                        mmSystems.scoringArm.setPosition(ScoringArm.scoreSpecimen)
                )
        );
    }

    public static Command ScoreSpecimen() {
        return new SequentialCommandGroup(
                mmSystems.elevator.moveToPose(Elevator.highChamberScorePose),
                mmSystems.scoringClawEndUnit.openScoringClaw(),
                new ParallelCommandGroup(
                        mmSystems.scoringArm.setPosition(ScoringArm.scoreSample),
                        mmSystems.elevator.moveToPose(Elevator.elevatorDown)

                ));
}

public static Command ScoreSample() {
    return new SequentialCommandGroup(
            mmSystems.scoringClawEndUnit.openScoringClaw(),
            new WaitCommand(timeClawOpen),
            new ParallelCommandGroup(
                    mmSystems.elevator.moveToPose(Elevator.elevatorDown),
                    mmSystems.scoringArm.setPosition(ScoringArm.scoringArmHold)
            ));
}

public static Command FoldSystems() {
    return new ParallelCommandGroup(
            mmSystems.elevator.moveToPose(Elevator.elevatorDown),
            mmSystems.scoringArm.setPosition(ScoringArm.scoringArmHold),
            mmSystems.linearIntake.setPosition(linearIntakeClosed),
            mmSystems.intakeArm.setPosition(IntakeArm.up),
            mmSystems.linearIntakeEndUnitRotator.setPosition(LinearIntakeEndUnitRotator.holdpose),
            mmSystems.intakEndUnit.openIntakeClaw()
    );
}


}


