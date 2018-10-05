echo "start create updateOracle.sh"
mycp=""
for jarfile in $(ls ./lib)
do 
  #echo $jarfile
  mycp="$mycp./lib/$jarfile:"
done
mycp="mycp=.:$mycp"

indexall="java -Xms128m -Xmx1024m -cp \$mycp com.interlib.opac.updatedb.UpdateOracleDataBase"
rm $PWD/updateOracle.sh
echo $mycp >> $PWD/updateOracle.sh
echo $indexall >> $PWD/updateOracle.sh

