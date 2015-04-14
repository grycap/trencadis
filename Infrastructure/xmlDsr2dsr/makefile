CFLAGS = -Wall
LDFLAGS = -ldcmsr -ldcmdata -lofstd -lxml2 -lz -lpthread -lxerces-c
CXX = c++

.PHONY: clean all

all: xmlDsr2dsr

xmlDsr2dsr: SAXDSRGenerator.o mdfdsman.o xmlDsr2dsr.o
	$(CXX) SAXDSRGenerator.o mdfdsman.o xmlDsr2dsr.o $(CFLAGS) -o $@ $(LDFLAGS)

SAXDSRGenerator.o: SAXDSRGenerator.cc SAXDSRGenerator.h
	$(CXX) $(CFLAGS) -c SAXDSRGenerator.cc

mdfdsman.o: mdfdsman.cc mdfdsman.h
	$(CXX) $(CFLAGS) -c mdfdsman.cc

xmlDsr2dsr.o: xmlDsr2dsr.cc SAXDSRGenerator.h mdfdsman.h
	$(CXX) $(CFLAGS) -c xmlDsr2dsr.cc

clean:
	rm -f xmlDsr2dsr *.dcm *.o *~ \#*\#
