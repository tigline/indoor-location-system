pipeline {
    agent any
    environment {
        VERSION = ''
        JAR_FILE_NAME = ''
    }
    stages {
        stage('Get Version') {
            steps {
                sh './gradlew saveVersion'
                script {
                    env.VERSION = readFile('version.txt').trim()
                    env.JAR_FILE_NAME = "indoor-positioning-system-${env.VERSION}.jar"
                }
            }
        }
        stage('Build') {
            steps {
                echo 'Building the application...'
                sh './gradlew build'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying to AWS EC2...'
                sshagent(credentials: ['awsserver']) {
                    sh "scp -o StrictHostKeyChecking=no -i /var/jenkins_home/helloindoor_2.pem your-application.jar ubuntu@13.112.168.219:/home/ubuntu/ips"
                    sh "ssh -o StrictHostKeyChecking=no -i /var/jenkins_home/helloindoor_2.pem ubuntu@13.112.168.219 'chmod +x /home/ubuntu/ips/deploy.sh && /home/ubuntu/ips/deploy.sh'"
                }
            }
        }
    }
}

