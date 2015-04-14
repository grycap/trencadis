rm trencadis_infrastructure_services_DICOMStorage.gar
rm -rf build
sh globus-build-service.sh -d trencadis/infrastructure/services/DICOMStorage/ -s schema/DICOMStorage/DICOMStorage.wsdl -fs schema/DICOMStorage/DICOMStorageFactory.wsdl
 scp trencadis_infrastructure_services_DICOMStorage.gar trencadisv06.i3m.upv.es:/home/trencadis/
