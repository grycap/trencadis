
package trencadis.middleware.files.pdfs;

 public class CONSTANTS_XPATH_EQ {
        
        /*************************************************/
        /***** TIPOS DE INFORME  *************************/                
        static String strTYPE_REPORT_CODE_SCHEME  = "SNOMED-CT";                         
        static String strTYPE_REPORT_CODE__VALUE  = "172117008";
        static String strTYPE_REPORT_CODE_MEANING = "Exploración Clínica";
        /*************************************************/
     
        /*************************************************/          
        /***** TIPOS DE LESIONES *************************/                
        static String [] astrTYPE_LESION_CODE_SCHEME  = {"TRENCADIS_MAMO", "TRENCADIS_MAMO"};                         
        static String [] astrTYPE_LESION_CODE_VALUE   = {"TRMM0009",       "TRMM0124"};
        static String [] astrTYPE_LESION_CODE_MEANING = {"Nódulo/Masa",    "Induración"};
        /*************************************************/                                                                  
        
        //HALLAZGOS ASOCIADOS parametrizados con @MAMA_DERECHA_CODE_SCHEME / @MAMA_DERECHA_CODE_VALUE y @HA_CODE_SCHEMA / @HA_CODE_VALUE
        static String XPath_HA                                   = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/NUM[CONCEPT_NAME/CODE_VALUE='@HA_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@HA_CODE_SCHEMA']//VALUE";
        static String XPath_HA_Adenopatia_Axilar                 = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/NUM[CONCEPT_NAME/CODE_VALUE='RID34272' and CONCEPT_NAME/CODE_SCHEMA='RADLEX']";
        static String XPath_HA_Adenopatia_Supraclavicular        = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/NUM[CONCEPT_NAME/CODE_VALUE='TRMM0012' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']";
        static String XPath_HA_Secrecion                         = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/NUM[CONCEPT_NAME/CODE_VALUE='54302000' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']";
        static String XPath_HA_Características_Secrecion         = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/CODE[CONCEPT_NAME/CODE_VALUE='TRMM0125' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']";
        static String XPath_HA_Transtornos_Funcionales           = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/CODE[CONCEPT_NAME/CODE_VALUE='TRMM0128' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']";
        static String XPath_HA_Alteraciones_Pezon                = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/NUM[CONCEPT_NAME/CODE_VALUE='RID34314' and CONCEPT_NAME/CODE_SCHEMA='RADLEX']";
        static String XPath_HA_Retraccion_Pezon                  = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/NUM[CONCEPT_NAME/CODE_VALUE='RID34269' and CONCEPT_NAME/CODE_SCHEMA='RADLEX']";
        static String XPath_HA_Costras                           = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/NUM[CONCEPT_NAME/CODE_VALUE='TRMM0131' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']";
        static String XPath_HA_Eczema                            = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/NUM[CONCEPT_NAME/CODE_VALUE='TRMM0132' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']";
        static String XPath_HA_Otras_Alteraciones_Pezon          = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/NUM[CONCEPT_NAME/CODE_VALUE='TRMM0133' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']";
        static String XPath_HA_Alteraciones_Cutaneas             = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/NUM[CONCEPT_NAME/CODE_VALUE='RID34267' and CONCEPT_NAME/CODE_SCHEMA='RADLEX']";
        static String XPath_HA_Retraccion_Piel                   = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/NUM[CONCEPT_NAME/CODE_VALUE='RID34383' and CONCEPT_NAME/CODE_SCHEMA='RADLEX']";
        static String XPath_HA_Piel_Naranja                      = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/NUM[CONCEPT_NAME/CODE_VALUE='TRMM0134' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']";
        static String XPath_HA_Sin_Piel_Naranja                  = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/NUM[CONCEPT_NAME/CODE_VALUE='TRMM0135' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']";
        static String XPath_HA_Cutanides                         = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/NUM[CONCEPT_NAME/CODE_VALUE='TRMM0136' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']";
        static String XPath_HA_Ulceracion_Piel_Mama              = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/NUM[CONCEPT_NAME/CODE_VALUE='TRMM0137' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']";
        static String XPath_HA_Ganglios_Cutaneos_Satelite        = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/NUM[CONCEPT_NAME/CODE_VALUE='TRMM0138' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']";
        static String XPath_HA_Signos_Inflamatorios              = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/NUM[CONCEPT_NAME/CODE_VALUE='TRMM0139' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']";
        static String XPath_HA_Otras_Alteraciones_Cutaneas       = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/NUM[CONCEPT_NAME/CODE_VALUE='TRMM0140' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']";
        static String XPath_HA_Descrip_Otras_Alter_Cutaneas      = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/TEXT[CONCEPT_NAME/CODE_VALUE='TRMM0143' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']";
        static String XPath_HA_Otros_Hallazgos_Asociados         = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/NUM[CONCEPT_NAME/CODE_VALUE='TRMM0141' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']";
        static String XPath_HA_Descrip_Otros_Hallazgos_Asociados = "/DICOM_SR/CONTAINER[CONCEPT_NAME/CODE_VALUE='172117008' and CONCEPT_NAME/CODE_SCHEMA='SNOMED-CT']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='@MAMA_CODE_VALUE' and CONCEPT_NAME/CODE_SCHEMA='@MAMA_CODE_SCHEMA']/CHILDREN/CONTAINER[CONCEPT_NAME/CODE_VALUE='TRMM0011' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']/CHILDREN/TEXT[CONCEPT_NAME/CODE_VALUE='TRMM0142' and CONCEPT_NAME/CODE_SCHEMA='TRENCADIS_MAMO']";
        
        static String [] xPaths_Hallazgos_Asociados = {XPath_HA_Adenopatia_Axilar,
                                                       XPath_HA_Adenopatia_Supraclavicular,
                                                       XPath_HA_Secrecion,
                                                       XPath_HA_Características_Secrecion,
                                                       XPath_HA_Transtornos_Funcionales,
                                                       XPath_HA_Alteraciones_Pezon,
                                                       XPath_HA_Retraccion_Pezon,
                                                       XPath_HA_Costras,
                                                       XPath_HA_Eczema,
                                                       XPath_HA_Otras_Alteraciones_Pezon,
                                                       XPath_HA_Alteraciones_Cutaneas,
                                                       XPath_HA_Retraccion_Piel,
                                                       XPath_HA_Piel_Naranja,
                                                       XPath_HA_Sin_Piel_Naranja,
                                                       XPath_HA_Cutanides,
                                                       XPath_HA_Ulceracion_Piel_Mama,
                                                       XPath_HA_Ganglios_Cutaneos_Satelite,
                                                       XPath_HA_Signos_Inflamatorios,
                                                       XPath_HA_Otras_Alteraciones_Cutaneas,
                                                       XPath_HA_Descrip_Otras_Alter_Cutaneas,
                                                       XPath_HA_Otros_Hallazgos_Asociados,
                                                       XPath_HA_Descrip_Otros_Hallazgos_Asociados};
        
}