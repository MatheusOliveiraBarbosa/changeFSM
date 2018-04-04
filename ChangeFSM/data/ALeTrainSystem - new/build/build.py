import os, re
import shutil

# 

BUILDPATH = os.path.dirname(os.path.realpath(__file__))
ROOTPATH = re.sub(r'/build', '', os.path.dirname(os.path.realpath(__file__)))
RESOURCEPATH = BUILDPATH+"/configs"
MAPPATH = RESOURCEPATH+"/maps"
ROOTPROJ = ROOTPATH.split('/').pop()
REMOTEPATH = "/home/lejos/programs"
REMOTERESPATH = REMOTEPATH+"/resources"
REMOTEMAPPATH = REMOTERESPATH+"/maps"
POINTSWITCHEXECUTABLE = "point.jar"
POINTCONFIG = "pointswitchcontroller.properties"
TRAINEXECUTABLE = "train.jar"
TRAINCONFIG = "traincontroller.properties"
MAP = "ngorongoro2.map"

POINTSWITCHpointS = ["192.168.0.101", "192.168.0.102", "192.168.0.103", "192.168.0.104"]
TRAINS = ["192.168.0.191", "192.168.0.195"]

def get_target_dirs():
    target_dirs = []
    for target in os.listdir(ROOTPATH+'/..'):
        if target.startswith(ROOTPROJ) and target.endswith('_exe'):
            target_dirs.append(os.path.relpath(ROOTPATH+'/../'+target))
    return target_dirs

def distribute_log4j():
    target_dirs = get_target_dirs()
    for target in target_dirs:
        print 'Copying log4j.properties to ' + target
        shutil.copyfile(ROOTPATH+'/log4j.properties', target+'/log4j.properties')

def distribute_configs():
    for point in POINTSWITCHpointS:
        print "Copying point config to {0}".format(point)
        subfolder = getLastIpSubSection(point)
        os.system("scp {0}/{1}/{2} root@{3}:{4}/".format(RESOURCEPATH, subfolder, POINTCONFIG, point, REMOTERESPATH))
    for train in TRAINS:
        print "Copying train config to {0}".format(train)
        subfolder = getLastIpSubSection(train)
        os.system("scp {0}/{1}/{2} root@{3}:{4}/".format(RESOURCEPATH, subfolder, TRAINCONFIG, train, REMOTERESPATH))
        print "Copying " + MAP + " to {0}".format(train)
        os.system("scp {0}/{1} root@{2}:{3}/".format(MAPPATH, MAP, train, REMOTEMAPPATH))

def distribute_points():
    for point in POINTSWITCHpointS:
        print "Copying point to {0}".format(point)
        os.system("scp {0}/{1} root@{2}:{3}/".format(BUILDPATH, POINTSWITCHEXECUTABLE, point, REMOTEPATH))

def distribute_trains():
    for train in TRAINS:
        print "Copying train to {0}".format(train)
        os.system("scp {0}/{1} root@{2}:{3}/".format(BUILDPATH, TRAINEXECUTABLE, train, REMOTEPATH))

def execute():
    for point in POINTSWITCHpointS:
        print "Executing point on {0}".format(point)
        os.system('run mintty -t {0} ssh root@{0} "cd {1} && jrun -cp {1}/{2} aletrainsystem.point.Start; read -p \"Press enter to close this window.\""'.format(point, REMOTEPATH, POINTSWITCHEXECUTABLE))
        #os.system("ssh root@{0} jrun -cp {1}/{2} aletrainsystem.point.Start &".format(point, REMOTEPATH, POINTSWITCHEXECUTABLE))
    for train in TRAINS:
        print "Executing train on {0}".format(train)
        os.system('run mintty -t {0} ssh root@{0} "cd {1} && jrun -cp {1}/{2} aletrainsystem.train.Start; read -p \"Press enter to close this window.\""'.format(train, REMOTEPATH, TRAINEXECUTABLE))
        #os.system("ssh root@{0} jrun -cp {1}/{2} aletrainsystem.train.Start &".format(train, REMOTEPATH, TRAINEXECUTABLE))

def kill_jars():
    for point in POINTSWITCHpointS:
        print "Ending point on {0}".format(point)
        os.system("ssh root@{0} killall java".format(point))
    for train in TRAINS:
        print "Ending train on {0}".format(train)
        os.system("ssh root@{0} killall java".format(train))

def add_trustee(ip):
    os.system("ssh-copy-id -i ~/.ssh/id_rsa.pub root@{0}".format(ip))

def getLastIpSubSection(ip):
    return ip.split('.').pop()

if (__name__ == "__main__"):
    # distribute_log4j()
    distribute_points()
    distribute_trains()
    distribute_configs()
