/*********************************************************************
 *
 * Authors: Vincenzo Ciaschini - Vincenzo.Ciaschini@cnaf.infn.it
 *
 * Copyright (c) Members of the EGEE Collaboration. 2004-2010.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Parts of this code may be based upon or even include verbatim pieces,
 * originally written by other people, in which case the original header
 * follows.
 *
 *********************************************************************/
package trencadis.infrastructure.voms.ac;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.IssuerSerial;

class NameConverter {
    private String      value;

    public NameConverter(GeneralName gn) {
        switch (gn.getTagNo()) {
        case 6:
            value = DERIA5String.getInstance(gn.getName()).getString();
            break;
        default:
            throw new IllegalArgumentException("Erroneous encoding of Targets");
        }
    }

    public static NameConverter getInstance(GeneralName gn) {
        return new NameConverter(gn);
    }

    public String getAsString() {
        return value;
    }
}

/**
 * The intent of this class is to represent a single target.
 *
 * @author Vincenzo Ciaschini
 */
public class ACTarget implements DEREncodable {
    private GeneralName  name;
    private GeneralName  group;
    private IssuerSerial cert;

    /**
     * empty contructor
     */

    public ACTarget() {
        name = null;
        group = null;
        cert = null;
    }

    /**
     * Creates a string representation of the target.
     *
     * @return the string, or null if there were problems.
     */
    public String toString() {
        if (name != null)
            return getName();
        if (group != null)
            return getGroup();
        if (cert != null)
            return getIssuerSerialString();

        return "";
    }


    /**
     * Gets the name element.
     *
     * @return the name.
     */
    public String getName() {
        return NameConverter.getInstance(name).getAsString();
    }

    /**
     * Gets the group element
     *
     * @return the group.
     */
    public String getGroup() {
        return NameConverter.getInstance(group).getAsString();
    }

    /**
     * Gets the IssuerSerial
     *
     * @return the IssuerSerial
     */
    public IssuerSerial getIssuerSerial() {
        return cert;
    }

    /**
     * Gets the IssuerSerial element
     *
     * @return the IssuerSerial as String.
     */
    public String getIssuerSerialString() {
        ASN1Sequence seq = ASN1Sequence.getInstance(cert.getIssuer().getDERObject());
        GeneralName  name  = GeneralName.getInstance(seq.getObjectAt(0));

        return NameConverter.getInstance(name).getAsString() + ":" + 
                          (cert.getSerial().toString());
    }

    /**
     * Sets the name
     *
     * @param n the name
     */
    public void setName(GeneralName n) {
        name = n;
    }

    /**
     * Sets the name
     *
     * @param s the name.
     */
    public void setName(String s) {
        name = new GeneralName(new DERIA5String(s), 6);
    }

    /**
     * Sets the group.
     *
     * @param g the group
     */
    public void setGroup(GeneralName g) {
        group = g;
    }

    /**
     * Sets the group
     *
     * @param s the group name.
     */
    public void setGroup(String s) {
        group = new GeneralName(new DERIA5String(s), 6);
    }

    /**
     * Sets the IssuerSerial
     *
     * @param is the IssuerSerial
     */
    public void setIssuerSerial(IssuerSerial is) {
        cert = is;
    }

    /**
     * Sets the IssuerSerial
     *
     * @param s a textual representation of the IssuerSerial, in the from
     * subject:serial
     */
    public void setIssuerSerial(String s) {
        int ch = s.lastIndexOf(':');
        if (ch != -1) {
            String iss = s.substring(0, ch);
            GeneralName nm = new GeneralName(new DERIA5String(iss), 6);
            ASN1Sequence seq = ASN1Sequence.getInstance(name.getDERObject());

            ASN1EncodableVector v = new ASN1EncodableVector();
            v.add(nm);
            v.add(seq);
            cert = new IssuerSerial(new DERSequence(v));
        }
        else throw new IllegalArgumentException("cannot identify issuer and serial");
    }

    /**
     * Static variant of the constructor.
     *
     * @see #ACTarget(ASN1Sequence seq)
     */
    public static ACTarget getInstance(ASN1Sequence seq) {
        return new ACTarget(seq);
    }

    /**
     * Creates an ACTarget from a sequence
     *
     * @param seq the Sequence
     *
     * @throws IllegalArgumentException if there are parsing problems.
     */
    public ACTarget(ASN1Sequence seq) {
        int i = 0;
        name = group = null;
        cert = null;

        while (i <= seq.size()) {
            if (seq.getObjectAt(i) instanceof ASN1TaggedObject) {
                ASN1TaggedObject obj = (ASN1TaggedObject) seq.getObjectAt(i);
                switch (obj.getTagNo()) {
                case 0:
                    group = null;
                    cert = null;
                    name = GeneralName.getInstance((ASN1TaggedObject)obj, true);
                    break;
                case 1:
                    cert = null;
                    group = GeneralName.getInstance((ASN1TaggedObject)obj, true);
                    name = null;
                    break;
                case 2:
                    group = null;
                    name = null;
                    cert = new IssuerSerial((ASN1Sequence)obj.getObject());
                    break;
                default:
                    throw new IllegalArgumentException("Bad tag in encoding ACTarget");
                }
            }
            else
                throw new IllegalArgumentException("Bad value type encoding ACTarget");
            i++;
        }
    }

    /**
     * Makes a DERObject representation.
     *
     * @return the DERObject
     */
    public DERObject getDERObject() {
        ASN1EncodableVector v = new ASN1EncodableVector();

        if (name != null)
            v.add(new DERTaggedObject(0, name));
        if (group != null)
            v.add(new DERTaggedObject(1, group));
        if (cert != null)
            v.add(new DERTaggedObject(2, cert));

        return new DERSequence(v);
    }
}


                    
