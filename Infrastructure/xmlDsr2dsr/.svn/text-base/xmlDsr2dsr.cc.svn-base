#include <xercesc/sax2/SAX2XMLReader.hpp>
#include <xercesc/sax2/XMLReaderFactory.hpp>
#include <xercesc/sax2/DefaultHandler.hpp>
#include <xercesc/util/XMLString.hpp>
using namespace xercesc;

#include <iostream>
using namespace std;

#include "SAXDSRGenerator.h"
#include "mdfdsman.h"

int main(int argc, char* argv[]) {
    
    if (argc != 3) {
        cerr << "Uso:" << endl << "\txmlDsr2dsr <input file> <output file>" << endl;
        return 1;
    }
    
    DSRDocument document(DSRTypes::DT_ComprehensiveSR);
    
    try {
        XMLPlatformUtils::Initialize();
    }
    catch (const XMLException& toCatch) {
        char* message = XMLString::transcode(toCatch.getMessage());
        cerr << "Error during initialization! :\n";
        cerr << "Exception message is: \n" << message << "\n";
        XMLString::release(&message);
        return 1;
    }
    
    char* xmlFile = argv[1];
    
    SAX2XMLReader* parser = XMLReaderFactory::createXMLReader();
    parser->setFeature(XMLUni::fgSAX2CoreValidation, true);
    parser->setFeature(XMLUni::fgSAX2CoreNameSpaces, true);
    
    SAXDSRGenerator* saxDSRGenerator = new SAXDSRGenerator(&document);
    parser->setContentHandler(saxDSRGenerator);
    parser->setErrorHandler(saxDSRGenerator);
    
    try {
        parser->parse(xmlFile);
    }
    catch (const XMLException& toCatch) {
        char* message = XMLString::transcode(toCatch.getMessage());
        cerr << "ERROR: Exception message is: " << endl
                << message << "\n";
        XMLString::release(&message);
        return 1;
    }
    catch (const SAXParseException& toCatch) {
        char* message = XMLString::transcode(toCatch.getMessage());
        cerr << "ERROR: Exception message is: " << endl
                << message << "\n";
        XMLString::release(&message);
        return 1;
    }
    catch (char *error) {
        cerr << "ERROR: "<< error << endl;
        return 1;
    }
    catch (...) {
        return 1;
    }
    
    document.completeDocument();
    
    DcmFileFormat fileformat;
    OFCondition status = document.write(*fileformat.getDataset());
    if (status.good()) {
        status = fileformat.saveFile(argv[2], EXS_LittleEndianExplicit);
        if (status.bad()) {
            cerr << "ERROR: Cannot write DICOM file." << endl;
            return 1;
        }
    } else {
        cerr << "ERROR: Cannot write SR document." << endl;
        return 1;
    }

    MdfDatasetManager datasetManager;
    
    status = datasetManager.loadFile(argv[2]);
    OFCondition status2 = datasetManager.modifyOrInsertTag("StudyInstanceUID", saxDSRGenerator->studyInstanceUID, true, true);
    OFCondition status3 = datasetManager.modifyOrInsertTag("SOPInstanceUID", saxDSRGenerator->SOPInstanceUID, true, true);
    
    if (status.good() && status2.good() && status3.good()) {
        datasetManager.saveFile();
    } else {
        cerr << "ERROR: Cannot set dcm file UID in the generated file." << endl;
        return 1;
    }
    
    delete parser;
    delete saxDSRGenerator;
    
    return 0;
}
