#!/bin/bash                                                                                    
                                                                                               
DEST_DIR="./javaSource"                                                                        
REPO="git@github.com:GameSmart/JavaGames.git"                                                  
PROJECT_DIR="/home/ubuntu/javaSource/SmartFox"                                          
PROJECT_BASE_DIR="/home/ubuntu/javaSource/VideoBingoEngine/"
                                                                                                                                                                              
                                                                                               
BRANCH=${1:-master}                                                                                                                                                                                                         
JAR_DIR="SFS2X/extensions/NewEngineExtension/"
PROJECT_MACHINES="config/machines"                                                                                                  
JAR_BASE_DIR="SFS2X/extensions/__lib__/"                                                                       
BACKUP_DIR="NewEngineBackup/"                                                                                                                                          
SFS_DIR="/data/"                                                                                                                                                         
                                                                                               
read -p "deploy '$BRANCH' branch to $SFS_DIR [y,n]: " -n 1 -r                                  
echo    # (optional) move to a new line                                                        
if [[ ! $REPLY =~ ^[Yy]$ ]]                                                                    
then                                                                                           
    exit 1                                                                                     
fi

                                                                                               
if [ -d "$DEST_DIR" ]                                                                          
then                                                                                           
  rm -rf "$DEST_DIR"                                                                           
  echo "deleting $DEST_DIR"                                                                    
fi                                                                                             
                                                                                               
                                                                                               
echo "creating $DEST_DIR"                                                                      
mkdir $DEST_DIR                                                                                
                                                                                               
mkdir -p $BACKUP_DIR                                                                           
                                                                                               
LATEST_FILE="$SFS_DIR$JAR_DIR""NewEngineExtension.jar"                                             
LAST_BASE_FILE="$SFS_DIR$JAR_BASE_DIR""Engine.jar"
LAST_CONFIG="$SFS_DIR$JAR_DIR""config"                                                                                   
                                                                                               
                                                                                               
COUNT=`ls $BACKUP_DIR  -afq | wc -l`                                                           
BCK_FILE="$BACKUP_DIR""NewEngineExtension.jar"$COUNT                                               
BCK_BASE_FILE="$BACKUP_DIR""Engine.jar"$COUNT                                                                            
BAK_CONFIG="$BACKUP_DIR""config"$COUNT                                                                            
                                                                                               
read -p "[$SFS_DIR$JAR_DIR] backup NewEngineExtension.jar to NewEngineExtension.jar$COUNT  [y,n]: " -n 
echo    # (optional) move to a new line                                                        
if [[ ! $REPLY =~ ^[Yy]$ ]]                                                                    
then                                                                                           
    exit 1                                                                                     
fi                                                                                             
cp $LATEST_FILE $BCK_FILE                                                                      
cp $LAST_BASE_FILE $BCK_BASE_FILE
cp -r $LAST_CONFIG/. $BAK_CONFIG                                                                                                          
                                                                                               
echo "clone repo $REPO"                                                                        
cd $DEST_DIR                                                                                   
git clone -b $BRANCH  $REPO .                                                                  
                                                                                               
cd $PROJECT_BASE_DIR                                                                           
ant                                                                                            
                                                                                               
cd $PROJECT_DIR                                                                                
ant                                                                                           
                                                                                               
                                                                                               
echo "copy jar file..."                                                                        
                                                                                               
cd $PROJECT_DIR                                                                                
cp NewEngineExtension.jar $SFS_DIR$JAR_DIR                                                         
                                                                                               
cd $PROJECT_BASE_DIR                                                                           
cp Engine.jar $SFS_DIR$JAR_BASE_DIR                                                                                                                                                                                                                                                            
                                                                                               
if [ -d "$SFS_DIR$JAR_DIR$PROJECT_MACHINES" ]                                                                          
then                                                                                           
  rm -rf "$SFS_DIR$JAR_DIR$PROJECT_MACHINES"                                                                           
  echo "deleting $SFS_DIR$JAR_DIR$PROJECT_MACHINES"                                                                    
fi                                                                                             
                                                                                               
                                                                                               
echo "creating $SFS_DIR$JAR_DIR$PROJECT_MACHINES"                                                                      
mkdir -p $SFS_DIR$JAR_DIR$PROJECT_MACHINES

cd "$SFS_DIR$JAR_DIR$PROJECT_MACHINES"
cp -r "$PROJECT_BASE_DIR$PROJECT_MACHINES/." "$SFS_DIR$JAR_DIR$PROJECT_MACHINES"                                                                   
                                                                                               
echo "completed! please restart smartfox"                                                      
