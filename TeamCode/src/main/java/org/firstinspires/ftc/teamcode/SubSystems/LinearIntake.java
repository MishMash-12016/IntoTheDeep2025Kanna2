package org.firstinspires.ftc.teamcode.SubSystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.SubsystemBase;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleServo;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.utils.Configuration;

import java.util.function.DoubleSupplier;

public class LinearIntake extends SubsystemBase {

    private final MMRobot robotInstance = MMRobot.getInstance();

    private final CuttleServo servoLeft;
    private final CuttleServo servoRight;

    public LinearIntake(){
        servoLeft = new CuttleServo(robotInstance.mmSystems.controlHub, Configuration.LEFT_LINEAR_INTAKE);
        servoRight = new CuttleServo(robotInstance.mmSystems.expansionHub, Configuration.RIGHT_LINEAR_INTAKE);

        servoRight.setPosition(0);
        servoLeft.setPosition(0);

        this.setDefaultCommand(setPosition(0).perpetually());
    }

    public Command setPosition(double newPos){
        return new InstantCommand(()-> {
            servoLeft.setPosition(1 - newPos);
            servoRight.setPosition(newPos);} ,
                this);
    }

    public Command setPositionByJoystick(DoubleSupplier doubleSupplier){
        return new RunCommand(()-> {
            servoLeft.setPosition(doubleSupplier.getAsDouble());
            servoRight.setPosition(1 - doubleSupplier.getAsDouble());} ,
                this);
    }

    public String getPosition(){
        return String.valueOf(servoLeft.getPosition())+" "+String.valueOf(servoRight.getPosition()) ;
    }
}