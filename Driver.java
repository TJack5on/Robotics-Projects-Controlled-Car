import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;

import lejos.hardware.Bluetooth;
import lejos.remote.nxt.NXTCommConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.utility.Delay;
import lejos.hardware.motor.Motor;

public class Driver {

	public static void main(String[] args) throws Exception {
		NXTCommConnector connector = Bluetooth.getNXTCommConnector();

		System.out.println("Waiting for connection ...");
		NXTConnection con = connector.waitForConnection(0, NXTConnection.RAW);
		System.out.println("Connected");
		System.out.println("Opening data Stream");
		DataInputStream dis = con.openDataInputStream();
		DataOutputStream dos = con.openDataOutputStream();
		System.out.println("Data Stream Open");

		int fullSpeed = 1000;
		System.out.println("Setting speed...");
		Motor.A.setSpeed(fullSpeed);
		Motor.D.setSpeed(fullSpeed);
		System.out.println("Speed set at 500");

		// byte[] n = new byte[8];
		// int n = dis.readInt();
		while (true) {
			// try{
			// if (dis.readInt() == -1)
			// break;
			// } catch (EOFException e)
			// {
			// break;
			// }
			// System.out.println("Read " + n[0]+n[1]+ n[2]+n[3]+ n[4]+n[5]+
			// n[6]+n[7]);
			System.out.println("reading n from other robot");
			int n = dis.read();
			System.out.println("n = " + n);

			dos.write(n);
			// dos.flush();

			// System.out.println(n[0]);
			// if (n == 1) {
			// System.out.println("up");
			// Motor.A.forward();
			// Motor.D.forward();
			// dos.write(n);

			if (n == 1) {
				System.out.println("up");

				while (n != 2) {
					n = dis.read();
					Motor.A.setSpeed(fullSpeed);
					Motor.D.setSpeed(fullSpeed);
					Motor.A.forward();
					Motor.D.forward();

					if (n == 16) {
						Motor.A.setSpeed(fullSpeed - 450);
						Motor.A.forward();
						Motor.D.forward();
						n = 1;
					}

					if (n == 8) {
						Motor.D.setSpeed(fullSpeed - 450);
						Motor.A.forward();
						Motor.D.forward();
						n = 1;
					}
					Delay.msDelay(300);
					dos.write(n);
				}
			} else if (n == 4) {
				System.out.println("down");
				Motor.A.backward();
				Motor.D.backward();
				dos.write(n);
			} else if (n == 16) {
				System.out.println("left");
				Motor.A.rotate(-180);
				Motor.D.rotate(180);
				dos.write(n);
			} else if (n == 8) {
				System.out.println("right");
				Motor.D.rotate(-180);
				Motor.A.rotate(180);

				dos.write(n);
			} else if (n == 2) {
				System.out.println("center");
				Motor.A.stop();
				Motor.D.stop();
				dos.write(n);
			} else {

			}
		}

	}
}