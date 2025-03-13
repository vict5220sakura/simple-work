cd ..
cd simple-java-project
cd src
cd main
cd resources
cd static
cd app
del index.html
rd /s /q assets
rd /s /q static

cd ..
cd ..
cd ..
cd ..
cd ..
cd ..
cd simple-app-project
cd dist
cd build


robocopy ./h5 ../../../simple-java-project/src/main/resources/static/app /E
