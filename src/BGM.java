import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class BGM {

	private static AudioInputStream stream;
	private static Clip bgm;

	public static void setTrack(AudioInputStream stream) {
		if (BGM.stream == stream) {
			return;
		}
		try {
			if (bgm != null && bgm.isRunning()) {
				bgm.stop();
			}
			bgm = AudioSystem.getClip();
			bgm.open(stream);
			Sound.setVolume();
			bgm.loop(Clip.LOOP_CONTINUOUSLY);
			BGM.stream = stream;
		} catch (IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}
}
