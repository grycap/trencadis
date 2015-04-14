/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.middleware.files.pdfs;


import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NodeList;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;


/**
 *
 * @author root
 */
public class PDF_EQ_Report extends PDFBreastReport {
    
        
    public  PDF_EQ_Report(String xmlInputPath, String pdfOutputPath, String strNombrePaciente) throws Exception {
        
        super(xmlInputPath, pdfOutputPath, strNombrePaciente);

    }
        
    protected int iGeneraTituloInforme(){                           
        return this.iPdfGeneraTituloInforme(CONSTANTS_XPATH_EQ.strTYPE_REPORT_CODE_MEANING);                                 
    }
    
    protected int iGeneraCabeceraInforme(String strNombrePaciente){            
        return this.iPdfCabeceraInforme(CONSTANTS_XPATH_EQ.strTYPE_REPORT_CODE_SCHEME,CONSTANTS_XPATH_EQ.strTYPE_REPORT_CODE__VALUE);                           
    }
    
    protected int iGeneraResumenInforme(){
        return this.iPdfGeneraResumenInforme(CONSTANTS_XPATH_EQ.strTYPE_REPORT_CODE_SCHEME,CONSTANTS_XPATH_EQ.strTYPE_REPORT_CODE__VALUE,
                                             CONSTANTS_XPATH_EQ.astrTYPE_LESION_CODE_SCHEME, 
                                             CONSTANTS_XPATH_EQ.astrTYPE_LESION_CODE_VALUE, 
                                             CONSTANTS_XPATH_EQ.astrTYPE_LESION_CODE_MEANING );
    }
                
    protected int iGeneraDetalleLesiones(){            
                
         return iPdfGeneraDetalleLesiones(CONSTANTS_XPATH_EQ.strTYPE_REPORT_CODE_SCHEME,CONSTANTS_XPATH_EQ.strTYPE_REPORT_CODE__VALUE,                                                     
                                          CONSTANTS_XPATH_EQ.astrTYPE_LESION_CODE_SCHEME, 
                                          CONSTANTS_XPATH_EQ.astrTYPE_LESION_CODE_VALUE,
                                          CONSTANTS_XPATH_EQ.astrTYPE_LESION_CODE_MEANING);                            
    }
        
    protected int iPdfDataLesion(){
        return 0;
    }
    
    
    private int iGeneraHallazgosAsociados(String strMAMA_CodeSchema, String strMAMACodeValue){ 
            try{
                //Cabecera Detalle                    
                document.add(tblCabeceraDetalleLesion("Hallazgos Asociados: "));  
                
                //Descripción de los Hallazgos Asociados
                document.add(tblLineaHallazgosAsociados(CONSTANTS_XPATH_EQ.xPaths_Hallazgos_Asociados, strMAMA_CodeSchema, strMAMACodeValue));                                        
                
                
                return 0;
                
            }catch(Exception ex){                
                strError = ex.toString();
                return -1;
            }
    }
        
