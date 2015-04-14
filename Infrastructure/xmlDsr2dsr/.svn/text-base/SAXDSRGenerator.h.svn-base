#include <xercesc/sax2/DefaultHandler.hpp>
#include <xercesc/sax2/Attributes.hpp>
using namespace xercesc;

#include "dcmtk/config/cfunix.h"
#include "dcmtk/dcmsr/dsrdoc.h"

#include <iostream>
#include <string>

using namespace std;

class SAXDSRGenerator : public DefaultHandler {
public:
    
    DSRDocument *document;
    DSRDocumentTree *documentTree;
    
    DSRTypes::E_AddMode addMode;
    
    size_t returnValue;

    char *studyInstanceUID;
    char *SOPInstanceUID;
            
    string codeValue;
    string codeSchema;
    string codeMeaning;

    string entryValue;
    string entrySchema;
    string entryMeaning;

    string unitsValue;
    string unitsSchema;
    string unitsMeaning;
    
    DSRSpatialCoordinatesValue *coord;

    string buffer;

    SAXDSRGenerator(DSRDocument *document);

    void startDocument();
    void endDocument();
    void startElement(const XMLCh* const uri, const XMLCh* const localname, const XMLCh* const qname, const Attributes&  ttrs);
    void endElement(const XMLCh* const uri, const XMLCh* const localname, const XMLCh* const qname);
    void characters(const XMLCh* const chars, const unsigned int length);
    void fatalError(const SAXParseException&);

};
