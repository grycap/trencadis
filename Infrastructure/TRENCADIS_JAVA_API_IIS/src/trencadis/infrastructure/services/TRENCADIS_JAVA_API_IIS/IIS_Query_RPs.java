/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.infrastructure.services.TRENCADIS_JAVA_API_IIS;

import java.util.Vector;

import org.globus.axis.message.addressing.Address;
import org.globus.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.WSRFConstants;
import org.oasis.wsrf.properties.QueryExpressionType;
import org.oasis.wsrf.properties.QueryResourcePropertiesResponse;
import org.oasis.wsrf.properties.QueryResourceProperties_Element;
import org.oasis.wsrf.properties.QueryResourceProperties_PortType;
import org.oasis.wsrf.properties.WSResourcePropertiesServiceAddressingLocator;

/**
 *
 * @author root
 */
public class IIS_Query_RPs {
     
    private String strError = "";

    public String strGetError(){
          return strError;
    }
    
    public QueryResourcePropertiesResponse QueryRPs(String strIIS_URL, String strxPathQuery){
        try{                        
            //Query the information service about the EOUIDGeneratorService
            WSResourcePropertiesServiceAddressingLocator locator_query = new WSResourcePropertiesServiceAddressingLocator();
            EndpointReferenceType epr = new EndpointReferenceType(new Address(strIIS_URL));
            QueryResourceProperties_PortType port = locator_query.getQueryResourcePropertiesPort(epr);

            //Get resource
            QueryExpressionType query = new QueryExpressionType();	
                                                         
            query.setDialect(WSRFConstants.XPATH_1_DIALECT);
            query.setValue(strxPathQuery);
               
            QueryResourceProperties_Element request = new QueryResourceProperties_Element();
            request.setQueryExpression(query);
            QueryResourcePropertiesResponse response = port.queryResourceProperties(request);       
                             
            return response;
        }catch(Exception ex){
            strError = ex.toString();
            ex.printStackTrace();
            return null;
        }
    }
    
    public QueryResourcePropertiesResponse QueryRPs(Vector vIIS_URLs, String strxPathQuery){
        
        String strIIS_URL = "";
        QueryResourcePropertiesResponse response;
        
        for (int i=0; i<vIIS_URLs.size(); i++){
            strIIS_URL = vIIS_URLs.get(i).toString();            
            response = QueryRPs(strIIS_URL, strxPathQuery);
                        
            //Fail or not found RP
            if ((response != null)&&response.get_any()!=null&&response.get_any().length >= 1) return response;
                                    
        }
        strError = "No IIS is Active or No URI of the Service (" + strxPathQuery + ") is published in any central IIS";
        return null;
    }
                
}
