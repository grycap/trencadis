package trencadis.infrastructure.gateKeeper;
         
import trencadis.infrastructure.gateKeeper.sql.CSql;
import trencadis.infrastructure.gateKeeper.security.VOMSCertificate;

import java.util.Vector;
import java.util.List;
import java.util.Enumeration;

import javax.security.auth.x500.X500Principal;

public class CGateKeeper {

	private String strcaTrustCertPath;
	private CSql sql;

	/******************************************************************************************************************/
	/* Constructor class                                                                                              */
	/******************************************************************************************************************/

	public CGateKeeper(String strcaTrustCertPath) {
            	this.strcaTrustCertPath = strcaTrustCertPath;
        }
        
	public CGateKeeper(String driver, String poolName, String URLBegin, String strDBHostName, String strDBPort, String strDBName, String strDBUser, String strDBPassword,
			String strcaTrustCertPath) {
		this.strcaTrustCertPath = strcaTrustCertPath;
		sql = new CSql(driver, poolName, URLBegin, strDBHostName, strDBPort, strDBName, strDBUser, strDBPassword);
	}

	/******************************************************************************************************************/
	/* Method that indicates if the access is allowed                                                                 */
	/* Input:                                                                                                         */
	/*      strCertificate: Certificate                                                                               */
	/* Return:                                                                                                        */
	/*     if the access is or no is valid                                                                            */
	/*     ERROR --> [1] Description Error                                                                            */
	/*     OK                                                                                                         */
	/*     DENIED                                                                                                     */
	/******************************************************************************************************************/
	public String strAllowAccess(String strCertificate) {
		try{
			Vector vAux      = new Vector();
			String strAux    = null;
			String strVO     = null;
			String strAccess = null;
			boolean bAccess  = true;                                               
                        
			//Checks CA and voms cert 
			VOMSCertificate voms_certificate = new VOMSCertificate(strCertificate, strcaTrustCertPath);
			String strDN = voms_certificate.getSubjectDN();                        
                       
			//Checks DN in DB
			strAux = sql.strDNAccess(strDN);
			if (strAux.compareTo("NOT FOUND") != 0) {
				if (strAux.compareTo("OK") == 0) return "OK";
				if (strAux.compareTo("DENIED") == 0) return "DENIED";
				if (strAux.substring(0, 5).compareTo("ERROR") == 0) return strAux;
			}
                                                                                                
			//extract groups from the certificate
			Vector vGroups = this.vExtractGroups(strCertificate);
                        
                       
			if (vGroups.get(0).toString().compareTo("ERROR")==0) {
				return "ERROR --> " + vGroups.get(1).toString();
			}
                     
                        
			//Extract VO, check VO in DB
			Enumeration eGroups = vGroups.elements();
			vAux = (Vector) eGroups.nextElement();
			strVO = vAux.get(0).toString();
			strAccess = sql.strGroupAccess(strVO);
			if (strAccess.compareTo("OK") != 0){
				if (strAccess.substring(0, 5).compareTo("ERROR") == 0)return
				strAccess;
				else {
					if (strAccess.compareTo("NOT FOUND") == 0)return "DENIED";
					if (strAccess.compareTo("DENIED") == 0)return "DENIED";
				}
			}

                        
			//Extract other groups, check groups in DB
			while(eGroups.hasMoreElements()) {
				bAccess = true;
				vAux = (Vector) eGroups.nextElement();
				Enumeration eSubGroups = vAux.elements();
				while(eSubGroups.hasMoreElements()){
					strAux = (String)eSubGroups.nextElement();
					//Mirar si tiene acceso permitido.
					strAccess = sql.strGroupAccess(strAux);
					if (strAccess.compareTo("OK")        == 0) break;
					if (strAccess.compareTo("NOT FOUND") == 0) break;
					if (strAccess.compareTo("DENIED")    == 0) {
						bAccess = false;
						break;
					}
					if (strAccess.substring(0,5).compareTo("ERROR") == 0) return strAccess;
				}
				if (bAccess) return "OK";
			}

			return "OK";

		} catch(Exception ex){
			return "ERROR" + ex.toString();
		}

	}

	/******************************************************************************************************************/
	/* Method extracts the groups from certificate                                                                    */
	/* Input:                                                                                                         */
	/*    strCertificate: Certificate                                                                                 */
	/* Return:                                                                                                        */
	/*    [0] = "ERROR"--> [1] Description ERROR                                                                      */
	/*    Vector of vectors with diferents groups of the certificate                                                             */
	/******************************************************************************************************************/
	public Vector vExtractGroups(String strCertificate){
		try{
			Vector vAux     = new Vector();
			Vector vResult  = new Vector();
			String strAux   = "";
			int iIndex3     = 0;
                        
			VOMSCertificate voms_certificate = new VOMSCertificate(strCertificate,strcaTrustCertPath);
                        
			List lGroups = voms_certificate.getAssertions();
                        
			String [] astrAux = new String[lGroups.size()];
			for (int iIndex = 0; iIndex < lGroups.size(); iIndex++){                        
				iIndex3 = lGroups.get(iIndex).toString().lastIndexOf("/Role");
				astrAux[iIndex] = lGroups.get(iIndex).toString().substring(0,iIndex3+1);
			}

			//Get VO
			String strVO = astrAux[0];
			vAux.add(strVO.substring(0,strVO.length()-1));
			vResult.add(vAux);

			//Get a line of groups
			for (int iIndex=1; iIndex < astrAux.length; iIndex++){
				strAux   = astrAux[iIndex];
				if (strAux.length() != 0){
					astrAux[iIndex] = "";
					vAux = new Vector();
					vAux.add(strAux.substring(0,strAux.length()-1));
					for (int iIndex2 = iIndex + 1; iIndex2 < astrAux.length;iIndex2++) {
						if (astrAux[iIndex2].length() != 0){
							if (strAux.compareTo(astrAux[iIndex2].substring(0,strAux.length())) == 0) {
								vAux.add(astrAux[iIndex2].substring(0,astrAux[iIndex2].length()-1));
								astrAux[iIndex2] = "";
							}
						}
					}
					vResult.add(vAux);
				}
			}
                                                                                                
			return vResult;
		} catch(Exception e) {
			Vector vAux = new Vector();
			vAux.add("ERROR");
			vAux.add(e.toString());
			return vAux;
		}

	}

	public String strExtractDN(String strCertificate) {
		try {
			VOMSCertificate voms_certificate = new VOMSCertificate(strCertificate, strcaTrustCertPath);
			return voms_certificate.getSubjectDN();
		} catch(Exception ex) {
			return "ERROR" + ex.toString();
		}
	}
               

}
