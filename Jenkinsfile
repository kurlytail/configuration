pipeline {

    agent none
    
    parameters {
        string(defaultValue: "0.0", description: 'Build version prefix', name: 'BUILD_VERSION_PREFIX')
        string(defaultValue: "", description: 'Build number offset', name: 'BUILDS_OFFSET')
    }

    stages {
        stage('Prepare env') {
            agent {
                label 'master'
            }
            
            steps {
                script {
                    loadLibrary()
                    env['MAVEN_VERSION_NUMBER'] = getMavenVersion 'kurlytail/configuration/master', params.BUILD_VERSION_PREFIX, params.BUILDS_OFFSET
                	currentBuild.displayName = env['MAVEN_VERSION_NUMBER']
                }
            }
        }
        
        stage ('Build') {
            agent {
                label 'mvn'
            }
            
            steps {
                sh 'rm -rf *'
     
                checkout scm
                withMaven {
		            sh '/usr/local/bin/mvn --batch-mode release:update-versions -DautoVersionSubmodules=true -DdevelopmentVersion=$MAVEN_VERSION_NUMBER'
		            sh '/usr/local/bin/mvn -s settings.xml deploy'
		            sh '/usr/local/bin/mvn docker:build'
		        }
		        script {
			        try {
			            sh "docker stop container"
			            sh "docker rm container"
			        }
			        catch(msg) {
			            echo "Ignoring error $msg"
			        }
		        }

		        sh '''docker run -d --dns $(docker inspect -f '{{.NetworkSettings.IPAddress}}' dns) --dns-search brainspeedtech.com --name configuration brainspeedtech/configuration:''' + env['MAVEN_VERSION_NUMBER']
            }
        }
    }
}

