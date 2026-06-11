pipeline {
	// Usa um contêiner temporário com Maven e Java 17 para rodar os comandos abaixo
	agent {
		docker {
			image 'maven:3.9-eclipse-temurin-21'
			args '-v /root/.m2:/root/.m2 --network=host' // Faz cache das dependências do Spring e compartilha a mesma rede da VM com o container
		}
	}

	environment {
	    SONARQUBE_HOST_URL = 'http://localhost:9000'
	    SONARQUBE_NEW_PASSWORD = 'SenhaUltraSecreta123@'
    }

	options {
            timeout(time: 1, unit: 'HOURS') //Define quanto tempo a pipeline e executada antes de poder ser cancelada
            disableConcurrentBuilds() //Impede que duas execuções rodem ao mesmo tempo na mesma branch
            buildDiscarder(logRotator(numToKeepStr: '10')) //Define o limite de logs salvos
            timestamps()
    }

    triggers {
           githubPush() // Roda o pipeline toda vez que o repositorio tem um commit novo
    }

    parameters {
           string(name: 'DEPLOY_ENV', defaultValue: 'staging', description: 'Ambiente de destino')
           choice(name: 'LOG_LEVEL', choices: ['INFO', 'DEBUG', 'ERROR'], description: 'Nível de log')
           booleanParam(name: 'RUN_TESTS', defaultValue: true, description: 'Desmarque para pular os testes')
    }


	stages {
		stage('Checkout') {
			steps {
				checkout scm
			}
		}

		stage('Concede permissões para o Maven') {
		    steps {
		        // Garante que o arquivo mvnw tem permissão para ser executado no Linux
                sh 'chmod +x mvnw'
		    }
		}

		stage('Linter') {
		    steps {
		        echo "Realizando verificação com o linter Checkstyle..."
		        sh './mvnw checkstyle:check'
		    }
		}

		stage('Code Formater') {
		    steps {
		        echo "Realizando verificação de formatação com o Spotless..."
		        sh './mvnw spotless:check'
		    }
		}

		stage('Compilação e Testes') {
        	steps {
        		// Apaga os builds antigos e roda os testes
                sh './mvnw clean package'
        	}
        }

//		stage('Analise SonarQube e build do projeto') {
//			steps {
//				script {
//                    // Aguarda ate o container do sonarQube estar respondendo na porta 9000
//                    echo "Waiting for SonarQube container to be ready..."
//                    sh "until \$(curl --output /dev/null --silent --head --fail ${env.SONARQUBE_HOST_URL}); do sleep 5; done"
//
//                    // Muda a senha padrao
//                    echo "Changing default admin password..."
//                    sh """
//                        curl -u admin:admin -X POST \
//                        "${env.SONARQUBE_HOST_URL}/api/users/change_password?login=admin&previousPassword=admin&password=${env.SONARQUBE_NEW_PASSWORD}"
//                    """
//
//                    // Executa as verificacoes no codigo
//                    echo "Executing Maven Sonar scanner..."
//                    sh """
//                        ./mvnw sonar:sonar \
//                          -Dsonar.host.url=${env.SONARQUBE_HOST_URL} \
//                          -Dsonar.login=admin \
//                          -Dsonar.password=${env.SONARQUBE_NEW_PASSWORD} \
//                          -Dsonar.projectName="Sistema Receitas" \
//                          -Dsonar.qualitygate.wait=true
//                    """
//                }
//			}
//		}

		stage('Prepara imagens Docker para deploy') {
			agent any

            steps {
                echo "Cria a imagem padrão da aplicação através do Dockerfile"
                sh '/usr/bin/docker build -t registro-receitas-image .'
            }
        }

//		stage('Deploy Homologacao'){
//		    steps{
//                echo ""
//                sh """
//                    docker run -d \
//                      --name registro-receitas-homologacao
//                      -e SERVER_ADDRESS= \
//                      -e SERVER_PORT= \
//                      -e DB_HOST= \
//                      -e EMAIL_HOST= \
//                      -e EMAIL_USERNAME= \
//                      -e EMAIL_PASSWORD= \
//                      -e JWT_SECRET_KEY= \
//                      -p 8081:80 \
//                      registro-receitas-image
//                   """
//		    }
//		}
//
//        // Estágio de aprovação para Produção
//        stage('Aprovacao para Producao') {
//            steps {
//                input message: 'Aprovar deploy para Produção?', ok: 'Y'
//            }
//        }
//
//		stage('Deploy Producao'){
//		    steps{
//
//		    }
//		}
	}
}