    private  PdfPTable tblLineaHallazgosAsociados(String [] xPaths, String strMAMA_CodeSchema, String strMAMACodeValue) {
        try{            
            XPathExpression expr     = null;
            XPath xpath              = XPathFactory.newInstance().newXPath();
            int iCont                = 0;
            int iCont2               = 0;  
            String strmsg            = null;
            boolean [] strShows      = new boolean[xPaths.length];
            String [] strValues      = new String[xPaths.length];
            String [] strMenings     = new String[xPaths.length];
            String xPath             = "";
            int i,j;
                                                                           
            //Capturo los cuadrantes
            for (i=0; i<xPaths.length ; i++){                                
                xPath = xPaths[i].replace("@MAMA_CODE_SCHEMA", strMAMA_CodeSchema);
                xPath = xPath.replace("@MAMA_CODE_VALUE", strMAMACodeValue);
                
                System.out.println(xPath);
                                               
                //Identificadores de Lesiones Nódulo/Masa en mgetLengthama derecha
                expr = xpath.compile(xPath);                
                NodeList nodes  = (NodeList)expr.evaluate(xmlDoc, XPathConstants.NODESET); 
                
                if ((nodes.item(0) == null)){
                    strShows[i]   = false;
                    strValues[i]  = "0";                    
                    strMenings[i] = "";
                }else{                
                    //Si es un NUM                
                    if ((nodes.item(0).getNodeName().equals("NUM"))){                        
                        //VALUE
                        strValues[i] = "";
                        //CODE MEANING                     
                        strMenings[i] = nodes.item(0).getChildNodes().item(1).getChildNodes().item(5).getTextContent();
                        if (nodes.item(0).getChildNodes().item(5).getTextContent().equals("0")) strShows[i]=false;
                        else strShows[i]=true;
                    }
                    //Si es un CODE
                    if ((nodes.item(0).getNodeName().equals("CODE"))){
                        strValues[i] = nodes.item(0).getChildNodes().item(3).getChildNodes().item(5).getTextContent();
                        //CODE MEANING                     
                        strMenings[i] = nodes.item(0).getChildNodes().item(1).getChildNodes().item(5).getTextContent();
                        strShows[i]=true;
                    }

                    //Si es un TEXT o DATE
                    if ((nodes.item(0).getNodeName().equals("TEXT") || nodes.item(0).getNodeName().equals("DATE"))){
                        strValues[i] = nodes.item(0).getChildNodes().item(3).getTextContent();
                        //CODE MEANING                     
                        strMenings[i] = nodes.item(0).getChildNodes().item(1).getChildNodes().item(5).getTextContent();
                        strShows[i]=true;
                    }
                }                   
                if (strShows[i]) iCont++;
            }
            
            
            for (i=0; i<xPaths.length; i++){
                if (strShows[i]){
                    if (iCont2 == 0) strmsg = strMenings[i];                    
                    else{
                        if (iCont2 == iCont-1){
                            strmsg = strmsg + " y " + strMenings[i] + " " + strValues[i];
                        }else{                            
                            if (i == 3){                                
                               strmsg = strmsg +  "[";
                               for (j=i;j<=4;j++){                                                                        
                                    if (j==4) strmsg = strmsg + strValues[j]; 
                                    else strmsg = strmsg + strValues[j] + ", "; 
                               }
                               strmsg = strmsg +  "]";
                               i=j-1;
                            }else if (i == 6) {
                               strmsg = strmsg +  "[";
                               for (j=i;j<=9;j++){                                                                        
                                    if (j==9) strmsg = strmsg + strMenings[j]; 
                                    else strmsg = strmsg + strMenings[j] + ", "; 
                               }
                               strmsg = strmsg +  "]";
                               i=j-1;
                                
                            }else if (i== 11){
                               strmsg = strmsg +  "[";
                               for (j=i;j<=19;j++){                                                                        
                                    if (j==19) strmsg = strmsg + strValues[j]; 
                                    else strmsg = strmsg + strMenings[j] + ", "; 
                               }
                               strmsg = strmsg +  "]";
                               i=j-1;
                            
                            }else if (i == 21){
                               strmsg = strmsg +  "[";
                               strmsg = strmsg + strValues[i];                                
                               strmsg = strmsg +  "]";                                                             
                            } else strmsg = strmsg + ", " + strMenings[i];                        

                        }
                    }
                    iCont2++;
                }                                        
            }
            

            
            float[] linea = { 100F };
            PdfPTable table = new PdfPTable(linea);
            table.setWidthPercentage(70);
            table.setHorizontalAlignment(Rectangle.ALIGN_LEFT);
            table.getDefaultCell().setBorderWidth(10);		
            table.setWidthPercentage(100f);
            table.setHorizontalAlignment(0);
            table.setTotalWidth(CONSTANTS_PDF_REPORTS.totalWidth); 
            table.setLockedWidth(true);
		                
            Paragraph p = new Paragraph(new Phrase("     " + strmsg, CONSTANTS_PDF_REPORTS.font10));
		
            PdfPCell cell = new PdfPCell(p);
            cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderColor(CONSTANTS_PDF_REPORTS.azul_oscuro);
            cell.setBorderWidth(1F);
            table.addCell(cell);
				                              			
            return table;
        }catch(Exception ex){
            ex.printStackTrace();
            strError = ex.toString();
            return null;
        
        }
    }
}
   

