#!/usr/bin/env bash
#DIRNAME=$(dirname ${BASH_SOURCE[0]})

DIRNAME="/home/user/repositories/Gigaspaces/UIDemo"

if [[ -z "${GS_DIR}" ]]; then
    echo "GS_DIR environment variable is not set"
    exit 1
fi


#SERVER=""

#if [[ -n "${SERVER}" ]]; then
#    SERVER_PARAM="--server=${SERVER}"
#fi


/home/user/Desktop/gigaspaces-xap-enterprise-15.0.0-m17-tue-49/bin/gs.sh pu deploy --partitions=10 --backups=1 --max-instances-per-vm=1 products-catalog /home/user/repositories/Gigaspaces/UIDemo/products-catalog/target/products-catalog.jar
/home/user/Desktop/gigaspaces-xap-enterprise-15.0.0-m17-tue-49/bin/gs.sh pu deploy products-loader /home/user/repositories/Gigaspaces/UIDemo/products-loader/target/products-loader.jar
/home/user/Desktop/gigaspaces-xap-enterprise-15.0.0-m17-tue-49/bin/gs.sh pu deploy products-feeder /home/user/repositories/Gigaspaces/UIDemo/products-feeder/target/products-feeder.jar


#${GS_DIR}/bin/gs.sh pu deploy mirror ${DIRNAME}/mirror/target/mirror.jar

#${GS_DIR}/bin/gs.sh pu deploy --partitions=2 --backups=1 --max-instances-per-vm=1 data-processor ${DIRNAME}/data-processor/target/data-processor.jar

#${GS_DIR}/bin/gs.sh pu deploy products-loader ${DIRNAME}/products-loader/target/products-loader.jar

#${GS_DIR}/bin/gs.sh pu deploy batch-feeder ${DIRNAME}/batch-feeder/target/batch-feeder.jar

#${GS_DIR}/bin/gs.sh pu deploy products-catalogue ${DIRNAME}/products-catalogue/target/products-catalogue.jar
