#!/opt/procursus/bin/fish

# This is a private script, which will only work on my local machine. 
# All it does is transfer my local plugin file to my testing server.

scp target/SillyPunishments-1.0-SNAPSHOT.jar silly@192.168.0.156:TestingServer/TestingServer/plugins/
echo "Updated plugin, please refresh server."
