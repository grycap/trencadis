rm -rf build
rm trencadis_infrastructure_services_EOUIDGenerator.gar
sh globus-build-service.sh -d trencadis/infrastructure/services/EOUIDGenerator/ -s schema/EOUIDGenerator/EOUIDGenerator.wsdl
