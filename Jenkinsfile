pipeline {
    agent any
    tools {
        jdk 'jdk-17'
    }
//     environment {
//         VERSION = '1.0.0'
//         JAR_FILE_NAME = 'indoor-positioning-system'
//     }
    stages {
        stage('Get Version') {
            steps {
                echo 'Getting the version...'
                sh './gradlew saveVersion'
                script {
                    VERSION = readFile('version.txt').trim()
                    JAR_FILE_NAME = "indoor-positioning-system-${env.VERSION}.jar"
                }
                echo "Version: ${VERSION}"
                echo "JAR_FILE_NAME: ${JAR_FILE_NAME}"
            }
        }
        stage('Build') {
            steps {
                echo 'Building the application...'
                sh './gradlew clean build'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying to AWS EC2...'
                echo  "JAR_NAME: ${JAR_FILE_NAME}..."
                sshagent(credentials: ['awsserver']) {
                    sh "scp -o StrictHostKeyChecking=no -i /var/jenkins_home/helloindoor_2.pem build/libs/${JAR_FILE_NAME} ubuntu@13.112.168.219:/home/ubuntu/ips"
                    sh "ssh -o StrictHostKeyChecking=no -i /var/jenkins_home/helloindoor_2.pem ubuntu@13.112.168.219 'chmod +x /home/ubuntu/ips/deploy.sh && /home/ubuntu/ips/deploy.sh'"
                }
            }
        }
    }
}

