git clone https://github.com/rage/tmc-langs.git
mvn clean install -U -Dmaven.test.skip=true -f tmc-langs/pom.xml
git clone https://github.com/rage/tmc-core.git
mvn clean install -U -Dmaven.test.skip=true -f tmc-core/pom.xml
