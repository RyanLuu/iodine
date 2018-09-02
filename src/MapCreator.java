import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MapCreator {

	private static final String IMAGE_PATH = "f.png";
	private static final String OUT_PATH = "f.map";

	public static void main(String[] args) {
		try {
			BufferedImage in = ImageIO.read(new File(IMAGE_PATH));
			if (in.getWidth() != Map.WIDTH_IN_TILES || in.getHeight() != Map.HEIGHT_IN_TILES) {
				throw new InvalidMapException(String.format("Image dimensions (%dx%d) are incorrect! (should be %dx%d)", in.getWidth(), in.getHeight(), Map.WIDTH_IN_TILES, Map.HEIGHT_IN_TILES));
			}
			int[] dataIn = new int[in.getWidth() * in.getHeight()];
			in.getRGB(0, 0, in.getWidth(), in.getHeight(), dataIn, 0, in.getWidth());
			FileOutputStream out = new FileOutputStream(OUT_PATH);

			byte[] dataOut = new byte[dataIn.length / 8 * 2];
			int iOut = 0;
			String bitstring = "";
			boolean hasSpawnPoint = false;
			for (int iIn = 0; iIn < dataIn.length; iIn++) {
				int x = dataIn[iIn];
				if (x == 0xFFFFFFFF) {
					bitstring += "00";
				} else if (x == 0xFF000000) {
					bitstring += "11";
				} else if (x == 0xFFFF0000) {
					bitstring += "10";
					hasSpawnPoint = true;
				} else if (x == 0xFF7F7F7F) {
					bitstring += "01";
				} else {
					out.close();
					System.out.println();
					throw new InvalidMapException(String.format("Pixel at (%d, %d) has an invalid color (%s).", iIn % in.getWidth(), iIn / in.getWidth(), "#" + Integer.toHexString(x).toUpperCase()));
				}
				if (bitstring.length() == 8) {
					System.out.print(bitstring);
					if (iOut % 8 == 7) {
						System.out.println();
					}
					dataOut[iOut] = (byte) Integer.parseInt(bitstring, 2);
					bitstring = "";
					iOut++;
				}
			}
			if (!hasSpawnPoint) {
				out.close();
				throw new InvalidMapException("Map has no spawn point!");
			}
			out.write(dataOut);
			out.close();
		} catch (IOException | InvalidMapException e) {
			e.printStackTrace();
		}
	}
}

@SuppressWarnings("serial")
class InvalidMapException extends Exception {
	
	public InvalidMapException(String message) {
		super(message);
	}
	
}