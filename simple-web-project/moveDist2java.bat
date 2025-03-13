cd ..
cd simple-java-project
cd src
cd main
cd resources
cd static
cd web
del index.html
del favicon.ico
rd /s /q assets

cd ..
cd ..
cd ..
cd ..
cd ..
cd ..
cd simple-web-project

robocopy ./dist ../simple-java-project/src/main/resources/static/web /E
