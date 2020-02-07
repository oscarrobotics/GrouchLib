package frc.team832.lib.util;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.music.Orchestra;
import edu.wpi.first.wpilibj.Filesystem;
import frc.team832.lib.Debug;
import frc.team832.lib.motorcontrol2.vendor.CANTalonFX;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class MusicBox {

    private final Orchestra _orchestra;
    private final ArrayList<TalonFX> _instruments = new ArrayList<>();

    public MusicBox(CANTalonFX... instruments) {
        for (var instrument : instruments) {
            _instruments.add(instrument.getBaseController());
        }

        _orchestra = new Orchestra(_instruments);
    }

    public void loadSong(String songFileName) {
        Path songPath = Path.of(Filesystem.getDeployDirectory().getAbsolutePath(),"songs", songFileName);
        if (!Files.exists(songPath)) {
            Debug.showDSError(String.format("No song at given path: %s\n", songPath.toString()));
            return;
        }

        _orchestra.loadMusic(songPath.toString());
    }

    public void play() {
        _orchestra.play();
    }
}
