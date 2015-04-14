/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.middleware.files.pdfs;

/**
 *
 * @author root
 */
public class PDF_RM_Report extends PDFBreastReport {
    
    public  PDF_RM_Report(String xmlInputPath, String pdfOutputPath, String strNombrePaciente) throws Exception {        
        super(xmlInputPath, pdfOutputPath, strNombrePaciente);                
    }    
    protected int iGeneraTituloInforme(){                           
        return this.iPdfGeneraTituloInforme(CONSTANTS_XPATH_RM.strTYPE_REPORT_CODE_MEANING);                                 
    }            
    protected int iGeneraCabeceraInforme(String strNombrePaciente){        
        return this.iPdfCabeceraInforme(CONSTANTS_XPATH_RM.strTYPE_REPORT_CODE_SCHEME,CONSTANTS_XPATH_RM.strTYPE_REPORT_CODE__VALUE);             
    }                        
    protected int iGeneraResumenInforme(){
        return this.iPdfGeneraResumenInforme(CONSTANTS_XPATH_RM.strTYPE_REPORT_CODE_SCHEME,CONSTANTS_XPATH_RM.strTYPE_REPORT_CODE__VALUE,
                                             CONSTANTS_XPATH_RM.astrTYPE_LESION_CODE_SCHEME, 
                                             CONSTANTS_XPATH_RM.astrTYPE_LESION_CODE_VALUE, 
                                             CONSTANTS_XPATH_RM.astrTYPE_LESION_CODE_MEANING );
    }            
    protected int iGeneraDetalleLesiones(){                      
                
       return iPdfGeneraDetalleLesiones(CONSTANTS_XPATH_RM.strTYPE_REPORT_CODE_SCHEME,CONSTANTS_XPATH_RM.strTYPE_REPORT_CODE__VALUE,                                                     
                                          CONSTANTS_XPATH_RM.astrTYPE_LESION_CODE_SCHEME, 
                                          CONSTANTS_XPATH_RM.astrTYPE_LESION_CODE_VALUE,
                                          CONSTANTS_XPATH_RM.astrTYPE_LESION_CODE_MEANING);                                
    }
    protected int iPdfDataLesion(){
        return 0;
    }
}
