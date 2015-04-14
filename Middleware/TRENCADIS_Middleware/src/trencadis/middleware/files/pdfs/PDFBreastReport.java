package trencadis.middleware.files.pdfs;

import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public abstract class PDFBreastReport {
	
        //Path of the xml document that contains the DICOM SR Report
        private String xmlInputPath      = null;
        //DOM objet to manage xml document
        protected Document xmlDoc          = null;
        //Path of the pdf Result    
        private String pdfOutputPath     = null;
        //Name of Patient        
        private String strNombrePaciente = null;
        //Description of last error ocurred                               
        public String strError = "";
        
        //Writer of pdf document
        private PdfWriter writer = null;
	protected com.itextpdf.text.Document document   = null;
        
                                
        protected abstract int iGeneraTituloInforme();        
        protected abstract int iGeneraCabeceraInforme(String strNombrePaciente);        
        protected abstract int iGeneraResumenInforme();
        protected abstract int iGeneraDetalleLesiones();        
        protected abstract int iPdfDataLesion();    
        
        
		        
	public  PDFBreastReport(String xmlInputPath, String pdfOutputPath, String strNombrePaciente) throws Exception {
            
            this.xmlInputPath      = xmlInputPath;
            this.pdfOutputPath     = pdfOutputPath;
            this.strNombrePaciente = strNombrePaciente;            
            
            try{
                DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
                domFactory.setNamespaceAware(true);
                DocumentBuilder builder = domFactory.newDocumentBuilder();
                xmlDoc                  = builder.parse(xmlInputPath);
                document                = new com.itextpdf.text.Document(PageSize.A4, 25, 25, 30, 25);
                writer                  = PdfWriter.getInstance(document,new FileOutputStream(pdfOutputPath));
                document.open();
                                
            }catch(Exception ex){                
                ex.toString();
                throw new Exception("Can not create Pdf document. ", ex);    
            }            
        }
        
        public String strPathPdfFile(){
            return pdfOutputPath;
        }
        
        public int iGeneraPDF() {
	try {						                                               			
            /********* TÍTULO DEL INFORME DIAGNÓSTICO *************************/
            int iErr = iGeneraTituloInforme();
            if (iErr != 0) return iErr;
            /******************************************************************/
            
            /********* CABECERA DEL INFORME DIAGNÓSTICO ***********************/
            iErr = iGeneraCabeceraInforme(strNombrePaciente);
            if (iErr != 0) return iErr;            
            /******************************************************************/
            
            /********* Resumen *******************************************/
            iErr = iGeneraResumenInforme();
            if (iErr != 0) return iErr;                    
            /*************************************************************/                        
                        
            /********* Detalle Lesiones **********************************/
            iErr = iGeneraDetalleLesiones();
            if (iErr != 0) return iErr;                    
            /*************************************************************/                        
                        
            java.net.URL url  = null;
            ClassLoader classLoader = PDFBreastReport.class.getClassLoader();
                        
                        
                        
                        
                        
                        url= classLoader.getResource ("trencadis/middleware/images/der_mamapeq_00.png");
			Image der_mama = Image.getInstance(url);
			der_mama.setAlignment(Image.MIDDLE);
			der_mama.scalePercent(50, 50);
			
                        url= classLoader.getResource ("trencadis/middleware/images/der_mamapeq_01.png");
			Image der_mama01 = Image.getInstance(url);
			der_mama01.setAlignment(Image.MIDDLE);
			der_mama01.scalePercent(50, 50);
			
                        url= classLoader.getResource ("trencadis/middleware/images/der_mamapeq_02.png");
			Image der_mama02 = Image.getInstance(url);
			der_mama02.setAlignment(Image.MIDDLE);
			der_mama02.scalePercent(50, 50);
			
                        url= classLoader.getResource ("trencadis/middleware/images/der_mamapeq_03.png");
			Image der_mama03 = Image.getInstance(url);
			der_mama03.setAlignment(Image.MIDDLE);
			der_mama03.scalePercent(50, 50);
			
                        url= classLoader.getResource ("trencadis/middleware/images/der_mamapeq_04.png");
			Image der_mama04 = Image.getInstance(url);
			der_mama04.setAlignment(Image.MIDDLE);
			der_mama04.scalePercent(50, 50);
			
                        url= classLoader.getResource ("trencadis/middleware/images/der_mamapeq_05.png");
			Image der_mama05 = Image.getInstance(url);
			der_mama05.setAlignment(Image.MIDDLE);
			der_mama05.scalePercent(50, 50);
			
                        url= classLoader.getResource ("trencadis/middleware/images/izq_mamapeq_00.png");
			Image izq_mama = Image.getInstance(url);
			izq_mama.setAlignment(Image.MIDDLE);
			izq_mama.scalePercent(50, 50);
			
                        url= classLoader.getResource ("trencadis/middleware/images/izq_mamapeq_01.png");
			Image izq_mama01 = Image.getInstance(url);
			izq_mama01.setAlignment(Image.MIDDLE);
			izq_mama01.scalePercent(50, 50);
			
                        url= classLoader.getResource ("trencadis/middleware/images/izq_mamapeq_02.png");
			Image izq_mama02 = Image.getInstance(url);
			izq_mama02.setAlignment(Image.MIDDLE);
			izq_mama02.scalePercent(50, 50);
			
                        url= classLoader.getResource ("trencadis/middleware/images/izq_mamapeq_03.png");			
                        Image izq_mama03 = Image.getInstance(url);
			izq_mama03.setAlignment(Image.MIDDLE);
			izq_mama03.scalePercent(50, 50);
			
			url= classLoader.getResource ("trencadis/middleware/images/izq_mamapeq_04.png");			
                        Image izq_mama04 = Image.getInstance(url);                        
			izq_mama04.setAlignment(Image.MIDDLE);
			izq_mama04.scalePercent(50, 50);
			
                        url= classLoader.getResource ("trencadis/middleware/images/izq_mamapeq_05.png");			
			Image izq_mama05 = Image.getInstance(url);
			izq_mama05.setAlignment(Image.MIDDLE);
			izq_mama05.scalePercent(50, 50);
						                        
			 			 			
			// Encabezado
			 String fechaLabel="???";
			 String fecha="???";
			 String tipoInforme="???";
			 
			 String identificadorInformeLabel = "???";
			 String identificadorInforme = "???";
			 String identificadorPacienteLabel = "???";
			 String identificadorPaciente = "???";
			
			 
			 Node nodeDICOM_SR = null;
			 Node nodeCONTAINER1 = null;
			 
			// nodes = doc.getElementsByTagName("DICOM_SR");
			 //nodeDICOM_SR = nodes.item(0); 
			 
                         //System.out.println(nodeDICOM_SR.getNodeName());   //DICOM_SR

/*			 
 * 
 *
			 nodes = nodeDICOM_SR.getChildNodes();
			 for (int i = 0; i < nodes.getLength(); i++) {
				 node= nodes.item(i);				 
				 if (node.getNodeName().equals("CONTAINER")){  // CONTAINER general
					 //System.out.println(i+"  " +node.getNodeName());
					 nodes = node.getChildNodes();
					 for (int i1 = 0; i1 < nodes.getLength(); i1++) {
						 node_hijo= nodes.item(i1);
						 nodes_nietos =  node_hijo.getChildNodes();
						 if (node_hijo.getNodeName().equals("CONCEPT_NAME")){              ////CONCEPT_NAME
//							 System.out.println(node_hijo.getNodeName());
							 nodoDICOM1 = new  DICOM_Nodos(node_hijo);
							 tipoInforme = nodoDICOM1.getCodeMeaning();   // Exploración  de la Mama
//							 System.out.println("----->" + nodoDICOM1.getCodeMeaning()); 
//							 System.out.println("----->" + nodoDICOM1.getCodeSchema()); 
//							 System.out.println("----->" + nodoDICOM1.getCodeValue()); 
						 }
						 if (node_hijo.getNodeName().equals("CHILDREN")){              ////CHILDS
							 //System.out.println(node_hijo.getNodeName());
							 for (int i2 = 0; i2 < nodes_nietos.getLength(); i2++) {
								 node_nieto= nodes_nietos.item(i2);
								// System.out.println("H----> "+node_nieto.getNodeName());
							 
								 if (node_nieto.getNodeName().equals("DATE")){              ////DATE
									 //System.out.println(node_nieto.getNodeName());
									 nodoDICOM1 = new  DICOM_Nodos(node_nieto);
//									 System.out.println("----->" + nodoDICOM1.getCodeMeaning()); 
//									 System.out.println("----->" + nodoDICOM1.getCodeSchema()); 
//									 System.out.println("----->" + nodoDICOM1.getCodeValue()); 
//									 System.out.println("----->" + nodoDICOM1.getValue()); 
									 fechaLabel = nodoDICOM1.getCodeMeaning();
									 fecha = nodoDICOM1.getValue();
								 }
								 if (node_nieto.getNodeName().equals("TEXT")){              ////TEXT
									 //System.out.println(node_nieto.getNodeName());
									 nodoDICOM1 = new  DICOM_Nodos(node_nieto);
									 
									 if (nodoDICOM1.getCodeValue().equals("RID13159")){
										 identificadorPacienteLabel = nodoDICOM1.getCodeMeaning();
										 identificadorPaciente = 	nodoDICOM1.getValue();	 
										 document.add(cabecera(tipoInforme,fechaLabel,fecha,identificadorPaciente,identificadorInforme ));  /// Escribo la cabecera
										 document.add(FranjaBlanca());
										 document.add(FranjaBlanca());
										 document.add(FranjaBlanca());
										 
									 }else if (nodoDICOM1.getCodeValue().equals("118522005")){
										 identificadorInformeLabel = nodoDICOM1.getCodeMeaning();
										 identificadorInforme = 	nodoDICOM1.getValue();
									 }
//									 System.out.println("----->" + nodoDICOM1.getCodeMeaning()); 
//									 System.out.println("----->" + nodoDICOM1.getCodeSchema()); 
//									 System.out.println("----->" + nodoDICOM1.getCodeValue()); 
//									 System.out.println("----->" + nodoDICOM1.getValue()); 
								 }
								
								 if (node_nieto.getNodeName().equals("CONTAINER")){              ////CONTAINER
									 //System.out.println(node_nieto.getNodeName());
									 datosMama(node_nieto);
//									 System.out.println("----->" + nodoDICOM1.getCodeMeaning()); 
//									 System.out.println("----->" + nodoDICOM1.getCodeSchema()); 
//									 System.out.println("----->" + nodoDICOM1.getCodeValue()); 
//									 System.out.println("----->" + nodoDICOM1.getValue()); 
								 }
							 }
						 }
				   }
					 
				 }
		     }
			 
*/	
			System.out.println("==========FIN================");


			document.close();
                       return 0;
		} catch (Exception e) {
			e.printStackTrace();
                        strError = e.toString();
                        return -1;
		}
	}
        
                
        
        
        
	/*

	public  void datosMama(Node containerMama) throws DocumentException{
		Node node= null;
		NodeList nodes = null;
		Node node_hijo= null;
		NodeList nodes_hijos = null;
		Node node_nieto= null;
		NodeList nodes_nietos = null;
		Node node_nieto1= null;
		NodeList nodes_nietos1 = null;
		
		document.add(tblFranjaBlanca());
		nodes = containerMama.getChildNodes();
//		System.out.println("-----> datosMama()");
		for (int i = 0; i < nodes.getLength(); i++) {
			 node= nodes.item(i);
			 if (node.getNodeName().equals("CONCEPT_NAME")){              ////CONCEPT_NAME
				 nodoDICOM1 = new  DICOM_Nodos(node);
				 document.add(cabeceraMama("", nodoDICOM1.getCodeMeaning()));   // MAMA DERECHA/IZQUIERDA
				 document.add(tblFranjaBlanca());
//				 System.out.println("----->" + nodoDICOM1.getCodeMeaning()); 
//				 System.out.println("----->" + nodoDICOM1.getCodeSchema()); 
//				 System.out.println("----->" + nodoDICOM1.getCodeValue()); 
			 }
			 if (node.getNodeName().equals("CHILDS")){                               ////CHILDS
				 //System.out.println("xxx>" + node.getNodeName());
				 nodes_hijos = node.getChildNodes();
				 for (int i1 = 0; i1 < nodes_hijos.getLength(); i1++) {
						 node_hijo= nodes_hijos.item(i1);
						 if (node_hijo.getNodeName().equals("CONTAINER")){
							     //System.out.println(".....>" + node_hijo.getNodeName());
							     nodes_nietos = node_hijo.getChildNodes();
							     for (int i2 = 0; i2 < nodes_nietos.getLength(); i2++) {
							    	 node_nieto= nodes_nietos.item(i2);
							    	 if (node_nieto.getNodeName().equals("CONCEPT_NAME")){ /// Tipo Anomalia
							    		 //System.out.println("...................>" + node_nieto.getNodeName());
							    		 nodoDICOM1 = new  DICOM_Nodos(node_nieto);
							    		 document.add(cabeceraMama2("", nodoDICOM1.getCodeMeaning()));   // Masa Asimetrias Hallazgos .......
										 
							    	 }
							    	 if (node_nieto.getNodeName().equals("CHILDS")){
							    		 //System.out.println("...................>" + node_nieto.getNodeName());
							    		 
							    		 nodes_nietos1 = node_nieto.getChildNodes();
							    		 resultadosMamaImagen(document, nodoDICOM1.getCodeMeaning(), nodoDICOM1.getValue());

							    		 for (int i3 = 0; i3 < nodes_nietos1.getLength(); i3++) {
							    		     	 node_nieto1= nodes_nietos1.item(i3);
							    		     	//System.out.println(".................................................>" + node_nieto1.getNodeName());
							    		     	 if (node_nieto1.getNodeName().equals("TEXT")){
							    		     		nodoDICOM1 = new  DICOM_Nodos(node_nieto1);
							    		     		document.add(cabeceraMama2("", nodoDICOM1.getValue()));
							    		     		document.add(tblFranjaBlanca());
							    		     		//System.out.println(".................................................>" + node_nieto1.getNodeName());
							    		     	 }
							    		     	 if (node_nieto1.getNodeName().equals("NUM")){
							    		     		nodoDICOM1 = new  DICOM_Nodos(node_nieto1);
							    		     		document.add(tblResultadosMama(nodoDICOM1.getCodeMeaning(), nodoDICOM1.getValue()));
							    		     		
							    		     		//System.out.println(".................................................>" + node_nieto1.getNodeName());
							    		     	 }
							    		 }		    		 

							    	 }
							     }
						         
						 }
				 
				 }
				 //nodoDICOM1 = new  DICOM_Nodos(node);
//				 System.out.println("----->" + nodoDICOM1.getCodeMeaning()); 
//				 System.out.println("----->" + nodoDICOM1.getCodeSchema()); 
//				 System.out.println("----->" + nodoDICOM1.getCodeValue()); 
			 }
			 
			 System.out.println(node.getNodeName());
		}

	}


	
	
	
	
	
	private static PdfPTable cabeceraMama(String  schema, String meaning) {
		float[] linea_25_25 = { 25F, 25F };
		table = new PdfPTable(linea_25_25);
		table.setWidthPercentage(50);
        table.setHorizontalAlignment(Rectangle.ALIGN_LEFT);
		table.getDefaultCell().setBorderWidth(10);
		//table.getDefaultCell().setBorderColor(azul_oscuro);
		table.setWidthPercentage(100f);
		table.setHorizontalAlignment(0);
		table.setTotalWidth(totalWidth); //     width = 595.0
		table.setLockedWidth(true);
		font14Bold.setColor(Color.WHITE);
		p = new Paragraph(new Phrase(meaning  , font14Bold));
		cell = new PdfPCell(p);
		cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
		//cell.setBorder(PdfPCell.LEFT | PdfPCell.RIGHT |PdfPCell.TOP | PdfPCell.BOTTOM);
		cell.setBackgroundColor(azul_oscuro);
		cell.setBorder(Rectangle.LEFT);
		cell.setBorderColor(azul_oscuro);
		cell.setBorderWidth(1F);
		table.addCell(cell);
	
		p = new Paragraph(new Phrase(schema, font12));
		cell = new PdfPCell(p);
		cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
		cell.setBackgroundColor(azul_oscuro);
		cell.setBorder(Rectangle.RIGHT);
		cell.setBorderColor(azul_oscuro);
		cell.setBorderWidth(1F);
		table.addCell(cell);
		
	   return table;
}

	
	private static PdfPTable cabeceraMama2(String  schema, String meaning) {
		float[] linea_25_25 = { 25F, 25F };
		table = new PdfPTable(linea_25_25);
		table.setWidthPercentage(50);
                table.setHorizontalAlignment(Rectangle.ALIGN_LEFT);
		table.getDefaultCell().setBorderWidth(10);
		//table.getDefaultCell().setBorderColor(azul_oscuro);
		table.setWidthPercentage(100f);
		table.setHorizontalAlignment(0);
		table.setTotalWidth(totalWidth); //     width = 595.0
		table.setLockedWidth(true);
		
		p = new Paragraph(new Phrase(meaning  , font12Bold));
		cell = new PdfPCell(p);
		cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
		//cell.setBorder(PdfPCell.LEFT | PdfPCell.RIGHT |PdfPCell.TOP | PdfPCell.BOTTOM);
		cell.setBackgroundColor(azul_claro);
		cell.setBorder(Rectangle.LEFT);
		cell.setBorderColor(azul_oscuro);
		cell.setBorderWidth(1F);
		table.addCell(cell);
	
		p = new Paragraph(new Phrase(schema, font12));
		cell = new PdfPCell(p);
		cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
		cell.setBackgroundColor(azul_claro);
		cell.setBorder(Rectangle.RIGHT);
		cell.setBorderColor(azul_oscuro);
		cell.setBorderWidth(1F);
		table.addCell(cell);
		
	   return table;
}
	
	private static PdfPTable tblResultadosMama(String  meaning, String value) {
		float[] linea_35_15 = { 35F, 15F };
		table = new PdfPTable(linea_35_15);
		table.setWidthPercentage(70);
                table.setHorizontalAlignment(Rectangle.ALIGN_LEFT);
		table.getDefaultCell().setBorderWidth(10);		
		table.setWidthPercentage(100f);
		table.setHorizontalAlignment(0);
		table.setTotalWidth(totalWidth); 
		table.setLockedWidth(true);
		
                if (value.equals("1")){
			p = new Paragraph(new Phrase(meaning  , font11Bold));
		}else{
			p = new Paragraph(new Phrase(meaning  , font10));
		}
		cell = new PdfPCell(p);
		cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
		//cell.setBorder(PdfPCell.LEFT | PdfPCell.RIGHT |PdfPCell.TOP | PdfPCell.BOTTOM);
		cell.setBorder(Rectangle.LEFT);
		cell.setBorderColor(azul_oscuro);
		cell.setBorderWidth(1F);
		table.addCell(cell);
		
		 Image check_no = null;
		 Image check = null;
		 try{
                     ClassLoader classLoader = PDFBreastReport.class.getClassLoader();
                     java.net.URL url= classLoader.getResource("trencadis/middleware/images/check_no.jpg");
                     check_no = Image.getInstance(url);
                     url= classLoader.getResource("trencadis/middleware/images/check.jpg");
                     check = Image.getInstance(url);
                     check_no.setAlignment(Image.MIDDLE);
                     check_no.scalePercent(50, 50);
                     check.setAlignment(Image.MIDDLE);
                     check.scalePercent(50, 50);
		 }catch(Exception e){}
		
		if (value.equals("1")){
			 cell = new PdfPCell(check);
		}else{
			 cell = new PdfPCell(check_no);
		}
		cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.RIGHT);
		cell.setBorderColor(azul_oscuro);
		cell.setBorderWidth(1F);
		table.addCell(cell);
			
            return table;
}
	
        
	
	
	
	private static void resultadosMamaImagen(com.itextpdf.text.Document doc,String  meaning, String value) {
		float posy=0;
		try {
			
		posy = writer.getVerticalPosition(true);
			
//		Image img1 = null;
//		img1 = Image.getInstance("C:\\tmp\\der_mamapeq_00.png");
//		img1.setAlignment(Image.MIDDLE);
//		img1.scalePercent(50, 50);
//		img1.setAbsolutePosition( 450,posy-120);
//		der_mama.setAlignment(Image.MIDDLE);
//		der_mama.scalePercent(50, 50);
//		der_mama.setAbsolutePosition( 450,posy-120);
		
		Image img2 = null;
		img2 = Image.getInstance("/root/images/der_mamapeq_02.gif");
		img2.setAlignment(Image.MIDDLE);
		img2.scalePercent(50, 50);
		img2.setAbsolutePosition(450,posy-120);
		
		Image img3 = null;
		img3 = Image.getInstance("/root/images/der_mamapeq_03.gif");
		img3.setAlignment(Image.MIDDLE);
		img3.scalePercent(50, 50);
		img3.setAbsolutePosition(450,posy-120);
				
		//doc.add(img1);
		 doc.add(img2);
		 doc.add(img3);
		}catch(Exception e){}
		
        }
    */
        
    protected PdfPTable tblFranjaAzul() {
		
        float[] cabecera_100 = { 100F };
        PdfPTable table = new PdfPTable(cabecera_100);
        table.setWidthPercentage(100);        
        Paragraph p = new Paragraph(new Phrase("", CONSTANTS_PDF_REPORTS.font20Bold));

        PdfPCell cell = new PdfPCell(p);
        cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.RIGHT | Rectangle.LEFT | Rectangle.TOP  | Rectangle.BOTTOM);
        cell.setBackgroundColor(CONSTANTS_PDF_REPORTS.azul_oscuro);
        cell.setBorderColor(CONSTANTS_PDF_REPORTS.azul_oscuro);
        cell.setBorderWidth(1F);
        table.addCell(cell);

        return table;   
    }

    protected PdfPTable tblFranjaFinaAzul() {
		
         float[] linea = { 100F};
        PdfPTable table = new PdfPTable(linea);                    
        table.setWidthPercentage(100f);
        table.setHorizontalAlignment(0);
        table.setTotalWidth(CONSTANTS_PDF_REPORTS.totalWidth); 
        table.setLockedWidth(true);

        Paragraph p = new Paragraph(new Phrase("", CONSTANTS_PDF_REPORTS.font10Bold));
        PdfPCell cell = new PdfPCell(p);		
        cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);				
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);		
        table.addCell(cell);
        return table;                  

    }
    
    protected PdfPTable tblFranjaBlanca() {
		
        float[] cabecera_100 = { 100F };
        PdfPTable table = new PdfPTable(cabecera_100);
        table.setWidthPercentage(100);
        Paragraph p = new Paragraph(new Phrase("", CONSTANTS_PDF_REPORTS.font20Bold));

        PdfPCell cell = new PdfPCell(p);
        cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.RIGHT | Rectangle.LEFT);
        cell.setBorderColor(CONSTANTS_PDF_REPORTS.azul_oscuro);
        cell.setBorderWidth(1F);
        table.addCell(cell);

        return table;
    }
    
    protected PdfPTable tblCabeceraSeccion(String  strTítulo) {        
        float[] linea = { 100F};
        PdfPTable table = new PdfPTable(linea);
        table.getDefaultCell().setBorderWidth(10);		
        table.setWidthPercentage(100f);
        table.setHorizontalAlignment(0);
        table.setTotalWidth(CONSTANTS_PDF_REPORTS.totalWidth); 
        table.setLockedWidth(true);

        Paragraph p = new Paragraph(new Phrase(strTítulo, CONSTANTS_PDF_REPORTS.font14Bold));
        PdfPCell cell = new PdfPCell(p);		
        cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);				
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT );		
        table.addCell(cell);             

        return table;        
    }
    
    protected PdfPTable tblCabeceraDetalleLesion(String  strMsg) {        
        float[] linea = { 100F};
        PdfPTable table = new PdfPTable(linea);                    
        table.setWidthPercentage(100f);
        table.setHorizontalAlignment(0);
        table.setTotalWidth(CONSTANTS_PDF_REPORTS.totalWidth); 
        table.setLockedWidth(true);

        Paragraph p = new Paragraph(new Phrase(strMsg, CONSTANTS_PDF_REPORTS.font12Bold));
        PdfPCell cell = new PdfPCell(p);		
        cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);				
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);		
        table.addCell(cell);
        return table;                    
    }
    
    
            
    protected int iPdfLocalizacionLesion(String [] xPaths,  String strIDLesion) {
        try{
            Object result            = null;
            XPathExpression expr     = null;
            XPath xpath              = XPathFactory.newInstance().newXPath();
            int iCont                = 0;
            int iCont2               = 0;  
            String strLocalizaciones = null;
            String [] strResults     = new String[xPaths.length];
            String xPath              = "";
                                        
            
            //Capturo los cuadrantes
            for (int i=0; i<xPaths.length; i++){                                
               xPath = xPaths[i].replace("@IDLESION", strIDLesion);               
                                               
                //Identificadores de Lesiones Nódulo/Masa en mgetLengthama derecha
                expr = xpath.compile(xPath);                
                result = expr.evaluate(xmlDoc, XPathConstants.STRING); 
                strResults[i] = result.toString();
                if (!result.toString().equals("0")) iCont++;
            }
            
            if (iCont == xPaths.length) strLocalizaciones = "Todos los Cuadrantes.";
            else{
                for (int i=0; i<xPaths.length; i++){
                    if (!strResults[i].equals("0")){
                        if (iCont2 == 0) strLocalizaciones = CONSTANTS_PDF_REPORTS.astrCODE_MEANING_Breast_Zones[i];                    
                        else{
                            if (iCont2 == iCont-1){
                                strLocalizaciones = strLocalizaciones + " y " + CONSTANTS_PDF_REPORTS.astrCODE_MEANING_Breast_Zones[i];
                            }else{
                                strLocalizaciones = strLocalizaciones + ", " + CONSTANTS_PDF_REPORTS.astrCODE_MEANING_Breast_Zones[i];
                            }
                        }
                        iCont2++;
                    }                                        
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
		                
            Paragraph p = new Paragraph(new Phrase("     Localizado en " + strLocalizaciones, CONSTANTS_PDF_REPORTS.font10));
		
            PdfPCell cell = new PdfPCell(p);
            cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderColor(CONSTANTS_PDF_REPORTS.azul_oscuro);
            cell.setBorderWidth(1F);
            table.addCell(cell);
            
            document.add(table);		                              			
            
            return 0;
        }catch(Exception ex){
            ex.printStackTrace();
            strError = ex.toString();
            return -1;
        
        }
    }
    
    protected PdfPTable tblLogoTexto(String strTexto, String strTexto2) {
		
        float[] cabecera_100 = { 100F };
        PdfPTable table = new PdfPTable(cabecera_100);
        PdfPCell cell;

        Paragraph p = new Paragraph(new Phrase(strTexto, CONSTANTS_PDF_REPORTS.font20Bold));		
        cell = new PdfPCell(p);
        cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        p = new Paragraph(new Phrase(strTexto2, CONSTANTS_PDF_REPORTS.font20Bold));		
        cell = new PdfPCell(p);
        cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        return table;
    }
    
    protected Image imgLogo(String strPathlogo, int posX, int posY) {
        Image logo_trencadis = null;
        try {
            ClassLoader classLoader = PDFBreastReport.class.getClassLoader();
            java.net.URL url= classLoader.getResource(strPathlogo);
            logo_trencadis = Image.getInstance(url);
            logo_trencadis.setAlignment(Image.MIDDLE);
            logo_trencadis.scalePercent(75, 75);

            logo_trencadis.setAbsolutePosition(posX, posY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logo_trencadis;
    }
	
	
    protected Paragraph pFranjaSeparacion(float separacion) {
        Paragraph paragraphTable = new Paragraph(); 
        paragraphTable.setSpacingAfter(separacion);
        return paragraphTable;
    }
    
                           
    protected int iPdfGeneraDetalleLesion(String xPathIDLesiones, 
                                       String [] xPathsLocalizaciones,                                              
                                       int iContHallazgos, 
                                       String strTIPO_LESION_CODE_SCHEMA, String strTIPO_LESION_CODE_VALUE, String strTIPO_LESION_CODE_MEANING){
            try{
                int iErr = 0;
                
                XPath xpath = XPathFactory.newInstance().newXPath();
                XPathExpression expr = null;
                Object result        = null;
                NodeList nodes       = null;
                String strIDLesion   = null;
                                
                //Descripción completa del apartado de una lesion
                expr = xpath.compile(xPathIDLesiones);                
                result = expr.evaluate(xmlDoc, XPathConstants.NODESET);  
                nodes = (NodeList) result;                                                
                for (int i = 0; i < nodes.getLength(); i++) {                    
                    strIDLesion = nodes.item(i).getChildNodes().item(0).getNodeValue();                    
                 
                    //Cabecera Detalle                    
                    document.add(tblCabeceraDetalleLesion("Hallazgo " + iContHallazgos + " (" + strIDLesion + ") - " + strTIPO_LESION_CODE_MEANING + ":"));
                    iContHallazgos++;
                                        
                    //Descripción de la localización
                    iErr = iPdfLocalizacionLesion(xPathsLocalizaciones, strIDLesion);                                        
                    if (iErr != 0) return iErr;
                    
                    //Descripción campos propios de la lesion
                    iErr = iPdfDataLesion();                                        
                    if (iErr != 0) return iErr;
                    
                    
                    document.add(tblFranjaBlanca());                                                                                              
                }
                
                return iContHallazgos;
                
            }catch(Exception ex){
                ex.printStackTrace();
                strError = ex.toString();
                return -1;
            }
            
        }
    protected int iNumeroLesiones(String strTYPE_EXPLORATION_CODE_SCHEME, String strTYPE_EXPLORATION_CODE_VALUE,
                                  String strBREAST_CODE_SCHEME, String strBREAST_CODE_VALUE,
                                  String strTYPE_LESION_CODE_SCHEME, String strTYPE_LESION_CODE_VALUE){
        try{
                String xPath = null;
                XPath xpath = XPathFactory.newInstance().newXPath();
                
                /**************************************************************/
                //Número de Lesiones en una EQ, en la mama derecha de tipo Nódulo/Masa 
                /**************************************************************/
                //Tipo de Exploracion es EQ
                xPath = CONSTANTS_PDF_REPORTS.XPath_Numero_Lesiones.replace("@TYPE_EXPLORATION_CODE_SCHEME",strTYPE_EXPLORATION_CODE_SCHEME);
                xPath = xPath.replace("@TYPE_EXPLORATION_CODE_VALUE",strTYPE_EXPLORATION_CODE_VALUE);                                        
                //Mama derecha
                xPath = xPath.replace("@BREAST_CODE_SCHEME",strBREAST_CODE_SCHEME);
                xPath = xPath.replace("@BREAST_CODE_VALUE",strBREAST_CODE_VALUE);                           
                //Tipo de Lesion Nódulo Masa
                xPath = xPath.replace("@TYPE_LESION_CODE_SCHEME",strTYPE_LESION_CODE_SCHEME);
                xPath = xPath.replace("@TYPE_LESION_CODE_VALUE",strTYPE_LESION_CODE_VALUE);                                                                   
                
                XPathExpression expr=xpath.compile(xPath);                
                
                Object result = expr.evaluate(xmlDoc, XPathConstants.STRING);
                
                return Integer.parseInt(result.toString());
                
                
        }catch(Exception ex){
            ex.printStackTrace();
            strError = ex.toString();
            return -1;
        }
    }    
    protected int iPdfGeneraTituloInforme(String strTitulo){
            try{
                                                 
                document.add(imgLogo("trencadis/middleware/images/logo_trencadis.gif",20, 750));	 
		document.add(tblLogoTexto("Informe Diagnóstico",strTitulo));
		document.add(imgLogo("trencadis/middleware/images/logo_peset.gif",450, 750));
		
                document.add(pFranjaSeparacion(20));
		document.add(tblFranjaAzul());
                
                return 0;
            }catch(Exception ex){
                ex.printStackTrace();
                strError = ex.toString();
                return -1;
            }
        }
    protected int iPdfCabeceraInforme(String strTYPE_EXPLORATION_CODE_SCHEME, String strTYPE_EXPLORATION_CODE_VALUE) {
	try{	      
                String xPath = null;                                
                XPath xpath = XPathFactory.newInstance().newXPath();
                                
                //IDPaciente
                xPath = CONSTANTS_PDF_REPORTS.XPath_IDPaciente.replace("@TYPE_EXPLORATION_CODE_SCHEME",strTYPE_EXPLORATION_CODE_SCHEME); 
                xPath = xPath.replace("@TYPE_EXPLORATION_CODE_VALUE",strTYPE_EXPLORATION_CODE_VALUE);                                                                                                
                XPathExpression expr = xpath.compile(xPath);                
                Object result = expr.evaluate(xmlDoc, XPathConstants.STRING);
                String strIDPaciente = result.toString();
                                
                //IDInforme
                xPath = CONSTANTS_PDF_REPORTS.XPath_IDInforme.replace("@TYPE_EXPLORATION_CODE_SCHEME",strTYPE_EXPLORATION_CODE_SCHEME); 
                xPath = xPath.replace("@TYPE_EXPLORATION_CODE_VALUE",strTYPE_EXPLORATION_CODE_VALUE);                                                                                                
                expr=xpath.compile(xPath);                
                result = expr.evaluate(xmlDoc, XPathConstants.STRING);
                String strIdInforme = result.toString();
                
                //Fecha Informe
                xPath = CONSTANTS_PDF_REPORTS.XPath_FechaInforme.replace("@TYPE_EXPLORATION_CODE_SCHEME",strTYPE_EXPLORATION_CODE_SCHEME); 
                xPath = xPath.replace("@TYPE_EXPLORATION_CODE_VALUE",strTYPE_EXPLORATION_CODE_VALUE);                                                                                                
                expr=xpath.compile(xPath);
                result = expr.evaluate(xmlDoc, XPathConstants.STRING);
                String strFechaInforme = result.toString();

                //Procedencia
                xPath = CONSTANTS_PDF_REPORTS.XPath_Procedencia.replace("@TYPE_EXPLORATION_CODE_SCHEME",strTYPE_EXPLORATION_CODE_SCHEME); 
                xPath = xPath.replace("@TYPE_EXPLORATION_CODE_VALUE",strTYPE_EXPLORATION_CODE_VALUE);                                                                                                
                expr=xpath.compile(xPath);
                result = expr.evaluate(xmlDoc, XPathConstants.STRING);
                String strProcedencia = result.toString();

                //Descripcion Otras Procedencias 
                xPath = CONSTANTS_PDF_REPORTS.XPath_OtrasProcedencias.replace("@TYPE_EXPLORATION_CODE_SCHEME",strTYPE_EXPLORATION_CODE_SCHEME); 
                xPath = xPath.replace("@TYPE_EXPLORATION_CODE_VALUE",strTYPE_EXPLORATION_CODE_VALUE);                                                                                                
                expr=xpath.compile(xPath);
                result = expr.evaluate(xmlDoc, XPathConstants.STRING);
                String strOtrasProcedencias = result.toString();
                if (!strOtrasProcedencias.equals("")) strProcedencia = strProcedencia + "(" + strOtrasProcedencias + ")";
                
                //Estudio DICOM 
                xPath = CONSTANTS_PDF_REPORTS.XPath_EstudioDICOM.replace("@TYPE_EXPLORATION_CODE_SCHEME",strTYPE_EXPLORATION_CODE_SCHEME); 
                xPath = xPath.replace("@TYPE_EXPLORATION_CODE_VALUE",strTYPE_EXPLORATION_CODE_VALUE);                                                                                                
                expr=xpath.compile(xPath);
                result = expr.evaluate(xmlDoc, XPathConstants.STRING);
                String strIDEstudioDICOM = result.toString();
                                                               
        
                float[] linea_20_45_35 = { 20F,  45F, 35F };
		PdfPTable table = new PdfPTable(linea_20_45_35);
		table.getDefaultCell().setBorderWidth(10);		
		table.setWidthPercentage(100f);
		table.setHorizontalAlignment(0);
		table.setTotalWidth(CONSTANTS_PDF_REPORTS.totalWidth); 
		table.setLockedWidth(true);
		
                //Primera Fila
                Paragraph p = new Paragraph(new Phrase("Id.Paciente: ", CONSTANTS_PDF_REPORTS.font10Bold));
		PdfPCell cell = new PdfPCell(p);		
		cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);	

		cell.setBackgroundColor(CONSTANTS_PDF_REPORTS.azul_claro);
                cell.setBorder(Rectangle.LEFT | Rectangle.TOP);		
		table.addCell(cell);
                
                p = new Paragraph(new Phrase("Nombre:", CONSTANTS_PDF_REPORTS.font10Bold));
		cell = new PdfPCell(p);
		cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);		
		cell.setBackgroundColor(CONSTANTS_PDF_REPORTS.azul_claro);		
		cell.setBorder(Rectangle.TOP);		
		table.addCell(cell);
                
                p = new Paragraph(new Phrase("Procedencia: ", CONSTANTS_PDF_REPORTS.font10Bold));
		cell = new PdfPCell(p);
                cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.RIGHT | Rectangle.TOP);			
		cell.setBackgroundColor(CONSTANTS_PDF_REPORTS.azul_claro);			
		table.addCell(cell);
                
                //Segunda Fila
                p = new Paragraph(new Phrase(strIDPaciente , CONSTANTS_PDF_REPORTS.font13));
		cell = new PdfPCell(p);		
		cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);		
		cell.setBackgroundColor(CONSTANTS_PDF_REPORTS.azul_claro);
                cell.setBorder(Rectangle.LEFT);		
		table.addCell(cell);
                
                p = new Paragraph(new Phrase(strNombrePaciente, CONSTANTS_PDF_REPORTS.font13));
		cell = new PdfPCell(p);
		cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);		
		cell.setBackgroundColor(CONSTANTS_PDF_REPORTS.azul_claro);		
		cell.setBorder(Rectangle.NO_BORDER);		
		table.addCell(cell);
                
                p = new Paragraph(new Phrase(strProcedencia, CONSTANTS_PDF_REPORTS.font13));
		cell = new PdfPCell(p);
                cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.RIGHT);			
		cell.setBackgroundColor(CONSTANTS_PDF_REPORTS.azul_claro);			
		table.addCell(cell);
                
                //// Tercera Fila			
                p = new Paragraph(new Phrase("Id. Informe: ", CONSTANTS_PDF_REPORTS.font10Bold));
		cell = new PdfPCell(p);
		cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.LEFT | Rectangle.TOP);	
		cell.setBorderColor(CONSTANTS_PDF_REPORTS.azul_oscuro);
		cell.setBorderWidth(1F);
		table.addCell(cell);               
                			 
		p = new Paragraph(new Phrase("Fecha Informe: ", CONSTANTS_PDF_REPORTS.font10Bold));
		cell = new PdfPCell(p);
		cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);		
		cell.setBorder(Rectangle.TOP);
		cell.setBorderColor(CONSTANTS_PDF_REPORTS.azul_oscuro);
		cell.setBorderWidth(1F);		
		table.addCell(cell);		   
								
		p = new Paragraph(new Phrase("Estudio DICOM", CONSTANTS_PDF_REPORTS.font10Bold));
		cell = new PdfPCell(p);
                cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);		
		cell.setBorder(Rectangle.RIGHT | Rectangle.TOP);
		cell.setBorderColor(CONSTANTS_PDF_REPORTS.azul_oscuro);
		cell.setBorderWidth(1F);
		table.addCell(cell);
                
                //// Cuarta Fila			
                p = new Paragraph(new Phrase(strIdInforme, CONSTANTS_PDF_REPORTS.font13));
		cell = new PdfPCell(p);
		cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.LEFT);	
		cell.setBorderColor(CONSTANTS_PDF_REPORTS.azul_oscuro);
		cell.setBorderWidth(1F);
		table.addCell(cell);               
                
			 
		p = new Paragraph(new Phrase(strFechaInforme, CONSTANTS_PDF_REPORTS.font13));
		cell = new PdfPCell(p);
		cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);		
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderColor(CONSTANTS_PDF_REPORTS.azul_oscuro);
		cell.setBorderWidth(1F);		
		table.addCell(cell);		   
								
		p = new Paragraph(new Phrase(strIDEstudioDICOM, CONSTANTS_PDF_REPORTS.font13));
		cell = new PdfPCell(p);
                cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);		
		cell.setBorder(Rectangle.RIGHT);
		cell.setBorderColor(CONSTANTS_PDF_REPORTS.azul_oscuro);
		cell.setBorderWidth(1F);
		table.addCell(cell);
                
		document.add(table);                
                document.add(tblFranjaBlanca());
                document.add(tblFranjaBlanca());
                document.add(tblFranjaAzul());   
                                
                return 0;
        }catch(Exception ex){
            ex.printStackTrace();
            strError = ex.toString();
            return 1;
        }
    }   
    protected int iPdfGeneraResumenInforme(String strTYPE_REPORT_CODE_SCHEME,  String strTYPE_REPORT_CODE_VALUE,            
                                           String [] astrTYPE_LESION_CODE_SCHEME, String [] astrTYPE_LESION_CODE_VALUE, String [] astrTYPE_LESION_CODE_MEANING ){
            try{
                Paragraph p   = null;
                PdfPCell cell = null;
                    
                                
                int [] aiNumeroLesionesMamaDerecha   = new int [astrTYPE_LESION_CODE_SCHEME.length];
                int [] aiNumeroLesionesMamaIzquierda = new int [astrTYPE_LESION_CODE_SCHEME.length];
                
                int iTotalLesiones = 0;
                                               
                
                /********** TITULO SECCIÓN RESUMEN ****************************/
                document.add(tblCabeceraSeccion("RESUMEN"));
                document.add(tblFranjaBlanca());
                /**************************************************************/
                                
                
                /**************************************************************/
                /**** Recupero Información de las Lesiones ********************/                   
                //Numero Lesiones en la mama derecha
                iTotalLesiones = 0;
                for (int i=0; i < aiNumeroLesionesMamaDerecha.length;i++){                    
                    aiNumeroLesionesMamaDerecha[i] = iNumeroLesiones(strTYPE_REPORT_CODE_SCHEME, strTYPE_REPORT_CODE_VALUE, 
                                                CONSTANTS_PDF_REPORTS.RIGHT_BREAST_CODE_SCHEMA, CONSTANTS_PDF_REPORTS.RIGHT_BREAST_CODE_VALUE, 
                                                astrTYPE_LESION_CODE_SCHEME[i], astrTYPE_LESION_CODE_VALUE[i]);
                    iTotalLesiones = iTotalLesiones + aiNumeroLesionesMamaDerecha[i];
                    if (aiNumeroLesionesMamaDerecha[i] < 0)return aiNumeroLesionesMamaDerecha[i];
                }
                //Numero Lesiones en la mama izquierda
                for (int i=0; i < aiNumeroLesionesMamaDerecha.length;i++){                    
                    aiNumeroLesionesMamaIzquierda[i] = iNumeroLesiones(strTYPE_REPORT_CODE_SCHEME, strTYPE_REPORT_CODE_VALUE, 
                                                CONSTANTS_PDF_REPORTS.LEFT_BREAST_CODE_SCHEMA, CONSTANTS_PDF_REPORTS.LEFT_BREAST_CODE_VALUE, 
                                                astrTYPE_LESION_CODE_SCHEME[i], astrTYPE_LESION_CODE_VALUE[i]);                    
                    iTotalLesiones = iTotalLesiones + aiNumeroLesionesMamaIzquierda[i];
                    if (aiNumeroLesionesMamaIzquierda[i] < 0)return aiNumeroLesionesMamaIzquierda[i];
                }
                /**************************************************************/                

                if (iTotalLesiones == 0){
                    float[] linea_100 = { 100F};
                    PdfPTable table = new PdfPTable(linea_100);
                    table.getDefaultCell().setBorderWidth(10);		
                    table.setWidthPercentage(100f);
                    table.setHorizontalAlignment(0);
                    table.setTotalWidth(CONSTANTS_PDF_REPORTS.totalWidth); 
                    table.setLockedWidth(true);                                    
                    p = new Paragraph(new Phrase("NO SE HAN ENCONTRADO LESIONES", CONSTANTS_PDF_REPORTS.font12));
                    cell = new PdfPCell(p);		
                    cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);				
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);		
                    table.addCell(cell);
                    document.add(table);
                    return 0;
                }else{
                
                    float[] linea_50_50 = { 50F, 50F};
                    PdfPTable table = new PdfPTable(linea_50_50);
                    table.getDefaultCell().setBorderWidth(10);		
                    table.setWidthPercentage(100f);
                    table.setHorizontalAlignment(0);
                    table.setTotalWidth(CONSTANTS_PDF_REPORTS.totalWidth); 
                    table.setLockedWidth(true);
                
                    
                    p = new Paragraph(new Phrase("MAMA IZQUIERDA", CONSTANTS_PDF_REPORTS.font12));
                    cell = new PdfPCell(p);		
                    cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);				
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);		
                    table.addCell(cell);
                    
                    p = new Paragraph(new Phrase("MAMA DERECHA", CONSTANTS_PDF_REPORTS.font12));
                    cell = new PdfPCell(p);		
                    cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);				
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);		
                    table.addCell(cell);
                    
                    int iDer = 0;
                    int iZq  = 0;
                    for (int i=0; i< aiNumeroLesionesMamaDerecha.length; i++ ){                
                        
                        for (int j=iDer; ((j<aiNumeroLesionesMamaDerecha.length)&&(aiNumeroLesionesMamaDerecha[j] == 0));j++) iDer++;

                        if (iDer >= aiNumeroLesionesMamaDerecha.length){
                            p = new Paragraph(new Phrase("", CONSTANTS_PDF_REPORTS.font12));                       
                            cell = new PdfPCell(p);		
                            cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);				
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);		
                            table.addCell(cell);    
                        }else{
                            if (aiNumeroLesionesMamaDerecha[iDer] == 1){
                                p = new Paragraph(new Phrase("               Una Lesion Tipo " + astrTYPE_LESION_CODE_MEANING[iDer] + ".", CONSTANTS_PDF_REPORTS.font12));
                            }else{
                                p = new Paragraph(new Phrase("               " + aiNumeroLesionesMamaDerecha[iDer] + " Lesiones Tipo " + astrTYPE_LESION_CODE_MEANING[iDer] + ".", CONSTANTS_PDF_REPORTS.font12));
                            }
                            cell = new PdfPCell(p);		
                            cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);				
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);		
                            table.addCell(cell);                                    
                            iDer++;
                        }               
                        
                        for (int j=iZq; ((j<aiNumeroLesionesMamaIzquierda.length)&&(aiNumeroLesionesMamaIzquierda[j] == 0));j++) iZq++;

                        if (iZq >= aiNumeroLesionesMamaIzquierda.length){
                            p = new Paragraph(new Phrase("", CONSTANTS_PDF_REPORTS.font12));                       
                            cell = new PdfPCell(p);		
                            cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);				
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);		
                            table.addCell(cell);    
                        }else{
                            if (aiNumeroLesionesMamaIzquierda[iZq] == 1){
                                p = new Paragraph(new Phrase("               Una Lesion Tipo " + astrTYPE_LESION_CODE_MEANING[iZq] + ".", CONSTANTS_PDF_REPORTS.font12));
                            }else{
                                p = new Paragraph(new Phrase("               " + aiNumeroLesionesMamaIzquierda[iZq] + " Lesiones Tipo " + astrTYPE_LESION_CODE_MEANING[iZq] + ".", CONSTANTS_PDF_REPORTS.font12));
                            }
                            cell = new PdfPCell(p);		
                            cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);				
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);		
                            table.addCell(cell);                                    
                            iZq++;
                        }               

                    }
                    
                                                                                                                  
                    /*
                    for (int i=0; i< aiNumeroLesionesMamaIzquierda.length; i++ ){                        
                        if (aiNumeroLesionesMamaIzquierda[i] > 0){
                            if (aiNumeroLesionesMamaIzquierda[i] == 1){
                                p = new Paragraph(new Phrase("               Una Lesion Tipo" + astrTYPE_LESION_CODE_MEANING[i] + ".", CONSTANTS_PDF_REPORTS.font12));
                            }else{
                                p = new Paragraph(new Phrase("               " + aiNumeroLesionesMamaIzquierda[i] + " Lesiones Tipo " + astrTYPE_LESION_CODE_MEANING[i] + ".", CONSTANTS_PDF_REPORTS.font12));
                            }
                            cell = new PdfPCell(p);		
                            cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);				
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);		
                            table.addCell(cell);                                    
                        }     */
                        document.add(table);
                    }
                                                                               
                    
                    document.add(tblFranjaBlanca());
                    document.add(tblFranjaBlanca());
                    document.add(tblFranjaAzul());
                
                
                return 0;
            }catch(Exception ex){
                ex.printStackTrace();
                strError = ex.toString();
                return -1;
            }
        }

    protected int iPdfGeneraDetalleLesiones(String strTYPE_EXPLORATION_CODE_SCHEME, String strTYPE_EXPLORATION_CODE_VALUE,                                                                                                                                            
                                            String [] astrTYPE_LESION_CODE_SCHEME, String [] astrTYPE_LESION_CODE_VALUE, String [] astrTYPE_LESION_CODE_MEANING){
            try{
        
                int iErr = 0;
                
                //Lesiones en la Mama Derecha
                iErr = iPdfGeneraDetalleLesionesMama(strTYPE_EXPLORATION_CODE_SCHEME,strTYPE_EXPLORATION_CODE_VALUE,
                                                     CONSTANTS_PDF_REPORTS.RIGHT_BREAST_CODE_SCHEMA,CONSTANTS_PDF_REPORTS.RIGHT_BREAST_CODE_VALUE, CONSTANTS_PDF_REPORTS.RIGHT_BREAST_CODE_MEANING,                                                
                                                     CONSTANTS_PDF_REPORTS.astrCODE_SCHEMES_Right_Breast_Zones, CONSTANTS_PDF_REPORTS.astrCODE_VALUES_Right_Breast_Zones,
                                                     astrTYPE_LESION_CODE_SCHEME, astrTYPE_LESION_CODE_VALUE,astrTYPE_LESION_CODE_MEANING);
                if (iErr != 0) return iErr;
                
                //Lesiones en la Mama Izquierda
                iErr = iPdfGeneraDetalleLesionesMama(strTYPE_EXPLORATION_CODE_SCHEME,strTYPE_EXPLORATION_CODE_VALUE,
                                                     CONSTANTS_PDF_REPORTS.LEFT_BREAST_CODE_SCHEMA,CONSTANTS_PDF_REPORTS.LEFT_BREAST_CODE_VALUE, CONSTANTS_PDF_REPORTS.LEFT_BREAST_CODE_MEANING,                                                
                                                     CONSTANTS_PDF_REPORTS.astrCODE_SCHEMES_Left_Breast_Zones, CONSTANTS_PDF_REPORTS.astrCODE_VALUES_Left_Breast_Zones,                                                     
                                                     astrTYPE_LESION_CODE_SCHEME, astrTYPE_LESION_CODE_VALUE,astrTYPE_LESION_CODE_MEANING);
                if (iErr != 0) return iErr;
                
                                                
                /****************************** Hallazgos Asociados a la Mama DERECHA ***********************/                
                //iErr = iGeneraHallazgosAsociados(CONSTANTS_PDF_REPORTS.RIGHT_BREAST_CODE_SCHEMA, CONSTANTS_PDF_REPORTS. RIGHT_BREAST_CODE_VALUE);
                //if (iErr != 0) return iErr;
                
                                                                               
                document.add(tblFranjaAzul());
                
                return 0;
            }catch(Exception ex){
                ex.printStackTrace();
                strError = ex.toString();
                return -1;
            }
    
    }
    private int iPdfGeneraDetalleLesionesMama(String strTYPE_EXPLORATION_CODE_SCHEME, String strTYPE_EXPLORATION_CODE_VALUE,                                                
                                                String strBREAST_CODE_SCHEME, String strBREAST_CODE_VALUE,String strBREAST_CODE_MEANING,
                                                String [] astrCODE_SCHEMES_Breast_Zones, String [] astrCODE_VALUES_Breast_Zones, 
                                                String [] astrTYPE_LESION_CODE_SCHEME, String [] astrTYPE_LESION_CODE_VALUE, String [] astrTYPE_LESION_CODE_MEANING){
            try{
                
                int iContHallazgos   = 1;                
                String strTituloCabecera = null;
                
                /********************************************************************************************/
                /********************************************************************************************/
                /********************** DETALLE MAMA  *******************************************************/
                /********************************************************************************************/
                /********************************************************************************************/
                strTituloCabecera = "DETALLE " + strBREAST_CODE_MEANING;                
                document.add(tblCabeceraSeccion(strTituloCabecera.toUpperCase()));                                               
                document.add(tblFranjaBlanca());
                                                
                /****************************** Lesiones MAMA  para cada Tipo de Lesión **********************/     
                for (int i=0; i < astrTYPE_LESION_CODE_SCHEME.length; i++){

                    /********* xPath para extraer los id de las lesiones *************************************************/    
                    //Tipo de Exploracion
                    String xPathIdLesiones = CONSTANTS_PDF_REPORTS.XPath_Identificadores_Lesiones.replace("@TYPE_EXPLORATION_CODE_SCHEME",strTYPE_EXPLORATION_CODE_SCHEME);
                    xPathIdLesiones = xPathIdLesiones.replace("@TYPE_EXPLORATION_CODE_VALUE",strTYPE_EXPLORATION_CODE_VALUE);                                        
                    //Mama 
                    xPathIdLesiones = xPathIdLesiones.replace("@BREAST_CODE_SCHEME",strBREAST_CODE_SCHEME);
                    xPathIdLesiones = xPathIdLesiones.replace("@BREAST_CODE_VALUE",strBREAST_CODE_VALUE);                           
                    //Tipo de Lesion
                    xPathIdLesiones = xPathIdLesiones.replace("@TYPE_LESION_CODE_SCHEME",astrTYPE_LESION_CODE_SCHEME[i]);
                    xPathIdLesiones = xPathIdLesiones.replace("@TYPE_LESION_CODE_VALUE",astrTYPE_LESION_CODE_VALUE[i]); 
                    
                    /********* xPaths para extraer los valoes de la localizacion de la mama *************************************************/    
                    String [] xPathsLocalizaciones = new String[CONSTANTS_PDF_REPORTS.astrCODE_MEANING_Breast_Zones.length];
                    for (int j=0; j<CONSTANTS_PDF_REPORTS.astrCODE_MEANING_Breast_Zones.length;j++){
                         //Tipo de Exploracion
                         String xPathLocalizacion = CONSTANTS_PDF_REPORTS.XPath_Localizacion.replace("@TYPE_EXPLORATION_CODE_SCHEME",strTYPE_EXPLORATION_CODE_SCHEME);
                         xPathLocalizacion = xPathLocalizacion.replace("@TYPE_EXPLORATION_CODE_VALUE",strTYPE_EXPLORATION_CODE_VALUE);                                        
                         //Mama 
                         xPathLocalizacion = xPathLocalizacion.replace("@BREAST_CODE_SCHEME",strBREAST_CODE_SCHEME);
                         xPathLocalizacion = xPathLocalizacion.replace("@BREAST_CODE_VALUE",strBREAST_CODE_VALUE);                           
                         //Tipo de Lesion
                         xPathLocalizacion = xPathLocalizacion.replace("@TYPE_LESION_CODE_SCHEME",astrTYPE_LESION_CODE_SCHEME[i]);
                         xPathLocalizacion = xPathLocalizacion.replace("@TYPE_LESION_CODE_VALUE",astrTYPE_LESION_CODE_VALUE[i]);                                                   
                         //localizacion
                         xPathLocalizacion = xPathLocalizacion.replace("@LOCALIZACION_CODE_SCHEME",astrCODE_SCHEMES_Breast_Zones[j]);
                         xPathLocalizacion = xPathLocalizacion.replace("@LOCALIZACION_CODE_VALUE",astrCODE_VALUES_Breast_Zones[j]);                                                                                                                  
                         xPathsLocalizaciones[j] = xPathLocalizacion;                             
                    }
                                                            
                    iContHallazgos = iPdfGeneraDetalleLesion(xPathIdLesiones, 
                                                          xPathsLocalizaciones,iContHallazgos, 
                                                          astrTYPE_LESION_CODE_SCHEME[i],astrTYPE_LESION_CODE_VALUE[i], astrTYPE_LESION_CODE_MEANING[i]);
                    if (iContHallazgos < 0) return iContHallazgos;
                }
                
                                                

                /****************************** Hallazgos Asociados a la Mama DERECHA ***********************/                
                //iErr = iGeneraHallazgosAsociados(CONSTANTS_PDF_REPORTS.RIGHT_BREAST_CODE_SCHEMA, CONSTANTS_PDF_REPORTS. RIGHT_BREAST_CODE_VALUE);
                //if (iErr != 0) return iErr;
                
                /********************************************************************************************/
                /********************************************************************************************/
                /********************************************************************************************/                

                document.add(tblFranjaFinaAzul());
                                
                
                return 0;
            }catch(Exception ex){
                ex.printStackTrace();
                strError = ex.toString();
                return -1;
            }
    }

}
