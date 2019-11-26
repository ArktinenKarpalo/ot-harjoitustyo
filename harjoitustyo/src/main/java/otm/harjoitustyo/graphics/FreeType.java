package otm.harjoitustyo.graphics;

import com.mlomb.freetypejni.Face;
import com.mlomb.freetypejni.Library;
import otm.harjoitustyo.Resources;

public class FreeType {

	private static FreeType freeType = new FreeType();
	private static Library lib;
	private String currentFont = "";
	private Face face;

	public static FreeType getInstance() {
		return freeType;
	}

	public Face getFont(String fontName) {
		if(!currentFont.equals(fontName)) {
			currentFont = fontName;
			face = lib.newFace(Resources.getResourceAsTemporaryFile(fontName).getAbsolutePath(), 0);
		}
		return face;
	}

	private FreeType() {
		lib = com.mlomb.freetypejni.FreeType.newLibrary();
	}

	public void delete() {
		lib.delete();
	}
}
