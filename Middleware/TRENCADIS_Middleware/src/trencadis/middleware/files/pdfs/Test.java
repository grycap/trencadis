package trencadis.middleware.files.pdfs;



public class Test {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
    
        public static void main(String[] args) {        			
		try {
			//XMLReader xr;
			//xr = XMLReaderFactory.createXMLReader();
                                                                        						
                        PDF_EQ_Report pdfCreadoEQ = new PDF_EQ_Report("/home/trencadis/Test/EQ_2_3.xml", "/home/trencadis/Test/EQ.pdf", "J. Damian Segrelles Quilis");                        
                        pdfCreadoEQ.iGeneraPDF();    
                    
                        PDF_MAMO_Report pdfCreadoMAMO = new PDF_MAMO_Report("/home/trencadis/Test/MAMO_2_1.xml", "/home/trencadis/Test/MAMO.pdf", "J. Damian Segrelles Quilis");                        
                        pdfCreadoMAMO.iGeneraPDF();
                                                                        
                        PDF_ECO_Report pdfCreadoECO = new PDF_ECO_Report("/home/trencadis/Test/ECO_3_1.xml", "/home/trencadis/Test/ECO.pdf", "J. Damian Segrelles Quilis");                        
                        pdfCreadoECO.iGeneraPDF();
                        
                        PDF_RM_Report pdfCreadoRM = new PDF_RM_Report("/home/trencadis/Test/RESO_4_0.xml", "/home/trencadis/Test/RM.pdf", "J. Damian Segrelles Quilis");                        
                        pdfCreadoRM.iGeneraPDF();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
