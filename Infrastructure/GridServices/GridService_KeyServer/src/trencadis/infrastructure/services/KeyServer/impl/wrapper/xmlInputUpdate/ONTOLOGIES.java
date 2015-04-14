package trencadis.infrastructure.services.KeyServer.impl.wrapper.xmlInputUpdate;

/******************************************************************* 
 *********** CLASE GENERADA AUTOM�TICAMENTE POR XMLWRAPPER ********** 
 ********-----Germ�n Molt� Mart�nez (gmolto@dsic.upv.es) -----******* 
 ********************************************************************/ 


import org.w3c.dom.*;

/**
 * Contiene los m�todos que permiten obtener y cambiar la informaci�n
 * de los elementos <STRONG>ONTOLOGIES </STRONG> del documento XML.
 */
public class ONTOLOGIES extends Wrapper{

/**
 * Crea un Wrapper que trabajar� a partir de un elemento ONTOLOGIES
 * @param e El elemento sobre el cual trabaja el Wrapper
*/
public ONTOLOGIES(Element e){
 super(e);
}

/**
 * Permite iterar por todos los elementos IDONTOLOGY
 * @return Un java.util.iterator para facilitar la iteraci�n <BR>
 * Ejemplo de uso: <BR>
 *    Iterator it = getAll_IDONTOLOGY(); <BR> 
 *    while (it.hasNext()){  <BR>
 *     <UL>  IDONTOLOGY obj = (IDONTOLOGY) it.next(); <BR>   
 *     }
 */
public java.util.Iterator getAll_IDONTOLOGY() {
 java.util.Vector v = getChildsByTagName("IDONTOLOGY");
 int length = v.size(); 
 for (int i = 0 ; i < length ; i++ ){
   v.setElementAt(new  IDONTOLOGY((Element)v.elementAt(i)) , i);
 }
 return v.iterator();
}

/**
 * A�ade un nuevo elemento IDONTOLOGY. El contenido del nuevo elemento es vacio
 * y deber� ser establecido mediante los m�todos del wrapper obtenido
 * @return Un wrapper al nuevo elemento introducido
 */
 public IDONTOLOGY add_IDONTOLOGY(){
  Element newIDONTOLOGY = doc.createElement("IDONTOLOGY");
  elem.appendChild(newIDONTOLOGY);
  return new IDONTOLOGY(newIDONTOLOGY);
 }
}