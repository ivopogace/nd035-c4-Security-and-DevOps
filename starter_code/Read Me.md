Fork this repo https://github.com/ivopogace/nd035-c4-Security-and-DevOps.git

Run docker-compose up -d

Jenkins is running in localhost:8081

First Log in
View the generated administrator password to log in the first time.
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
Register to jenkins, and after you log in create credentials with ssh-key that you
will find in "the git ssh-key for jenkins credentials.txt" file.

Create the first job using this git repo https://github.com/ivopogace/nd035-c4-Security-and-DevOps.git
and the credentials you created.

Execute te below command to enter tomcat bash
docker exec -it tomcat bash

After that populate the webapps with the default tomcat server homepage
cp -r webapps.dist/* webapps

----
find . -name context.xml
result: ./webapps/manager/META-INF/context.xml

open in vi editor
apt-get update
apt-get install -y vim
-y => Will by pass the permission, default permission will set to Yes.
vim => Name of the package you want to install.
vi ./webapps/manager/META-INF/context.xml

find . -name tomcat-users.xml 
result: ./conf/tomcat-users.xml
vi ./conf/tomcat-users.xml

