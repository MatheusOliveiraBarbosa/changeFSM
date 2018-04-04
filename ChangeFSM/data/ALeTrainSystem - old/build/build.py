import os, re
import shutil

# 

BUILDPATH = os.path.dirname(os.path.realpath(__file__))
ROOTPATH = re.sub(r'/build', '', os.path.dirname(os.path.realpath(__file__)))
ROOTPROJ = ROOTPATH.split('/').pop()
REMOTEPATH = "/home/lejos/programs"
POINTSWITCHEXECUTABLE = "pointswitchcontroller.jar"

# POINTSWITCHCONTROLLERS = ["192.168.0.191", "192.168.0.190"]
POINTSWITCHCONTROLLERS = ["192.168.0.101"]

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

def distribute_jars():
    for controller in POINTSWITCHCONTROLLERS:
        print "Copying pointswitchcontroller to {0}".format(controller)
        os.system("scp {0}/{1} root@{2}:{3}/".format(BUILDPATH, POINTSWITCHEXECUTABLE, controller, REMOTEPATH))

def execute_jars():
    for controller in POINTSWITCHCONTROLLERS:
        print "Executing pointswitchcontroller on {0}".format(controller)
        os.system("ssh root@{0} jrun -cp {1}/{2} ntnu.no.aletrainsystem.ev3.Start &".format(controller, REMOTEPATH, POINTSWITCHEXECUTABLE))

def kill_jars():
    for controller in POINTSWITCHCONTROLLERS:
        print "Ending pointswitchcontroller on {0}".format(controller)
        os.system("ssh root@{0} kill $(ps | grep {1} | grep -v 'grep' | awk ' {{ print $1 }} ' )".format(controller, POINTSWITCHEXECUTABLE))

def add_trustee(ip):
    os.system("ssh-copy-id -i ~/.ssh/id_rsa.pub root@{0}".format(ip))

if (__name__ == "__main__"):
    # distribute_log4j()
    distribute_jars()
