
package trencadis.middleware.files.pdfs;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

public class PDF_MAMO_Report extends PDFBreastReport {
    
    public  PDF_MAMO_Report(String xmlInputPath, String pdfOutputPath, String strNombrePaciente) throws Exception {
        
        super(xmlInputPath, pdfOutputPath, strNombrePaciente);
                
    }
    
    protected int iGeneraTituloInforme(){                           
        return this.iPdfGeneraTituloInforme(CONSTANTS_XPATH_MAMO.strTYPE_REPORT_CODE_MEANING);                                 
    }            
    protected int iGeneraCabeceraInforme(String strNombrePaciente){        
        return this.iPdfCabeceraInforme(CONSTANTS_XPATH_MAMO.strTYPE_REPORT_CODE_SCHEME,CONSTANTS_XPATH_MAMO.strTYPE_REPORT_CODE__VALUE);             
    }                       
    protected int iGeneraResumenInforme(){
        return this.iPdfGeneraResumenInforme(CONSTANTS_XPATH_MAMO.strTYPE_REPORT_CODE_SCHEME,CONSTANTS_XPATH_MAMO.strTYPE_REPORT_CODE__VALUE,
                                             CONSTANTS_XPATH_MAMO.astrTYPE_LESION_CODE_SCHEME, 
                                             CONSTANTS_XPATH_MAMO.astrTYPE_LESION_CODE_VALUE, 
                                             CONSTANTS_XPATH_MAMO.astrTYPE_LESION_CODE_MEANING );
    }            
    protected int iGeneraDetalleLesiones(){                      
                
       return iPdfGeneraDetalleLesiones(CONSTANTS_XPATH_MAMO.strTYPE_REPORT_CODE_SCHEME,CONSTANTS_XPATH_MAMO.strTYPE_REPORT_CODE__VALUE,                                                     
                                          CONSTANTS_XPATH_MAMO.astrTYPE_LESION_CODE_SCHEME, 
                                          CONSTANTS_XPATH_MAMO.astrTYPE_LESION_CODE_VALUE,
                                          CONSTANTS_XPATH_MAMO.astrTYPE_LESION_CODE_MEANING);                                
    }
    protected int iPdfDataLesion(){
        return 0;
    }    
}
