#include "SAXDSRGenerator.h"

SAXDSRGenerator::SAXDSRGenerator(DSRDocument *document){
    this->document = document;
    this->documentTree = &document->getTree();
}

void SAXDSRGenerator::startDocument() {
    document->setSpecificCharacterSetType(DSRTypes::CS_UTF8);
}

void SAXDSRGenerator::endDocument() {}

void SAXDSRGenerator::startElement( const XMLCh* const uri, const XMLCh* const localname, const XMLCh* const qname, const Attributes& attrs) {

  char* qName = XMLString::transcode(qname);

    if (!XMLString::compareString(qName, "CODE_VALUE")) {
        buffer.clear();
        codeValue.clear();
    } else if (!XMLString::compareString(qName, "CODE_SCHEMA")) {
        buffer.clear();
        codeSchema.clear();
    } else if (!XMLString::compareString(qName, "CODE_MEANING")) {
        buffer.clear();
        codeMeaning.clear();
    } else if (!XMLString::compareString(qName, "PERSON_NAME")) {
        buffer.clear();
    } else if (!XMLString::compareString(qName, "TEXT_VALUE")) {
        buffer.clear();
    } else if (!XMLString::compareString(qName, "DATE_VALUE")) {
        buffer.clear();
    } else if (!XMLString::compareString(qName, "TIME_VALUE")) {
        buffer.clear();
    } else if (!XMLString::compareString(qName, "DATETIME_VALUE")) {
        buffer.clear();
    } else if (!XMLString::compareString(qName, "GRAPHIC_DATA")) {
        buffer.clear();
    } else if (!XMLString::compareString(qName, "GRAPHIC_TYPE")) {
        buffer.clear();
    } else if (!XMLString::compareString(qName, "ENTRY_VALUE")) {
        buffer.clear();
        entryValue.clear();
    } else if (!XMLString::compareString(qName, "ENTRY_SCHEMA")) {
        buffer.clear();
        entrySchema.clear();
    } else if (!XMLString::compareString(qName, "ENTRY_MEANING")) {
        buffer.clear();
        entryMeaning.clear();
    } else if (!XMLString::compareString(qName, "UNITS_VALUE")) {
        buffer.clear();
        unitsValue.clear();
    } else if (!XMLString::compareString(qName, "UNITS_SCHEMA")) {
        buffer.clear();
        unitsSchema.clear();
    } else if (!XMLString::compareString(qName, "UNITS_MEANING")) {
        buffer.clear();
        unitsMeaning.clear();
    } else if (!XMLString::compareString(qName, "NUMERIC_VALUE")) {
        buffer.clear();
    } else if (!XMLString::compareString(qName, "UID")) {
        buffer.clear();
    } else if (!XMLString::compareString(qName, "CONTINUITY")) {
        buffer.clear();
    } else if (!XMLString::compareString(qName, "PNAME")) {
        returnValue = documentTree->addContentItem(DSRTypes::RT_hasObsContext, DSRTypes::VT_PName, addMode);
        if (!returnValue) {
            throw "Can not add PNAME content item";
        }
        addMode = DSRTypes::AM_afterCurrent;
    } else if (!XMLString::compareString(qName, "TEXT")) {
        returnValue = documentTree->addContentItem(DSRTypes::RT_hasObsContext, DSRTypes::VT_Text, addMode);
        if (!returnValue) {
            throw "Can not add TEXT content item";
        }
        addMode = DSRTypes::AM_afterCurrent;
    } else if (!XMLString::compareString(qName, "DATE")) {
        returnValue = documentTree->addContentItem(DSRTypes::RT_hasObsContext, DSRTypes::VT_Date, addMode);
        if (!returnValue) {
            throw "Can not add DATE content item";
        }
        addMode = DSRTypes::AM_afterCurrent;
    } else if (!XMLString::compareString(qName, "TIME")) {
        returnValue = documentTree->addContentItem(DSRTypes::RT_hasObsContext, DSRTypes::VT_Time, addMode);
        if (!returnValue) {
            throw "Can not add TIME content item";
        }
        addMode = DSRTypes::AM_afterCurrent;
    } else if (!XMLString::compareString(qName, "DATETIME")) {
        returnValue = documentTree->addContentItem(DSRTypes::RT_hasObsContext, DSRTypes::VT_DateTime, addMode);
        if (!returnValue) {
            throw "Can not add DATETIME content item";
        }
        addMode = DSRTypes::AM_afterCurrent;
    } else if (!XMLString::compareString(qName, "SCOORD")) {
        returnValue = documentTree->addContentItem(DSRTypes::RT_inferredFrom, DSRTypes::VT_SCoord, addMode);
        if (!returnValue) {
            throw "Can not add SCOORD content item";
        }
        addMode = DSRTypes::AM_afterCurrent;
    } else if (!XMLString::compareString(qName, "CODE")) {
        returnValue = documentTree->addContentItem(DSRTypes::RT_hasObsContext, DSRTypes::VT_Code, addMode);
        if (!returnValue) {
            throw "Can not add CODE content item";
        }
        addMode = DSRTypes::AM_afterCurrent;
    } else if (!XMLString::compareString(qName, "NUM")) {
        returnValue = documentTree->addContentItem(DSRTypes::RT_hasObsContext, DSRTypes::VT_Num, addMode);
        if (!returnValue) {
            throw "Can not add NUM content item";
        }
        addMode = DSRTypes::AM_afterCurrent;
    } else if (!XMLString::compareString(qName, "UIDREF")) {
        returnValue = documentTree->addContentItem(DSRTypes::RT_hasObsContext, DSRTypes::VT_UIDRef, addMode);
        if (!returnValue) {
            throw "Can not add UIDREF content item";
        }
        addMode = DSRTypes::AM_afterCurrent;
    } else if (!XMLString::compareString(qName, "CONTAINER")) {
        returnValue = documentTree->addContentItem(DSRTypes::RT_contains, DSRTypes::VT_Container, addMode);
        if (!returnValue) {
            throw "Can not add content item";
        }
        documentTree->getCurrentContentItem().setContinuityOfContent(DSRTypes::COC_Separate);
        addMode = DSRTypes::AM_afterCurrent;
    } else if (!XMLString::compareString(qName, "CHILDS")) {
        addMode = DSRTypes::AM_belowCurrent;
    } else if (!XMLString::compareString(qName, "DICOM_SR")) {
        documentTree->addContentItem(DSRTypes::RT_isRoot, DSRTypes::VT_Container);
        XMLCh *attribute = XMLString::transcode("reportType");
        char* reportType = XMLString::transcode(attrs.getValue(attribute));
        documentTree->getCurrentContentItem().setConceptName(DSRCodedEntryValue(reportType, "Grycap", "A report type"));
        documentTree->getCurrentContentItem().setContinuityOfContent(DSRTypes::COC_Separate);
        addMode = DSRTypes::AM_belowCurrent;

        XMLCh *attribute2 = XMLString::transcode("patientsName");
        char* patientsName = XMLString::transcode(attrs.getValue(attribute2));
        document->setPatientsName(patientsName);

        XMLCh *attribute3 = XMLString::transcode("studyUID");
        char* studyUID = XMLString::transcode(attrs.getValue(attribute3));
        document->setStudyID(studyUID);
        this->studyInstanceUID = studyUID;

        XMLCh *attribute4 = XMLString::transcode("reportID");
        this->SOPInstanceUID = XMLString::transcode(attrs.getValue(attribute4));

    }

    XMLString::release(&qName);
}

