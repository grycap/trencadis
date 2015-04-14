package trencadis.infrastructure.services.dicomstorage.indexer.supports.amga.dsr.objects;

import java.io.FileWriter;
import java.io.IOException;

import trencadis.infrastructure.services.dicomstorage.indexer.supports.amga.dsr.objects.DSRNode;

public class CODE extends DSRNode {

	private static String[] attrKeys = new String[]{
		"ENTRY_CODE_VALUE", "ENTRY_CODE_SCHEMA", "ENTRY_CODE_MEANING"
	};
	private static String[] attrTypes = new String[]{
		"TEXT", "TEXT", "TEXT"
	};
	private String[] attrValues = new String[attrKeys.length];

	public CODE() {
	}

	public void setEntryValue(String value) {
		attrValues[0] = value;
	}

	public void setEntrySchema(String value) {
		attrValues[1] = value;
	}

	public void setEntryMeaning(String value) {
		attrValues[2] = value;
	}

	public String[] getAttrKeys() {
		String[] aux = new String[attrKeysCN.length + attrKeys.length];
		System.arraycopy(attrKeysCN, 0, aux, 0, attrKeysCN.length);
		System.arraycopy(attrKeys, 0, aux, attrKeysCN.length, attrKeys.length);

		return aux;
	}

	public String[] getAttrTypes() {
		String[] aux = new String[attrTypesCN.length + attrTypes.length];
		System.arraycopy(attrTypesCN, 0, aux, 0, attrTypesCN.length);
		System.arraycopy(attrTypes, 0, aux, attrTypesCN.length, attrTypes.length);

		return aux;
	}

	public String[] getAttrValues() {
		String[] aux = new String[attrValuesCN.length + attrValues.length];
		System.arraycopy(attrValuesCN, 0, aux, 0, attrValuesCN.length);
		System.arraycopy(attrValues, 0, aux, attrValuesCN.length, attrValues.length);

		return aux;
	}

	public void printDICOMSRBinaryObjectData(FileWriter fw, int treeLevel) throws IOException {

		if (treeLevel > 0) {
			fw.write("(fffe,e000) Item\n");
		}

		fw.write("(0040,a040) Value Type \"CODE\"\n");
		fw.write("(0040,a043) Concept Name Code Sequence\n");
		fw.write("(fffe,e000) Item\n");
		fw.write("(0008,0100) Code Value \"" + attrValuesCN[0] + "\"\n");
		fw.write("(0008,0102) Coding Scheme Designator \"" + attrValuesCN[1] + "\"\n");
		fw.write("(0008,0104) Code Meaning \"" + attrValuesCN[2] + "\"\n");
		fw.write("(fffe,e00d) Item Delimitation Item\n");
		fw.write("(fffe,e0dd) Sequence Delimitation Item\n");
		fw.write("(0040,a168) Concept Code Sequence\n");
		fw.write("(fffe,e000) Item\n");
		fw.write("(0008,0100) Code Value \"" + attrValues[0] + "\"\n");
		fw.write("(0008,0102) Coding Scheme Designator \"" + attrValues[1] + "\"\n");
		fw.write("(0008,0104) Code Meaning \"" + attrValues[2] + "\"\n");
		fw.write("(fffe,e00d) Item Delimitation Item\n");
		fw.write("(fffe,e0dd) Sequence Delimitation Item\n");

	}

	public String getTypeIdentifier() {
		return "CODE";
	}

}
