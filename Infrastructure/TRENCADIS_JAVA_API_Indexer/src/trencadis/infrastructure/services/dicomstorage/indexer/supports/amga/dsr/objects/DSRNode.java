package trencadis.infrastructure.services.dicomstorage.indexer.supports.amga.dsr.objects;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public abstract class DSRNode {

	protected int id         = 1;
        protected int iCardMin   = 0;
        protected int iCardMax   = 0;
        protected String strPath = new String();
        
        

	protected static String[] attrKeysCN = new String[]{
		"CODE_VALUE", "CODE_SCHEMA", "CODE_MEANING"
	};
	protected static String[] attrTypesCN = new String[]{
		"TEXT", "TEXT", "TEXT"
	};
	protected String[] attrValuesCN = new String[attrKeysCN.length];
	protected LinkedList<DSRNode> children = new LinkedList<DSRNode>();

	public DSRNode() {
	}

	public void setId(int value) {
		id = value;
	}
        public void setCardMin(int value) {
		iCardMin = value;
	}
	public void setCardMax(int value) {
		iCardMax = value;
	}

	public void addPath(String straddPath) {
		if (strPath.equals("")) strPath = straddPath;
		else strPath = strPath + "/" + straddPath;
	}
    public void setPath(String strPath) {
    	this.strPath = strPath;
                
	}
                
	public void setCodeValue(String value) {
		attrValuesCN[0] = value;
	}
    public String getCodeValue() {
		return attrValuesCN[0];
	}

	public void setCodeSchema(String value) {
		attrValuesCN[1] = value;
	}
    public String getCodeSchema() {
		return attrValuesCN[1];
	}

	public void setCodeMeaning(String value) {
		attrValuesCN[2] = value;
	}
        
    public String getCodeMeaning() {
		return attrValuesCN[2];
	}
        
	public int getId() {
		return id;
	}
        
    public int getCardMin() {
		return iCardMin;
	}
	public int getCardMax() {
		return iCardMax;
	}
               	
    public String getPath() {
		return strPath;
	}
        
	public String[] getAttrKeysCN() {
		return attrKeysCN;
	}

	public String[] getAttrTypesCN() {
		return attrTypesCN;
	}

	public String[] getAttrValuesCN() {
		return attrValuesCN;
	}

	public String getDirName() {
		return attrValuesCN[0] + "_" + attrValuesCN[1] + "_" + id;
	}
        
        public String getDirName2() {
		return attrValuesCN[0] + "_" + attrValuesCN[1];
	}

	public abstract String getTypeIdentifier();

	public abstract String[] getAttrKeys();

	public abstract String[] getAttrTypes();

	public abstract String[] getAttrValues();

	public static void printDICOMSRBinaryTreeStart(FileWriter fw) throws IOException {
		fw.write("(0040,a730) Content Sequence\n");
	}

	public static void printDICOMSRBinaryTreeEnd(FileWriter fw) throws IOException {
		fw.write("(fffe,e0dd) Sequence Delimitation Item\n");
	}

	public static void printDICOMSRTreeElementEnd(FileWriter fw) throws IOException {
		fw.write("(fffe,e00d) Item Delimitation Item\n");
	}

	public abstract void printDICOMSRBinaryObjectData(FileWriter fw, int treeLevel) throws IOException;

}
