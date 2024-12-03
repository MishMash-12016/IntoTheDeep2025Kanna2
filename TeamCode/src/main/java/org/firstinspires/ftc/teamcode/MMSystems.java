
package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleRevHub;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.MMBattery;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.MMIMU;
import org.firstinspires.ftc.teamcode.SubSystems.DriveTrain;
import org.firstinspires.ftc.teamcode.SubSystems.LinearIntake;
import org.firstinspires.ftc.teamcode.SubSystems.IntakEndUnit;
import org.firstinspires.ftc.teamcode.SubSystems.IntakeArm;
import org.firstinspires.ftc.teamcode.SubSystems.LinearIntakeEndUnitRotator;
import org.firstinspires.ftc.teamcode.SubSystems.Elevator;
import org.firstinspires.ftc.teamcode.SubSystems.ScoringArm;
import org.firstinspires.ftc.teamcode.SubSystems.ScoringClawEndUnit;
import org.firstinspires.ftc.teamcode.utils.AllianceColor;
import org.firstinspires.ftc.teamcode.utils.AllianceSide;
import org.firstinspires.ftc.teamcode.utils.OpModeType;

/**
 * this class should contain all ur robot's attributes and systems
 */
public class MMSystems {

    //Attributes & Hardware
    public OpModeType opModeType;
    public AllianceColor allianceColor;
    public AllianceSide robotSide;
    public HardwareMap hardwareMap;
    public CuttleRevHub controlHub;
    public CuttleRevHub expansionHub;
    public GamepadEx gamepadEx1;
    public GamepadEx gamepadEx2;
    public Telemetry telemetry;
    public MMBattery battery;
    public MMIMU imu;


    //Subsystems
    public DriveTrain driveTrain;
    public LinearIntake linearIntake;
    public IntakEndUnit intakEndUnit;
    public IntakeArm intakeArm;
    public LinearIntakeEndUnitRotator linearIntakeEndUnitRotator;

    public ScoringArm scoringArm;
    public ScoringClawEndUnit scoringClawEndUnit;
    public Elevator elevator;


    //creating and initiating all subsystems
    public void initRobotSystems() {
        driveTrain = new DriveTrain();
        driveTrain.setDefaultCommand(
                MMRobot.getInstance().mmSystems.driveTrain.fieldOrientedDrive(
                        () -> gamepadEx1.getLeftX(),
                        () -> gamepadEx1.getLeftY(),
                        () -> gamepadEx1.getRightX())
        );

        elevator = new Elevator();
        linearIntake = new LinearIntake();

        this.intakEndUnit = new IntakEndUnit();
        this.intakeArm = new IntakeArm();
        this.scoringArm = new ScoringArm();
        this.scoringClawEndUnit = new ScoringClawEndUnit();
        linearIntakeEndUnitRotator = new LinearIntakeEndUnitRotator();

    }


    public MMSystems(OpModeType type, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry) {
        this.opModeType = type;
        this.hardwareMap = hardwareMap;
        this.controlHub = new CuttleRevHub(hardwareMap, CuttleRevHub.HubTypes.CONTROL_HUB);
        if (type != OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION) {
            this.expansionHub = new CuttleRevHub(hardwareMap, "Expansion Hub 1");
        }
        this.gamepadEx1 = new GamepadEx(gamepad1);
        this.gamepadEx2 = new GamepadEx(gamepad2);
        this.telemetry = telemetry;
        this.battery = new MMBattery(hardwareMap);
        this.imu = new MMIMU(hardwareMap);

        CommandScheduler.getInstance().reset(); //reset the scheduler
    }
}
