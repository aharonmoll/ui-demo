@echo off

if not defined GS_DIR (
	echo GS_DIR environment variable is not set
    exit 1
)


%GS_DIR%\bin\gs.bat pu deploy Mirror %~dp0\mirror\target\mirror.jar

%GS_DIR%\bin\gs.bat pu deploy --partitions=2 --backups=1 ProductsCatalog %~dp0\products-catalog\target\products-catalog.jar

%GS_DIR%\bin\gs.bat pu deploy ProductsLoader %~dp0\products-loader\target\products-loader.jar

%GS_DIR%\bin\gs.bat pu deploy --instances=2 WebApplication %~dp0\web-application\target\web-application.war


%GS_DIR%\bin\gs.bat pu deploy --instances=1 DemoApp %~dp0\demo-app\target\demo-app.war
