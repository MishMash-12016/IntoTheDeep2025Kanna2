package org.firstinspires.ftc.teamcode.SubSystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;
import com.roboctopi.cuttlefish.utils.Direction;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleMotor;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.utils.Configuration;

import java.util.function.DoubleSupplier;

@Config
public class DriveTrain extends SubsystemBase {

    final double[][] transformationMatrix = {
            {1, 1, 1}, //frontLeft
            {-1, 1, 1}, //backLeft
            {-1, 1, -1}, //frontRight
            {1, 1, -1} //backRight
    };

    MMRobot mmRobot = MMRobot.getInstance();

    private final CuttleMotor motorFR;
    private final CuttleMotor motorFL;
    private final CuttleMotor motorBL;
    private final CuttleMotor motorBR;
    private double yawOffset = 0;

    public DriveTrain() {
        super(); //register this subsystem, in order to schedule default command later on.

        motorFL = new CuttleMotor(mmRobot.mmSystems.controlHub, Configuration.DRIVE_TRAIN_FRONT_LEFT);
        motorBL = new CuttleMotor(mmRobot.mmSystems.controlHub, Configuration.DRIVE_TRAIN_BACK_LEFT);
        motorFR = new CuttleMotor(mmRobot.mmSystems.controlHub, Configuration.DRIVE_TRAIN_FRONT_RIGHT);
        motorBR = new CuttleMotor(mmRobot.mmSystems.controlHub, Configuration.DRIVE_TRAIN_BACK_RIGHT);

        //TODO: reverse motors as needed
        motorBL.setDirection(Direction.REVERSE);
        motorFL.setDirection(Direction.REVERSE);

    }

    public DriveTrain(double lastAngle) {
        this();
        mmRobot.mmSystems.imu.setYaw(lastAngle);
    }


    private double[] joystickToPower(double x, double y, double yaw) {

        //v = (x, y, yaw)^t (3x1)
        RealVector joystickVector = MatrixUtils.createRealVector(new double[]{
                x,
                y,
                yaw
        });

        RealMatrix matrixT = MatrixUtils.createRealMatrix(transformationMatrix); //4x3

        //calculation of the power needed by T constants
        RealVector powerVector = matrixT.operate(joystickVector); //p = Tv

        double[] powerArray = powerVector.toArray(); //4x1

        //normalize the array
        for (int i = 0; i < powerArray.length; i++) {
            powerArray[i] = powerArray[i] / Math.max(Math.abs(x) + Math.abs(y) + Math.abs(yaw), 1);
        }

        return powerArray;

    }

    private void setMotorPower(double[] power) {
        motorFL.setPower(power[0]);
        motorBL.setPower(power[1]);
        motorFR.setPower(power[2]);
        motorBR.setPower(power[3]);
        updateTelemetry(power);
    }

    public void drive(double x, double y, double yaw) {
        setMotorPower(joystickToPower(x, y, yaw));
    }


    public Command fieldOrientedDrive(DoubleSupplier x, DoubleSupplier y, DoubleSupplier yaw) {
        return new RunCommand(
                () -> {
                    Vector2d joystickDirection = new Vector2d(x.getAsDouble(), y.getAsDouble());
                    Vector2d fieldOrientedVector = joystickDirection.rotateBy(-mmRobot.mmSystems.imu.getYawInDegrees());
                    drive(fieldOrientedVector.getX(), fieldOrientedVector.getY(), yaw.getAsDouble());
                }, this);
    }


    public void updateTelemetry(double[] power) {
        FtcDashboard.getInstance().getTelemetry().addData("frontLeft", power[0]);
        FtcDashboard.getInstance().getTelemetry().addData("backLeft", power[1]);
        FtcDashboard.getInstance().getTelemetry().addData("frontRight", power[2]);
        FtcDashboard.getInstance().getTelemetry().addData("backRight", power[3]);
        FtcDashboard.getInstance().getTelemetry().update();
    }

}

