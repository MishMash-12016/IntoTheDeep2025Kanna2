package org.firstinspires.ftc.teamcode.TeleOp;

import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.CommandGroup.RobotCommands;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.SubSystems.LinearIntakeEndUnitRotator;
import org.firstinspires.ftc.teamcode.utils.OpModeType;

@TeleOp
public class TeleopGeneral extends MMOpMode {
    //Constructor for TeleopGeneral.
    public TeleopGeneral() {
        super(OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION);
    }

    /**
     * Initializes the robot systems and sets up button triggers for specific actions.
     */
    public static boolean specimantoggled = true;
    public static boolean scoringtype = true;

    @Override
    public void onInit() {
        //init robot systems
        MMRobot.getInstance().mmSystems.initRobotSystems();


        //define robot triggers
        //driver 1

        Trigger intakeSampleTrigger = new Trigger(
                () -> MMRobot.getInstance().mmSystems.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.05);
        Trigger ejectTrigger = new Trigger(
                () -> MMRobot.getInstance().mmSystems.gamepadEx1.getButton(GamepadKeys.Button.A)
        );
        Trigger scoreTrigger = new Trigger(
                () -> MMRobot.getInstance().mmSystems.gamepadEx1.getButton(GamepadKeys.Button.RIGHT_BUMPER)
        );
        Trigger prepareSpecimenScoreTrigger = new Trigger(
                () -> MMRobot.getInstance().mmSystems.gamepadEx1.getButton(GamepadKeys.Button.LEFT_BUMPER)
        );

        //driver 2
        Trigger prepareHighSampleTrigger = new Trigger(
                () -> MMRobot.getInstance().mmSystems.gamepadEx2.getButton(GamepadKeys.Button.X)
        );
        Trigger prepareLowSampleTrigger = new Trigger(
                () -> MMRobot.getInstance().mmSystems.gamepadEx2.getButton(GamepadKeys.Button.A)
        );

        Trigger specimenIntakeTrigger = new Trigger(
                () -> MMRobot.getInstance().mmSystems.gamepadEx2.getButton(GamepadKeys.Button.B)
        );
        Trigger foldSystemsTrigger = new Trigger(
                () -> MMRobot.getInstance().mmSystems.gamepadEx2.getButton(GamepadKeys.Button.Y)
        );
        Trigger intakeAngleTrigger = new Trigger(
                () -> MMRobot.getInstance().mmSystems.gamepadEx2.getButton(GamepadKeys.Button.DPAD_RIGHT)
        );


        //define actions for triggers

        //driver 1
        // Handle sample intake (Right Trigger)
        intakeSampleTrigger.whileActiveOnce(
                RobotCommands.IntakeCommand(
                        () -> MMRobot.getInstance().mmSystems.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)

                ).whenFinished(
                        () -> RobotCommands.IntakeDoneCommand().schedule()));
        //eject sample (Button A)
        ejectTrigger.whenActive(
                RobotCommands.EjectSampleCommand()
        );
        //prepare score specimen
        prepareSpecimenScoreTrigger.whenActive(
                RobotCommands.prepareSpecimenScore()
        );


        //driver 2

        // Prepare to score High Basket(Button X)
        prepareHighSampleTrigger.whenActive(
                RobotCommands.PrepareHighSample()
                        .alongWith(new InstantCommand(() -> scoringtype = true))
        );

        // Prepare to score Low Basket(Button A)
        prepareLowSampleTrigger.whenActive(
                RobotCommands.PrepareLowSample()
                        .alongWith(new InstantCommand(() -> scoringtype = true))
        );
        //score specimen/specimen (right bumper)
        scoreTrigger.whenActive(
                new ConditionalCommand(
                        RobotCommands.ScoreSample(),
                        RobotCommands.ScoreSpecimen(),
                        () -> scoringtype
                )
        );
        //intake specimen (Button B)
        specimenIntakeTrigger.whenActive(
                RobotCommands.prepareSpecimenIntake()
        );
        //fold systems(Button Y)
        foldSystemsTrigger.whenActive(
                RobotCommands.FoldSystems()
        );
        //end unit angle (right d-pad)
        intakeAngleTrigger.whenActive(
                MMRobot.getInstance().mmSystems.linearIntake.setPosition(LinearIntakeEndUnitRotator.intakePose)

        );


        //hanging joystick

    }

    @Override
    public void run() {
        super.run();
    }
}
