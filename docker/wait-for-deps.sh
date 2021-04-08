#!/usr/bin/env bash

# This script waits for list of preconfigured services to be up and then starts final command
# (passed as parameter to the script).
# If referenced service could not be contacted in configured time this script will fail with error.
#
# Default timeout is 120 seconds but it can be changed with CFG_WAIT_FOR_TIMEOUT env variable.
#
# Main loop will look for environment variables starting with WAIT_FOR_ prefix
# and for each matching entry it will execute another script and pass value extracted from the variable.
#
# Current implementation is using https://github.com/vishnubob/wait-for-it script to
# check if referenced service is up.


CLI=("$@")

env | while IFS="=" read NAME VALUE; do
	if [[ "${NAME}" =~ ^WAIT_FOR_.* ]]; then
		echo "Found WAIT_FOR service configuration: NAME=${NAME}, VALUE=${VALUE}"
        (/wait-for-it.sh -s -t ${CFG_WAIT_FOR_TIMEOUT:-120} ${VALUE})
		STATUS=$?
		echo "Waiting for ${NAME}=${VALUE} finished with exit code ${STATUS}"
		if [ ${STATUS} -ne 0 ]; then
		    exit ${STATUS}
        fi
	fi
done
FINAL_STATUS=$?

if [ ${FINAL_STATUS} -eq 0 ]; then
    echo "Executing command: ${CLI[@]}"
    exec "${CLI[@]}"
else
    echo "Check for one of required services failed with code ${FINAL_STATUS}. Not starting the application."
    exit ${FINAL_STATUS}
fi
