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

import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;


/**
 * This class represents the single Generic Attribute.
 *
 * @author Vincenzo Ciaschini
 */
public class GenericAttribute implements DEREncodable {
    private String name;
    private String value;
    private String qualifier;

    /**
     * Empty contructor
     */
    public GenericAttribute() {
        name = value = qualifier = null;
    }

    /**
     * Creates a GenericAttributes object from a sequence.
     *
     * @param seq the Sequence
     *
     * @throws IllegalArgumentException if there are parsing problems.
     */
    public GenericAttribute(ASN1Sequence seq) {
        if (seq.size() != 3)
            throw new IllegalArgumentException("Encoding error in GenericAttribute");

        name = value = qualifier = null;

        if ((seq.getObjectAt(0) instanceof ASN1OctetString) &&
            (seq.getObjectAt(1) instanceof ASN1OctetString) &&
            (seq.getObjectAt(2) instanceof ASN1OctetString)) {
            value = new String(DEROctetString.getInstance(seq.getObjectAt(1)).getOctets());
            name = new String(DEROctetString.getInstance(seq.getObjectAt(0)).getOctets());
            qualifier = new String(DEROctetString.getInstance(seq.getObjectAt(2)).getOctets());
        }
        else
            throw new IllegalArgumentException("Encoding error in GenericAttribute");
    }
        
    /**
     * Static variant of the constructor.
     *
     * @see #GenericAttribute(ASN1Sequence seq)
     */
    public static GenericAttribute getInstance(ASN1Sequence seq) {
        return new GenericAttribute(seq);
    }

    /**
     * Gets the name of the attribute
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the value of the attribute
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the qualifier of the attribute
     *
     * @return the qualifier
     */
    public String getQualifier() {
        return qualifier;
    }

    /**
     * Makes a DERObject representation.
     *
     * @return the DERObject
     */
    public DERObject getDERObject() {
        ASN1EncodableVector v = new ASN1EncodableVector();

        v.add(new DEROctetString(name.getBytes()));
        v.add(new DEROctetString(value.getBytes()));
        v.add(new DEROctetString(qualifier.getBytes()));

        return new DERSequence(v);
    }
}
