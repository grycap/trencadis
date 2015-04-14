
package trencadis.middleware.files.pdfs;

import com.itextpdf.text.*;

import java.awt.Color;


public class CONSTANTS_PDF_REPORTS {

    //xPath relacionados con campos a insertar en la cabecera de los informes
    //Parametros @TYPE_EXPLORATION_CODE_SCHEME @TYPE_EXPLORATION_CODE_VALUE
    static String XPath_IDPaciente        = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='@TYPE_EXPLORATION_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@TYPE_EXPLORATION_CODE_SCHEME']/CHILDREN/TEXT[CONCEPT_NAME/CODE_VALUE='RID13159' and CONCEPT_NAME/CODE_SCHEMA='RADLEX']/VALUE";
    static String XPath_IDInforme         = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='@TYPE_EXPLORATION_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@TYPE_EXPLORATION_CODE_SCHEME']/CHILDREN/TEXT[CONCEPT_NAME/CODE_VALUE='118522005' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/VALUE";
    static String XPath_FechaInforme      = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='@TYPE_EXPLORATION_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@TYPE_EXPLORATION_CODE_SCHEME']/CHILDREN/DATE[CONCEPT_NAME/CODE_VALUE='399651003' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/VALUE";
    static String XPath_Procedencia       = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='@TYPE_EXPLORATION_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@TYPE_EXPLORATION_CODE_SCHEME']/CHILDREN/CODE[CONCEPT_NAME/CODE_VALUE='TRMM0161' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/VALUE/CODE_MEANING";
    static String XPath_OtrasProcedencias = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='@TYPE_EXPLORATION_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@TYPE_EXPLORATION_CODE_SCHEME']/CHILDREN/TEXT[CONCEPT_NAME/CODE_VALUE='TRMM0170' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/VALUE";
    static String XPath_EstudioDICOM      = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='@TYPE_EXPLORATION_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@TYPE_EXPLORATION_CODE_SCHEME']/CHILDREN/TEXT[CONCEPT_NAME/CODE_VALUE='TRMM0017' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/VALUE";
    
    //xPath que retorna el numero de lesiones en un tipo de informe determinado (@TYPE_EXPLORATION_CODE_SCHEME, @TYPE_EXPLORATION_CODE_VALUE),
    //de una mama determinada (@BREAST_CODE_SCHEME, @BREAST_CODE_VALUE) y de un tipo de lesion (@TYPE_LESION_CODE_SCHEME, @TYPE_LESION_CODE_VALUE)
    //tipo determinado en una mlesiones
    static String XPath_Numero_Lesiones          = "count(/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='@TYPE_EXPLORATION_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@TYPE_EXPLORATION_CODE_SCHEME']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@BREAST_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@BREAST_CODE_SCHEME']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@TYPE_LESION_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@TYPE_LESION_CODE_SCHEME'])";    
    
    //xPath que retorna los identificadores de lesiones en un tipo de informe determinado (@TYPE_EXPLORATION_CODE_SCHEME, @TYPE_EXPLORATION_CODE_VALUE),
    static String XPath_Identificadores_Lesiones =       "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='@TYPE_EXPLORATION_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@TYPE_EXPLORATION_CODE_SCHEME']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@BREAST_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@BREAST_CODE_SCHEME']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@TYPE_LESION_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@TYPE_LESION_CODE_SCHEME']/CHILDREN/TEXT[CONCEPT_NAME/CODE_VALUE='118522005' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/VALUE";
    
    
    //xPath que retorna las localizaciones de la mama derecha(@TYPE_EXPLORATION_CODE_SCHEME, @TYPE_EXPLORATION_CODE_VALUE),
    //de una mama determinada (@BREAST_CODE_SCHEME, @BREAST_CODE_VALUE) y de un tipo de lesion (@TYPE_LESION_CODE_SCHEME, @TYPE_LESION_CODE_VALUE) y de una lesion en concreto @IDLesion
    //de una localización determinada @LOCALIZACION_CODE_SCHEME, @LOCALIZACION_CODE_VALUE    
    static String XPath_Localizacion                        = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='@TYPE_EXPLORATION_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@TYPE_EXPLORATION_CODE_SCHEME']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@BREAST_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@BREAST_CODE_SCHEME']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@TYPE_LESION_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@TYPE_LESION_CODE_SCHEME']/CHILDREN[TEXT/CONCEPT_NAME/CODE_VALUE='118522005' and TEXT/CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT' and TEXT/VALUE = '@IDLESION']/NUM[CONCEPT_NAME/CODE_VALUE='@LOCALIZACION_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@LOCALIZACION_CODE_SCHEME']/VALUE";                                                                        
    
    
    //Breast Zones
    static String [] astrCODE_SCHEMES_Right_Breast_Zones = {"RADLEX",  "RADLEX",  "RADLEX",  "RADLEX",  "RADLEX",  "RADLEX",  "RADLEX",  "RADLEX",  "RADLEX",  "RADLEX",  "RADLEX",  "TRENCADIS_MAMO","TRENCADIS_MAMO","TRENCADIS_MAMO","TRENCADIS_MAMO"};
    static String [] astrCODE_VALUES_Right_Breast_Zones  = {"RID29929","RID29935","RID29932","RID29938","RID29947","RID29941","RID29953","RID29944","RID29950","RID29907","RID29918","TRMM0001",      "TRMM0003",      "TRMM0005",      "TRMM0007" };
    static String [] astrCODE_SCHEMES_Left_Breast_Zones =  {"RADLEX",  "RADLEX",  "RADLEX",  "RADLEX",  "RADLEX",  "RADLEX",  "RADLEX",  "RADLEX",  "RADLEX",  "RADLEX",  "RADLEX",  "TRENCADIS_MAMO","TRENCADIS_MAMO","TRENCADIS_MAMO","TRENCADIS_MAMO"};
    static String [] astrCODE_VALUES_Left_Breast_Zones  =  {"RID29930","RID29936","RID29933","RID29939","RID29948","RID29942","RID29954","RID29945","RID29951","RID29908","RID29919","TRMM0002",      "TRMM0004",      "TRMM0006",      "TRMM0008"};
         
    static String [] astrCODE_MEANING_Breast_Zones = {"Cuadrante Supero-Externo (CSE)",
                                                            "Cuadrante Infero-Externo (CIE)",
                                                            "Cuadrante Supero-Interno (CSI)",
                                                            "Cuadrante Infero-Interno (CII)",
                                                            "Línea Intercudrántica Externa (LIE)",
                                                            "Línea Intercuadrántica Superior (LIS)", 
                                                            "Línea Intercudrántica Inferior (LIInf)",
                                                            "Línea Intercudrántica Interna (LIInt)",
                                                            "Retroareolar",	
                                                            "Pezón",
                                                            "Areola",
                                                            "Prolongación Axilar",
                                                            "Axila",
                                                            "Surco Inframamario",
                                                            "Surco Intermamario"
                                                        };
    
    
    
    
    
    
    
       
    
    
    
    
    
      
       
        
    
    
    
    
    
    
    
    
    //#007DC3     0,125,195    Azul oscuro
    //#B5CBE9    181,203,233   Azul claro

    static BaseColor azul_oscuro = new BaseColor(new Color(0,125,195));
    static BaseColor azul_claro = new BaseColor(new Color(181,203,233));
    static Font font1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 1);
    static Font font2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 2);
    static Font font4 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 4);
    static Font font6 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 6);
    static Font font8  = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8);

    static Font font11 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11);
    static Font font12 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);
    static Font font13 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13);
    static Font font10Bold = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD);
    static Font font11Bold = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD);
    static Font font12Bold = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
    static Font font13Bold = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD);
    static Font font14Bold = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
    static Font font15Bold = FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, Font.BOLD);
    static Font font16Bold = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD);
    static Font font18Bold = FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD);
    static Font font20Bold = FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.BOLD, BaseColor.BLUE);
    static Font font10 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10);	
        
    static int totalWidth = 545;
   
    
    /* CODE SCHEMA/CODE_VALUE MAMAS */
    static String RIGHT_BREAST_CODE_SCHEMA   = "RADLEX";
    static String RIGHT_BREAST_CODE_VALUE    = "RID29896";
    static String RIGHT_BREAST_CODE_MEANING  = "Mama Derecha";
    static String LEFT_BREAST_CODE_SCHEMA    = "RADLEX";
    static String LEFT_BREAST_CODE_VALUE     = "RID29897";
    static String LEFT_BREAST_CODE_MEANING   = "Mama Izquierda";
    
}
