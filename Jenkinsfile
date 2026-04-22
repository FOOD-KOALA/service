pipeline {
    agent any

    // Cerința: Gradle 8 and JDK 21 configured in Jenkins tools
    tools {
        jdk 'JDK 21'
        gradle 'Gradle 8'
    }

    environment {
        // ATENȚIE: Setează aici username-ul tău REAL de pe Docker Hub (ex: oancea123)
        DOCKER_HUB_USER = 'iustinnc' 
        
        IMAGE_NAME = 'food-koala-service'
        // Creăm un tag unic (ex: v1.0.4) bazat pe numărul rulării din Jenkins
        TAG_NAME = "v1.0.${env.BUILD_NUMBER}" 
    }

    stages {
        stage('Build Application') {
            steps {
                echo '=== Compilam codul sursa ==='
                sh 'chmod +x gradlew'
                // Construiește acel prod-eng-0.0.1-SNAPSHOT.jar pe care îl așteaptă Dockerfile-ul tău
                sh './gradlew clean assemble -x test'
            }
        }

        stage('Run Unit Tests') {
            steps {
                echo '=== Rulam testele unitare ==='
                sh './gradlew test jacocoTestReport'
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                echo '=== Construim imaginea de Docker si o urcam pe Hub ==='
                script {
                    // Jenkins va căuta un "secret" salvat cu ID-ul 'dockerhub-creds'
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
                        sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                        // Aici se folosește Dockerfile-ul tău!
                        sh "docker build -t ${DOCKER_HUB_USER}/${IMAGE_NAME}:${TAG_NAME} ."
                        sh "docker push ${DOCKER_HUB_USER}/${IMAGE_NAME}:${TAG_NAME}"
                    }
                }
            }
        }

        stage('Create Git Tag') {
            steps {
                echo '=== Cream un tag pe GitHub pentru release ==='
                script {
                    // Jenkins va căuta un token de GitHub salvat cu ID-ul 'github-token'
                    withCredentials([string(credentialsId: 'github-token', variable: 'GIT_TOKEN')]) {
                        sh """
                            git config user.email "jenkins@foodkoala.com"
                            git config user.name "Jenkins CI"
                            git tag -a ${TAG_NAME} -m "Release automat generat de Jenkins"
                            # Urcăm tag-ul pe GitHub-ul vostru
                            git push https://${GIT_TOKEN}@github.com/FOOD-KOALA/service.git ${TAG_NAME}
                        """
                    }
                }
            }
        }
    }
    
    // Rămâne "verde" sau se face "roșu" la final
    post {
        always {
            echo "Pipeline-ul a terminat rularea."
        }
        success {
            echo "BUILD SUCCESSFUL! Imaginea este pe Docker Hub si Tag-ul pe GitHub."
        }
        failure {
            echo "UILD FAILED! Verifica logurile de mai sus."
        }
    }
}