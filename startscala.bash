#!/bin/bash

echo "Enter the directory the EZBUILD application was uncompressed to"
read EZBUILD_BUILDER

cd $EZBUILD_BUILDER
exec scala ezbuild
wait
echo "Completed"

