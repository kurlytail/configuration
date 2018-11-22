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
                	env.PATH = env.PATH + ':/usr/local/bin'
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
		            sh 'mvn --batch-mode -s settings.xml release:update-versions -DautoVersionSubmodules=true -DdevelopmentVersion=$MAVEN_VERSION_NUMBER'
		            sh 'mvn --batch-mode -s settings.xml deploy'
		            sh 'mvn --batch-mode -s settings.xml dockerfile:build'
		        }
		        script {
			        try {
			            sh "docker stop configuration"
			            sh "docker rm configuration"
			        }
			        catch(msg) {
			            echo "Ignoring error $msg"
			        }
		        }

		        sh '''docker run --restart unless-stopped --live-restore -d -p 10000:80 --dns \$(docker inspect -f \'{{.NetworkSettings.IPAddress}}\' dns) --dns-search brainspeedtech.com --name configuration brainspeedtech/configuration:\$MAVEN_VERSION_NUMBER'''
            }
        }
    }
}

