import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

public class Sound {

	private static float volume = 0.5f;

	public static void play(AudioInputStream stream) {
		if (stream != null) {
			try {
				Clip clip = AudioSystem.getClip();
				clip.addLineListener(new LineListener() {
					public void update(LineEvent myLineEvent) {
						if (myLineEvent.getType() == LineEvent.Type.STOP)
							clip.close();
					}
				});
				clip.open(stream);
				setVolume(volume);
				clip.start();
				stream.reset();
			} catch (IOException | LineUnavailableException e) {
				e.printStackTrace();
			}
		}
	}

	public static void loop(AudioInputStream stream) {
		if (stream != null) {
			try {
				Clip clip = AudioSystem.getClip();
				clip.open(stream);
				setVolume(volume);
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				stream.reset();
			} catch (IOException | LineUnavailableException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setVolume() {
		setVolume(volume);
	}
	
	public static void setVolume(float f) {
		volume = f;
		float dB = (float) (Math.log(f) / Math.log(10.0) * 20.0);
		Mixer.Info[] infos = AudioSystem.getMixerInfo();
		for (Mixer.Info info : infos) {
			Mixer mixer = AudioSystem.getMixer(info);
			Line[] lines = mixer.getSourceLines();
			for (Line line : lines) {
				FloatControl bc = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
				if (bc != null) {
					bc.setValue(dB);
				}
			}
		}
	}

	public static float getVolume() {
		return volume;
	}
}
