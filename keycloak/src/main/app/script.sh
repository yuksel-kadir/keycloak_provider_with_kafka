#!/bin/bash

# Define variables for ease of maintenance
KEYCLOAK_CLI="/opt/jboss/keycloak/bin/jboss-cli.sh"
SCAN_INTERVAL="-1"
SCANNER_PATH="/subsystem=deployment-scanner/scanner=default"
ENTRYPOINT_SCRIPT="/opt/jboss/tools/docker-entrypoint.sh"

# Function to execute Keycloak CLI commands
run_cli_command() {
    local command="$1"
    echo "TEST SCRIPT - Executing CLI command: $command"
    if $KEYCLOAK_CLI --connect <<< "$command"; then
        echo "TEST SCRIPT - CLI command executed successfully."
    else
        echo "TEST SCRIPT - Error executing CLI command: $command" >&2
        exit 1
    fi
}

# Execute the original entrypoint script in the background
echo "TEST SCRIPT - Starting original entrypoint script..."
$ENTRYPOINT_SCRIPT &

# Store the PID of the original entrypoint script
ENTRYPOINT_PID=$!

# Wait for Keycloak to start up
echo "TEST SCRIPT - Waiting for Keycloak to start..."
while true; do
  response=$(curl --write-out "%{http_code}" --silent --output /dev/null http://localhost:9990)
  
  if [ "$response" == "302" ]; then
    printf 'TEST SCRIPT - Keycloak is up and returned 302 Found. Exiting loop.\n'
    break
  else
    printf 'TEST SCRIPT - waiting for Keycloak to start...\n'
    sleep 5
  fi
done
echo -e "\nKeycloak is up and running."

# Run the CLI command to set scan-interval
echo "TEST SCRIPT - Connecting to jboss-cli..."
$KEYCLOAK_CLI --connect

echo "TEST SCRIPT - Setting scan-interval attribute..."
cli_command="${SCANNER_PATH}:write-attribute(name=scan-interval,value=${SCAN_INTERVAL})"
run_cli_command "$cli_command"

# Wait for the original entrypoint script to finish
wait $ENTRYPOINT_PID

# Wait indefinitely to keep the container running
echo "JBoss scan-interval attribute value is set to ${SCAN_INTERVAL}!"
exec "$@"
