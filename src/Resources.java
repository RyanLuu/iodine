import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Resources {

	public static BufferedImage BACKGROUND_IMAGE;
	public static AudioInputStream BGM_1, BGM_2;
	public static AudioInputStream BUTTON_PRESS, PLAY_BUTTON_PRESS;
	public static AudioInputStream JUMP, KILL, SHIELD, ATTACK;
	public static Font FONT;

	public static void load() {
		BGM_1 = loadAudio("unity-light.wav");
		BGM_2 = loadAudio("unity.wav");
		BUTTON_PRESS = loadAudio("press.wav");
		PLAY_BUTTON_PRESS = loadAudio("play.wav");
		JUMP = loadAudio("jump.wav");
		KILL = loadAudio("kill.wav");
		SHIELD = loadAudio("shield.wav");
		ATTACK = loadAudio("attack.wav");
		BACKGROUND_IMAGE = loadImage("bg.png");
		FONT = loadFont("neuropol.ttf");
	}

	private static AudioInputStream loadAudio(String path) {
		AudioInputStream ais = null;
		try {
			File file = new File("assets/audio/" + path);
			ais = AudioSystem.getAudioInputStream(file);
			byte[] buffer = new byte[1024 * 32];
			int read = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream(buffer.length);
			while ((read = ais.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, read);
			}
			AudioInputStream reusableAis = new AudioInputStream(new ByteArrayInputStream(baos.toByteArray()),
					ais.getFormat(), AudioSystem.NOT_SPECIFIED);
			return reusableAis;
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		} finally {
			if (ais != null) {
				try {
					ais.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ais;
	}

	private static BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(new File("assets/images/" + path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Font loadFont(String path) {
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/" + path));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(font);
			return font;
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
