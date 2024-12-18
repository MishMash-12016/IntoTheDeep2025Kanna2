package org.firstinspires.ftc.teamcode.SubSystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.RunCommand;
import com.roboctopi.cuttlefish.utils.Direction;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleEncoder;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleMotor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.PID.MMPIDCommand;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.PID.MMPIDSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.utils.Configuration;

import java.util.function.DoubleSupplier;

public class Elevator extends MMPIDSubsystem {

    //System parts:
    private final CuttleMotor motorLeft;
    private final CuttleMotor motorRight;
    private final CuttleEncoder motorEncoder;

    //constants:
    private final double TICKS_PER_REV = 537.7;
    private final double GEAR_RATIO = 2;
    private final double LEVELS = 4;
    private final double SPROCKET_PERIMETER = 6.56592;

    //PID:
    public static final double kP = 0.16;
    public static final double kI = 0;
    public static final double kD = 0;
    public static final double TOLERANCE = 2;

    double ticksOffset = 0;

    public final static double LOW_BASKET = 40;
    public final static double HIGH_BASKET = 70;

    public Elevator() {
        super(kP, kI, kD, TOLERANCE);



        motorLeft = new CuttleMotor(MMRobot.getInstance().mmSystems.controlHub, Configuration.ELEVATOR_LEFT);
        motorRight = new CuttleMotor(MMRobot.getInstance().mmSystems.controlHub, Configuration.ELEVATOR_RIGHT);

        motorLeft.setDirection(Direction.REVERSE);

        motorEncoder = new CuttleEncoder(MMRobot.getInstance().mmSystems.controlHub, Configuration.ELEVATOR_ENCODER, TICKS_PER_REV);

//        this.motorLeft.setZeroPowerBehaviour(DcMotor.ZeroPowerBehavior.BRAKE);
//        this.motorRight.setZeroPowerBehaviour(DcMotor.ZeroPowerBehavior.BRAKE);

        resetTicks();
    }

    public Command moveToPose(double setPoint){
        return new MMPIDCommand(this,setPoint);
    }

    public Command setPowerByJoystick(DoubleSupplier power){
        return new RunCommand(
                ()->setPower(power.getAsDouble())
                ,this);
    }

    @Override
    public void setPower(Double power){
        motorRight.setPower(power);
        motorLeft.setPower(power);
    }

    public double getTicks() {
        return motorEncoder.getCounts() + ticksOffset;
    }
    public void setTicks(double newTicks) {
        ticksOffset = newTicks - motorEncoder.getCounts();
    }
    public void resetTicks() {
        setTicks(0);
    }

    public double getHeight(){
        //getTicks-> current ticks value(current position of the encoder)
        //SPROCKET_PERIMETER -> gear diameter
        //LEVELS -> how many elevator levels there is
        return -1*((getTicks() / TICKS_PER_REV) * SPROCKET_PERIMETER * LEVELS / GEAR_RATIO) ;
    }

    @Override
    public double getCurrentValue() {
        return getHeight();
    }

    @Override
    public double getFeedForwardPower() {
        return 0.0;
    }

    @Override
    public void stop(){
        setPower(0.0);
    }

    @Override
    public void periodic() {
        super.periodic();
    }
}
