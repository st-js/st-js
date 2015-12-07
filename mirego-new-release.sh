mvn clean release:clean release:prepare release:perform

if [ $? -ne 0 ]; then
  echo "#############################"
  echo "# ERROR performing release  #"
  echo "#############################"
  #exit 1
fi

git push
if [ $? -ne 0 ]; then
  echo "###############################"
  echo "# ERROR pushing to repository #"
  echo "###############################"
  #exit 1
fi

echo "#################"
echo "# DONE          #"
echo "#################"