void SAXDSRGenerator::endElement(const XMLCh* const uri, const XMLCh* const localname, const XMLCh* const qname) {

    char* qName = XMLString::transcode(qname);

    if (!XMLString::compareString(qName, "CODE_VALUE")) {
        codeValue.append(buffer);
    } else if (!XMLString::compareString(qName, "CODE_SCHEMA")) {
        codeSchema.append(buffer);
    } else if (!XMLString::compareString(qName, "CODE_MEANING")) {
        codeMeaning.append(buffer);
        documentTree->getCurrentContentItem().setConceptName(DSRCodedEntryValue(codeValue.c_str(), codeSchema.c_str(), codeMeaning.c_str()));
    } else if (!XMLString::compareString(qName, "PERSON_NAME")) {
        documentTree->getCurrentContentItem().setStringValue(buffer.c_str());
    } else if (!XMLString::compareString(qName, "TEXT_VALUE")) {
        documentTree->getCurrentContentItem().setStringValue(buffer.c_str());
    } else if (!XMLString::compareString(qName, "DATE_VALUE")) {
        documentTree->getCurrentContentItem().setStringValue(buffer.c_str());
    } else if (!XMLString::compareString(qName, "TIME_VALUE")) {
        documentTree->getCurrentContentItem().setStringValue(buffer.c_str());
    } else if (!XMLString::compareString(qName, "DATETIME_VALUE")) {
        documentTree->getCurrentContentItem().setStringValue(buffer.c_str());
    } else if (!XMLString::compareString(qName, "GRAPHIC_DATA")) {

        coord = new DSRSpatialCoordinatesValue();
        string X, Y;
        unsigned int i, last=0, xy=0;

        for (i = 0; i < buffer.length(); i++) {
            if ( buffer[i] == ',' ) {
                if (xy == 0) {
                    X = buffer.substr(last, i-last);
                } else {
                    Y = buffer.substr(last, i-last);
                    coord->getGraphicDataList().addItem(atof(X.c_str()), atof(Y.c_str()));
                }
                last = i+1;
                xy = (xy + 1) % 2;
            }
        }

        Y = buffer.substr(last, i-last-1);
        coord->getGraphicDataList().addItem(atof(X.c_str()), atof(Y.c_str()));

    } else if (!XMLString::compareString(qName, "GRAPHIC_TYPE")) {
        if (!buffer.compare("POINT")) {
            coord->setGraphicType(DSRTypes::GT_Point);
        } else if (!buffer.compare("MULTIPOINT")) {
            coord->setGraphicType(DSRTypes::GT_Multipoint);
        } else if (!buffer.compare("POLYLINE")) {
            coord->setGraphicType(DSRTypes::GT_Polyline);
        } else if (!buffer.compare("CIRCLE")) {
            coord->setGraphicType(DSRTypes::GT_Circle);
        } else {
            /*Ellipse is not supported*/
            coord->setGraphicType(DSRTypes::GT_invalid);
        }
        documentTree->getCurrentContentItem().setSpatialCoordinates(*coord);
    } else if (!XMLString::compareString(qName, "ENTRY_VALUE")) {
        entryValue.append(buffer);
    } else if (!XMLString::compareString(qName, "ENTRY_SCHEMA")) {
        entrySchema.append(buffer);
    } else if (!XMLString::compareString(qName, "ENTRY_MEANING")) {
        entryMeaning.append(buffer);
        documentTree->getCurrentContentItem().setCodeValue(DSRCodedEntryValue(entryValue.c_str(), entrySchema.c_str(), entryMeaning.c_str()));
    } else if (!XMLString::compareString(qName, "UNITS_VALUE")) {
        unitsValue.append(buffer);
    } else if (!XMLString::compareString(qName, "UNITS_SCHEMA")) {
        unitsSchema.append(buffer);
    } else if (!XMLString::compareString(qName, "UNITS_MEANING")) {
        unitsMeaning.append(buffer);
    } else if (!XMLString::compareString(qName, "NUMERIC_VALUE")) {
        documentTree->getCurrentContentItem().setNumericValue( DSRNumericMeasurementValue(buffer.c_str(), DSRCodedEntryValue(unitsValue.c_str(), unitsSchema.c_str(), unitsMeaning.c_str()) ) );
    } else if (!XMLString::compareString(qName, "UIDREF")) {
        documentTree->getCurrentContentItem().setStringValue(buffer.c_str());
    } else if (!XMLString::compareString(qName, "CONTINUITY")) {
        if (!XMLString::compareString(buffer.c_str(), "CONTINUOUS")) {
            documentTree->getCurrentContentItem().setContinuityOfContent(DSRTypes::COC_Continuous);
        } else if (!XMLString::compareString(buffer.c_str(), "SEPARATE")) {
            documentTree->getCurrentContentItem().setContinuityOfContent(DSRTypes::COC_Separate);
        } else {
            throw "Bad value of continuity of content in container";
        }
    } else if (!XMLString::compareString(qName, "CHILDS")) {
        documentTree->goUp();
    }

    XMLString::release(&qName);
}

void SAXDSRGenerator::characters(const XMLCh* const chars, unsigned int length) {
    char* ch = XMLString::transcode(chars);
    buffer.append(ch);
    XMLString::release(&ch);
}

void SAXDSRGenerator::fatalError(const SAXParseException& exception) {
    char* message = XMLString::transcode(exception.getMessage());
    cout << "Fatal Error: " << message << " at line: " << exception.getLineNumber() << endl;
    XMLString::release(&message);
}
