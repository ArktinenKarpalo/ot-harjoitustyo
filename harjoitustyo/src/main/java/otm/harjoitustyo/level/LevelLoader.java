package otm.harjoitustyo.level;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import otm.harjoitustyo.Resources;

public class LevelLoader {

	private static int FORMAT_VERSION = 1;

	public static Level loadLevel(String path) {
		String levelName = null;
		String levelBackground = null;
		String levelBackgroundPath = null;
		String levelMusicPath = null;
		float scrollingSpeed = 0;
		LevelEvent[] levelEvents = null;
		try {
			Path zipRoot = FileSystems.newFileSystem(Resources.getResourceAsTemporaryFile(path).toPath(), null)
				.getRootDirectories()
				.iterator()
				.next();
			Object[] zipPaths = Files.walk(zipRoot).toArray();
			for(int i=0; i<zipPaths.length; i++) {
				Path p = (Path)zipPaths[i];
				if(p.getFileName() == null) // Is not a file
					continue;
				if(p.getFileName().toString().equals("level.ogg")) {
					levelMusicPath = makeTempCopy(p).toAbsolutePath().toString();
				} else if(p.getFileName().toString().equals("level.mp4")) {
					levelBackgroundPath = makeTempCopy(p).toAbsolutePath().toString();
				} else if(p.getFileName().toString().equals("info")) {
					List<String> lines = Files.readAllLines(p);
					if(Integer.parseInt(lines.get(0)) != FORMAT_VERSION) {
						throw new Error("Save file format of version: " + lines.get(0) + " expecting: " + FORMAT_VERSION);
					}
					levelName = lines.get(1);
					levelBackground = lines.get(2);
					scrollingSpeed = Float.parseFloat(lines.get(3));
					levelEvents = new LevelEvent[lines.size()-4];
					for(int j=4; j<lines.size(); j++) {
						String[] lineContent = lines.get(j).split(" ");
						if(lineContent[0].equals("KEY_PRESS")) {
							levelEvents[j-4] = new LevelEvent(LevelEventType.KEY_PRESS, Integer.parseInt(lineContent[2]), Long.parseLong(lineContent[1]));
						} else if(lineContent[0].equals("KEY_HOLD")) {
							levelEvents[j-4] = new LevelEvent(LevelEventType.KEY_HOLD, Integer.parseInt(lineContent[2]), Long.parseLong(lineContent[1]), Long.parseLong(lineContent[3]));
						}
					}
				}
			}
			return new Level(levelName, levelBackground, levelBackgroundPath, levelMusicPath, scrollingSpeed, levelEvents);
		} catch(IOException err) {
			err.printStackTrace();
		}
		return null;
	}

	private static Path makeTempCopy(Path path) {
		try {
			Path filePath = Files.createTempFile("otm_temp_", path.getFileName().toString());
			Files.copy(path, filePath, StandardCopyOption.REPLACE_EXISTING);
			filePath.toFile().deleteOnExit();
			return filePath;
		} catch(IOException err) {
			err.printStackTrace();
		}
		return null;
	}
}
