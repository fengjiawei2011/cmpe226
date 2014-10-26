for ((day=7; day<10; day++))
do
  for ((hour=0; hour<24; hour++))
    do 
      wget "http://data.githubarchive.org/2014-10-0$day-$hour.json.gz"
    done
done
