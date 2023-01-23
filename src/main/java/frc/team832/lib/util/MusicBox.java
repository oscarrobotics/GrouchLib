package frc.team832.lib.util;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.music.Orchestra;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.lib.motorcontrol.vendor.CANTalonFX;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class MusicBox {

	private final Orchestra _orchestra;

	public MusicBox(CANTalonFX... instruments) {
		ArrayList<TalonFX> _instruments = new ArrayList<>();
		for (var instrument : instruments) {
			_instruments.add(instrument.getBaseController());
		}

		_orchestra = new Orchestra(_instruments);
		SendableChooser<String> songChooser = new SendableChooser<>();

		try {
			try (var lol = Files.list(Filesystem.getDeployDirectory().toPath())) {
				lol.forEach((f) ->
				{
					var name = f.getFileName().toString().replace(".chrp", "");
					System.out.println(name);
					if (f.endsWith(".chrp")) {
						songChooser.addOption(name, name);
					}
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		SmartDashboard.putData("Songs", songChooser);
	}

	public void loadSong(String songFileName) {

		_orchestra.loadMusic(songFileName);
	}

	public void play() {
		_orchestra.play();
	}
}
