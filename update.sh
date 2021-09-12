#!/opt/procursus/bin/fish

scp target/SillyPunishments-1.0-SNAPSHOT.jar silly@192.168.0.156:TestingServer/TestingServer/plugins/
echo "Updated plugin, please refresh server."
